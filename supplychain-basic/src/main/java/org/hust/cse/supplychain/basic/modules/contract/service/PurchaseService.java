package org.hust.cse.supplychain.basic.modules.contract.service;

import org.hust.cse.supplychain.basic.modules.contract.po.entity.PurchaseContract;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface PurchaseService {
    // 采购合同操作
    List<PurchaseContract> getPurchaseContracts();
    PurchaseContract createPurchaseReverseContract(PurchaseContract contract, MultipartFile file, MultipartFile invoiceFile);
    PurchaseContract createPurchaseContract(PurchaseContract contract, MultipartFile file);
    PurchaseContract updatePurchaseContract(Long id, PurchaseContract contract);
    int deletePurchaseContract(Long id);
    List<PurchaseContract> searchPurchaseContractsByKeyword(String keyword);
    List<PurchaseContract> searchPurchaseContractsByDateRange(LocalDate startDate, LocalDate endDate);
    List<PurchaseContract> searchPurchaseContractsByStatus(String status);
    byte[] downloadFile(Long id) throws IOException;
    String getContractFileName(Long id);
}
