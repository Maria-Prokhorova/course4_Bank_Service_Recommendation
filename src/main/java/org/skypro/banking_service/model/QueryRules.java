package org.skypro.banking_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "queries")
public class QueryRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "query")
    private String query;

    @Column(name = "arguments")
    private String[] arguments;

    @Column(name = "negate")
    boolean negate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "recommendations_id")
    private Recommendation recommendations;

    public QueryRules() {
    }

    public QueryRules(String query, String[] arguments, boolean negate, Recommendation recommendations) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
        this.recommendations = recommendations;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public Recommendation getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Recommendation recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QueryRules query = (QueryRules) o;
        return negate == query.negate && Objects.equals(id, query.id) && Objects.equals(this.query, query.query) && Objects.equals(arguments, query.arguments) && Objects.equals(recommendations, query.recommendations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, arguments, negate, recommendations);
    }
}
