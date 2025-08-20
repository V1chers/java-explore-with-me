package ru.practicum.ewm.main.client.admin.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.compilation.CreateCompilationDto;
import ru.practicum.ewm.main.dto.compilation.UpdatedCompilationDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@SuppressWarnings("unused")
public class AdminCompilationController {

    private final AdminCompilationClient adminCompilationClient;

    @PostMapping
    public ResponseEntity<Object> createCompilation(@Valid @RequestBody CreateCompilationDto createCompilationDto) {
        return adminCompilationClient.createCompilation(createCompilationDto);
    }

    @DeleteMapping(path = "/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable int compId) {
        return adminCompilationClient.deleteCompilation(compId);
    }

    @PatchMapping(path = "/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable int compId,
                                                    @Valid @RequestBody UpdatedCompilationDto compilationDto) {
        return adminCompilationClient.updateCompilation(compId, compilationDto);
    }
}
