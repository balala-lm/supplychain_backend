package org.hust.cse.supplychain.basic.infrastructure.blockchain.dto;

import java.util.List;

import org.hust.cse.supplychain.basic.common.utils.IOUtil;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class EncodeDTO {
    private String funName;
    private List<Object> contractAbi = JSONObject.parseArray(IOUtil.readResourceAsString("abi/AllSigners.abi"));//合约编译后生成的abi文件内容
    private List<String> funcParam;
}
