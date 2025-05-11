package org.skypro.banking_service.repositories.postgreSQL.repository;

import org.skypro.banking_service.model.QueryRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<QueryRules, Long> {

    @Query(value = "SELECT * from queries WHERE recommendations_id=?", nativeQuery = true)
    List<QueryRules> findAllQueriesByRecommendationId (long recommendationId);

}
