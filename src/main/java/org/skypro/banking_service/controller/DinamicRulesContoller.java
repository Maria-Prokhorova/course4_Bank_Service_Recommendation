package org.skypro.banking_service.controller;

import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.service.impl.DinamicRulesServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class DinamicRulesContoller {
    private final DinamicRulesServiceImpl dinamicRulesService;

    public DinamicRulesContoller(DinamicRulesServiceImpl dinamicRulesService) {
        this.dinamicRulesService = dinamicRulesService;
    }

    @PostMapping
    public Recommendations createRecommendation(@RequestBody Recommendations recommendation) {
        return dinamicRulesService.addRecommendationByRule(recommendation);
    }

    @GetMapping()
    public List<Recommendations> getAllRecommendations() {
        return dinamicRulesService.getAllRecommendationByRule();
    }

    @DeleteMapping("{productId}")
    public void deleteFaculty(@PathVariable UUID productId) {
        dinamicRulesService.deleteRecommendationByRule(productId);
    }


}
