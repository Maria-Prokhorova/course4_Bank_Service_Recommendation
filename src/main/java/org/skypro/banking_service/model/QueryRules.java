package org.skypro.banking_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "queries")
@Schema(description = "Запрос в динамическом правиле банковского продукта.")
public class QueryRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Тип запроса", example = "USER_OF, ACTIVE_USER_OF, TRANSACTION_SUM_COMPARE, TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW")
    @Column(name = "query", columnDefinition = "TEXT")
    private String query;

    @Schema(description = "Аргументы запроса. Здесь указываются: тип продукта, тип транзакции, тип сравнения и число, с которым выполняется сравнение")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "query_arguments", joinColumns = @JoinColumn(name = "query_id", referencedColumnName = "id"))
    @Column(name = "argument")
    private List<String> arguments;

    @Schema(description = "Модификатор отрицания", example = "true/false")
    @Column(name = "negate")
    boolean negate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recommendations_id")
    private Recommendation recommendation;

    public QueryRules() {
    }

    public QueryRules(String query, List<String> arguments, boolean negate, Recommendation recommendation) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
        this.recommendation = recommendation;
    }

    public String getQuery() {
        return query;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QueryRules queries = (QueryRules) o;
        return Objects.equals(id, queries.id) && Objects.equals(query, queries.query) && Objects.equals(arguments, queries.arguments) && Objects.equals(recommendation, queries.recommendation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, arguments);
    }
}

