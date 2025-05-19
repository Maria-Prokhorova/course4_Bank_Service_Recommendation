package org.skypro.banking_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "queries")
public class QueryRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query", columnDefinition = "TEXT")
    private String query;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "query_arguments", joinColumns = @JoinColumn(name = "query_id", referencedColumnName = "id"))
    @Column(name = "argument")
    private List<String> arguments;

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

