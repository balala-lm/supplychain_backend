/**
 * @author: Lu Miao
 * @date: 2025-03-05
 * @description: 合同状态枚举类
 */
package org.hust.cse.supplychain.basic.modules.contract.po.enums;

public enum ContractStatusEnum {
    PENDING_APPROVAL(10, "待审批", "合同等待审批"),
    REJECTED(20, "已驳回", "合同被驳回"),
    CANCELLED(30, "已取消", "合同已取消"),
    PENDING_SIGN(40, "待签署", "合同已审批通过，等待签署"),
    SIGNED(50, "已签署", "合同已完成签署");

    private final int code;
    private final String name;
    private final String description;

    ContractStatusEnum(int code, String name, String description) {
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
