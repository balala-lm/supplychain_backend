package org.hust.cse.supplychain.basic.modules.contract.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.hust.cse.supplychain.basic.common.utils.FileUploadUtils;
import org.hust.cse.supplychain.basic.modules.contract.dao.mapper.PurchaseContractMapper;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.Product;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.PurchaseContract;
import org.hust.cse.supplychain.basic.modules.contract.po.enums.ContractStatusEnum;
import org.hust.cse.supplychain.basic.modules.contract.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseContractMapper purchaseContractMapper;

    @Override
    public List<PurchaseContract> getPurchaseContracts(){
        QueryWrapper<PurchaseContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createdAt");
        return purchaseContractMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseContract createPurchaseReverseContract(PurchaseContract contract, MultipartFile file, MultipartFile invoiceFile){
        try {
            // 处理合同上传(反向代理开票)
            String uploadPath= "D:\\myFile\\download\\";
            if (file.isEmpty()) {
                throw new RuntimeException("Contract file is empty");
            }
            if (invoiceFile.isEmpty()) {
                throw new RuntimeException("Invoice file is empty");
            }
            
            String uploadFileName= contract.getNumber()+"_"+contract.getName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String invoiceUploadFileName= "invoice_"+contract.getNumber()+"_"+contract.getName()+invoiceFile.getOriginalFilename().substring(invoiceFile.getOriginalFilename().lastIndexOf("."));
            
            boolean result1 = FileUploadUtils.UploadToServer(file,uploadPath,uploadFileName);
            boolean result2 = FileUploadUtils.UploadToServer(invoiceFile,uploadPath,invoiceUploadFileName);
            
            if(!result1 || !result2) {
                throw new RuntimeException("Failed to upload files");
            }
            
            // 处理发票上传
            contract.setStatus(ContractStatusEnum.PENDING_APPROVAL);
            contract.setDepositoryStatus(false);
            contract.setCreatedAt(LocalDateTime.now());
            contract.setUpdatedAt(LocalDateTime.now());
            contract.setFile(uploadPath+uploadFileName);
            contract.setInvoiceFile(uploadPath+invoiceUploadFileName);
            
            double totalPrice = 0.0;
            for (Product product : contract.getProducts()) {
                totalPrice += product.getWeight() * product.getUnitPrice();
            }
            contract.setTotalPrice(totalPrice);
            
            // 保存采购合同信息
            purchaseContractMapper.insert(contract);
            
            return contract;
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error creating purchase reverse contract: " + e.getMessage());
            e.printStackTrace();
            
            // 删除已上传的文件
            if (contract.getFile() != null) {
                File uploadedFile = new File(contract.getFile());
                if (uploadedFile.exists()) {
                    uploadedFile.delete();
                }
            }
            
            if (contract.getInvoiceFile() != null) {
                File uploadedInvoiceFile = new File(contract.getInvoiceFile());
                if (uploadedInvoiceFile.exists()) {
                    uploadedInvoiceFile.delete();
                }
            }
            
            // 抛出异常以触发事务回滚
            throw new RuntimeException("Failed to create purchase reverse contract: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseContract createPurchaseContract(PurchaseContract contract, MultipartFile file) {
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
            purchaseContractMapper.insert(contract);
            
            return contract;
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error creating purchase contract: " + e.getMessage());
            e.printStackTrace();
            
            // 删除已上传的文件
            if (contract.getFile() != null) {
                File uploadedFile = new File(contract.getFile());
                if (uploadedFile.exists()) {
                    uploadedFile.delete();
                }
            }
            
            // 抛出异常以触发事务回滚
            throw new RuntimeException("Failed to create purchase contract: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseContract updatePurchaseContract(Long id, PurchaseContract contract){
        try {
            contract.setUpdatedAt(LocalDateTime.now());
            contract.setId(id);
            
            double totalPrice = 0.0;
            for (Product product : contract.getProducts()) {
                totalPrice += product.getWeight() * product.getUnitPrice();
            }
            contract.setTotalPrice(totalPrice);
            
            // 更新基本合同信息
            UpdateWrapper<PurchaseContract> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            purchaseContractMapper.update(contract, updateWrapper);
            
            // 更新交易合同特定信息
            return purchaseContractMapper.selectById(id);
        } catch (Exception e) {
            // 记录错误日志
            System.err.println("Error updating purchase contract: " + e.getMessage());
            e.printStackTrace();
            
            // 抛出异常以触发事务回滚
            throw new RuntimeException("Failed to update purchase contract: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePurchaseContract(Long id){
        return purchaseContractMapper.deleteById(id);
    }

    @Override
    public List<PurchaseContract> searchPurchaseContractsByKeyword(String keyword){
        QueryWrapper<PurchaseContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("number", keyword).or().like("name", keyword);
        return purchaseContractMapper.selectList(queryWrapper);
    }

    @Override
    public List<PurchaseContract> searchPurchaseContractsByDateRange(LocalDate startDate, LocalDate endDate){
        QueryWrapper<PurchaseContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("createdAt", startDate, endDate);
        return purchaseContractMapper.selectList(queryWrapper);
    }

    @Override
    public List<PurchaseContract> searchPurchaseContractsByStatus(String status){
        QueryWrapper<PurchaseContract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return purchaseContractMapper.selectList(queryWrapper);
    }

    @Override
    public byte[] downloadFile(Long id) throws IOException {
        PurchaseContract contract = purchaseContractMapper.selectById(id);
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
        PurchaseContract contract = purchaseContractMapper.selectById(id);
        if (contract == null || contract.getFile() == null) {
            throw new RuntimeException("Contract or file not found");
        }
        return new File(contract.getFile()).getName();
    }
}
