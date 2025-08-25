package ru.practicum.ewm.statistics.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.models.BadRequestException;
import ru.practicum.ewm.statistics.server.model.GetStats;
import ru.practicum.ewm.statistics.server.model.Stats;
import ru.practicum.ewm.statistics.server.utils.StatsMapper;
import ru.practicum.ewm.stats.dto.GetStatsDto;
import ru.practicum.ewm.stats.dto.HitStatsDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;

    public void createStats(HitStatsDto hitStatsDto) {
        Stats stats = StatsMapper.fromHitDto(hitStatsDto);
        statisticsRepository.save(stats);
        log.info("Произошло сохранение статистики: {}", stats);
    }

    public List<GetStatsDto> getStats(String startString, String endString, List<String> uris, boolean unique) {
        log.info("Начало получения статистики: startString = {}, endString = {}, uris = {}, unique = {}",
                startString, endString, uris, unique);

        HashMap<String, Set<Integer>> uriPathIds = getUriPathIds(uris);
        Instant start = StringToInstant(startString);
        Instant end = StringToInstant(endString);
        List<GetStats> statsList = new ArrayList<>();

        isStartAfterEnd(start, end);

        if (uris == null) {
            if (unique) {
                statsList.addAll(statisticsRepository.getAllUniqueStats(start, end));
            } else {
                statsList.addAll(statisticsRepository.getAllStats(start, end));
            }
        } else {
            // мне показалось, что здесь может приемлемо n+1,
            // в рамках дипломной работы все равно больше одной итерации цикла не будет
            uriPathIds.forEach((String uriPath, Set<Integer> uriIdList) -> {
                if (unique) {
                    statsList.addAll(statisticsRepository.getUniqueStats(start, end, uriPath, uriIdList));
                } else {
                    statsList.addAll(statisticsRepository.getStats(start, end, uriPath, uriIdList));
                }
            });
        }

        log.info("Получена статистика: {}", statsList);
        return StatsMapper.toGetDto(statsList);

    }

    public void isStartAfterEnd(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Начало не может быть позже конца");
        }
    }

    private HashMap<String, Set<Integer>> getUriPathIds(List<String> uris) {
        HashMap<String, Set<Integer>> uriPathIds = new HashMap<>();

        if (uris != null) {
            uris.forEach(uri -> {
                String[] splittedUri = uri.split("/");

                uriPathIds.computeIfAbsent(splittedUri[1], k -> new HashSet<>());

                try {
                    uriPathIds.get(splittedUri[1])
                            .add(Integer.parseInt(splittedUri[2]));
                } catch (IndexOutOfBoundsException e) {
                    uriPathIds.get(splittedUri[1])
                            .add(-1);
                }
            });
        }

        return uriPathIds;
    }

    private Instant StringToInstant(String instantSting) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return StatsMapper.toInstant(LocalDateTime.parse(instantSting, dateTimeFormatter));
    }
}
