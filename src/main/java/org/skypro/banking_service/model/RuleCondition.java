package org.skypro.banking_service.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rule_conditions", schema = "public")
public class RuleCondition {

    @Id
    @GeneratedValue
    private UUID id;

    private String query;

    @ElementCollection
    private List<String> arguments;

    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "rule_id")
    private DynamicRule rule;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public DynamicRule getRule() {
        return rule;
    }

    public void setRule(DynamicRule rule) {
        this.rule = rule;
    }
}

