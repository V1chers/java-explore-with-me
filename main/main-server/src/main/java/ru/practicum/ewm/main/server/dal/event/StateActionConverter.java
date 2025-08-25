package ru.practicum.ewm.main.server.dal.event;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StateActionConverter implements AttributeConverter<StateAction, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StateAction stateAction) {
        if (stateAction == null) {
            return null;
        }

        return stateAction.getId();
    }

    @Override
    public StateAction convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return StateAction.of(id);
    }
}
