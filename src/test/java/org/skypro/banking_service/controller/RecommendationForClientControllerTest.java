package org.skypro.banking_service.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skypro.banking_service.dto.RecommendationDTO;
import org.skypro.banking_service.dto.RecommendationResponse;
import org.skypro.banking_service.exception.UserNotFoundException;
import org.skypro.banking_service.service.RecommendationForClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecommendationForClientController.class)
class RecommendationForClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationForClientService recommendationForClientService;

    @Test
    @DisplayName("GET /recommendation/{userId} - success")
    void testGetRecommendationsSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        RecommendationDTO rec1 = new RecommendationDTO(
                "Invest500",
                "1",
                "Инвестируйте 500 рублей в месяц");
        RecommendationDTO rec2 = new RecommendationDTO(
                "TopSaving",
                "2",
                "Лучшие условия для накоплений");

        RecommendationResponse mockResponse = new RecommendationResponse(userId, List.of(rec1, rec2));


        when(recommendationForClientService.getRecommendationsForClient(userId))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/recommendation/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.recommendations[0].name").value("Invest500"))
                .andExpect(jsonPath("$.recommendations[1].name").value("TopSaving"));
    }

    @Test
    @DisplayName("GET /recommendation/{userId} - user not found")
    void testGetRecommendationsUserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        when(recommendationForClientService.getRecommendationsForClient(userId))
                .thenThrow(new UserNotFoundException(userId));

        mockMvc.perform(get("/recommendation/{userId}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Клиент с id = " + userId + " в базе данных не найден."))
                );
    }
}

