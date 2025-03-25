package org.hust.cse.supplychain.basic.infrastructure.blockchain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.Log;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.enums.EvidenceType;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class EvidenceVO {
    private String number;
    private EvidenceType type;
    private String relatedId;
    private String user;
    private List<String> signers;
    
    // 使用自定义的日志VO类，包含格式化的时间
    private List<LogVO> logs;
    
    // 内部类，用于格式化日志中的时间
    @Data
    public static class LogVO {
        private String user;
        private String action;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String timestamp;
        
        // 从Log转换为LogVO的构造函数
        public LogVO(Log log) {
            this.user = log.getUser();
            this.action = log.getAction();
            
            // 格式化时间
            if (log.getTimestamp() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                this.timestamp = log.getTimestamp().format(formatter);
            }
        }
    }
    
    // 设置logs时进行格式转换
    public void setLogs(List<Log> logs) {
        if (logs == null) {
            this.logs = null;
            return;
        }
        
        this.logs = new ArrayList<>();
        for (Log log : logs) {
            this.logs.add(new LogVO(log));
        }
    }
}
