package org.hust.cse.supplychain.basic.modules.contract.controller;

import org.hust.cse.supplychain.basic.modules.contract.po.entity.TransportContract;
import org.hust.cse.supplychain.basic.modules.contract.service.TransportService;

import java.io.IOException;
import java.time.LocalDate;

import org.hust.cse.supplychain.basic.common.response.Result;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/contracts")
public class TransportController {
    @Autowired
    private TransportService transportService;

    // 交易类合同接口
    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 创建交易合同
     * @param contract 交易合同
     * @param file 合同文件
     * @return 合同上传结果
     * @throws IOException 文件处理错误
     * @throws Exception 文件上传接口调用失败
     */
    @PostMapping("/transport/create")
    public Result<String> createTransportContract(
            @RequestPart("contract") TransportContract contract,
            @RequestPart("file") MultipartFile file) {        
        TransportContract new_contract = transportService.createTransportContract(contract, file);
        if (new_contract!=null)
            return Result.success("运输合同上传成功");
        else
            return Result.error(222,"运输合同上传失败");
        
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 更新交易合同
     * @param id 合同ID
     * @param contract 交易合同
     * @return 更新结果
     */
    @PutMapping("/transport/update/{id}")
    public Result<String> updateTransportContract(
            @PathVariable Long id,
            @RequestBody TransportContract contract) {
        TransportContract new_contract = transportService.updateTransportContract(id, contract);
        if (new_contract!=null)
            return Result.success("运输合同更新成功");
        else
            return Result.error(333,"运输合同更新失败");
    }

    @DeleteMapping("/transport/delete/{id}")
    public Result<String> deleteTransportContract(@PathVariable Long id) {
        int result = transportService.deleteTransportContract(id);
        if (result > 0)
            return Result.success("运输合同删除成功");
        else
            return Result.error(444,"运输合同删除失败");
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 获取交易合同
     * @return 交易合同列表
     */
    @GetMapping("/transport")
    public ResponseEntity<?> getTransportContracts() {
        return ResponseEntity.ok(transportService.getTransportContracts());
    }


    // 交易合同查询接口
    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 根据关键字查询交易合同
     * @param keyword 关键字
     * @return 交易合同列表
     */
    @GetMapping("/transport/search/keyword")
    public ResponseEntity<List<TransportContract>> searchTransportByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(transportService.searchTransportContractsByKeyword(keyword));
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 根据日期范围查询交易合同
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 交易合同列表
     */
    @GetMapping("/transport/search/date")
    public ResponseEntity<List<TransportContract>> searchTransportByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(transportService.searchTransportContractsByDateRange(startDate, endDate));
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 根据状态查询交易合同
     * @param status 状态
     * @return 交易合同列表
     */
    @GetMapping("/transport/search/status")
    public ResponseEntity<List<TransportContract>> searchTransportByStatus(@RequestParam String status) {
        return ResponseEntity.ok(transportService.searchTransportContractsByStatus(status));
    }

    @GetMapping("/transport/download/{id}")
    public ResponseEntity<byte[]> downloadPurchaseContractFile(@PathVariable Long id) {
        try {
            byte[] fileContent = transportService.downloadFile(id);
            String fileName = transportService.getContractFileName(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
