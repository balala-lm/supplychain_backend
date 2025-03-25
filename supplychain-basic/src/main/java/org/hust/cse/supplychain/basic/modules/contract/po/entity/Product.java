/**
 * @author: Lu Miao
 * @date: 2025-03-05
 * @description: 产品类
 */
package org.hust.cse.supplychain.basic.modules.contract.po.entity;

import lombok.Data;

@Data
public class Product {
    private String name; // 品名
    private double weight; // 重量
    private double unitPrice; // 含税单价
}
