package ru.practicum.ewm.exception.models;

import lombok.Getter;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
public class ClientExceptionResult extends RuntimeException {
    HttpStatusCodeException exception;

    public ClientExceptionResult(HttpStatusCodeException e) {
        exception = e;
    }
}
