package org.skypro.banking_service.repositories.postgres.repository;

import org.skypro.banking_service.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface StatisticsRepository extends JpaRepository<Statistics, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE statistics r SET r.count = r.count + 1 WHERE r.ruleId = :ruleId",  nativeQuery = true)
    void incrementCounter(@Param("ruleId") UUID ruleId);
}
