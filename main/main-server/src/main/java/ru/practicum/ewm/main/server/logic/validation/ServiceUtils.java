package ru.practicum.ewm.main.server.logic.validation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.exception.models.NotFoundException;

import java.util.Optional;

public class ServiceUtils {

    public static <T, K> void isExist(JpaRepository<T, K> repository, K id, String message) {
        Optional<T> object = repository.findById(id);

        if (object.isEmpty()) {
            throw new NotFoundException(message + ": " + id);
        }
    }

    public static <T, K> T getIfExist(JpaRepository<T, K> repository, K id, String message) {
        Optional<T> object = repository.findById(id);

        if (object.isEmpty()) {
            throw new NotFoundException(message + ": " + id);
        } else {
            return object.get();
        }
    }
}
