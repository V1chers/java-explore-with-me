package ru.practicum.ewm.main.server.logic.open.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.event.GetEventDto;
import ru.practicum.ewm.main.dto.event.GetShortEventDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@SuppressWarnings("unused")
public class OpenEventController {

    private final OpenEventService openEventService;

    @GetMapping
    public List<GetShortEventDto> getEvents(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<@Positive Integer> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(required = false) Boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) String ip,
                                            HttpServletRequest request) {
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return openEventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                ip);
    }

    @GetMapping(path = "/{id}")
    public GetEventDto getEvent(@PathVariable int id, @RequestParam(required = false) String ip,
                                HttpServletRequest request) {
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return openEventService.getEvent(id, ip);
    }
}
