package org.skypro.banking_service.repositories.postgreSQL.repository;

import org.skypro.banking_service.model.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {
}
