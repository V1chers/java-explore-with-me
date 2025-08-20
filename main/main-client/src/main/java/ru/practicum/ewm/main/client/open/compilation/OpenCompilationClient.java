package ru.practicum.ewm.main.client.open.compilation;

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
public class OpenCompilationClient extends BaseClient {

    private static final String API_PREFIX = "/compilations";

    @Autowired
    public OpenCompilationClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<List> getCompilations(Boolean pinned, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "pinned", pinned,
                "from", from,
                "size", size
        );

        return get("?from={from}&size={size}&pinned={pinned}", parameters, List.class);
    }

    public ResponseEntity<Object> getCompilation(Integer compId) {
        Map<String, Object> parameters = Map.of(
                "compId", compId
        );
        return get("/{compId}", parameters, Object.class);
    }
}
