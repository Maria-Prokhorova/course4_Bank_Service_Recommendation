package org.skypro.banking_service.repositories.postgres.repository;

import org.skypro.banking_service.model.Recommendations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationsRepository extends JpaRepository<Recommendations, UUID> {

    @Query(value = "SELECT * FROM recommendations WHERE product_id = :productId",
            nativeQuery = true)
    Recommendations findByProductId(@Param("productId") UUID productId);
}
