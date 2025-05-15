package org.skypro.banking_service.telegramBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "rule_stat")
@Data
public class RuleStat {
    @Id
    private UUID ruleId;

    private Long count = 0L;

    public RuleStat(UUID ruleId, Long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public RuleStat() {

    }
}
