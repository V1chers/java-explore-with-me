package ru.practicum.ewm.statistics.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.practicum.ewm.statistics.model.App;

@Converter(autoApply = true)
public class AppConverter implements AttributeConverter<App, Integer> {

    @Override
    public Integer convertToDatabaseColumn(App app) {
        if (app == null) {
            return null;
        }

        return app.getId();
    }

    @Override
    public App convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return App.of(id);
    }
}
