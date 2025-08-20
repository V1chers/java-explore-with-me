package ru.practicum.ewm.main.server.dal.request;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RequestStatusConverter implements AttributeConverter<RequestStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(RequestStatus requestStatus) {
        if (requestStatus == null) {
            return null;
        }

        return requestStatus.getId();
    }

    @Override
    public RequestStatus convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }

        return RequestStatus.of(id);
    }
}
