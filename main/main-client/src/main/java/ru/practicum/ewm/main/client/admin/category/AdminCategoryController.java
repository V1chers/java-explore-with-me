package ru.practicum.ewm.main.client.admin.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.category.CategoryDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@SuppressWarnings("unused")
public class AdminCategoryController {

    private final AdminCategoryClient adminCategoryClient;

    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return adminCategoryClient.createCategory(categoryDto);
    }

    @DeleteMapping(path = "/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer catId) {
        return adminCategoryClient.deleteCategory(catId);
    }

    @PatchMapping(path = "/{catId}")
    public ResponseEntity<Object> changeCategory(@PathVariable Integer catId,
                                                 @Valid @RequestBody CategoryDto categoryDto) {
        return adminCategoryClient.changeCategory(catId, categoryDto);
    }
}
