package org.hust.cse.supplychain.basic.infrastructure.blockchain.po.enums;

public enum EvidenceType {
    PURCHASE_CONTRACT(10, "采购合同", "采购合同"),
    SALES_CONTRACT(20, "销售合同", "销售合同"),
    TRANSPORT_CONTRACT(30, "运输合同", "运输合同"),
    INVOICE(40, "发票", "发票信息"),
    PAYMENT(50, "付款记录", "付款信息"),
    PURCHASE_LEDGER(60, "台账", "采购台账"),
    SALES_LEDGER(70, "台账", "销售台账"),
    TRANSPORT_LEDGER(80, "台账", "运输台账");

    private final int code;
    private final String name;
    private final String description;

    EvidenceType(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
