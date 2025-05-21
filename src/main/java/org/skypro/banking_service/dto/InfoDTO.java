package org.skypro.banking_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Справочная информация о системе")
public record InfoDTO(
        @Schema(description = "Название приложения")
        String name,

        @Schema(description = "Версия приложения")
        String version) {
}
