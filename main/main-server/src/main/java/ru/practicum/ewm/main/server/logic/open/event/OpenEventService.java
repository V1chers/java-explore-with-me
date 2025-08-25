package ru.practicum.ewm.main.server.logic.open.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.main.dto.event.GetEventDto;
import ru.practicum.ewm.main.dto.event.GetShortEventDto;
import ru.practicum.ewm.main.server.client.StatsClient;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.EventMapper;
import ru.practicum.ewm.main.server.dal.event.EventRepository;
import ru.practicum.ewm.main.server.logic.validation.EventServiceUtils;
import ru.practicum.ewm.main.server.logic.validation.ServiceUtils;
import ru.practicum.ewm.stats.dto.GetStatsDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenEventService {

    private final EventRepository eventRepository;

    private final EventServiceUtils eventServiceUtils;

    private final StatsClient statsClient;

    public List<GetShortEventDto> getEvents(String text,
                                            List<Integer> categories,
                                            Boolean paid,
                                            String rangeStart,
                                            String rangeEnd,
                                            Boolean onlyAvailable,
                                            String sort,
                                            Integer from,
                                            Integer size,
                                            String ip) {
        if (text != null) {
            text = "%" + text + "%";
        }

        if (sort.equalsIgnoreCase("EVENT_DATE")) {
            return handleSortedByDate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size, ip);
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            return handleSortedByViews(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size, ip);
        } else {
            throw new ConflictException("параметр sort может быть равен только: EVENT_DATE, VIEWS ");
        }
    }

    public GetEventDto getEvent(int id, String ip) {
        Event event = ServiceUtils.getIfExist(eventRepository, id, "Событие с данным id не найдено");

        eventServiceUtils.isEventPublished(event);

        statsClient.createStats(id, ip);
        List<GetStatsDto> views = statsClient.getStats(id);

        return EventMapper.toGetDto(event, views);
    }

    private List<GetShortEventDto> handleSortedByDate(String text,
                                                      List<Integer> categories,
                                                      Boolean paid,
                                                      String rangeStart,
                                                      String rangeEnd,
                                                      Boolean onlyAvailable,
                                                      Integer from,
                                                      Integer size,
                                                      String ip) {
        List<Event> eventList = eventRepository.getAllFilteredSortedByDate(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, PageRequest.of(from, size)).toList();

        List<Integer> eventIdList = eventList.stream().map(Event::getId).toList();
        eventIdList.forEach(eventId -> statsClient.createStats(eventId, ip));
        HashMap<Integer, GetStatsDto> getStatsDtoList = statsClient.getStatsList(eventIdList);

        return EventMapper.toGetListDto(eventList, getStatsDtoList);
    }

    private List<GetShortEventDto> handleSortedByViews(String text,
                                                       List<Integer> categories,
                                                       Boolean paid,
                                                       String rangeStart,
                                                       String rangeEnd,
                                                       Boolean onlyAvailable,
                                                       Integer from,
                                                       Integer size,
                                                       String ip) {
        List<Event> eventList = eventRepository.getAllFiltered(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable);

        List<Integer> eventIdList = eventList.stream().map(Event::getId).toList();
        HashMap<Integer, GetStatsDto> getStatsDtoList = statsClient.getStatsList(eventIdList);

        List<GetShortEventDto> eventDtoList = EventMapper.toGetListDto(eventList, getStatsDtoList);
        List<GetShortEventDto> sortedEventDtoList = eventDtoList.stream()
                .sorted(Comparator.comparing(GetShortEventDto::getViews).reversed()).toList();
        List<GetShortEventDto> page = new ArrayList<>();

        for (int i = from * size; i < (from + 1) * size; i++) {
            try {
                page.add(sortedEventDtoList.get(i));
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        page.forEach(eventDto -> statsClient.createStats(eventDto.getId(), ip));

        return page;
    }
}
