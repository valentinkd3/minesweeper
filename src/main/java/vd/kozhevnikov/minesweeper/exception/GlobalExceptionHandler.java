package vd.kozhevnikov.minesweeper.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vd.kozhevnikov.minesweeper.dto.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleOnMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder messageBuilder = new StringBuilder();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> messageBuilder.append(error.getDefaultMessage()).append(" "));
        String message = messageBuilder.toString().trim();
        log.error(message, ex);
        return new ErrorResponse(message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BaseHttpException.class)
    public ErrorResponse handleOnHttpException(BaseHttpException ex) {
        String message = buildMessage(ex);
        log.error(message, ex);
        return new ErrorResponse(message);
    }

    private String buildMessage(BaseHttpException ex) {
        return (ex.getErrorDetails() == null)
                ? ex.getMessageTemplate()
                : String.format(ex.getMessageTemplate(), ex.getErrorDetails().toArray());
    }
}
