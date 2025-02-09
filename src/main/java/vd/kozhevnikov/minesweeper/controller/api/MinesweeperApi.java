package vd.kozhevnikov.minesweeper.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vd.kozhevnikov.minesweeper.dto.ErrorResponse;
import vd.kozhevnikov.minesweeper.dto.GameInfoResponse;
import vd.kozhevnikov.minesweeper.dto.GameTurnRequest;
import vd.kozhevnikov.minesweeper.dto.NewGameRequest;

@Tag(name = "АПИ для игры в сапера")
public interface MinesweeperApi {

    @PostMapping("/new")
    @Operation(
            summary = "Метод для создания новой игры",
            description = "Создание новой игры",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GameInfoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    GameInfoResponse createGame(@RequestBody @Valid NewGameRequest request);

    @PostMapping("/turn")
    @Operation(
            summary = "Метод для выполнения хода",
            description = "Выполнение хода",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GameInfoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    GameInfoResponse makeTurn(@RequestBody @Valid GameTurnRequest request);
}
