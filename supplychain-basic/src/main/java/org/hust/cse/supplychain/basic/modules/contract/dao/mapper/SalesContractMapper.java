/**
 * @author: Lu Miao
 * @date: 2025-03-05
 * @description: 交易合同Mapper
 */
package org.hust.cse.supplychain.basic.modules.contract.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hust.cse.supplychain.basic.modules.contract.po.entity.SalesContract;


@Mapper
public interface SalesContractMapper extends BaseMapper<SalesContract> {

}
