package ru.practicum.ewm.main.client.admin.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.main.dto.event.PatchEventDto;

import java.util.List;
import java.util.Map;

@Service
public class AdminEventClient extends BaseClient {

    private static final String API_PREFIX = "/admin/events";

    @Autowired
    public AdminEventClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<List> getEvents(List<Integer> users,
                                          List<String> states,
                                          List<Integer> categories,
                                          String rangeStart,
                                          String rangeEnd,
                                          Integer from,
                                          Integer size) {
        Map<String, Object> parameters = Map.of(
                "users", users,
                "states", states,
                "categories", categories,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "from", from,
                "size", size
        );

        return get("?users={users}&states={states}&categories={categories}&rangeStart={rangeStart}" +
                "&rangeEnd={rangeEnd}&from={from}&size={size}", parameters, List.class);
    }

    public ResponseEntity<Object> updateEvent(Integer eventId, PatchEventDto patchEventDto) {
        Map<String, Object> parameters = Map.of(
                "eventId", eventId
        );

        return patch("/{eventId}", parameters, patchEventDto, Object.class);
    }
}
