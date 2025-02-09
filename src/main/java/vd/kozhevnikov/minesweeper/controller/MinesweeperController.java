package vd.kozhevnikov.minesweeper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vd.kozhevnikov.minesweeper.controller.api.MinesweeperApi;
import vd.kozhevnikov.minesweeper.dto.GameInfoResponse;
import vd.kozhevnikov.minesweeper.dto.GameTurnRequest;
import vd.kozhevnikov.minesweeper.dto.NewGameRequest;
import vd.kozhevnikov.minesweeper.service.MinesweeperService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MinesweeperController implements MinesweeperApi {

    private final MinesweeperService minesweeperService;

    @Override
    public GameInfoResponse createGame(NewGameRequest request) {
        return minesweeperService.createGame(request);
    }

    @Override
    public GameInfoResponse makeTurn(GameTurnRequest request) {
        return minesweeperService.makeTurn(request);
    }
}
