package org.hust.cse.supplychain.basic.modules.contract.po.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.Data;
import org.hust.cse.supplychain.basic.modules.contract.dao.handler.CompanyTypeHandler;
import org.hust.cse.supplychain.basic.modules.contract.dao.handler.ListProductsTypeHandler;
import org.hust.cse.supplychain.basic.modules.contract.po.enums.ContractStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "t_purchase_contract",autoResultMap = true)
public class PurchaseContract {
    @TableId(type= IdType.AUTO)
    private Long id;
    private String name;
    private String number;
    @TableField(value ="seller", typeHandler = CompanyTypeHandler.class)
    private Company seller;//卖方
    @TableField(value ="buyer", typeHandler = CompanyTypeHandler.class)
    private Company buyer;//买方
    @TableField(value ="products", typeHandler = ListProductsTypeHandler.class)
    private List<Product> products;
    @TableField("totalPrice")
    private double totalPrice;
    @TableField("taxRate")
    private double taxRate; // 税率
    @TableField("invoiceType")
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType; // ENUM('forward','reverse')
    @TableField("paymentDate")
    private LocalDate paymentDate;//付款日期
    @TableField("paymentMethod")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // ENUM('contracting', 'loading', 'billing', 'paybacks')付款方式
    @TableField("startDate")
    private LocalDate startDate; // 履约期限
    @TableField("endDate")
    private LocalDate endDate;
    private String location; //履约地点
    @TableField("deliveryMethod")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod; // 履约方式：自提或送货ENUM('self-pickup', 'delivery')
    @TableField("signedAt")
    private LocalDate signedAt; // 签定时间
    private String manager;//合同经办人ID
    @TableField("invoiceFile")
    private String invoiceFile;//反向开票同意书路径
    private String file;//合同文件路径

    private ContractStatusEnum status;//合同状态
    @TableField("depositoryStatus")
    private Boolean depositoryStatus; // 是否上链存证
    @TableField("createdAt")
    @Column(name = "createdAt")
    private LocalDateTime createdAt; //创建时间
    @TableField("updatedAt")
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt; //更新时间

    public enum DeliveryMethod {
        SELF_PICKUP,
        DELIVERY
    }
    public enum InvoiceType {
        FORWARD,
        REVERSE
    }
    public enum PaymentMethod {
        CONTRACTING, // 签订合同预付款
        LOADING,    // 装车预付款
        BILLING,    // 结算单付款
        PAYBACKS    // 钢厂回款后付款
    }
}
