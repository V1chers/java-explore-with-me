package ru.practicum.ewm.main.server.logic.admin.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.ewm.exception.models.BadRequestException;
import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.main.dto.event.*;
import ru.practicum.ewm.main.server.client.StatsClient;
import ru.practicum.ewm.main.server.dal.event.*;
import ru.practicum.ewm.main.server.logic.utils.EventServiceUtils;
import ru.practicum.ewm.main.server.logic.utils.ServiceUtils;
import ru.practicum.ewm.stats.dto.GetStatsDto;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventService {
    private final EventRepository eventRepository;

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

        List<Integer> eventIdList = eventList.stream().map(Event::getId).toList();
        HashMap<Integer, GetStatsDto> getStatsDtoHashMap = statsClient.getStatsList(eventIdList);

        return EventMapper.toGetDto(eventList, getStatsDtoHashMap);
    }

    public List<GetEventDto> getAllPendingEvents() {
        List<Event> eventList = eventRepository.getAllPending();

        return EventMapper.toGetDto(eventList);
    }

    @Transactional
    public GetEventDto updateEvent(@PathVariable Integer eventId,
                                   @RequestBody PatchEventDto patchEventDto) {
        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найдено");

        Event newEvent = EventMapper.fromAdminPatchDto(patchEventDto, event);

        eventServiceUtils.isAdminEventDateTooEarly(newEvent.getEventDate());

        eventServiceUtils.setUpdatingEventFields(patchEventDto, newEvent, event);

        checkStateAction(event, newEvent);

        newEvent = eventRepository.save(newEvent);

        return EventMapper.toGetDto(newEvent);
    }

    @Transactional
    public GetEventDtoWithComment cancelEventWithComment(@PathVariable Integer eventId,
                                                         @RequestBody PatchEventDtoWithComment patchEventDto) {
        isCancelUpdate(patchEventDto);

        Event event = ServiceUtils.getIfExist(eventRepository, eventId, "Событие с данным id не найдено");
        Event newEvent = EventMapper.fromAdminPatchDto(patchEventDto.getPatchEventDto(), event);

        eventServiceUtils.isAdminEventDateTooEarly(newEvent.getEventDate());

        eventServiceUtils.setUpdatingEventFields(patchEventDto.getPatchEventDto(), newEvent, event);
        setAdminComment(newEvent, patchEventDto.getAdminCommentDto());

        checkStateAction(event, newEvent);

        newEvent = eventRepository.save(newEvent);

        return EventMapper.toGetDtoWithComment(newEvent);
    }

    public void checkStateAction(Event event, Event updatingEvent) {
        if (updatingEvent.getStateAction() == null) {
            return;
        }
        if (event.getStateAction() == StateAction.PUBLISHED && updatingEvent.getStateAction() == StateAction.CANCELED) {
            throw new ConflictException("Нельзя отменить опубликованное событие");
        }
        if (event.getStateAction() == StateAction.PUBLISHED) {
            throw new ConflictException("Опубликованные события нельзя изменить");
        }
        if (event.getStateAction() != StateAction.PENDING && updatingEvent.getStateAction() == StateAction.PUBLISHED) {
            throw new ConflictException("Можно опубликовать только событие, ожидающее публикации");
        }
    }

    public void isCancelUpdate(PatchEventDtoWithComment patchEventDtoWithComment) {
        String patchStateAction = patchEventDtoWithComment.getPatchEventDto().getStateAction();

        if (!patchStateAction.equals("REJECT_EVENT")) {
            throw new BadRequestException("Событие должно быть отменено");
        }
    }

    private void setAdminComment(Event event, AdminCommentDto adminCommentDto) {
        if (adminCommentDto == null) {
            return;
        }

        AdminComment adminComment = new AdminComment();
        adminComment.setId(event.getId());
        adminComment.setComment(adminCommentDto.getComment());
        adminComment.setEvent(event);

        event.setAdminComment(adminComment);
    }
}
