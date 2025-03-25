package org.hust.cse.supplychain.basic.modules.contract.service.Impl;

import org.hust.cse.supplychain.basic.common.utils.FileUploadUtils;
import org.hust.cse.supplychain.basic.modules.contract.dao.mapper.TransportContractMapper;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.Product;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.TransportContract;
import org.hust.cse.supplychain.basic.modules.contract.po.enums.ContractStatusEnum;
import org.hust.cse.supplychain.basic.modules.contract.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransportServiceImpl implements TransportService {

    @Autowired
    private TransportContractMapper transportContractMapper;

    @Override
    public List<TransportContract> getTransportContracts(){
        QueryWrapper<TransportContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createdAt");
        return transportContractMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransportContract createTransportContract(TransportContract contract, MultipartFile file){
        try {
            // 处理合同上传
            String uploadPath= "D:\\myFile\\download\\";
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }
            
            String uploadFileName= contract.getNumber()+"_"+contract.getName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            boolean result = FileUploadUtils.UploadToServer(file,uploadPath,uploadFileName);
            if(!result) {
                throw new RuntimeException("Failed to upload file");
            }
            
            //设置基本信息
            contract.setStatus(ContractStatusEnum.PENDING_APPROVAL);
            contract.setDepositoryStatus(false);
            contract.setCreatedAt(LocalDateTime.now());
            contract.setUpdatedAt(LocalDateTime.now());
            contract.setFile(uploadPath+uploadFileName);
            
            double totalPrice = 0.0;
            for (Product product : contract.getProducts()) {
                totalPrice += product.getWeight() * product.getUnitPrice();
            }
            contract.setTotalPrice(totalPrice);
            
            // 保存采购合同信息
            transportContractMapper.insert(contract);
            
            return contract;
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error creating transport contract: " + e.getMessage());
            e.printStackTrace();
            
            // 删除已上传的文件
            if (contract.getFile() != null) {
                File uploadedFile = new File(contract.getFile());
                if (uploadedFile.exists()) {
                    uploadedFile.delete();
                }
            }
            
            // 抛出异常以触发事务回滚
            throw new RuntimeException("Failed to create transport contract: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransportContract updateTransportContract(Long id, TransportContract contract){
        try {
            contract.setUpdatedAt(LocalDateTime.now());
            contract.setId(id);
            
            double totalPrice = 0.0;
            for (Product product : contract.getProducts()) {
                totalPrice += product.getWeight() * product.getUnitPrice();
            }
            contract.setTotalPrice(totalPrice);
            
            // 更新基本合同信息
            UpdateWrapper<TransportContract> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            transportContractMapper.update(contract, updateWrapper);
            
            return transportContractMapper.selectById(id);
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error updating transport contract: " + e.getMessage());
            e.printStackTrace();
            
            // 抛出异常以触发事务回滚
            throw new RuntimeException("Failed to update transport contract: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteTransportContract(Long id){
        return transportContractMapper.deleteById(id);
    }
    
    @Override
    public List<TransportContract> searchTransportContractsByKeyword(String keyword){
        QueryWrapper<TransportContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("number", keyword).or().like("name", keyword);
        return transportContractMapper.selectList(queryWrapper);
    }

    @Override
    public List<TransportContract> searchTransportContractsByDateRange(LocalDate startDate, LocalDate endDate){
        QueryWrapper<TransportContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("createdAt", startDate, endDate);
        return transportContractMapper.selectList(queryWrapper);
    }

    @Override
    public List<TransportContract> searchTransportContractsByStatus(String status){
        QueryWrapper<TransportContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return transportContractMapper.selectList(queryWrapper);
    }

    @Override
    public byte[] downloadFile(Long id) throws IOException {
        TransportContract contract = transportContractMapper.selectById(id);
        if (contract == null || contract.getFile() == null) {
            throw new RuntimeException("Contract or file not found");
        }
        File file = new File(contract.getFile());
        if (!file.exists()) {
            throw new RuntimeException("Contract file not found");
        }
        return FileCopyUtils.copyToByteArray(file);
    }

    @Override
    public String getContractFileName(Long id) {
        TransportContract contract = transportContractMapper.selectById(id);
        if (contract == null || contract.getFile() == null) {
            throw new RuntimeException("Contract or file not found");
        }
        return new File(contract.getFile()).getName();
    }
}
