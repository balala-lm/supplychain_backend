package org.hust.cse.supplychain.basic.modules.contract.service.Impl;

import org.hust.cse.supplychain.basic.common.utils.FileUploadUtils;
import org.hust.cse.supplychain.basic.modules.contract.dao.mapper.SalesContractMapper;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.Product;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.SalesContract;
import org.hust.cse.supplychain.basic.modules.contract.po.enums.ContractStatusEnum;
import org.hust.cse.supplychain.basic.modules.contract.service.SalesService;
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
public class SalesServiceImpl implements SalesService {
    @Autowired
    private SalesContractMapper salesContractMapper;
    
    @Override
    public List<SalesContract> getSalesContracts(){
        QueryWrapper<SalesContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createdAt");
        return salesContractMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SalesContract createSalesContract(SalesContract contract, MultipartFile file){
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
            
            // 设置基本信息
            contract.setStatus(ContractStatusEnum.PENDING_APPROVAL);
            contract.setDepositoryStatus(false);
            contract.setAssignmentStatus(false);
            contract.setCreatedAt(LocalDateTime.now());
            contract.setUpdatedAt(LocalDateTime.now());
            contract.setFile(uploadPath+uploadFileName);
            
            double totalPrice = 0.0;
            for (Product product : contract.getProducts()) {
                totalPrice += product.getWeight() * product.getUnitPrice();
            }
            contract.setTotalPrice(totalPrice);
            
            // 保存采购合同信息
            salesContractMapper.insert(contract);
            
            return contract;
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error creating sales contract: " + e.getMessage());
            e.printStackTrace();
            
            // 删除已上传的文件
            if (contract.getFile() != null) {
                File uploadedFile = new File(contract.getFile());
                if (uploadedFile.exists()) {
                    uploadedFile.delete();
                }
            }
            
            // 抛出异常以触发事务回滚
            throw new RuntimeException("Failed to create sales contract: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SalesContract updateSalesContract(Long id, SalesContract contract){
        try {
            contract.setUpdatedAt(LocalDateTime.now());
            contract.setId(id);
            
            double totalPrice = 0.0;
            for (Product product : contract.getProducts()) {
                totalPrice += product.getWeight() * product.getUnitPrice();
            }
            contract.setTotalPrice(totalPrice);
            
            // 更新基本合同信息
            UpdateWrapper<SalesContract> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            salesContractMapper.update(contract, updateWrapper);
            
            return salesContractMapper.selectById(id);
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error updating sales contract: " + e.getMessage());
            e.printStackTrace();
            
            // 抛出异常以触发事务回滚
            throw new RuntimeException("Failed to update sales contract: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSalesContract(Long id){
        return salesContractMapper.deleteById(id);
    }
    
    @Override
    public List<SalesContract> searchSalesContractsByKeyword(String keyword){
        QueryWrapper<SalesContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("number", keyword).or().like("name", keyword);
        return salesContractMapper.selectList(queryWrapper);
    }

    @Override
    public List<SalesContract> searchSalesContractsByDateRange(LocalDate startDate, LocalDate endDate){
        QueryWrapper<SalesContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("createdAt", startDate, endDate);
        return salesContractMapper.selectList(queryWrapper);
    }

    @Override
    public List<SalesContract> searchSalesContractsByStatus(String status){
        QueryWrapper<SalesContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return salesContractMapper.selectList(queryWrapper);
    }
    
    @Override
    public byte[] downloadFile(Long id) throws IOException {
        SalesContract contract = salesContractMapper.selectById(id);
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
        SalesContract contract = salesContractMapper.selectById(id);
        if (contract == null || contract.getFile() == null) {
            throw new RuntimeException("Contract or file not found");
        }
        return new File(contract.getFile()).getName();
    }
}
