package org.skypro.banking_service.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_text")
    private String productText;

    @OneToMany(mappedBy = "recommendations", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QueryRules> rule = new ArrayList<>();

    public Recommendation(UUID productId, String productName, String productText, List<QueryRules> rule) {
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
        this.rule = rule;

        for (QueryRules q : rule) {
            q.setRecommendations(this);
        }
    }

    public Recommendation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<QueryRules> getRule() {
        return rule;
    }

    public void setRule(List<QueryRules> rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(productName, that.productName) && Objects.equals(productText, that.productText) && Objects.equals(rule, that.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, productText, rule);
    }

    @Override
    public String toString() {
        return "Recommendations{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productText='" + productText + '\'' +
                ", rule=" + rule +
                '}';
    }
}
