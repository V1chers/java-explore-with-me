package ru.practicum.ewm.main.server.logic.admin.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.compilation.CompilationDto;
import ru.practicum.ewm.main.dto.compilation.CreateCompilationDto;
import ru.practicum.ewm.main.dto.compilation.UpdatedCompilationDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@SuppressWarnings("unused")
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody CreateCompilationDto createCompilationDto) {
        return adminCompilationService.createCompilation(createCompilationDto);
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compId) {
        adminCompilationService.deleteCompilation(compId);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationDto updateCompilation(@PathVariable int compId,
                                            @Valid @RequestBody UpdatedCompilationDto compilationDto) {
        return adminCompilationService.updateCompilation(compId, compilationDto);
    }
}
