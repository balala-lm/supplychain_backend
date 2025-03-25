package org.hust.cse.supplychain.basic.infrastructure.blockchain.controller;

import java.util.List;

import org.hust.cse.supplychain.basic.common.response.Result;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.AddSignDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.EvidenceDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.dto.ValidateDTO;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.po.enums.EvidenceType;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.service.EvidenceService;
import org.hust.cse.supplychain.basic.infrastructure.blockchain.vo.EvidenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evidence")
public class EvidenceController {

    @Autowired
    private EvidenceService evidenceService;

    @PostMapping("/register")
    public Result<String> register(@RequestParam String name){
        try {
            evidenceService.register(name);
            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @PostMapping("/create")
    public Result<String> createEvidence(@RequestBody EvidenceDTO evidenceDTO) {
        try {
            evidenceService.createEvidence(evidenceDTO);
            return Result.success("Evidence created successfully");
        } catch (Exception e) {
            return Result.error(111,e.getMessage());
        }
    }

    @PostMapping("/addSign")
    public Result<String> addSign(@RequestBody AddSignDTO addSignDTO) {
        try {
            evidenceService.addSign(addSignDTO);
            return Result.success("Sign added successfully");
        } catch (Exception e) {
            return Result.error(111,e.getMessage());
        }
    }

    @PostMapping("/validate")
    public Result<String> validateEvidence(@RequestBody ValidateDTO validateDTO) {
        try {
            boolean isValid = evidenceService.validateEvidence(validateDTO);
            if (isValid) {
                return Result.success("Evidence validated successfully");
            } else {
                return Result.error(222, "Evidence does not match");
            }
        } catch (Exception e) {
            return Result.error(111,e.getMessage());
        }
    }

    @GetMapping("/get")
    public Result<List<EvidenceVO>> getEvidence() {
        try {
            List<EvidenceVO> evidenceVOList = evidenceService.getEvidence();
            return Result.success(evidenceVOList);
        } catch (Exception e) {
            return Result.error(111,e.getMessage());
        }
    }

    @GetMapping("/get/keyword")
    public Result<List<EvidenceVO>> getEvidenceByKeyword(@RequestParam String keyword) {
        try {
            List<EvidenceVO> evidenceVOList = evidenceService.getEvidenceByKeyword(keyword);
            return Result.success(evidenceVOList);
        } catch (Exception e) {
            return Result.error(111,e.getMessage());
        }
    }

    @GetMapping("/get/user")
    public Result<List<EvidenceVO>> getEvidenceByUser(@RequestParam String user) {
        try {
            List<EvidenceVO> evidenceVOList = evidenceService.getEvidenceByUser(user);
            return Result.success(evidenceVOList);
        } catch (Exception e) {
            return Result.error(111,e.getMessage());
        }
    }

    @GetMapping("/get/type")
    public Result<List<EvidenceVO>> getEvidenceByType(@RequestParam EvidenceType type) {
        try {
            List<EvidenceVO> evidenceVOList = evidenceService.getEvidenceByType(type);
            return Result.success(evidenceVOList);
        } catch (Exception e) {
            return Result.error(111,e.getMessage());
        }
    }
}
