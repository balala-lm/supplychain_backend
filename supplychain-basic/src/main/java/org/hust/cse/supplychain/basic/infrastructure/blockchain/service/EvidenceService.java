package org.hust.cse.supplychain.basic.infrastructure.blockchain.service;

import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.EvidenceDTO;

import java.util.List;

import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.AddSignDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.ValidateDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.enums.EvidenceType;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.vo.EvidenceVO;

public interface EvidenceService {
    void register(String name) throws Exception;
    void createEvidence(EvidenceDTO evidenceDTO) throws Exception;
    void addSign(AddSignDTO addSignDTO) throws Exception;
    boolean validateEvidence(ValidateDTO validate) throws Exception;
    List<EvidenceVO> getEvidence() throws Exception;
    List<EvidenceVO> getEvidenceByType(EvidenceType type) throws Exception;
    List<EvidenceVO> getEvidenceByUser(String user) throws Exception;
    List<EvidenceVO> getEvidenceByKeyword(String keyword) throws Exception;
}
