package ru.practicum.ewm.main.server.logic.admin.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.main.dto.event.GetEventDto;
import ru.practicum.ewm.main.dto.event.PatchEventDto;
import ru.practicum.ewm.main.server.client.StatsClient;
import ru.practicum.ewm.main.server.dal.category.Category;
import ru.practicum.ewm.main.server.dal.category.CategoryRepository;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.EventMapper;
import ru.practicum.ewm.main.server.dal.event.EventRepository;
import ru.practicum.ewm.main.server.dal.event.StateAction;
import ru.practicum.ewm.main.server.logic.validation.EventServiceUtils;
import ru.practicum.ewm.main.server.logic.validation.ServiceUtils;
import ru.practicum.ewm.stats.dto.GetStatsDto;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventService {
    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final EventServiceUtils eventServiceUtils;

    private final StatsClient statsClient;

    public List<GetEventDto> getEvents(List<Integer> users,
                                       List<String> states,
                                       List<Integer> categories,
                                       String rangeStart,
                                       String rangeEnd,
                                       Integer from,
                                       Integer size) {
        List<Integer> statesIdList = null;
        if (states != null) {
            statesIdList = states.stream().map(state -> StateAction.of(state).getId() - 1).toList();
        }
        Instant start = eventServiceUtils.stringToInstant(rangeStart);
        Instant end = eventServiceUtils.stringToInstant(rangeEnd);

        List<Event> eventList = eventRepository
                .findAllFiltered(users, statesIdList, categories, start, end, PageRequest.of(from, size)).toList();

        System.out.println(eventList);
        List<Integer> eventIdList = eventList.stream().map(Event::getId).toList();
        HashMap<Integer, GetStatsDto> getStatsDtoHashMap = statsClient.getStatsList(eventIdList);

        return EventMapper.toGetDto(eventList, getStatsDtoHashMap);
    }

    @Transactional
    public GetEventDto updateEvent(@PathVariable Integer eventId,
                                   @RequestBody PatchEventDto patchEventDto) {
        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найдено");

        Event newEvent = EventMapper.fromAdminPatchDto(patchEventDto, event);

        eventServiceUtils.isAdminEventDateTooEarly(newEvent.getEventDate());

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
        checkStateAction(event, newEvent);

        newEvent = eventRepository.save(newEvent);

        return EventMapper.toGetDto(newEvent);
    }

    public void checkStateAction(Event event, Event updatingEvent) {
        if (updatingEvent.getStateAction() == null) {
            return;
        }
        if (event.getStateAction() == StateAction.PUBLISHED) {
            throw new ConflictException("Опубликованные события нельзя изменить");
        }
        if (event.getStateAction() != StateAction.PENDING && updatingEvent.getStateAction() == StateAction.PUBLISHED) {
            throw new ConflictException("Можно опубликовать только событие, ожидающее публикации");
        }
        if (event.getStateAction() == StateAction.PUBLISHED && updatingEvent.getStateAction() == StateAction.CANCELED) {
            throw new ConflictException("Нельзя отменить опубликованное событие");
        }
    }
}
