package ru.practicum.ewm.main.server.logic.auth.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.models.NotFoundException;
import ru.practicum.ewm.main.dto.event.CreateEventDto;
import ru.practicum.ewm.main.dto.event.GetEventDto;
import ru.practicum.ewm.main.dto.event.GetShortEventDto;
import ru.practicum.ewm.main.dto.event.PatchEventDto;
import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.dto.request.StatusUpdateRequestDto;
import ru.practicum.ewm.main.dto.request.StatusUpdateResultDto;
import ru.practicum.ewm.main.server.client.StatsClient;
import ru.practicum.ewm.main.server.dal.category.Category;
import ru.practicum.ewm.main.server.dal.category.CategoryRepository;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.EventMapper;
import ru.practicum.ewm.main.server.dal.event.EventRepository;
import ru.practicum.ewm.main.server.dal.location.Location;
import ru.practicum.ewm.main.server.dal.request.Request;
import ru.practicum.ewm.main.server.dal.request.RequestMapper;
import ru.practicum.ewm.main.server.dal.request.RequestRepository;
import ru.practicum.ewm.main.server.dal.request.RequestStatus;
import ru.practicum.ewm.main.server.dal.user.User;
import ru.practicum.ewm.main.server.dal.user.UserRepository;
import ru.practicum.ewm.main.server.logic.validation.EventServiceUtils;
import ru.practicum.ewm.main.server.logic.validation.RequestServiceUtils;
import ru.practicum.ewm.main.server.logic.validation.ServiceUtils;
import ru.practicum.ewm.stats.dto.GetStatsDto;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthEventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final EventServiceUtils eventServiceUtils;

    private final RequestServiceUtils requestServiceUtils;

    private final StatsClient statsClient;

    public List<GetShortEventDto> getEvents(Integer userId, Integer from, Integer size) {
        ServiceUtils.isExist(userRepository, userId, "Пользователь с данным id не существует");

        List<Event> eventList = eventRepository.findByUserId(userId, PageRequest.of(from, size)).getContent();

        List<Integer> eventIdList = eventList.stream().map(Event::getId).toList();
        HashMap<Integer, GetStatsDto> getStatsDtoList = statsClient.getStatsList(eventIdList);

        return EventMapper.toGetListDto(eventList, getStatsDtoList);
    }

    @Transactional
    public GetEventDto createEvents(Integer userId, CreateEventDto createEventDto) {
        User user = ServiceUtils.getIfExist(userRepository, userId, "Пользователь с данным id не найден");
        Category category = ServiceUtils.getIfExist(categoryRepository, createEventDto.getCategory(),
                "Категория с данным id не найдено");
        Location location = eventServiceUtils.getLocation(createEventDto.getLocation());

        Event event = EventMapper.fromCreateDto(createEventDto);

        eventServiceUtils.isEventDateTooEarly(event.getEventDate());

        event.setUser(user);
        event.setCategory(category);
        event.setLocation(location);

        event = eventRepository.save(event);

        return EventMapper.toGetDto(event);
    }

    public GetEventDto getEvent(Integer userId, Integer eventId) {
        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найдено");

        if (event.getUser().getId() == userId) {
            List<GetStatsDto> views = statsClient.getStats(eventId);

            return EventMapper.toGetDto(event, views);
        } else {
            throw new NotFoundException("Неверное id пользователя");
        }
    }

    @Transactional
    public GetEventDto updateEvent(Integer userId, Integer eventId, PatchEventDto patchEventDto) {
        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найдено");

        if (event.getUser().getId() != userId) {
            throw new NotFoundException("Неверное id пользователя");
        }
        eventServiceUtils.isWaitingOrCancel(event.getStateAction());

        Event newEvent = EventMapper.fromUserPatchDto(patchEventDto, event);

        eventServiceUtils.isEventDateTooEarly(newEvent.getEventDate());

        if (patchEventDto.getCategory() != null) {
            Category category = ServiceUtils.getIfExist(categoryRepository, patchEventDto.getCategory(),
                    "Категория с данным id не найдено");
            newEvent.setCategory(category);
        } else {
            newEvent.setCategory(event.getCategory());
        }
        if (patchEventDto.getLocation() != null) {
            newEvent.setLocation(eventServiceUtils.getLocation(patchEventDto.getLocation())); // переделать
        } else {
            newEvent.setLocation(event.getLocation());
        }

        newEvent = eventRepository.save(newEvent);

        List<GetStatsDto> views = statsClient.getStats(eventId);

        return EventMapper.toGetDto(newEvent, views);
    }

    public List<RequestDto> getEventRequests(Integer userId, Integer eventId) {
        ServiceUtils.isExist(userRepository, userId, "Пользователь с данным id не найден");

        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найдено");

        eventServiceUtils.isOrganizer(userId, event);

        List<Request> requestList = requestRepository.findAllByEventId(eventId);

        return RequestMapper.toDto(requestList);
    }

    @Transactional
    public StatusUpdateResultDto updateEventRequests(Integer userId, Integer eventId,
                                                     StatusUpdateRequestDto statusUpdateRequestDto) {
        ServiceUtils.isExist(userRepository, userId, "Пользователь с данным id не найден");

        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найден");

        eventServiceUtils.isModerationRequired(event);
        requestServiceUtils.isLimitReached(event);

        List<Request> requestList = requestRepository.findAllByEventIdAndIdIn(eventId,
                statusUpdateRequestDto.getRequestIds());

        eventServiceUtils.isRequestsPending(requestList);

        int confirmedRequests = requestRepository.findAllByEventIdAndStatus(eventId,
                RequestStatus.CONFIRMED).size();

        for (Request request : requestList) {
            if (confirmedRequests < event.getParticipantLimit() || event.getParticipantLimit() == 0) {
                confirmedRequests++;
                request.setStatus(RequestStatus.of(statusUpdateRequestDto.getStatus()));
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
        }

        requestRepository.saveAll(requestList);

        return RequestMapper.toStatusUpdateResultDto(requestList);
    }
}
