package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidException(final MethodArgumentNotValidException e) {
        ValidationErrorResponse validationErrorResponse =
                new ValidationErrorResponse("Ошибка валидации", e.getFieldErrors());
        log.warn("Ошибка валидации, ошибка 400: {}", validationErrorResponse);

        return validationErrorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(final InternalServerError e) {
        log.warn("Ошибка внутреннего сервера, ошибка 500: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Throwable e) {
        log.error("Неучтенная ошибка, ошибка 500: {}, {}", e.getClass(), e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
