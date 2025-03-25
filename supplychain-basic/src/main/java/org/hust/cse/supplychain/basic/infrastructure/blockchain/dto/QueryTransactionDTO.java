package org.hust.cse.supplychain.basic.infrastructure.blockchain.dto;

import java.util.List;

import org.hust.cse.supplychain.basic.common.utils.IOUtil;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class QueryTransactionDTO {
    private String user;
    @Value("${contract.address}")
    private String contractAddress;//合约地址
    private String funcName;//方法名
    private List<Object> contractAbi = JSONObject.parseArray(IOUtil.readResourceAsString("abi/AllSigners.abi"));//合约编译后生成的abi文件内容
    private Integer groupId = 1;//群组ID，默认为1
}
