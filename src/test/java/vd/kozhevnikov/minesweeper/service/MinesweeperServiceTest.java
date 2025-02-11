package vd.kozhevnikov.minesweeper.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vd.kozhevnikov.minesweeper.dto.GameInfoResponse;
import vd.kozhevnikov.minesweeper.dto.GameTurnRequest;
import vd.kozhevnikov.minesweeper.dto.NewGameRequest;
import vd.kozhevnikov.minesweeper.exception.BaseHttpException;
import vd.kozhevnikov.minesweeper.exception.CellOutOfBoundException;
import vd.kozhevnikov.minesweeper.exception.GameNotFoundException;
import vd.kozhevnikov.minesweeper.exception.GameOverException;
import vd.kozhevnikov.minesweeper.exception.NotValidMinesCountException;
import vd.kozhevnikov.minesweeper.mapper.GameMapper;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static vd.kozhevnikov.minesweeper.BuildHelper.buildObject;

public class MinesweeperServiceTest {

    private MinesweeperService minesweeperService;

    private GameMapper gameMapper;

    @BeforeEach
    void setUp() {
        gameMapper = new GameMapper();
        minesweeperService = new MinesweeperService(gameMapper);
    }

    @Test
    void createGame_success_returnGameResponse() {
        NewGameRequest newGameRequest = buildObject("/dto/NewGameRequest.json", NewGameRequest.class);

        GameInfoResponse actual = minesweeperService.createGame(newGameRequest);
        GameInfoResponse expected = buildObject("/dto/GameInfoResponse.json", GameInfoResponse.class);
        actual.setGameId(expected.getGameId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createGame_notValidMinesCount_throwNotValidMinesCountException() {
        NewGameRequest newGameRequest = buildObject("/dto/NewGameRequest.json", NewGameRequest.class);
        newGameRequest.setMinesCount(1000);

        Throwable throwable = catchThrowable(() -> minesweeperService.createGame(newGameRequest));

        assertThat(throwable).isInstanceOf(NotValidMinesCountException.class);
        assertThat(((BaseHttpException) throwable).getMessageTemplate())
                .isEqualTo("Количество мин должно быть меньше количества ячеек.");
    }

    @Test
    void makeTurn_gameWasNotCreated_throwGameNotFoundException() {
        GameTurnRequest gameTurnRequest = buildObject("/dto/GameTurnRequest.json", GameTurnRequest.class);

        Throwable throwable = catchThrowable(() -> minesweeperService.makeTurn(gameTurnRequest));

        assertThat(throwable).isInstanceOf(GameNotFoundException.class);
        assertThat(((BaseHttpException) throwable).getMessageTemplate())
                .isEqualTo("Игры с gameId = %s не существует.");
    }

    @Test
    void makeTurn_noMinesOnField_winTheGame() {
        NewGameRequest newGameRequest = buildObject("/dto/NewGameRequest.json", NewGameRequest.class);
        newGameRequest.setMinesCount(0);
        GameInfoResponse game = minesweeperService.createGame(newGameRequest);
        GameTurnRequest gameTurnRequest = buildObject("/dto/GameTurnRequest.json", GameTurnRequest.class);
        gameTurnRequest.setGameId(game.getGameId().toString());

        GameInfoResponse actual = minesweeperService.makeTurn(gameTurnRequest);
        String[][] expectedField = new String[newGameRequest.getHeight()][newGameRequest.getWidth()];
        for (String[] row : expectedField) {
            Arrays.fill(row, "0");
        }

        assertThat(actual.getCompleted()).isTrue();
        assertThat(actual.getField()).isDeepEqualTo(expectedField);
    }

    @Test
    void makeTurn_gameWasCompleted_throwGameOverException() {
        NewGameRequest newGameRequest = buildObject("/dto/NewGameRequest.json", NewGameRequest.class);
        newGameRequest.setMinesCount(0);
        GameInfoResponse game = minesweeperService.createGame(newGameRequest);
        GameTurnRequest gameTurnRequest = buildObject("/dto/GameTurnRequest.json", GameTurnRequest.class);
        gameTurnRequest.setGameId(game.getGameId().toString());

        minesweeperService.makeTurn(gameTurnRequest);
        Throwable throwable = catchThrowable(() -> minesweeperService.makeTurn(gameTurnRequest));

        assertThat(throwable).isInstanceOf(GameOverException.class);
        assertThat(((BaseHttpException) throwable).getMessageTemplate())
                .isEqualTo("Игра с gameId = %s уже завершена.");
    }

    @Test
    void makeTurn_cellOutOfBound_throwCellOutOfBoundException() {
        NewGameRequest newGameRequest = buildObject("/dto/NewGameRequest.json", NewGameRequest.class);
        GameInfoResponse game = minesweeperService.createGame(newGameRequest);
        GameTurnRequest gameTurnRequest = buildObject("/dto/GameTurnRequest.json", GameTurnRequest.class);
        gameTurnRequest.setGameId(game.getGameId().toString());
        gameTurnRequest.setRow(15);

        Throwable throwable = catchThrowable(() -> minesweeperService.makeTurn(gameTurnRequest));

        assertThat(throwable).isInstanceOf(CellOutOfBoundException.class);
        assertThat(((BaseHttpException) throwable).getMessageTemplate())
                .isEqualTo("Ячейка с координатами (%s, %s) выходит за границы поля");
    }
}
