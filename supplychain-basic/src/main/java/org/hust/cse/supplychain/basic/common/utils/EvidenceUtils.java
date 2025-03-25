package org.hust.cse.supplychain.basic.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.TransactionDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.Record;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EvidenceUtils {

    @Value("${system.url}")
    private String serverHost;
    @Value("${system.evidenceAddress}")
    private String evidenceAddress;

    public String getHash(Record record) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", record.getId());
        jsonObject.put("type", record.getType());
        jsonObject.put("value", record.getValue());
    
        String str = jsonObject.toJSONString();
        System.out.println("Hash input: " + str);
        return "0x" + org.apache.commons.codec.digest.DigestUtils.sha256Hex(str);
    }
    
    public String getArrayHash(List<Record> records) {
        JSONArray objectsJson = JSONArray.parseArray(JSONObject.toJSONString(records));
        String str = objectsJson.toJSONString();
        System.out.println("str: " + str);
        return "0x"+org.apache.commons.codec.digest.DigestUtils.sha256Hex(str);
    }

    public List<Record> toRecords(Object objects, Long id) {
        List<Record> records = new ArrayList<>();     
        if (objects != null) {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(objects);

            System.out.println("jsonObject: " + jsonObject);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (entry.getValue() != null) {
                    Record record = new Record();
                    record.setId(id);
                    record.setType(entry.getKey());
                    record.setValue(String.valueOf(entry.getValue()));
                    records.add(record);
                }
            }
        }
        return records;
    }

    public JSONObject sendTransaction(TransactionDTO transactionDTO) {
    
        RestTemplate restTemplate = new RestTemplate();
        String url = serverHost + "/trans/handle";
        transactionDTO.setContractAddress(evidenceAddress);
        // 调用外部接口
        ResponseEntity<String> response = restTemplate.postForEntity(url, transactionDTO, String.class);
        if (response.getStatusCode().is2xxSuccessful())
            System.out.println("Request successful");
        else
            System.out.println("Request failed with status code: " + response.getStatusCode());
        
        JSONObject jsonObject = JSON.parseObject(response.getBody());
        return jsonObject;
    }
    public String queryTransaction(TransactionDTO transactionDTO) {
        RestTemplate restTemplate = new RestTemplate();
        String url = serverHost + "/trans/handle";
        transactionDTO.setContractAddress(evidenceAddress);
        ResponseEntity<String> response = restTemplate.postForEntity(url, transactionDTO, String.class);
        System.out.println("response: " + response.getBody());  
        return response.getBody();
    }
}
