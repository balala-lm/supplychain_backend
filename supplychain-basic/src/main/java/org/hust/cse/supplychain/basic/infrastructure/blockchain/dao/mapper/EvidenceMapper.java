package org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.Evidence;

@Mapper
public interface EvidenceMapper extends BaseMapper<Evidence> {
}
