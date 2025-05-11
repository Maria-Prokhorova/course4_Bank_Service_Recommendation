package org.skypro.banking_service.rule_system.dinamic_rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.model.QueryRules;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.repositories.postgreSQL.repository.QueryRepository;
import org.skypro.banking_service.repositories.postgreSQL.repository.RecommendationRepository;
import org.skypro.banking_service.rule_system.dinamic_rules.parameter.QueryParameters;
import org.skypro.banking_service.rule_system.dinamic_rules.queries.DimanicQueryExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DimanicRuleImp implements DimanicRule {

    private final RecommendationRepository recommendationRepository;
    private final QueryRepository queryRepository;
    private final List<DimanicQueryExecutor> queryExecutorList;

    public static int FLAG = 0;

    public DimanicRuleImp(RecommendationRepository recommendationRepository, QueryRepository queryRepository, List<DimanicQueryExecutor> queryExecutorList) {
        this.recommendationRepository = recommendationRepository;
        this.queryRepository = queryRepository;
        this.queryExecutorList = queryExecutorList;
    }

    @Override
    public List<RecommendationDto> checkOutDinamicRule(UUID userId) {

        List<Recommendation> allRecommendations = recommendationRepository.findAll();
        List<RecommendationDto> checkRecommendations = new ArrayList<>();

        allRecommendations.forEach(recommendation -> {

            List<QueryRules> queriesByRecommendation = queryRepository.findAllQueriesByRecommendationId(recommendation.getId());
            FLAG = 0;

            queriesByRecommendation.forEach(queryRules -> {
                DimanicQueryExecutor executor = queryExecutorList.stream()
                        .filter(e -> e.checkOutNameQuery(queryRules.getQuery()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No executor for query: " + queryRules.getQuery()));

                String[] arguments = queryRules.getArguments();
                String typeProduct = null;
                String typeTransaction = null;
                String typeOperator = null;
                int limit = 0;
                if (arguments.length == 4) {
                    typeProduct = arguments[0];
                    typeTransaction = arguments[1];
                    typeOperator = arguments[2];
                    limit = Integer.parseInt(arguments[3]);
                }
                if (arguments.length == 2) {
                    typeProduct = arguments[0];
                    typeOperator = arguments[1];
                }
                if (arguments.length == 1) {
                    typeProduct = arguments[0];
                }

                if (executor.checkOutQuery(QueryParameters.of(userId, typeProduct, typeTransaction, typeOperator, limit, queryRules.isNegate()))) {
                    FLAG++;
                }
            });
            if (FLAG == 3) {
                checkRecommendations.add(new RecommendationDto(recommendation.getProductName(), String.valueOf(recommendation.getProductId()), recommendation.getProductText()));
            }
        });

        return checkRecommendations;
    }

    /*
    private QueryParameters checkParameters(String[] arguments) {
        String typeProduct = null;
        String typeTransaction = null;
        String typeOperator = null;
        int limit = 0;
        if (arguments.length == 4) {
            typeProduct = arguments[0];
            typeTransaction = arguments[1];
            typeOperator = arguments[2];
            limit = Integer.parseInt(arguments[3]);
        }
        if (arguments.length == 2) {
            typeProduct = arguments[0];
            typeOperator = arguments[1];
        }
        if (arguments.length == 1) {
            typeProduct = arguments[0];
        }
        return QueryParameters.of()
    }*/

}
