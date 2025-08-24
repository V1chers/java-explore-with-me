package ru.practicum.ewm.main.client.auth.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;

import java.util.List;
import java.util.Map;

@Service
public class AuthRequestClient extends BaseClient {

    private static final String API_PREFIX = "/users/{userId}/requests";

    @Autowired
    public AuthRequestClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<List> getRequest(Integer userId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId
        );

        return get("", parameters, List.class);
    }

    public ResponseEntity<Object> createRequest(Integer userId, Integer eventId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "eventId", eventId
        );

        return post("?eventId={eventId}", parameters, Object.class);
    }

    public ResponseEntity<Object> cancelRequest(Integer userId, Integer requestId) {
        Map<String, Object> parameters = Map.of(
                "userId", userId,
                "requestId", requestId
        );

        return patch("/{requestId}/cancel", parameters, Object.class);
    }
}
