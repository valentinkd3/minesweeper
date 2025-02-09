package vd.kozhevnikov.minesweeper.exception;

import java.util.List;

public class OpenedCellException extends BaseHttpException {

    public static final String MESSAGE_TEMPLATE = "Ячека с координатами (%s, %s) была проверена ранее";

    public OpenedCellException(Integer col, Integer row) {
        super(MESSAGE_TEMPLATE, List.of(col.toString(), row.toString()));
    }
}
