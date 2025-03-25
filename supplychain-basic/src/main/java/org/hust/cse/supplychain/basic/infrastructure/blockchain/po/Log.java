package org.hust.cse.supplychain.basic.infrastructure.blockchain.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Log {
    private String user;
    private LocalDateTime timestamp;
    private String action;
}
