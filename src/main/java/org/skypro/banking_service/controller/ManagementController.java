package org.skypro.banking_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.skypro.banking_service.service.ManagementService;
import org.skypro.banking_service.dto.InfoDTO;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление системой.", description = "В данном разделе можно узнать информацию о системе и сбросить кеш.")
@RequestMapping("/management")
@RestController
public class ManagementController {

    private final ManagementService managementService;
    private final BuildProperties buildProperties;

    public ManagementController(ManagementService managementService, BuildProperties buildProperties) {
        this.managementService = managementService;
        this.buildProperties = buildProperties;
    }

    @Operation(summary = "Сброс кеша рекомендаций.", description = "Данный эндпоинт позволяет очистить все " +
            "закешированные результаты, что означает обновление базы данных.")
    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        managementService.clearCache();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Информация о системе.", description = "Данный эндпоинт позволяет получить информацию о " +
            "названии и версии системы.")
    @GetMapping("/info")
    public ResponseEntity<InfoDTO> getServiceInfo() {
        return ResponseEntity.ok(
                new InfoDTO(
                        buildProperties.getName(),
                        buildProperties.getVersion()
                )
        );
    }
}
