package org.hust.cse.supplychain.basic.infrastructure.blockchain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.handler.ListLogsTypeHandler;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.handler.ListStringTypeHandler;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.enums.EvidenceType;

import java.util.List;

@TableName(value = "t_evidence",autoResultMap = true)
@Data
public class Evidence {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String number;
    private EvidenceType type;
    @TableField("relatedId")
    private String relatedId;
    @TableField(value ="logs", typeHandler = ListLogsTypeHandler.class)
    private List<Log> logs;
    private String user;
    @TableField(value ="signers", typeHandler = ListStringTypeHandler.class)
    private List<String> signers;
}
