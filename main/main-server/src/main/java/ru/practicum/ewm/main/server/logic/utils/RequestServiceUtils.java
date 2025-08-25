package ru.practicum.ewm.main.server.logic.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.StateAction;
import ru.practicum.ewm.main.server.dal.request.Request;
import ru.practicum.ewm.main.server.dal.request.RequestRepository;
import ru.practicum.ewm.main.server.dal.request.RequestStatus;

@Service
@RequiredArgsConstructor
public class RequestServiceUtils {

    private final RequestRepository requestRepository;

    public void isRequestRepeated(int userId, int eventId) {
        if (requestRepository.findByUserIdAndEventId(userId, eventId) != null) {
            throw new ConflictException("Нельзя отправить повторный запрос");
        }
    }

    public void isRequester(int userId, Request request) {
        if (request.getUserId() != userId) {
            throw new ConflictException("Заявка не создана данным пользователем");
        }
    }

    public void isOrganizer(int userId, Event event) {
        if (event.getUser().getId() == userId) {
            throw new ConflictException("Организатор не может запросить участие в своем событии");
        }
    }

    public void isPublished(Event event) {
        if (event.getStateAction() != StateAction.PUBLISHED) {
            throw new ConflictException("Нельзя принимать участие в неопубликованном событии");
        }
    }

    public void isLimitReached(Event event) {
        int createdRequests = requestRepository.findAllByEventIdAndStatus(event.getId(),
                RequestStatus.CONFIRMED).size();

        if (createdRequests >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Количество участников достигло лимита");
        }
    }
}
