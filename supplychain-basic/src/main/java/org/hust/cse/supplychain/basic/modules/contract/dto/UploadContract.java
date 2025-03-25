package org.hust.cse.supplychain.basic.modules.contract.dto;

import lombok.Data;

@Data
public class UploadContract {
    private String account;
    private String fmd5;
    private String fdata;
    private String fpages;
    private String fname;
    private String ftype="PDF";
    private String title;
    private String description;
    private String expireTime;
    private String hotStoragePeriod;
}
