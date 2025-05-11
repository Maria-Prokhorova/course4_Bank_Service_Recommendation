package org.skypro.banking_service.controller;

import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.service.DynamicRulesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class DynamicRulesController {
    private final DynamicRulesService dynamicRulesService;

    public DynamicRulesController(DynamicRulesService dynamicRulesService) {
        this.dynamicRulesService = dynamicRulesService;
    }

    @PostMapping
    public Recommendations createRecommendation(@RequestBody Recommendations recommendation) {
        return dynamicRulesService.addRecommendationByRule(recommendation);
    }

    @GetMapping
    public List<Recommendations> getAllRecommendations() {
        return dynamicRulesService.getAllRecommendationByRule();
    }

    @DeleteMapping("{productId}")
    public void deleteRecommendation(@PathVariable UUID productId) {
        dynamicRulesService.deleteRecommendationByRule(productId);
    }


}
