package vd.kozhevnikov.minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Данные для создания новой игры")
public class NewGameRequest {

    @Min(value = 0, message = "Ширина поля не может быть меньше 0.")
    @Max(value = 30, message = "Ширина поля не может быть больше 30.")
    @NotNull(message = "Необходимо указать ширину поля.")
    @Schema(description = "Ширина поля", example = "10")
    private Integer width;

    @Min(value = 0, message = "Высота поля не может быть меньше 0.")
    @Max(value = 30, message = "Высота поля не может быть больше 30.")
    @NotNull(message = "Необходимо указать высоту поля.")
    @Schema(description = "Высота поля", example = "10")
    private Integer height;

    @Min(value = 0, message = "Количество мин не может быть меньше 0.")
    @NotNull(message = "Необходимо указать количество мин.")
    @JsonProperty("mines_count")
    @Schema(description = "Количество мин", example = "10")
    private Integer minesCount;
}
