package org.skypro.banking_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skypro.banking_service.dto.StatisticsDTO;
import org.skypro.banking_service.exception.RecommendationNotFoundException;
import org.skypro.banking_service.model.Recommendation;
import org.skypro.banking_service.service.DynamicRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DynamicRulesController.class)
class DynamicRulesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DynamicRulesService dynamicRulesService;

    @Test
    @DisplayName("POST /rule — Успешное добавление продукта")
    void testCreateRecommendation() throws Exception {
        Recommendation input = new Recommendation();
        input.setProductId(UUID.randomUUID());
        input.setProductName("TestProduct");
        input.setProductText("Description");

        when(dynamicRulesService.addProductWithDynamicRule(any())).thenReturn(input);

        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("TestProduct"))
                .andExpect(jsonPath("$.productText").value("Description"));
    }

    @Test
    @DisplayName("POST /rule — Неверный формат динамического правила")
    void testCreateRecommendationInvalidFormat() throws Exception {
        Recommendation invalidRecommendation = new Recommendation();
        invalidRecommendation.setProductId(UUID.randomUUID());
        invalidRecommendation.setProductName("InvalidProduct");
        invalidRecommendation.setProductText("Invalid text");

        when(dynamicRulesService.addProductWithDynamicRule(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRecommendation)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Новый банковский продукт имеет неверный " +
                                                           "формат динамического правила")));
    }

    @Test
    @DisplayName("DELETE /rule/{productId} — Успешное удаление продукта")
    void deleteRecommendation() throws Exception {
        UUID productId = UUID.randomUUID();

        doNothing().when(dynamicRulesService).deleteProductWithDynamicRule(productId);

        mockMvc.perform(delete("/rule/{productId}", productId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /rule/{productId} — Продукт не найден")
    void testDeleteRecommendationNotFound() throws Exception {
        UUID productId = UUID.randomUUID();

        doThrow(new RecommendationNotFoundException("Not found"))
                .when(dynamicRulesService).deleteProductWithDynamicRule(productId);

        mockMvc.perform(delete("/rule/{productId}", productId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Продукт с id = " + productId + " не найден.")));
    }

    @Test
    @DisplayName("GET /rule — Получение списка всех банковских продуктов")
    void testGetAllRecommendations() throws Exception {
        Recommendation r1 = new Recommendation();
        r1.setProductId(UUID.randomUUID());
        r1.setProductName("Product A");
        r1.setProductText("Description A");

        Recommendation r2 = new Recommendation();
        r2.setProductId(UUID.randomUUID());
        r2.setProductName("Product B");
        r2.setProductText("Description B");

        List<Recommendation> mockList = List.of(r1, r2);

        when(dynamicRulesService.getAllProductsWithDynamicRule()).thenReturn(mockList);

        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productName").value("Product A"))
                .andExpect(jsonPath("$[1].productName").value("Product B"));
    }


    @Test
    @DisplayName("GET /rule/stats — Получение статистики срабатывания правил")
    void testGetStats() throws Exception {
        UUID ruleId1 = UUID.randomUUID();
        UUID ruleId2 = UUID.randomUUID();

        StatisticsDTO stat1 = new StatisticsDTO(ruleId1, 15L);
        StatisticsDTO stat2 = new StatisticsDTO(ruleId2, 8L);

        List<StatisticsDTO> mockStats = List.of(stat1, stat2);

        when(dynamicRulesService.getStatistics()).thenReturn(mockStats);

        mockMvc.perform(get("/rule/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.stats.length()").value(2))
                .andExpect(jsonPath("$.stats[0].ruleId").value(ruleId1.toString()))
                .andExpect(jsonPath("$.stats[0].count").value(15))
                .andExpect(jsonPath("$.stats[1].ruleId").value(ruleId2.toString()))
                .andExpect(jsonPath("$.stats[1].count").value(8));
    }


}