package org.skypro.banking_service.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import java.util.List;

@Entity
@Table(name = "dynamic_rules", schema = "public")
public class DynamicRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String productName;

    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RuleCondition> conditions = new ArrayList<>();

    public DynamicRule() {
    }

    public DynamicRule(String productName, List<RuleCondition> conditions) {
        this.productName = productName;
        this.conditions = conditions;
        this.conditions.forEach(condition -> condition.setRule(this));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<RuleCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RuleCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DynamicRule that = (DynamicRule) o;
        return Objects.equals(id, that.id) && Objects.equals(productName, that.productName) && Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, conditions);
    }
}
