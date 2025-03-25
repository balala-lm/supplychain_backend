/**
 * @author: Lu Miao
 * @date: 2025-03-05
 * @description: 合同Mapper
 */
package org.hust.cse.supplychain.basic.modules.contract.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.PurchaseContract;


@Mapper
public interface PurchaseContractMapper extends BaseMapper<PurchaseContract> {

}
