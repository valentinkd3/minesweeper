package vd.kozhevnikov.minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UUID;

import static org.hibernate.validator.constraints.UUID.LetterCase.INSENSITIVE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные для выполнения хода")
public class GameTurnRequest {

    @NotNull(message = "Необходимо передать идентификатор игры.")
    @UUID(letterCase = INSENSITIVE, message = "Передан некорректный идентификатор игры (должен быть формат UUID)")
    @JsonProperty("game_id")
    @Schema(description = "Идентификтор игры", example = "bb6254eb-5dea-476f-bc5e-9027d2e57db9")
    private String gameId;

    @NotNull(message = "Необходимо указать колонку проверяемой ячейки.")
    @Min(value = 0, message = "Колонка не может быть меньше 0.")
    @Max(value = 30, message = "Нельзя выбрать колонку беольше заданной ширины поля.")
    @Schema(description = "Колонка", example = "5")
    private Integer col;

    @NotNull(message = "Необходимо указать ряд проверяемой ячейки.")
    @Min(value = 0, message = "Ряд не может быть меньше 0.")
    @Max(value = 30, message = "Нельзя выбрать ряд больше заданной высоты поля.")
    @Schema(description = "Ряд", example = "5")
    private Integer row;
}
