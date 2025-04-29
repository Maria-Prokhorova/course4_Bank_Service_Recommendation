package org.skypro.banking_service.controller;

import org.skypro.banking_service.repository.RecommendationsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationsRepository repository;

    public RecommendationController(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/user_id")
    public int findRecommendation(@RequestParam UUID id) {
        return repository.getRandomTransactionAmount(id);
    }
}
