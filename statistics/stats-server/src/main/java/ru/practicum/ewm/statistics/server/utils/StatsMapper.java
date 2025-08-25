package ru.practicum.ewm.statistics.server.utils;

import ru.practicum.ewm.statistics.server.model.App;
import ru.practicum.ewm.statistics.server.model.GetStats;
import ru.practicum.ewm.statistics.server.model.Stats;
import ru.practicum.ewm.stats.dto.GetStatsDto;
import ru.practicum.ewm.stats.dto.HitStatsDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class StatsMapper {

    public static List<GetStatsDto> toGetDto(List<GetStats> statsList) {
        List<GetStatsDto> getStatsDtoList = new ArrayList<>();

        statsList.forEach(getStats -> {
            GetStatsDto getStatsDto = new GetStatsDto();
            getStatsDto.setUri(getStats.getUri());
            getStatsDto.setApp(getStats.getApp().toString());
            getStatsDto.setHits(getStats.getTotalViews());

            getStatsDtoList.add(getStatsDto);
        });

        return getStatsDtoList;
    }

    public static Stats fromHitDto(HitStatsDto hitStatsDto) {
        Stats stats = new Stats();
        stats.setApp(App.of(hitStatsDto.getApp()));
        stats.setUri(hitStatsDto.getUri());
        stats.setIp(hitStatsDto.getIp());
        stats.setCreated(toInstant(hitStatsDto.getTimestamp()));

        return stats;
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
