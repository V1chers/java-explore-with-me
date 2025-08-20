package ru.practicum.ewm.main.server.logic.auth.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.event.CreateEventDto;
import ru.practicum.ewm.main.dto.event.GetEventDto;
import ru.practicum.ewm.main.dto.event.GetShortEventDto;
import ru.practicum.ewm.main.dto.event.PatchEventDto;
import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.dto.request.StatusUpdateRequestDto;
import ru.practicum.ewm.main.dto.request.StatusUpdateResultDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@SuppressWarnings("unused")
public class AuthEventController {

    private final AuthEventService authEventService;

    @GetMapping
    public List<GetShortEventDto> getEvents(@PathVariable Integer userId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        return authEventService.getEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GetEventDto createEvents(@PathVariable Integer userId,
                                    @Valid @RequestBody CreateEventDto createEventDto) {
        return authEventService.createEvents(userId, createEventDto);
    }

    @GetMapping(path = "/{eventId}")
    public GetEventDto getEvent(@PathVariable Integer userId,
                                @PathVariable Integer eventId) {
        return authEventService.getEvent(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public GetEventDto updateEvent(@PathVariable Integer userId,
                                   @PathVariable Integer eventId,
                                   @Valid @RequestBody PatchEventDto patchEventDto) {
        return authEventService.updateEvent(userId, eventId, patchEventDto);
    }

    @GetMapping(path = "/{eventId}/requests")
    public List<RequestDto> getEventRequests(@PathVariable Integer userId,
                                             @PathVariable Integer eventId) {
        return authEventService.getEventRequests(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}/requests")
    public StatusUpdateResultDto updateEventRequests(@PathVariable Integer userId,
                                                     @PathVariable Integer eventId,
                                                     @Valid @RequestBody StatusUpdateRequestDto statusUpdateRequestDto) {
        return authEventService.updateEventRequests(userId, eventId, statusUpdateRequestDto);
    }
}
