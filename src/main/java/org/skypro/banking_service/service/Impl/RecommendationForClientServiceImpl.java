package org.skypro.banking_service.service.Impl;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.repositories.h2.repository.UserTransactionRepository;
import org.skypro.banking_service.rule_system.dinamic_rules.DimanicRule;
import org.skypro.banking_service.rule_system.static_rules.rules.StaticRule;
import org.skypro.banking_service.service.RecommendationForClientService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RecommendationForClientServiceImpl implements RecommendationForClientService {

    private final UserTransactionRepository repository;
    private final List<StaticRule> listStaticRules;
    private final DimanicRule dimanicRule;

    public RecommendationForClientServiceImpl(UserTransactionRepository repository, List<StaticRule> listStaticRules, DimanicRule dimanicRule) {
        this.repository = repository;
        this.listStaticRules = listStaticRules;
        this.dimanicRule = dimanicRule;
    }

    /**
     * Метод, позволяющий получить рекомендаций для клиента по новым банковским продуктам.
     * В методе выполняется проверка на валидность данных, а также выполнения установленных условий по продуктам.
     *
     * @param userId идентификатор клиента.
     * @return список рекомендаций по новым продуктам, которые подходят клиенту.
     * * В случае, если клиенту не подходит ни одни из продуктов, вернется пустой лист.
     */
    @Override
    public RecommendationResponse getRecommendations(UUID userId) {
        validateUserExists(userId);

        List<RecommendationDto> recommendationsByStaticRules = collectRecommendation(userId);
        List<RecommendationDto> fullListRecommendation = new ArrayList<>(recommendationsByStaticRules);
        List<RecommendationDto> recommendationsByDinamicRules = dimanicRule.checkOutDinamicRule(userId);
        fullListRecommendation.addAll(recommendationsByDinamicRules);

        return new RecommendationResponse(userId, fullListRecommendation);
    }

    /**
     * Внутренний метод для проверки валидности данных: проверяет существование клиента в БД.
     *
     * @param userId - идентификатор клиента.
     * @throws UserNotFoundException если клиент с заданным Id в БД не найдет.
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
        for (StaticRule staticRule : listStaticRules) {
            staticRule.checkOutStaticRule(userId).ifPresent(recommend::add);
        }
        return recommend;
    }

}
