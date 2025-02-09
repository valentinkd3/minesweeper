package vd.kozhevnikov.minesweeper.exception;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class BaseHttpException extends RuntimeException {

    private final String messageTemplate;

    private final List<String> errorDetails;

    public BaseHttpException(String messageTemplate, List<String> errorDetails) {
        this.messageTemplate = messageTemplate;
        this.errorDetails = errorDetails;
    }

    public BaseHttpException(String messageTemplate) {
        this(messageTemplate, null);
    }
}
