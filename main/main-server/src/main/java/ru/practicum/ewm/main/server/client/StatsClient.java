package ru.practicum.ewm.main.server.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.stats.dto.GetStatsDto;
import ru.practicum.ewm.stats.dto.HitStatsDto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class StatsClient {

    private static final String app = "ewm-main-service";

    private final RestTemplate restTemplate;
    private final DateTimeFormatter format;

    @Autowired
    public StatsClient(@Value("${ewm.stats.client.url}") String serverUrl, RestTemplateBuilder builder) {
        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

        format = DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm:ss");
    }

    public List<GetStatsDto> getStats(int eventId) {

        return restTemplate.exchange(getRequestParams(eventId), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GetStatsDto>>() {
                }).getBody();
    }

    public HashMap<Integer, GetStatsDto> getStatsList(Collection<Integer> eventIdList) {
        List<GetStatsDto> getStatsDtoList = restTemplate.exchange(getRequestParams(eventIdList), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GetStatsDto>>() {
                }).getBody();

        return toHashMap(getStatsDtoList);
    }

    public void createStats(int eventId, String ip) {
        restTemplate.postForLocation("/hit", createHitStatsDto(eventId, ip));
    }

    private String getRequestParams(Integer eventId) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+0"));
        UriComponentsBuilder builder;


        builder = UriComponentsBuilder.fromUriString("/stats")
                .queryParam("uris", "/events/" + eventId)
                .queryParam("start", "1950-01-01+00:00:00")
                .queryParam("end", now.plusDays(2).format(format))
                .queryParam("unique", true);

        return builder.toUriString();
    }

    private String getRequestParams(Collection<Integer> eventIdList) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+0"));
        UriComponentsBuilder builder;

        List<String> urisParam = eventIdList.stream().map(eventId -> "/events/" + eventId).toList();

        builder = UriComponentsBuilder.fromUriString("/stats")
                .queryParam("uris", urisParam)
                .queryParam("start", "1950-01-01+00:00:00")
                .queryParam("end", now.plusDays(2).format(format))
                .queryParam("unique", true);

        return builder.toUriString();
    }

    private HitStatsDto createHitStatsDto(int eventId, String ip) {
        HitStatsDto hitStatsDto = new HitStatsDto();
        hitStatsDto.setApp(app);
        hitStatsDto.setTimestamp(LocalDateTime.now());
        hitStatsDto.setUri("/events/" + eventId);
        hitStatsDto.setIp(ip);

        return hitStatsDto;
    }

    private HashMap<Integer, GetStatsDto> toHashMap(List<GetStatsDto> getStatsDtoList) {
        HashMap<Integer, GetStatsDto> getStatsDtoHashMap = new HashMap<>();

        getStatsDtoList.forEach(getStatsDto -> {
            String[] splittedUri = getStatsDto.getUri().split("/");
            getStatsDtoHashMap.put(Integer.parseInt(splittedUri[2]), getStatsDto);
        });

        return getStatsDtoHashMap;
    }
}
