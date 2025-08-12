package ru.practicum.ewm.statistics.utils;

import ru.practicum.ewm.statistics.model.App;
import ru.practicum.ewm.statistics.model.GetStats;
import ru.practicum.ewm.statistics.model.Stats;
import ru.practicum.ewm.stats.dto.GetStatsDto;
import ru.practicum.ewm.stats.dto.HitStatsDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class StatsMapper {

    /*public static List<GetStatsDto> toGetDto(List<Stats> statsList) {
        HashMap<String, List<Stats>> groupedStatsList = new HashMap<>();
        List<GetStatsDto> getStatsDtoList = new ArrayList<>();

        statsList.forEach(stats -> {
            groupedStatsList.computeIfAbsent(stats.getUri(), k -> new ArrayList<>());
            groupedStatsList.get(stats.getUri())
                    .add(stats);
        });

        groupedStatsList.forEach((uri, groupedStats) -> {
            GetStatsDto getStatsDto = new GetStatsDto();
            getStatsDto.setUri(uri);
            String app = groupedStats.getFirst().getApp().toString();
            getStatsDto.setApp(app);
            getStatsDto.setHits(groupedStats.size());

            getStatsDtoList.add(getStatsDto);
        });

        return getStatsDtoList;
    }*/

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
