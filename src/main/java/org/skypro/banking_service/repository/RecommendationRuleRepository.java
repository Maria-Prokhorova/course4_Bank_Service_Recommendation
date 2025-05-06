package org.skypro.banking_service.repository;

import org.skypro.banking_service.model.Recommendations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRuleRepository extends JpaRepository <Recommendations, Long> {
}
