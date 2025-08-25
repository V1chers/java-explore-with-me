package ru.practicum.ewm.main.client.auth.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
@SuppressWarnings({"unused", "rawtypes"})
public class AuthRequestController {

    private final AuthRequestClient authRequestClient;

    @GetMapping
    public ResponseEntity<List> getRequest(@PathVariable Integer userId) {
        return authRequestClient.getRequest(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@PathVariable Integer userId,
                                                @RequestParam Integer eventId) {
        return authRequestClient.createRequest(userId, eventId);
    }

    @PatchMapping(path = "/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable Integer userId,
                                                @PathVariable Integer requestId) {
        return authRequestClient.cancelRequest(userId, requestId);
    }
}
