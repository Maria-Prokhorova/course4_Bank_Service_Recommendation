package org.skypro.banking_service.controller;

import org.junit.jupiter.api.Test;
import org.skypro.banking_service.dto.RecommendationDto;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @Test
    void getRecommendations_shouldReturnResponse() throws Exception {
        UUID userId = UUID.randomUUID();
        RecommendationDto dto = new RecommendationDto();
        dto.setId("prod");
        dto.setName("Product 1");
        dto.setText("Выбери этот продукт");

        RecommendationResponse response = new RecommendationResponse();
        response.setUserId(userId);
        response.setRecommendations(List.of(dto));

        when(recommendationService.getRecommendations(userId)).thenReturn(response);

        mockMvc.perform(get("/recommendation/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.recommendations", hasSize(1)))
                .andExpect(jsonPath("$.recommendations[0].id").value("prod"))
                .andExpect(jsonPath("$.recommendations[0].name").value("Product 1"))
                .andExpect(jsonPath("$.recommendations[0].text").value("Выбери этот продукт"));
    }

}