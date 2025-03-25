package org.hust.cse.supplychain.basic.modules.contract.service;

import org.hust.cse.supplychain.basic.modules.contract.po.entity.SalesContract;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface SalesService {
    // 销售合同操作
    List<SalesContract> getSalesContracts();
    SalesContract createSalesContract(SalesContract contract, MultipartFile file);
    SalesContract updateSalesContract(Long contractId, SalesContract contract);
    int deleteSalesContract(Long id);
    List<SalesContract> searchSalesContractsByKeyword(String keyword);
    List<SalesContract> searchSalesContractsByDateRange(LocalDate startDate, LocalDate endDate);
    List<SalesContract> searchSalesContractsByStatus(String status);
    byte[] downloadFile(Long id) throws IOException;
    String getContractFileName(Long id);
}
