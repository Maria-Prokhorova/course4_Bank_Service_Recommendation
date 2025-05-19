package org.skypro.banking_service.repositories.postgres.repository;

import org.skypro.banking_service.model.QueryRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryRepository extends JpaRepository<QueryRules, Long> {

}
