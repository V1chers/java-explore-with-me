package ru.practicum.ewm.exception;

import lombok.Getter;
import org.springframework.web.client.HttpStatusCodeException;

@Getter
public class ClientExceptionResult extends RuntimeException {
    HttpStatusCodeException exception;

    public ClientExceptionResult(HttpStatusCodeException e) {
        exception = e;
    }
}
