package org.hust.cse.supplychain.basic.infrastructure.blockchain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_record", autoResultMap = true)
public class Record {
    private Long id;
    private String type;
    private String value;
    
    // 在计算哈希值时排除此字段
    private String address;
}
