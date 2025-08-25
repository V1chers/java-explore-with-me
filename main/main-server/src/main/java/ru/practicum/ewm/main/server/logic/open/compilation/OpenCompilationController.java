package ru.practicum.ewm.main.server.logic.open.compilation;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.compilation.CompilationDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@SuppressWarnings("unused")
public class OpenCompilationController {

    private final OpenCompilationService openCompilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return openCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    public CompilationDto getCompilation(@PathVariable Integer compId) {
        return openCompilationService.getCompilation(compId);
    }

}
