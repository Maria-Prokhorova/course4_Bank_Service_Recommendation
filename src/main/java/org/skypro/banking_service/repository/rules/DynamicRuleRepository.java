package org.skypro.banking_service.repository.rules;

import org.skypro.banking_service.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional("rulesTransactionManager")
public interface DynamicRuleRepository extends JpaRepository <DynamicRule, UUID> {
}
