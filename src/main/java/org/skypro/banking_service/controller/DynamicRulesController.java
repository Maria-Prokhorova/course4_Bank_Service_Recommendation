package org.skypro.banking_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.dto.StatisticsDTO;
import org.skypro.banking_service.dto.StatisticsResponse;
import org.skypro.banking_service.service.DynamicRulesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Управление банковскими продуктами.", description = "Все необходимые операции над банковскими продуктами: " +
        "добавление, удаление, получение полного списка, а также статистика.")
@RestController
@RequestMapping("/rule")
public class DynamicRulesController {

    private final DynamicRulesService dynamicRulesService;

    public DynamicRulesController(DynamicRulesService dynamicRulesService) {
        this.dynamicRulesService = dynamicRulesService;
    }

    @Operation(summary = "Добавление нового банковского продукта в базу данных.", description = "В ответе возвращается " +
            "информация о добавленном банковском продукте, со следующими полями productId, productName, productText и queries.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт успешно добавлен в базу данных."),
            @ApiResponse(responseCode = "400", description = "Введены некорректные данные по продукту.")
    })
    @PostMapping
    public ResponseEntity<?> createRecommendation(@Parameter(
            description = "Информация о новом продукте, который хотим добавить в базу.",
            required = true)
                                                  @RequestBody Recommendation recommendation) {
        Recommendation newRecommendation = dynamicRulesService.addProductWithDynamicRule(recommendation);
        return new ResponseEntity<>(newRecommendation, HttpStatus.OK);
    }

    @Operation(summary = "Удаление из базы данных банковского продукта по его ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Продукт успешно удалён."),
            @ApiResponse(responseCode = "404", description = "Продукт с таким id не найден.")
    })
    @DeleteMapping("{productId}")
    public ResponseEntity<?> deleteRecommendation(@Parameter(
            description = "ID продукта, который хотим удалить из базы.",
            required = true)
                                                  @PathVariable UUID productId) {
        dynamicRulesService.deleteProductWithDynamicRule(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Получение списка всех банковских продуктов.", description = "В ответе возвращается список " +
            "банковских продуктов, которые есть в базе данных.")
    @GetMapping
    public List<Recommendation> getAllRecommendations() {
        return dynamicRulesService.getAllProductsWithDynamicRule();
    }

    @Operation(summary = "Статистика срабатывания правил рекомендаций.", description = "Позволяет получить информацию " +
            "о том, как часто срабатывают динамические правила рекомендаций для клиентов банка.")
    @GetMapping("/stats")
    public ResponseEntity<StatisticsResponse> getStats() {
        List<StatisticsDTO> stats = dynamicRulesService.getStatistics();
        return ResponseEntity.ok(new StatisticsResponse(stats));
    }
}
