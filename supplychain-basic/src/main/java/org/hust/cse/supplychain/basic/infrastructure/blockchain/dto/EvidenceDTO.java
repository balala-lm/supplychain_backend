package org.hust.cse.supplychain.basic.infrastructure.blockchain.dto;

import java.util.List;

import lombok.Data;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.enums.EvidenceType;

@Data
public class EvidenceDTO {
    private Object evidence;
    private EvidenceType type;
    private String relatedId;
    private String user;
    private List<String> signers;
}
