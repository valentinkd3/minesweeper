package vd.kozhevnikov.minesweeper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vd.kozhevnikov.minesweeper.dto.ErrorResponse;
import vd.kozhevnikov.minesweeper.dto.GameInfoResponse;
import vd.kozhevnikov.minesweeper.dto.GameTurnRequest;
import vd.kozhevnikov.minesweeper.dto.NewGameRequest;
import vd.kozhevnikov.minesweeper.exception.GameNotFoundException;
import vd.kozhevnikov.minesweeper.exception.GameOverException;
import vd.kozhevnikov.minesweeper.exception.NotValidMinesCountException;
import vd.kozhevnikov.minesweeper.exception.OpenedCellException;
import vd.kozhevnikov.minesweeper.service.MinesweeperService;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static vd.kozhevnikov.minesweeper.BuildHelper.buildObject;

@WebMvcTest(MinesweeperController.class)
public class MinesweeperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private MinesweeperService minesweeperService;

    private NewGameRequest newGameRequest;

    private GameInfoResponse gameInfoResponse;

    private GameTurnRequest gameTurnRequest;

    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp(){
        newGameRequest = buildObject("/dto/NewGameRequest.json", NewGameRequest.class);
        gameInfoResponse = buildObject("/dto/GameInfoResponse.json", GameInfoResponse.class);
        gameTurnRequest = buildObject("/dto/GameTurnRequest.json", GameTurnRequest.class);
    }

    @Test
    @SneakyThrows
    void createGame_success_200ok() {
        when(minesweeperService.createGame(newGameRequest)).thenReturn(gameInfoResponse);

        mockMvc.perform(post("/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newGameRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(objectMapper.writeValueAsString(gameInfoResponse)));
    }

    @Test
    @SneakyThrows
    void makeTurn_success_200ok() {
        when(minesweeperService.makeTurn(gameTurnRequest)).thenReturn(gameInfoResponse);

        mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameTurnRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(objectMapper.writeValueAsString(gameInfoResponse)));
    }

    @Test
    @SneakyThrows
    void createGame_notValidRequest_handleValidationException() {
        newGameRequest.setHeight(1000);
        newGameRequest.setWidth(1000);
        newGameRequest.setMinesCount(null);

        mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGameRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Высота поля не может быть больше 30.")))
                .andExpect(content().string(containsString("Ширина поля не может быть больше 30.")))
                .andExpect(content().string(containsString("Необходимо указать количество мин.")));
    }

    @Test
    @SneakyThrows
    void makeTurn_notValidRequest_handleValidationException() {
        gameTurnRequest.setGameId("dummy");
        gameTurnRequest.setCol(null);
        gameTurnRequest.setRow(10000);

        mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameTurnRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Нельзя выбрать ряд больше заданной высоты поля.")))
                .andExpect(content().string(containsString("Передан некорректный идентификатор игры (должен быть формат UUID)")))
                .andExpect(content().string(containsString("Необходимо указать колонку проверяемой ячейки.")));
    }

    @Test
    @SneakyThrows
    void makeTurn_throwGameNotFoundException_handleGameNotFoundException() {
        String gameId = UUID.randomUUID().toString();
        GameNotFoundException gameNotFoundException = new GameNotFoundException(gameId);
        errorResponse = new ErrorResponse(String.format("Игры с gameId = %s не существует.", gameId));
        when(minesweeperService.makeTurn(gameTurnRequest)).thenThrow(gameNotFoundException);

        mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameTurnRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));
    }

    @Test
    @SneakyThrows
    void makeTurn_throwGameOverException_handleGameOverException() {
        String gameId = UUID.randomUUID().toString();
        GameOverException gameOverException = new GameOverException(gameId);
        errorResponse = new ErrorResponse(String.format("Игра с gameId = %s уже завершена.", gameId));
        when(minesweeperService.makeTurn(gameTurnRequest)).thenThrow(gameOverException);

        mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameTurnRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));
    }

    @Test
    @SneakyThrows
    void makeTurn_throwOpenedCellException_handleOpenedCellException() {
        OpenedCellException openedCellException = new OpenedCellException(gameTurnRequest.getCol(), gameTurnRequest.getRow());
        errorResponse = new ErrorResponse(String.format(
                "Ячека с координатами (%s, %s) была проверена ранее", gameTurnRequest.getCol(), gameTurnRequest.getRow()));
        when(minesweeperService.makeTurn(gameTurnRequest)).thenThrow(openedCellException);

        mockMvc.perform(post("/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameTurnRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));
    }

    @Test
    @SneakyThrows
    void createGame_throwNotValidMinesCountException_handleNotValidMinesCountException() {
        errorResponse = new ErrorResponse("Количество мин должно быть меньше количества ячеек.");
        NotValidMinesCountException notValidMinesCountException = new NotValidMinesCountException();
        when(minesweeperService.createGame(newGameRequest)).thenThrow(notValidMinesCountException);

        mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGameRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));
    }
}
