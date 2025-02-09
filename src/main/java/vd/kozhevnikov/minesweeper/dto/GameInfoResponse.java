package vd.kozhevnikov.minesweeper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Ответ о состоянии игры")
public class GameInfoResponse {

    @JsonProperty("game_id")
    @Schema(description = "Идентификатор игры")
    private UUID gameId;

    @JsonProperty("mines_count")
    @Schema(description = "Количество мин")
    private Integer minesCount;

    @Schema(description = "Ширина поля")
    private Integer width;

    @Schema(description = "Высота поля")
    private Integer height;

    @Schema(description = "Статус игры")
    private Boolean completed;

    @Schema(description = "Состояние игры")
    private String[][] field;
}
