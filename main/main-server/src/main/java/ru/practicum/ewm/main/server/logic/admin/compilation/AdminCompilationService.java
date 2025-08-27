package ru.practicum.ewm.main.server.logic.admin.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.dto.compilation.CreateCompilationDto;
import ru.practicum.ewm.main.dto.compilation.UpdatedCompilationDto;
import ru.practicum.ewm.main.server.dal.compialtion.Compilation;
import ru.practicum.ewm.main.server.dal.compialtion.CompilationMapper;
import ru.practicum.ewm.main.server.dal.compialtion.CompilationRepository;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.EventRepository;
import ru.practicum.ewm.main.server.logic.utils.ServiceUtils;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto createCompilation(CreateCompilationDto createCompilationDto) {
        Compilation compilation = CompilationMapper.fromCreateDto(createCompilationDto);
        List<Event> eventList = eventRepository.findAllByIdList(createCompilationDto.getEvents());
        compilation.setEventList(new HashSet<>(eventList));

        compilation = compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilation);
    }

    @Transactional
    public void deleteCompilation(int compId) {
        ServiceUtils.isExist(compilationRepository, compId, "Компиляция с таким id не найден");

        compilationRepository.deleteById(compId);
    }

    @Transactional
    public CompilationDto updateCompilation(int compId, UpdatedCompilationDto compilationDto) {
        Compilation compilation = ServiceUtils.getIfExist(compilationRepository, compId,
                "Компиляция с таким id не найдена");
        List<Event> eventList = eventRepository.findAllByIdList(compilationDto.getEvents());

        Compilation updatedCompilation = CompilationMapper.toUpdatedCompilation(compilationDto, compilation, eventList);

        compilationRepository.save(updatedCompilation);

        return CompilationMapper.toDto(updatedCompilation);
    }
}
