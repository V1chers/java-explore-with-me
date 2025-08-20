package ru.practicum.ewm.main.server.logic.auth.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.request.RequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
@SuppressWarnings("unused")
public class AuthRequestController {

    private final AuthRequestService authRequestService;

    @GetMapping
    public List<RequestDto> getRequest(@PathVariable Integer userId) {
        return authRequestService.getRequest(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(@PathVariable Integer userId,
                                    @RequestParam Integer eventId) {
        return authRequestService.createRequest(userId, eventId);
    }

    @PatchMapping(path = "/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Integer userId,
                                    @PathVariable Integer requestId) {
        return authRequestService.cancelRequest(userId, requestId);
    }
}
