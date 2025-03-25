package org.hust.cse.supplychain.basic.infrastructure.blockchain.dto;

import lombok.Data;

import org.hust.cse.supplychain.basic.common.utils.IOUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

@Data
public class TransactionDTO {
    private String user;
    private String contractName = "AllSigners";//合约名称
    private String contractAddress;//合约地址
    private String funcName;//方法名
    private List<Object> contractAbi = JSONObject.parseArray(IOUtil.readResourceAsString("abi/AllSigners.abi"));//合约编译后生成的abi文件内容
    private List<String> funcParam;//String数组，每个参数都使用String字符串表示，数组也需要放在双引号内
    private Integer groupId = 1;//群组ID，默认为1
    private Boolean useCns = false;//是否使用cns调用
    private String cnsName;//CNS名称，useCns为true时不能为空
    private String version;//CNS版本，useCns为true时不能为空
}
