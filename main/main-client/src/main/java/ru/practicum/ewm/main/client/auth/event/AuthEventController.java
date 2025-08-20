package ru.practicum.ewm.main.client.auth.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.event.CreateEventDto;
import ru.practicum.ewm.main.dto.event.PatchEventDto;
import ru.practicum.ewm.main.dto.request.StatusUpdateRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
@SuppressWarnings({"unused", "rawtypes"})
public class AuthEventController {

    private final AuthEventClient authEventClient;

    @GetMapping
    public ResponseEntity<List> getEvents(@PathVariable Integer userId,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return authEventClient.getEvents(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createEvents(@PathVariable Integer userId,
                                               @Valid @RequestBody CreateEventDto createEventDto) {
        return authEventClient.createEvents(userId, createEventDto);
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable Integer userId,
                                           @PathVariable Integer eventId) {
        return authEventClient.getEvent(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Integer userId,
                                              @PathVariable Integer eventId,
                                              @Valid @RequestBody PatchEventDto patchEventDto) {
        return authEventClient.updateEvent(userId, eventId, patchEventDto);
    }

    @GetMapping(path = "/{eventId}/requests")
    public ResponseEntity<List> getEventRequests(@PathVariable Integer userId,
                                                 @PathVariable Integer eventId) {
        return authEventClient.getEventRequests(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}/requests")
    public ResponseEntity<Object> updateEventRequests(@PathVariable Integer userId,
                                                      @PathVariable Integer eventId,
                                                      @Valid @RequestBody StatusUpdateRequestDto statusUpdateRequestDto) {
        return authEventClient.updateEventRequests(userId, eventId, statusUpdateRequestDto);
    }
}
