package org.hust.cse.supplychain.basic.modules.contract.service;

import org.hust.cse.supplychain.basic.modules.contract.po.entity.TransportContract;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface TransportService {
    // 运输合同操作
    List<TransportContract> getTransportContracts();
    TransportContract createTransportContract(TransportContract contract, MultipartFile file);
    TransportContract updateTransportContract(Long contractId, TransportContract contract);
    int deleteTransportContract(Long id);
    List<TransportContract> searchTransportContractsByKeyword(String keyword);
    List<TransportContract> searchTransportContractsByDateRange(LocalDate startDate, LocalDate endDate);
    List<TransportContract> searchTransportContractsByStatus(String status);
    byte[] downloadFile(Long id) throws IOException;
    String getContractFileName(Long id);
}
