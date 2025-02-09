package vd.kozhevnikov.minesweeper.exception;

import java.util.Collections;

public class GameNotFoundException extends BaseHttpException {

    private static final String MESSAGE_TEMPLATE = "Игры с gameId = %s не существует.";

    public GameNotFoundException(String gameId) {
        super(MESSAGE_TEMPLATE, Collections.singletonList(gameId));
    }
}
