package org.skypro.banking_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "statistics")
@Schema(description = "Статистика по срабатыванию рекомендаций")
public class Statistics {

    @Id
    @Schema(description = "ID правила")
    private UUID ruleId; // id правила

    @Schema(description = "Счетчик")
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

    public Long getCount() {
        return count;
    }
}
