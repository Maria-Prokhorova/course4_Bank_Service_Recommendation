package org.skypro.banking_service.repositories.postgres.repository;

import org.skypro.banking_service.model.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueriesRepository extends JpaRepository<Queries, Long> {
    List<Queries> findAllByRecommendationsId(Long recommendationId);
}
