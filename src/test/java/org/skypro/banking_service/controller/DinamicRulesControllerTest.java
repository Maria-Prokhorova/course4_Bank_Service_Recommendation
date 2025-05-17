package org.skypro.banking_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skypro.banking_service.model.Queries;
import org.skypro.banking_service.model.Recommendations;
import org.skypro.banking_service.service.DinamicRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.skypro.banking_service.constants.ProductConstants.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DinamicRulesControllerTest.class)
public class DinamicRulesControllerTest {

    @Autowired
    MockMvc mvc;

    @Mock
    DinamicRulesService service;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnCreatedRecommendations() throws Exception {
        Queries query = new Queries();
        List<Queries> queries = new ArrayList<>(List.of(query));
        Recommendations recommendations = new Recommendations(
                UUID.fromString(PRODUCT_ID_TOP_SAVING),
                PRODUCT_NAME_TOP_SAVING,
                DESCRIPTION_TOP_SAVING,
                queries);

        Mockito.when(service.addRecommendationByRule(recommendations)).thenReturn(recommendations);

        mockMvc.perform(MockMvcRequestBuilders.post(
                "/rule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(recommendations)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationsId").value(recommendations.getProductId()))
                .andExpect(jsonPath("$.recommendationsName").value(recommendations.getProductName()))
                .andExpect(jsonPath("$.recommendationsText").value(recommendations.getProductText()))
                .andExpect(jsonPath("$.rules").value(recommendations.getRule()));
    }
}
