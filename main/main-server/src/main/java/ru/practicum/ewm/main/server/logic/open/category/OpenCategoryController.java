package ru.practicum.ewm.main.server.logic.open.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.category.CategoryDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@SuppressWarnings("unused")
public class OpenCategoryController {

    private final OpenCategoryService openCategoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return openCategoryService.getCategories(from, size);
    }

    @GetMapping(path = "/{catId}")
    public CategoryDto getCategory(@PathVariable int catId) {
        return openCategoryService.getCategory(catId);
    }
}
