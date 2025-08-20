package ru.practicum.ewm.main.client.auth.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.main.dto.event.CreateEventDto;
import ru.practicum.ewm.main.dto.event.PatchEventDto;
import ru.practicum.ewm.main.dto.request.StatusUpdateRequestDto;

import java.util.List;
import java.util.Map;

@Service
public class AuthEventClient extends BaseClient {

    private static final String API_PREFIX = "/users/{userId}/events";

    @Autowired
    public AuthEventClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<List> getEvents(Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "from", from,
                "size", size
        );

        return get("?from={from}&size={size}", parameters, List.class);
    }

    public ResponseEntity<Object> createEvents(Integer userId, CreateEventDto createEventDto) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );

        return post("", parameters, createEventDto, Object.class);
    }

    public ResponseEntity<Object> getEvent(Integer userId, Integer eventId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "eventId", eventId
        );

        return get("/{eventId}", parameters, Object.class);
    }

    public ResponseEntity<Object> updateEvent(Integer userId, Integer eventId, PatchEventDto patchEventDto) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "eventId", eventId
        );

        return patch("/{eventId}", parameters, patchEventDto, Object.class);
    }

    public ResponseEntity<List> getEventRequests(Integer userId, Integer eventId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "eventId", eventId
        );

        return get("/{eventId}/requests", parameters, List.class);
    }

    public ResponseEntity<Object> updateEventRequests(Integer userId, Integer eventId, StatusUpdateRequestDto statusUpdateRequestDto) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "eventId", eventId
        );

        return patch("/{eventId}/requests", parameters, statusUpdateRequestDto, Object.class);
    }
}
