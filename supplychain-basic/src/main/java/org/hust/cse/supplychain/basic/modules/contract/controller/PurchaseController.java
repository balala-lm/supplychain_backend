package org.hust.cse.supplychain.basic.modules.contract.controller;

import org.hust.cse.supplychain.basic.modules.contract.po.entity.PurchaseContract;
import org.hust.cse.supplychain.basic.modules.contract.service.PurchaseService;

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
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    // 交易类合同接口
    /**
     * @author: Lu Miao
     * @date: 2025-03-20
     * @description: 创建交易合同
     * @param contract 交易合同
     * @param file 合同文件
     * @return 合同上传结果
     * @throws Exception 文件上传接口调用失败
     */
    @PostMapping("/purchase/create")
    public Result<String> createPurchaseContract(
            @RequestPart("contract") PurchaseContract contract,
            @RequestPart("file") MultipartFile file) {        
        PurchaseContract new_contract = purchaseService.createPurchaseContract(contract, file);
        if (new_contract!=null)
            return Result.success("采购合同上传成功");
        else
            return Result.error(222,"采购合同上传失败");
        
    }

    @PostMapping("/purchase/create/reverse")
    public Result<String> createPurchaseReverseContract(
            @RequestPart("contract") PurchaseContract contract,
            @RequestPart("file") MultipartFile file,
            @RequestPart("invoiceFile") MultipartFile invoiceFile) {        
        PurchaseContract new_contract = purchaseService.createPurchaseReverseContract(contract, file, invoiceFile);
        if (new_contract!=null)
            return Result.success("采购合同上传成功");
        else
            return Result.error(222,"采购合同上传失败");
        
    }


    /**
     * @author: Lu Miao
     * @date: 2025-03-20
     * @description: 更新交易合同
     * @param id 合同ID
     * @param contract 交易合同
     * @return 更新结果
     */
    @PutMapping("/purchase/update/{id}")
    public Result<String> updatePurchaseContract(
            @PathVariable Long id,
            @RequestBody PurchaseContract contract) {
        PurchaseContract new_contract = purchaseService.updatePurchaseContract(id, contract);
        if (new_contract!=null)
            return Result.success("采购合同更新成功");
        else
            return Result.error(333,"采购合同更新失败");
    }

    @DeleteMapping("/purchase/delete/{id}")
    public Result<String> deletePurchaseContract(@PathVariable Long id) {
        int result = purchaseService.deletePurchaseContract(id);
        if (result > 0)
            return Result.success("采购合同删除成功");
        else
            return Result.error(444,"采购合同删除失败");
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-20
     * @description: 获取交易合同
     * @return 交易合同列表
     */
    @GetMapping("/purchase")
    public ResponseEntity<?> getPurchaseContracts() {
        return ResponseEntity.ok(purchaseService.getPurchaseContracts());
    }


    // 交易合同查询接口
    /**
     * @author: Lu Miao
     * @date: 2025-03-20
     * @description: 根据关键字查询交易合同
     * @param keyword 关键字
     * @return 交易合同列表
     */
    @GetMapping("/purchase/search/keyword")
    public ResponseEntity<List<PurchaseContract>> searchPurchaseByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(purchaseService.searchPurchaseContractsByKeyword(keyword));
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-20
     * @description: 根据日期范围查询交易合同
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 交易合同列表
     */
    @GetMapping("/purchase/search/date")
    public ResponseEntity<List<PurchaseContract>> searchPurchaseByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(purchaseService.searchPurchaseContractsByDateRange(startDate, endDate));
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-20
     * @description: 根据状态查询交易合同
     * @param status 状态
     * @return 交易合同列表
     */
    @GetMapping("/purchase/search/status")
    public ResponseEntity<List<PurchaseContract>> searchPurchaseByStatus(@RequestParam String status) {
        return ResponseEntity.ok(purchaseService.searchPurchaseContractsByStatus(status));
    }

    /**
     * @author: Lu Miao
     * @date: 2025-03-20
     * @description: 下载交易合同文件
     * @param id 合同ID
     * @return 文件字节数组
     * @throws Exception 文件下载接口调用失败
     */
    @GetMapping("/purchase/download/{id}")
    public ResponseEntity<byte[]> downloadPurchaseContractFile(@PathVariable Long id) {
        try {
            byte[] fileContent = purchaseService.downloadFile(id);
            String fileName = purchaseService.getContractFileName(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
