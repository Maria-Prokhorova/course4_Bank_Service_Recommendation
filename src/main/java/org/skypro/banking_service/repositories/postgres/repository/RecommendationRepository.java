package org.skypro.banking_service.repositories.postgres.repository;

import org.skypro.banking_service.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {

    @Query(value = "SELECT * FROM recommendations WHERE product_id = :productId",
            nativeQuery = true)
    Recommendation findByProductId(@Param("productId") UUID productId);
}
