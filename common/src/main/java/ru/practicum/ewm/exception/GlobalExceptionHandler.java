package ru.practicum.ewm.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.practicum.ewm.exception.models.*;

@RestControllerAdvice
@Slf4j
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<JsonNode> clientExceptionResult(final ClientExceptionResult e) {
        HttpStatusCodeException result = e.getException();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseBody = mapper.readTree(result.getResponseBodyAsString());
            return ResponseEntity.status(result.getStatusCode()).body(responseBody);
        } catch (JsonProcessingException jme) {
            log.error("Произошла ошибка при создании json объекта");
            throw new InternalServerError("Произошла ошибка при создании json объекта");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidException(final MethodArgumentNotValidException e) {
        ValidationErrorResponse validationErrorResponse =
                new ValidationErrorResponse("Ошибка валидации", e.getFieldErrors());
        log.warn("Ошибка валидации, ошибка 400: {}", validationErrorResponse);

        return validationErrorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn("Ошибка 400, не указали все требуемые параметры запроса: {}", e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHandlerMethodValidationException(final HandlerMethodValidationException e) {
        log.warn("Ошибка 400, ошибка валидации: {}", e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final BadRequestException e) {
        log.warn("Ошибка 400: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("Ошибка 404: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotFoundException(final ConflictException e) {
        log.warn("Ошибка 409: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
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
