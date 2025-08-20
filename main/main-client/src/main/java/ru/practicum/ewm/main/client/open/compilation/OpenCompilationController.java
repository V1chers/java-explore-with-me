package ru.practicum.ewm.main.client.open.compilation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@SuppressWarnings({"unused", "rawtypes"})
public class OpenCompilationController {

    private final OpenCompilationClient openCompilationClient;

    @GetMapping
    public ResponseEntity<List> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return openCompilationClient.getCompilations(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    public ResponseEntity<Object> getCompilation(@PathVariable Integer compId) {
        return openCompilationClient.getCompilation(compId);
    }

}
