package ru.practicum.ewm.exception.models;

public class InternalServerError extends RuntimeException {
    public InternalServerError(String message) {
        super(message);
    }
}
