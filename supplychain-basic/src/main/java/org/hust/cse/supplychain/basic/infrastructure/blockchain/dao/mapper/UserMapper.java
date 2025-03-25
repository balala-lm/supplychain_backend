package org.hust.cse.supplychain.basic.infrastructure.blockchain.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.User;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    @Select("SELECT t_user.address FROM t_user WHERE name = #{name}")
    String getAddressByName(String name);
}
