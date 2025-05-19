package org.skypro.banking_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_text")
    private String productText;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QueryRules> queries = new HashSet<>();

    public Recommendation(UUID productId, String productName, String productText, Set<QueryRules> queries) {
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
        this.queries = queries;

        for (QueryRules q : queries) {
            q.setRecommendation(this);
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

    public Set<QueryRules> getQueries() {
        return queries;
    }

    public void setQueries(Set<QueryRules> queries) {
        this.queries = queries;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId)
               && Objects.equals(productName, that.productName)
               && Objects.equals(productText, that.productText)
               && Objects.equals(queries, that.queries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, productText);
    }

}
