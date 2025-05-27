package org.skypro.banking_service.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.banking_service.service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ManagementController.class)
class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagementService managementService;

    @MockBean
    private BuildProperties buildProperties;

    @Test
    @DisplayName("POST /management/clear-caches — должен сбрасывать кэш")
    void testClearCaches() throws Exception {
        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(managementService, times(1)).clearCache();
    }

    @Test
    @DisplayName("GET /management/info — должен возвращать информацию о сервисе")
    void testGetServiceInfo() throws Exception {
        when(buildProperties.getName()).thenReturn("banking-service");
        when(buildProperties.getVersion()).thenReturn("1.0.0");

        mockMvc.perform(get("/management/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("banking-service"))
                .andExpect(jsonPath("$.version").value("1.0.0"));
    }

}