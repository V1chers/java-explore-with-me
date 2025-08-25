package ru.practicum.ewm.exception.models;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
