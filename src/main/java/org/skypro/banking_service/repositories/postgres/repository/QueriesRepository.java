package org.skypro.banking_service.repositories.postgres.repository;

import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueriesRepository extends JpaRepository<Queries, Long> {
}
