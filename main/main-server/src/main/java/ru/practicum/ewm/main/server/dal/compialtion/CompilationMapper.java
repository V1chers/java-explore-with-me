package ru.practicum.ewm.main.server.dal.compialtion;

import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.dto.compilation.CreateCompilationDto;
import ru.practicum.ewm.main.dto.compilation.UpdatedCompilationDto;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.EventMapper;
import ru.practicum.ewm.stats.dto.GetStatsDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CompilationMapper {

    public static Compilation fromCreateDto(CreateCompilationDto createCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(createCompilationDto.isPinned());
        compilation.setTitle(createCompilationDto.getTitle());

        return compilation;
    }

    public static CompilationDto toDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(EventMapper.toGetShortDto(new ArrayList<>(compilation.getEventList())));

        return compilationDto;
    }

    public static CompilationDto toDto(Compilation compilation, HashMap<Integer, GetStatsDto> views) {
        CompilationDto compilationDto = CompilationMapper.toDto(compilation);

        compilationDto.getEvents().forEach(eventDto -> {
            if (views.get(eventDto.getId()) != null) {
                int eventViews = views.get(eventDto.getId()).getHits();
                eventDto.setViews(eventViews);
            } else {
                eventDto.setViews(0);
            }
        });

        return compilationDto;
    }

    public static List<CompilationDto> toDto(List<Compilation> compilationList) {
        return compilationList.stream().map(CompilationMapper::toDto).toList();
    }

    public static List<CompilationDto> toDto(List<Compilation> compilationList, HashMap<Integer, GetStatsDto> views) {
        return compilationList.stream().map(compilation -> CompilationMapper.toDto(compilation, views)).toList();
    }

    public static Compilation toUpdatedCompilation(UpdatedCompilationDto compilationDto, Compilation compilation,
                                                   List<Event> updatedList) {
        Compilation updatedCompilation = new Compilation();
        updatedCompilation.setId(compilation.getId());
        updatedCompilation.setTitle(compilationDto.getTitle() == null ? compilation.getTitle() : compilationDto.getTitle());
        updatedCompilation.setPinned(compilationDto.getPinned() == null ? compilation.getPinned() : compilationDto.getPinned());
        updatedCompilation.setEventList(updatedList.isEmpty() ? compilation.getEventList() : new HashSet<>(updatedList));

        return updatedCompilation;
    }
}
