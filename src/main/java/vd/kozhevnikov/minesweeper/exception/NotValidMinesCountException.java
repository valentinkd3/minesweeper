package vd.kozhevnikov.minesweeper.exception;

public class NotValidMinesCountException extends BaseHttpException {

    public static final String MESSAGE = "Количество мин должно быть меньше количества ячеек.";

    public NotValidMinesCountException() {
        super(MESSAGE);
    }
}
