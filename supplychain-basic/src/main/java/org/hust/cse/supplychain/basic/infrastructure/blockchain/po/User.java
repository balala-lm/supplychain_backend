package org.hust.cse.supplychain.basic.infrastructure.blockchain.po;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String address;
    @TableField("publickey")
    private String publicKey;
    @TableField("privatekey")
    private String privateKey;
    private int type=0;//0-本地用户；1-本地随机；2-外部用户；默认为0
}
