package org.skypro.banking_service.telegramBot.Repository;

import org.skypro.banking_service.telegramBot.model.RuleStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RuleStatRepository extends JpaRepository<RuleStat, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RuleStat r SET r.count = r.count + 1 WHERE r.ruleId = :ruleId")
    void incrementCount(@Param("ruleId") UUID ruleId);

}
