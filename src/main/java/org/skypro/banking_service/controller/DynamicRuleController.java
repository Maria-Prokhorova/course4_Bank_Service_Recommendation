package org.skypro.banking_service.controller;

import jakarta.validation.Valid;
import org.skypro.banking_service.dto.DynamicRuleDto;
import org.skypro.banking_service.model.DynamicRule;
import org.skypro.banking_service.service.DynamicRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rules")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
    }

    @PostMapping
    public ResponseEntity<Void> createRule(@RequestBody DynamicRuleDto ruleDto) {
        dynamicRuleService.createRule(ruleDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<DynamicRuleDto> getAllRules() {
        return dynamicRuleService.getAllRules();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        dynamicRuleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}

