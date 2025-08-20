package ru.practicum.ewm.statistics.server;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.GetStatsDto;
import ru.practicum.ewm.stats.dto.HitStatsDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void createStats(@Valid @RequestBody HitStatsDto hitStatsDto) {
        statisticsService.createStats(hitStatsDto);
    }

    @GetMapping("/stats")
    public List<GetStatsDto> getStats(@RequestParam @NotBlank String start,
                                      @RequestParam @NotBlank String end,
                                      @RequestParam(required = false) List<String> uris,
                                      @RequestParam(required = false) boolean unique) {
        return statisticsService.getStats(start, end, uris, unique);
    }
}
