/**
 * @author: Lu Miao
 * @date: 2025-03-05
 * @description: 公司类
 */
package org.hust.cse.supplychain.basic.modules.contract.po.entity;

import lombok.Data;

@Data
public class Company {
    private Long id;
    private String name;
    private String signer;
    private String account;
}
