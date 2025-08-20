package ru.practicum.ewm.main.server.logic.open.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.server.client.StatsClient;
import ru.practicum.ewm.main.server.dal.compialtion.Compilation;
import ru.practicum.ewm.main.server.dal.compialtion.CompilationMapper;
import ru.practicum.ewm.main.server.dal.compialtion.CompilationRepository;
import ru.practicum.ewm.main.server.logic.validation.ServiceUtils;
import ru.practicum.ewm.stats.dto.GetStatsDto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenCompilationService {

    private final CompilationRepository compilationRepository;

    private final StatsClient statsClient;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilationList = compilationRepository.findAll(pinned, PageRequest.of(from, size)).toList();

        HashMap<Integer, GetStatsDto> views = getViews(compilationList);

        return CompilationMapper.toDto(compilationList, views);
    }

    public CompilationDto getCompilation(Integer compId) {
        Compilation compilation = ServiceUtils.getIfExist(compilationRepository, compId, "Компиляции с таким id не найдено");

        HashMap<Integer, GetStatsDto> views = getViews(compilation);

        return CompilationMapper.toDto(compilation, views);
    }

    private HashMap<Integer, GetStatsDto> getViews(List<Compilation> compilationList) {
        Set<Integer> eventIdList = new HashSet<>();

        compilationList.forEach(compilation ->
                compilation.getEventList().forEach(event ->
                        eventIdList.add(event.getId())
                )
        );

        return statsClient.getStatsList(eventIdList);
    }

    private HashMap<Integer, GetStatsDto> getViews(Compilation compilation) {
        Set<Integer> eventIdList = new HashSet<>();

        compilation.getEventList().forEach(event ->
                eventIdList.add(event.getId())
        );

        return statsClient.getStatsList(eventIdList);
    }
}
