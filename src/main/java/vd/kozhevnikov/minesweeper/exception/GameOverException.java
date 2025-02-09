package vd.kozhevnikov.minesweeper.exception;

import java.util.Collections;

public class GameOverException extends BaseHttpException {

    private static final String MESSAGE_TEMPLATE = "Игра с gameId = %s уже завершена.";

    public GameOverException(String gameId) {
        super(MESSAGE_TEMPLATE, Collections.singletonList(gameId));
    }
}
