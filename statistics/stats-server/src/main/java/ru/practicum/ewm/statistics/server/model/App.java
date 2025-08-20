package ru.practicum.ewm.statistics.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum App {
    EWM_MAIN_SERVICE(1, "ewm-main-service");

    private final int id;

    private final String name;

    public static App of(int id) {
        return Stream.of(App.values())
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static App of(String name) {
        return Stream.of(App.values())
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String toString() {
        return name;
    }
}
