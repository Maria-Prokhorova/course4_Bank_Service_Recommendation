package org.skypro.banking_service.service.impl;

import org.skypro.banking_service.dto.DynamicRuleDto;
import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.model.DynamicRule;
import org.skypro.banking_service.repository.UserTransactionRepository;
import org.skypro.banking_service.ruleSystem.dynamicRuleSystem.condition.DynamicRuleEvaluator;
import org.skypro.banking_service.service.DynamicRuleService;
import org.skypro.banking_service.service.RecommendationService;
import org.skypro.banking_service.ruleSystem.staticRuleSystem.RecommendationRule;
import org.skypro.banking_service.mapper.DynamicRuleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final UserTransactionRepository repository;
    private final List<RecommendationRule> listRules;
    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleEvaluator dynamicRuleEvaluator;
    private final DynamicRuleMapper mapper;

    public RecommendationServiceImpl(UserTransactionRepository repository,
                                     List<RecommendationRule> listRules,
                                     DynamicRuleService dynamicRuleService,
                                     DynamicRuleEvaluator dynamicRuleEvaluator,
                                     DynamicRuleMapper mapper) {
        this.repository = repository;
        this.listRules = listRules;
        this.dynamicRuleService = dynamicRuleService;
        this.dynamicRuleEvaluator = dynamicRuleEvaluator;
        this.mapper = mapper;
    }

    /**
     * Метод, позволяющий получить рекомендаций для клиента по новым банковским продуктам.
     * В методе выполняется проверка на валидность данных, а также выполнения установленных условий по продуктам.
     *
     * @param userId идентификатор клиента.
     * @return список рекомендаций по новым продуктам, которые подходят клиенту.
     *      * В случае, если клиенту не подходит ни одни из продуктов, вернется пустой лист.
     */
    @Override
    public RecommendationResponse getRecommendations(UUID userId) {
        validateUserExists(userId);

        List<RecommendationDto> recommendations = new ArrayList<>();
        // Статические правила
        recommendations.addAll(collectRecommendation(userId));

        // Динамические правила
        recommendations.addAll(collectDynamicRecommendations(userId));

        return new RecommendationResponse(userId, recommendations);
    }

    /** Внутренний метод для проверки валидности данных: проверяет существование клиента в БД.
     *
     * @throws UserNotFoundException если клиент с заданным Id в БД не найдет.
     *
     * @param userId - идентификатор клиента.
     */
    private void validateUserExists(UUID userId) {
        if (!repository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    /**
     * Внутренний метод, который по очереди запускает одну их трех реализаций интерфейса RecommendationRule
     * (Invest500Rule, SimpleCreditRule, TopSavingRule), для проверки подходят ли клиенту новые банковские продукты.
     *
     * @param userId - идентификатор клиента.
     * @return список рекомендаций по новым продуктам, которые подходят клиенту.
     * В случае, если клиенту не подходит ни одни из продуктов, вернется пустой лист.
     */
    private List<RecommendationDto> collectRecommendation(UUID userId) {
        List<RecommendationDto> recommend = new ArrayList<>();
        for (RecommendationRule rule : listRules) {
            rule.checkOut(userId).ifPresent(recommend::add);
        }
        return recommend;
    }

    private List<RecommendationDto> collectDynamicRecommendations(UUID userId) {
        List<RecommendationDto> result = new ArrayList<>();
        List<DynamicRuleDto> dynamicRules = dynamicRuleService.getAllRules();

        for (DynamicRuleDto ruleDto : dynamicRules) {
            DynamicRule entity = mapper.toEntity(ruleDto);
            if (dynamicRuleEvaluator.evaluate(entity, userId)) {
                result.add(new RecommendationDto(ruleDto.productName(), ruleDto.id().toString(),
                        "Dynamic description"));
            }
        }
        return result;
    }

}
