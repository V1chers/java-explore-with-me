package ru.practicum.ewm.main.server.dal.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.exception.models.NotFoundException;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum RequestStatus {
    PENDING(1),
    CONFIRMED(2),
    CANCELED(3),
    REJECTED(4);

    private final int id;

    public static RequestStatus of(int id) {
        return Stream.of(RequestStatus.values())
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Статус не найден"));
    }

    public static RequestStatus of(String stringStatus) {
        return Stream.of(RequestStatus.values())
                .filter(p -> p.name().equalsIgnoreCase(stringStatus))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Статус не найден"));
    }
}
