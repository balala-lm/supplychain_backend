package org.hust.cse.supplychain.basic.modules.contract.controller;

import org.hust.cse.supplychain.basic.modules.contract.po.entity.SalesContract;
import org.hust.cse.supplychain.basic.modules.contract.service.SalesService;

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
public class SalesController {
    @Autowired
    private SalesService salesService;


    // 交易类合同接口
    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 创建交易合同
     * @param contract 交易合同
     * @param file 合同文件
     * @return 合同上传结果
     * @throws Exception 文件上传接口调用失败
     */
    @PostMapping("/sales/create")
    public Result<String> createSalesContract(
            @RequestPart("contract") SalesContract contract,
            @RequestPart("file") MultipartFile file) {        
        SalesContract new_contract = salesService.createSalesContract(contract, file);
        if (new_contract!=null)
            return Result.success("销售合同上传成功");
        else
            return Result.error(222,"销售合同上传失败");
        
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 更新交易合同
     * @param id 合同ID
     * @param contract 交易合同
     * @return 更新结果
     */
    @PutMapping("/sales/update/{id}")
    public Result<String> updateSalesContract(
            @PathVariable Long id,
            @RequestBody SalesContract contract) {
        SalesContract new_contract = salesService.updateSalesContract(id, contract);
        if (new_contract!=null)
            return Result.success("销售合同更新成功");
        else
            return Result.error(333,"销售合同更新失败");
    }

    @DeleteMapping("/sales/delete/{id}")
    public Result<String> deleteSalesContract(@PathVariable Long id) {
        int result = salesService.deleteSalesContract(id);
        if (result > 0)
            return Result.success("销售合同删除成功");
        else
            return Result.error(444,"销售合同删除失败");
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 获取交易合同
     * @return 交易合同列表
     */
    @GetMapping("/sales")
    public ResponseEntity<?> getSalesContracts() {
        return ResponseEntity.ok(salesService.getSalesContracts());
    }


    // 交易合同查询接口
    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 根据关键字查询交易合同
     * @param keyword 关键字
     * @return 交易合同列表
     */
    @GetMapping("/sales/search/keyword")
    public ResponseEntity<List<SalesContract>> searchSalesByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(salesService.searchSalesContractsByKeyword(keyword));
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 根据日期范围查询交易合同
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 交易合同列表
     */
    @GetMapping("/sales/search/date")
    public ResponseEntity<List<SalesContract>> searchSalesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(salesService.searchSalesContractsByDateRange(startDate, endDate));
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-05
     * @description: 根据状态查询交易合同
     * @param status 状态
     * @return 交易合同列表
     */
    @GetMapping("/sales/search/status")
    public ResponseEntity<List<SalesContract>> searchSalesByStatus(@RequestParam String status) {
        return ResponseEntity.ok(salesService.searchSalesContractsByStatus(status));
    }

    @GetMapping("/sales/download/{id}")
    public ResponseEntity<byte[]> downloadPurchaseContractFile(@PathVariable Long id) {
        try {
            byte[] fileContent = salesService.downloadFile(id);
            String fileName = salesService.getContractFileName(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
