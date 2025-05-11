package org.skypro.banking_service.repositories.postgreSQL.repository;

import org.skypro.banking_service.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository <Recommendation, Long> {

    @Query(value = "SELECT * from recommendations WHERE product_id=?", nativeQuery = true)
    Recommendation findByProductId(UUID productId);
}
