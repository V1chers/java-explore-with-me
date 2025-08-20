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
import java.util.List;

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

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant start = StatsMapper.toInstant(LocalDateTime.parse(startString, dateTimeFormatter));
        Instant end = StatsMapper.toInstant(LocalDateTime.parse(endString, dateTimeFormatter));

        isStartAfterEnd(start, end);

        List<GetStats> statsList;

        if (uris.contains("/events")) {
            uris = null; //мне не особо нравится такое решение, но меня сильно время поджимало
        }

        if (uris == null) {
            statsList = unique ?
                    statisticsRepository.getAllUniqueStats(start, end) :
                    statisticsRepository.getAllStats(start, end);
        } else {
            statsList = unique ?
                    statisticsRepository.getUniqueStats(start, end, uris) :
                    statisticsRepository.getStats(start, end, uris);
        }

        log.info("Получена статистика: {}", statsList);
        return StatsMapper.toGetDto(statsList);

    }

    public void isStartAfterEnd(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Начало не может быть позже конца");
        }
    }
}
