package org.hust.cse.supplychain.basic.infrastructure.blockchain.dto;


import lombok.Data;

@Data
public class AddSignDTO {
    private Long evidenceId;
    private String signer;
}
