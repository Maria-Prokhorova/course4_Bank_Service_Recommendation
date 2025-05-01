package org.skypro.banking_service.service.rules;

import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500Rule implements RecommendationRule {

    private final RecommendationRepository recommendationRepository;

    public Invest500Rule(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public Optional<RecommendationDto> checkOut(UUID userId) {
        RecommendationDto Invest500 = new RecommendationDto(
                "147f6a0f-3b91-413b-ab99-87f081d60d5a",
                "Invest 500",
                "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!"
        );

        boolean requirementFirst = recommendationRepository.existsUserProductByType(userId, "DEBIT");
        boolean requirementSecond = !(recommendationRepository.existsUserProductByType(userId, "INVEST"));
        boolean requirementThird = recommendationRepository.findTotalDepositByUserIdAndProductType(userId, "SAVING") > 1000;

        if (requirementFirst && requirementSecond && requirementThird) {
            return Optional.of(Invest500);
        }
        return Optional.empty();
    }

}