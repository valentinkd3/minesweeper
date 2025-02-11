package vd.kozhevnikov.minesweeper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import vd.kozhevnikov.minesweeper.dto.GameInfoResponse;
import vd.kozhevnikov.minesweeper.dto.GameTurnRequest;
import vd.kozhevnikov.minesweeper.dto.NewGameRequest;
import vd.kozhevnikov.minesweeper.exception.CellOutOfBoundException;
import vd.kozhevnikov.minesweeper.exception.GameNotFoundException;
import vd.kozhevnikov.minesweeper.exception.GameOverException;
import vd.kozhevnikov.minesweeper.exception.NotValidMinesCountException;
import vd.kozhevnikov.minesweeper.exception.OpenedCellException;
import vd.kozhevnikov.minesweeper.mapper.GameMapper;
import vd.kozhevnikov.minesweeper.model.Cell;
import vd.kozhevnikov.minesweeper.model.Game;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinesweeperService {

    private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    private final GameMapper gameMapper;

    public GameInfoResponse createGame(final NewGameRequest request) {
        if (!isValidMinesCount(request)) {
            throw new NotValidMinesCountException();
        }
        UUID gameId = UUID.randomUUID();
        Game game = new Game(request.getHeight(), request.getWidth(), request.getMinesCount());
        games.put(gameId, game);
        log.info("Создана игра {}", gameId);
        return gameMapper.toGameInfoResponse(gameId, game);
    }

    public GameInfoResponse makeTurn(final GameTurnRequest request) {
        UUID gameId = UUID.fromString(request.getGameId());
        Game game = games.get(gameId);
        validateTurn(game, request);
        game.makeTurn(request.getCol(), request.getRow());
        log.info("В игре с gameId = {} выполнен ход [{},{}]", request.getGameId(), request.getCol(), request.getRow());
        return gameMapper.toGameInfoResponse(gameId, game);
    }

    private boolean isValidMinesCount(final NewGameRequest request) {
        return request.getMinesCount() < (request.getHeight() * request.getHeight());
    }

    private void validateTurn(@Nullable final Game game, final GameTurnRequest request) {
        if (game == null) {
            throw new GameNotFoundException(request.getGameId());
        } else if (game.isCompleted()) {
            throw new GameOverException(request.getGameId());
        } else if (request.getCol() >= game.getWidth() || request.getRow() >= game.getHeight()) {
            throw new CellOutOfBoundException(request.getCol(), request.getRow());
        } else if (game.getCheckedCoordinates().contains(new Cell.Coordinate(request.getCol(), request.getRow()))) {
            throw new OpenedCellException(request.getCol(), request.getRow());
        }
    }
}
