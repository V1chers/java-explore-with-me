package ru.practicum.ewm.main.client.open.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@SuppressWarnings({"unused", "rawtypes"})
public class OpenCategoryController {

    private final OpenCategoryClient openCategoryClient;

    @GetMapping
    public ResponseEntity<List> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return openCategoryClient.getCategories(from, size);
    }

    @GetMapping(path = "/{catId}")
    public ResponseEntity<Object> getCategory(@PathVariable int catId) {
        return openCategoryClient.getCategory(catId);
    }
}
