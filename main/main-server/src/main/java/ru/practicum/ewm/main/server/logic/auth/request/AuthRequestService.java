package ru.practicum.ewm.main.server.logic.auth.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.EventRepository;
import ru.practicum.ewm.main.server.dal.request.Request;
import ru.practicum.ewm.main.server.dal.request.RequestMapper;
import ru.practicum.ewm.main.server.dal.request.RequestRepository;
import ru.practicum.ewm.main.server.dal.request.RequestStatus;
import ru.practicum.ewm.main.server.dal.user.UserRepository;
import ru.practicum.ewm.main.server.logic.utils.RequestServiceUtils;
import ru.practicum.ewm.main.server.logic.utils.ServiceUtils;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthRequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final RequestServiceUtils requestServiceUtils;

    public List<RequestDto> getRequest(Integer userId) {
        ServiceUtils.isExist(userRepository, userId, "Пользователь с данным id не найден");

        List<Request> requestList = requestRepository.findAllByUserId(userId);

        return RequestMapper.toDto(requestList);
    }

    @Transactional
    public RequestDto createRequest(Integer userId, Integer eventId) {
        ServiceUtils.isExist(userRepository, userId, "Пользователь с данным id не найден");
        requestServiceUtils.isRequestRepeated(userId, eventId);

        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найдено");

        requestServiceUtils.isOrganizer(userId, event);
        requestServiceUtils.isPublished(event);
        requestServiceUtils.isLimitReached(event);

        Request request = createRequestObject(userId, event);

        request = requestRepository.save(request);

        return RequestMapper.toDto(request);
    }

    @Transactional
    public RequestDto cancelRequest(Integer userId, Integer requestId) {
        Request request = ServiceUtils.getIfExist(requestRepository, requestId, "Запрос с таким id не было найден");

        requestServiceUtils.isRequester(userId, request);

        request.setStatus(RequestStatus.CANCELED);

        request = requestRepository.save(request);

        return RequestMapper.toDto(request);
    }

    private Request createRequestObject(Integer userId, Event event) {
        Request request = new Request();
        request.setCreated(Instant.now());
        request.setEventId(event.getId());
        request.setUserId(userId);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        return request;
    }
}
