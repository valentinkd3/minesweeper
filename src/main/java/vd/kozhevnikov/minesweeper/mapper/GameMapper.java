package vd.kozhevnikov.minesweeper.mapper;

import org.springframework.stereotype.Component;
import vd.kozhevnikov.minesweeper.dto.GameInfoResponse;
import vd.kozhevnikov.minesweeper.model.Game;

import java.util.UUID;

@Component
public class GameMapper {

    public GameInfoResponse toGameInfoResponse(UUID gameId, Game game) {
        return GameInfoResponse.builder()
                .gameId(gameId)
                .width(game.getWidth())
                .height(game.getHeight())
                .completed(game.isCompleted())
                .minesCount(game.getMinesCount())
                .field(game.getGameSnapshot())
                .build();
    }
}
