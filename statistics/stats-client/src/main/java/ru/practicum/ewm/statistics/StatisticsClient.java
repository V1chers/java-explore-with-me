package ru.practicum.ewm.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.stats.dto.HitStatsDto;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsClient extends BaseClient {

    @Autowired
    public StatisticsClient(@Value("${ewm.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Void> createStats(HitStatsDto hitStatsDto) {
        return post("/hit", hitStatsDto, Void.class);
    }

    public ResponseEntity<List> getStats(String start, String end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                null, parameters, List.class);
    }
}
