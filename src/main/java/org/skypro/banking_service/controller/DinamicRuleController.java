package org.skypro.banking_service.controller;

import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.service.RecommendationWithRulesServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class DinamicRuleController {
    private final RecommendationWithRulesServiceImpl dinamicRulesService;

    public DinamicRuleController(RecommendationWithRulesServiceImpl dinamicRulesService) {
        this.dinamicRulesService = dinamicRulesService;
    }

    @PostMapping
    public Recommendation createRecommendation(@RequestBody Recommendation recommendation) {
        return dinamicRulesService.addRecommendationByRule(recommendation);
    }

    @GetMapping
    public List<Recommendation> getAllRecommendations() {
        return dinamicRulesService.getAllRecommendationByRule();
    }

    @DeleteMapping("{productId}")
    public void deleteFaculty(@PathVariable UUID productId) {
        dinamicRulesService.deleteRecommendationByRule(productId);
    }


}
