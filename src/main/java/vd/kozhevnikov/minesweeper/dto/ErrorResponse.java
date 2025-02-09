package vd.kozhevnikov.minesweeper.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Сообщение об ошибке")
public class ErrorResponse {

    @Schema(description = "Текст ошибки")
    private String error;
}
