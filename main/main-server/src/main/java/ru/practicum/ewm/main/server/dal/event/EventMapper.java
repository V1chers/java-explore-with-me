package ru.practicum.ewm.main.server.dal.event;

import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.main.dto.event.CreateEventDto;
import ru.practicum.ewm.main.dto.event.GetEventDto;
import ru.practicum.ewm.main.dto.event.GetShortEventDto;
import ru.practicum.ewm.main.dto.event.PatchEventDto;
import ru.practicum.ewm.main.server.dal.category.CategoryMapper;
import ru.practicum.ewm.main.server.dal.location.LocationMapper;
import ru.practicum.ewm.main.server.dal.request.RequestStatus;
import ru.practicum.ewm.main.server.dal.user.UserMapper;
import ru.practicum.ewm.stats.dto.GetStatsDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

public class EventMapper {

    public static Event fromCreateDto(CreateEventDto createEventDto) {
        Event event = new Event();
        event.setAnnotation(createEventDto.getAnnotation());
        event.setDescription(createEventDto.getDescription());
        event.setTitle(createEventDto.getTitle());
        event.setEventDate(toInstant(createEventDto.getEventDate()));
        if (createEventDto.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(createEventDto.getPaid());
        }
        if (createEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(createEventDto.getParticipantLimit());
        }
        if (createEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else {
            event.setRequestModeration(createEventDto.getRequestModeration());
        }
        event.setStateAction(StateAction.PENDING);

        event.setCreatedOn(Instant.now());

        return event;
    }

    public static GetShortEventDto toGetListDto(Event event) {
        GetShortEventDto getShortEventDto = new GetShortEventDto();
        getShortEventDto.setId(event.getId());
        getShortEventDto.setInitiator(UserMapper.toDto(event.getUser()));
        getShortEventDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        getShortEventDto.setAnnotation(event.getAnnotation());
        getShortEventDto.setTitle(event.getTitle());
        getShortEventDto.setEventDate(toLocalDateTime(event.getEventDate()));
        getShortEventDto.setPaid(event.getPaid());

        if (event.getRequestList() != null) {
            getShortEventDto.setConfirmedRequests(event.getRequestList().stream()
                    .filter(request -> request.getStatus() == RequestStatus.CONFIRMED).toList().size());
        }
        return getShortEventDto;
    }

    public static List<GetShortEventDto> toGetListDto(List<Event> eventList) {
        return eventList.stream().map(EventMapper::toGetListDto).toList();
    }

    public static List<GetShortEventDto> toGetListDto(List<Event> eventList, HashMap<Integer, GetStatsDto> getStatsDtoHashMap) {
        return eventList.stream().map(event -> {
            GetShortEventDto getShortEventDto = EventMapper.toGetListDto(event);

            GetStatsDto getStatsDto = getStatsDtoHashMap.get(event.getId());
            if (getStatsDto != null) {
                getShortEventDto.setViews(getStatsDto.getHits());
            } else {
                getShortEventDto.setViews(0);
            }

            return getShortEventDto;
        }).toList();
    }

    public static GetEventDto toGetDto(Event event) {
        GetEventDto getEventDto = new GetEventDto();
        getEventDto.setId(event.getId());
        getEventDto.setInitiator(UserMapper.toDto(event.getUser()));
        getEventDto.setCategory(CategoryMapper.toDto(event.getCategory()));
        getEventDto.setLocation(LocationMapper.toDto(event.getLocation()));
        getEventDto.setState(event.getStateAction().toString());
        getEventDto.setAnnotation(event.getAnnotation());
        getEventDto.setDescription(event.getDescription());
        getEventDto.setTitle(event.getTitle());
        getEventDto.setEventDate(toLocalDateTime(event.getEventDate()));
        getEventDto.setPaid(event.getPaid());
        getEventDto.setParticipantLimit(event.getParticipantLimit());
        getEventDto.setRequestModeration(event.getRequestModeration());
        getEventDto.setCreatedOn(toLocalDateTime(event.getCreatedOn()));
        getEventDto.setPublishedOn(toLocalDateTime(event.getPublishedOn()));

        if (event.getRequestList() != null) {
            getEventDto.setConfirmedRequests(event.getRequestList().stream()
                    .filter(request -> request.getStatus() == RequestStatus.CONFIRMED).toList().size());
        }

        return getEventDto;
    }

    public static GetEventDto toGetDto(Event event, List<GetStatsDto> views) {
        GetEventDto getEventDto = EventMapper.toGetDto(event);
        if (views == null || views.isEmpty()) {
            getEventDto.setViews(0);
        } else {
            getEventDto.setViews(views.getFirst().getHits());
        }

        return getEventDto;
    }

    public static List<GetEventDto> toGetDto(List<Event> eventList, HashMap<Integer, GetStatsDto> getStatsDtoHashMap) {
        return eventList.stream().map(event -> {
            GetEventDto getEventDto = EventMapper.toGetDto(event);

            GetStatsDto getStatsDto = getStatsDtoHashMap.get(event.getId());
            if (getStatsDto != null) {
                getEventDto.setViews(getStatsDto.getHits());
            } else {
                getEventDto.setViews(0);
            }

            return getEventDto;
        }).toList();
    }

    public static Event fromPatchDto(PatchEventDto patchEventDto, Event event) {
        Event newEvent = new Event();
        newEvent.setUser(event.getUser());
        newEvent.setCreatedOn(event.getCreatedOn());

        newEvent.setId(event.getId());
        newEvent.setRequestList(event.getRequestList());
        newEvent.setAnnotation(
                patchEventDto.getAnnotation() != null ? patchEventDto.getAnnotation() : event.getAnnotation()
        );
        newEvent.setDescription(
                patchEventDto.getDescription() != null ? patchEventDto.getDescription() : event.getDescription()
        );
        newEvent.setEventDate(
                patchEventDto.getEventDate() != null ? toInstant(patchEventDto.getEventDate()) : event.getEventDate()
        );
        newEvent.setPaid(
                patchEventDto.getPaid() != null ? patchEventDto.getPaid() : event.getPaid()
        );
        newEvent.setParticipantLimit(
                patchEventDto.getParticipantLimit() != null ? patchEventDto.getParticipantLimit() : event.getParticipantLimit()
        );
        newEvent.setRequestModeration(
                patchEventDto.getRequestModeration() != null ? patchEventDto.getRequestModeration() : event.getRequestModeration()
        );
        newEvent.setTitle(
                patchEventDto.getTitle() != null ? patchEventDto.getTitle() : event.getTitle()
        );

        return newEvent;
    }

    public static Event fromAdminPatchDto(PatchEventDto patchEventDto, Event event) {
        Event newEvent = EventMapper.fromPatchDto(patchEventDto, event);

        if (patchEventDto.getStateAction() == null) {
            newEvent.setStateAction(event.getStateAction());
        } else if (patchEventDto.getStateAction().equalsIgnoreCase("PUBLISH_EVENT")) {
            newEvent.setPublishedOn(Instant.now());
            newEvent.setStateAction(StateAction.PUBLISHED);
        } else if (patchEventDto.getStateAction().equalsIgnoreCase("REJECT_EVENT")) {
            newEvent.setStateAction(StateAction.CANCELED);
        } else {
            throw new ConflictException("StateAction могут быть равны только: PUBLISH_EVENT, REJECT_EVENT");
        }

        return newEvent;
    }

    public static Event fromUserPatchDto(PatchEventDto patchEventDto, Event event) {
        Event newEvent = EventMapper.fromPatchDto(patchEventDto, event);

        if (patchEventDto.getStateAction() == null) {
            newEvent.setStateAction(event.getStateAction());
        } else if (patchEventDto.getStateAction().equalsIgnoreCase("SEND_TO_REVIEW")) {
            newEvent.setStateAction(StateAction.PENDING);
        } else if (patchEventDto.getStateAction().equalsIgnoreCase("CANCEL_REVIEW")) {
            newEvent.setStateAction(StateAction.CANCELED);
        } else {
            throw new ConflictException("StateAction могут быть равны только: SEND_TO_REVIEW, CANCEL_REVIEW");
        }

        return newEvent;
    }

    private static Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }

        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    private static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }

        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
