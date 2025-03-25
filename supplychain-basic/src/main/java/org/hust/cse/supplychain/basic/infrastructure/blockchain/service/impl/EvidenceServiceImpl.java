package org.hust.cse.supplychain.basic.infrastructure.blockchain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.ValidateDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.User;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.enums.EvidenceType;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.Evidence;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.Record;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.Log;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.AddSignDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.EvidenceDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.TransactionDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.service.EvidenceService;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.vo.EvidenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hust.cse.supplychain.basic.common.utils.EvidenceUtils;
import org.springframework.web.client.RestTemplate;


import org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.mapper.EvidenceMapper;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.mapper.UserMapper;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.mapper.RecordMapper;


@Service
public class EvidenceServiceImpl implements EvidenceService {
    @Value("${system.url}")
    private String serverHost;
    @Value("${system.evidenceAddress}")
    private String evidenceAddress;
    
    @Autowired
    private EvidenceMapper evidenceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private EvidenceUtils evidenceUtils;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String name) throws Exception {
        // 构建请求URL
        String url = serverHost+"/privateKey?type=0&userName=" + name;
        // 调用外部接口
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Request successful");
            } else {
                System.out.println("Request failed with status code: " + response.getStatusCode());
            }
            JSONObject jsonObject = JSON.parseObject(response.getBody());
            User user = new User();
            user.setName(name);
            user.setPublicKey(jsonObject.getString("publicKey"));
            user.setAddress(jsonObject.getString("address"));
            user.setPrivateKey(jsonObject.getString("privateKey"));
            user.setType(0);
            userMapper.insert(user);
        }
        catch (Exception e) {
            throw new Exception("Failed to register blockchain user: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createEvidence(EvidenceDTO evidenceDTO) throws Exception {
        String userAddress = userMapper.getAddressByName(evidenceDTO.getUser());
        if (userAddress == null) {
            throw new Exception("User not found: " + evidenceDTO.getUser());
        }
        
        //设置待签名用户
        String signersAddress;
        try {
            List<String> params = new ArrayList<>();
            List<String> signers = new ArrayList<>();
            for (String signer : evidenceDTO.getSigners()) {
                String signerAddr = userMapper.getAddressByName(signer);
                if (signerAddr == null) {
                    throw new Exception("Signer not found: " + signer);
                }
                signers.add(signerAddr);
            }
            JSONArray signersJson = JSONArray.parseArray(JSONObject.toJSONString(signers));
            params.add(signersJson.toJSONString());
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setUser(userAddress);
            transactionDTO.setFuncName("addSignersGroup");
            transactionDTO.setFuncParam(params);

            JSONObject jsonObject = evidenceUtils.sendTransaction(transactionDTO);
            if (jsonObject == null) {
                throw new Exception("Failed to add signers: No response from blockchain");
            }

            String output = jsonObject.getString("output");
            // 取后 40 位地址
            signersAddress = "0x" + output.substring(output.length() - 40);

        } catch (Exception e) {
            throw new Exception("Failed to add signers: " + e.getMessage());
        }

        System.out.println("signersAddress: " + signersAddress);
        // 创建证据
        Evidence evidence = new Evidence();
        try {
            List<Log> logs = new ArrayList<>();
            Log log = new Log();
            log.setUser(evidenceDTO.getUser());
            log.setTimestamp(LocalDateTime.now());
            log.setAction("createEvidence");
            logs.add(log);

            evidence.setType(evidenceDTO.getType());
            evidence.setRelatedId(evidenceDTO.getRelatedId());
            evidence.setUser(evidenceDTO.getUser());
            evidence.setSigners(evidenceDTO.getSigners());          
            evidence.setLogs(logs);

            // 先插入evidence记录以获取自增ID
            evidenceMapper.insert(evidence);
            
            // 获取插入后的ID (此时evidence对象已经被更新，包含了自增ID)
            Long evidenceId = evidence.getId();
            
            // 使用获取到的ID创建records
            List<Record> records = evidenceUtils.toRecords(evidenceDTO.getEvidence(), evidenceId);
            
            String number = evidenceUtils.getArrayHash(records);

            evidence.setNumber(number);

            evidenceMapper.updateById(evidence);
            
            for (Record record : records) {
                String hash = evidenceUtils.getHash(record);
                List<String> params = new ArrayList<>();
                params.add(hash);
                params.add(signersAddress);

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setUser(userAddress);
                transactionDTO.setFuncName("createEvidence");
                transactionDTO.setFuncParam(params);
                
                JSONObject jsonObject = evidenceUtils.sendTransaction(transactionDTO);
                if (jsonObject == null) {
                    throw new Exception("Failed to create evidence on blockchain: No response");
                }

                String output = jsonObject.getString("output");
                // 取后 40 位地址
                String address = "0x" + output.substring(output.length() - 40);
                record.setAddress(address);
                recordMapper.insert(record);
            }

            evidenceMapper.updateById(evidence);

        } catch (Exception e) {
            // 这里不需要手动回滚，@Transactional 注解会处理
            throw new Exception("Failed to create evidence: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSign(AddSignDTO addSignDTO) throws Exception {
        try {
            Evidence evidence = evidenceMapper.selectById(addSignDTO.getEvidenceId());
            if (evidence == null) {
                throw new Exception("Evidence not found with ID: " + addSignDTO.getEvidenceId());
            }
            
            String signer = addSignDTO.getSigner();
            String signerAddress = userMapper.getAddressByName(signer);
            if (signerAddress == null) {
                throw new Exception("Signer not found: " + signer);
            }
            
            List<Record> records = recordMapper.selectList(new QueryWrapper<Record>().eq("id", addSignDTO.getEvidenceId()));
            
            if (records == null || records.isEmpty()) {
                throw new Exception("No blockchain addresses found for this evidence");
            }
            
            for (Record record : records) {
                List<String> params = new ArrayList<>();
                params.add(record.getAddress());
                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setUser(signerAddress);
                transactionDTO.setFuncName("addSignatures");
                transactionDTO.setFuncParam(params);
                
                JSONObject jsonObject = evidenceUtils.sendTransaction(transactionDTO);
                if (jsonObject == null) {
                    throw new Exception("Failed to add signature on blockchain: No response");
                }
            }

            // 添加新的日志
            Log log = new Log();
            log.setUser(signer);
            log.setTimestamp(LocalDateTime.now());
            log.setAction("addSign");
            evidence.getLogs().add(log);
            
            // 直接更新整个对象
            evidenceMapper.updateById(evidence);
        } catch (Exception e) {
            throw new Exception("Failed to add sign: " + e.getMessage());
        }
    }

    @Override
    public boolean validateEvidence(ValidateDTO validateDTO) throws Exception {
        try {
            if (validateDTO == null) {
                throw new Exception("validateDTO is null");
            }

            List<Record> records = recordMapper.selectList(new QueryWrapper<Record>().eq("id",validateDTO.getEvidenceId()));

            if (records == null || records.isEmpty()) {
                throw new Exception("No records found for this evidence");
            }
            
            for (Record record : records) {
                List<String> params = new ArrayList<>();
                params.add(record.getAddress());
                params.add(evidenceUtils.getHash(record));

                String userAddress = userMapper.getAddressByName(validateDTO.getUser());

                TransactionDTO transactionDTO = new TransactionDTO();
                transactionDTO.setUser(userAddress);
                transactionDTO.setFuncName("isHash");
                transactionDTO.setFuncParam(params);

                String output = evidenceUtils.queryTransaction(transactionDTO);
                if (output == null) {
                    return false;
                }
                
                // 返回的是直接的数组
                JSONArray resultArray = JSON.parseArray(output);
                if (resultArray != null && !resultArray.isEmpty()) {
                    String resultStr = resultArray.getString(0);
                    boolean isValid = Boolean.parseBoolean(resultStr);
                    if (!isValid) {
                        System.out.println("验证失败:" + record);
                        return false;
                    }
                } else {
                    // 如果没有返回结果，视为验证失败
                    return false;
                }
            }
            return true;
        } catch(Exception e) {
            throw new Exception("Failed to validate evidence: " + e.getMessage());
        }
    } 

    @Override
    public List<EvidenceVO> getEvidence() throws Exception {

        try {
            List<Evidence> evidenceList = evidenceMapper.selectList(null);
            List<EvidenceVO> evidenceVOList = new ArrayList<>();
            for (Evidence evidence : evidenceList) {
                EvidenceVO evidenceVO = new EvidenceVO();
                evidenceVO.setNumber(evidence.getNumber());
                evidenceVO.setType(evidence.getType());
                evidenceVO.setRelatedId(evidence.getRelatedId());
                evidenceVO.setLogs(evidence.getLogs());
                evidenceVO.setUser(evidence.getUser());
                evidenceVO.setSigners(evidence.getSigners());
                evidenceVOList.add(evidenceVO);
            }
            return evidenceVOList;
        } catch (Exception e) {
            throw new Exception("Failed to get evidence: " + e.getMessage());
        }
    }

    @Override
    public List<EvidenceVO> getEvidenceByKeyword(String keyword) throws Exception {
        try{
            List<Evidence> evidenceList = evidenceMapper.selectList(new QueryWrapper<Evidence>().like("number", keyword).or().like("user", keyword));
            List<EvidenceVO> evidenceVOList = new ArrayList<>();
            for (Evidence evidence : evidenceList) {
                EvidenceVO evidenceVO = new EvidenceVO();
                evidenceVO.setNumber(evidence.getNumber());
                evidenceVO.setType(evidence.getType());
                evidenceVO.setRelatedId(evidence.getRelatedId());
                evidenceVO.setLogs(evidence.getLogs());
                evidenceVO.setUser(evidence.getUser());
                evidenceVO.setSigners(evidence.getSigners());
                evidenceVOList.add(evidenceVO);
            }
            return evidenceVOList;

        }catch(Exception e){
            throw new Exception("Failed to get evidence by keyword: " + e.getMessage());
        }
    }

    @Override
    public List<EvidenceVO> getEvidenceByUser(String user) throws Exception {
        try{
            List<Evidence> evidenceList = evidenceMapper.selectList(new QueryWrapper<Evidence>().eq("user", user));
            List<EvidenceVO> evidenceVOList = new ArrayList<>();
            for (Evidence evidence : evidenceList) {
                EvidenceVO evidenceVO = new EvidenceVO();
                evidenceVO.setNumber(evidence.getNumber());
                evidenceVO.setType(evidence.getType());
                evidenceVO.setRelatedId(evidence.getRelatedId());
                evidenceVO.setLogs(evidence.getLogs());
                evidenceVO.setUser(evidence.getUser());
                evidenceVO.setSigners(evidence.getSigners());
                evidenceVOList.add(evidenceVO);
            }
            return evidenceVOList;
        }catch(Exception e){
            throw new Exception("Failed to get evidence by user: " + e.getMessage());
        }
    }

    @Override
    public List<EvidenceVO> getEvidenceByType(EvidenceType type) throws Exception {
        try{
            List<Evidence> evidenceList = evidenceMapper.selectList(new QueryWrapper<Evidence>().eq("type", type));
            List<EvidenceVO> evidenceVOList = new ArrayList<>();
            for (Evidence evidence : evidenceList) {
                EvidenceVO evidenceVO = new EvidenceVO();
                evidenceVO.setNumber(evidence.getNumber());
                evidenceVO.setType(evidence.getType());
                evidenceVO.setRelatedId(evidence.getRelatedId());
                evidenceVO.setLogs(evidence.getLogs());
                evidenceVO.setUser(evidence.getUser());
                evidenceVO.setSigners(evidence.getSigners());
                evidenceVOList.add(evidenceVO);
            }
            return evidenceVOList;
        }catch(Exception e){
            throw new Exception("Failed to get evidence by type: " + e.getMessage());
        }
    }
}