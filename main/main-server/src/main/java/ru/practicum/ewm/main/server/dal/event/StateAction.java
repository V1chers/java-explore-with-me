package ru.practicum.ewm.main.server.dal.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.ewm.exception.models.NotFoundException;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum StateAction {
    PUBLISHED(1),
    PENDING(2),
    CANCELED(3);

    private final int id;

    public static StateAction of(int id) {
        return Stream.of(StateAction.values())
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Статус не найден"));
    }

    public static StateAction of(String stringState) {
        return Stream.of(StateAction.values())
                .filter(p -> p.name().equalsIgnoreCase(stringState))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Статус не найден"));
    }
}
