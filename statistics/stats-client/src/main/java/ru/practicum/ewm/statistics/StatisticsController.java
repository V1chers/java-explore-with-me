package ru.practicum.ewm.statistics;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.HitStatsDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsClient statisticsClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createStats(@Valid @RequestBody HitStatsDto hitStatsDto) {
        statisticsClient.createStats(hitStatsDto);
    }

    @GetMapping("/stats")
    public Object getStats(@RequestParam @NotBlank String start,
                           @RequestParam @NotBlank String end,
                           @RequestParam(required = false) List<String> uris,
                           @RequestParam(required = false) boolean unique) {
        return statisticsClient.getStats(start, end, uris, unique);
    }
}
