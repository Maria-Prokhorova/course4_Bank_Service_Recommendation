/*
package org.skypro.banking_service.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class RulesLiquibaseConfig {

    @Bean
    public SpringLiquibase rulesLiquibase(@Qualifier("secondaryDataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:/liquibase/changelog-master.yml");
        liquibase.setContexts("postgres");
        return liquibase;
    }
}*/
