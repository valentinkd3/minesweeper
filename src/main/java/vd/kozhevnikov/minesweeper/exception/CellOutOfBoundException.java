package vd.kozhevnikov.minesweeper.exception;

import java.util.List;

public class CellOutOfBoundException extends BaseHttpException {

    public static final String MESSAGE_TEMPLATE = "Ячейка с координатами (%s, %s) выходит за границы поля";

    public CellOutOfBoundException(Integer col, Integer row) {
        super(MESSAGE_TEMPLATE, List.of(col.toString(), row.toString()));
    }
}
