package ru.practicum.ewm.main.client.open.event;

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
public class OpenEventClient extends BaseClient {

    private static final String API_PREFIX = "/events";

    @Autowired
    public OpenEventClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<List> getEvents(String text,
                                          List<Integer> categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          Boolean onlyAvailable,
                                          String sort,
                                          Integer from,
                                          Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "categories", categories,
                "paid", paid,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "onlyAvailable", onlyAvailable,
                "sort", sort,
                "from", from,
                "size", size
        );

        return get("?text={text}&categories={categories}&paid={paid}&rangeStart={rangeStart}&rangeEnd={rangeEnd}" +
                "&onlyAvailable={onlyAvailable}&sort={sort}&from={from}&size={size}", parameters, List.class);
    }

    public ResponseEntity<Object> getEvent(int id) {
        Map<String, Object> parameters = Map.of(
                "id", id
        );

        return get("/{id}", parameters, Object.class);
    }
}
