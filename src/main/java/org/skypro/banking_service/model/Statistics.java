package org.skypro.banking_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "statistics")
public class Statistics {

    @Id
    private UUID ruleId; // id правила

    private Long count = 0L; // число срабатываний этого правила

    public Statistics(UUID ruleId, Long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public Statistics() {
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
