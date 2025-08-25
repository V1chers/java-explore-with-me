package ru.practicum.ewm.main.client.open.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@SuppressWarnings({"unused", "rawtypes"})
public class OpenEventController {

    private final OpenEventClient openEventClient;

    @GetMapping
    public ResponseEntity<List> getEvents(@RequestParam(required = false) String text,
                                          @RequestParam(required = false) List<Integer> categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(required = false) Boolean onlyAvailable,
                                          @RequestParam(required = false) String sort,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          HttpServletRequest request) {
        return openEventClient.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getEvent(@PathVariable int id, HttpServletRequest request) {
        return openEventClient.getEvent(id);
    }
}
