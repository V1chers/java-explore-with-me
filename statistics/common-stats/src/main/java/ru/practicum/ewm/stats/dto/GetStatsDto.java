package ru.practicum.ewm.stats.dto;

import lombok.Data;

@Data
public class GetStatsDto {

    private String app;

    private String uri;

    private Integer hits;
}
