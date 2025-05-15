package org.skypro.banking_service.telegramBot.controller;

import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.telegramBot.dto.InfoDTO;
import org.skypro.banking_service.telegramBot.dto.RuleStatsDTO;
import org.skypro.banking_service.telegramBot.dto.RuleStatsResponse;
import org.skypro.banking_service.telegramBot.service.RuleStatService;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TelegramBotController {

    private final RecommendationService recommendationService;
    private final RuleStatService ruleStatService;
    private final BuildProperties buildProperties;

    public TelegramBotController(RecommendationService recommendationService,
                                 RuleStatService ruleStatService,
                                 BuildProperties buildProperties) {
        this.recommendationService = recommendationService;
        this.ruleStatService = ruleStatService;
        this.buildProperties = buildProperties;
    }

    @GetMapping("/rule/stats")
    public ResponseEntity<RuleStatsResponse> getStats() {
        List<RuleStatsDTO> stats = ruleStatService.getAllStats();
        return ResponseEntity.ok(new RuleStatsResponse(stats));
    }

    @PostMapping("/management/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        recommendationService.clearCache();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/management/info")
    public ResponseEntity<InfoDTO> getServiceInfo() {
        return ResponseEntity.ok(
                new InfoDTO(
                        buildProperties.getName(),
                        buildProperties.getVersion()
                )
        );
    }
}
