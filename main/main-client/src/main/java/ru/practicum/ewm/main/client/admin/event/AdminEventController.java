package ru.practicum.ewm.main.client.admin.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.event.PatchEventDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@SuppressWarnings({"unused", "rawtypes"})
public class AdminEventController {

    private final AdminEventClient adminEventClient;

    @GetMapping
    public ResponseEntity<List> getEvents(@RequestParam(required = false) List<Integer> users,
                                          @RequestParam(required = false) List<String> states,
                                          @RequestParam(required = false) List<Integer> categories,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        return adminEventClient.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Integer eventId,
                                              @Valid @RequestBody PatchEventDto patchEventDto) {
        return adminEventClient.updateEvent(eventId, patchEventDto);
    }
}
