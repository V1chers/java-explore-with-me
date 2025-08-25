package ru.practicum.ewm.main.server.logic.admin.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.category.CategoryDto;
import ru.practicum.ewm.main.dto.category.CreateCategoryDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@SuppressWarnings("unused")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CreateCategoryDto categoryDto) {
        return adminCategoryService.createCategory(categoryDto);
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        adminCategoryService.deleteCategory(catId);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto changeCategory(@PathVariable Integer catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        return adminCategoryService.changeCategory(catId, categoryDto);
    }
}
