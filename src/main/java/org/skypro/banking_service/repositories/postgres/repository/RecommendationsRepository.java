package org.skypro.banking_service.repositories.postgres.repository;

import org.skypro.banking_service.model.Recommendations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationsRepository extends JpaRepository <Recommendations, Long> {

    @Query(value = "SELECT * from recommendations WHERE product_id=?", nativeQuery = true)
    Recommendations findByProductId(UUID productId);
}
