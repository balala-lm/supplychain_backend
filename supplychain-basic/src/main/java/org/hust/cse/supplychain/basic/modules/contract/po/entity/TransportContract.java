package org.hust.cse.supplychain.basic.modules.contract.po.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;
import org.hust.cse.supplychain.basic.modules.contract.dao.handler.CompanyTypeHandler;
import org.hust.cse.supplychain.basic.modules.contract.dao.handler.ListProductsTypeHandler;
import org.hust.cse.supplychain.basic.modules.contract.po.enums.ContractStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "t_transport_contract",autoResultMap = true)
public class TransportContract{
    @TableId(type= IdType.AUTO)
    private Long id;
    private String name;
    private String number;
    @TableField(value ="carrier", typeHandler = CompanyTypeHandler.class)
    private Company carrier;//承运方
    @TableField(value ="shipper", typeHandler = CompanyTypeHandler.class)
    private Company shipper;//托运方
    @TableField("startDate")
    private LocalDate startDate; // 合同内容中的起止日期
    @TableField("endDate")
    private LocalDate endDate;
    @TableField("deliveryMethod")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod; // 运输方式
    private String origin;//起运地
    private String destination;//目的地
    @TableField(value ="products", typeHandler = ListProductsTypeHandler.class)
    private List<Product> products;
    @TableField("totalPrice")
    private double totalPrice; // 运输总价
    @TableField("signedAt")
    private LocalDateTime signedAt; // 签署时间
    private String manager;//合同经办人ID
    private String file;//合同文件路径


    private ContractStatusEnum status;
    @TableField("depositoryStatus")
    private Boolean depositoryStatus; // 是否上链存证
    @TableField("createdAt")
    @Column(name = "createdAt")
    private LocalDateTime createdAt;
    @TableField("updatedAt")
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;


    public enum DeliveryMethod {
        TRUCK,
        BOAT,
        TRAIN
    }
}
