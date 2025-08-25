package ru.practicum.ewm.main.server.dal.category;

import ru.practicum.ewm.main.dto.category.CategoryDto;
import ru.practicum.ewm.main.dto.category.CreateCategoryDto;

import java.util.List;

public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        return categoryDto;
    }

    public static List<CategoryDto> toDto(List<Category> categoryList) {
        return categoryList.stream().map(CategoryMapper::toDto).toList();
    }

    public static Category fromDto(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());

        return category;
    }

    public static Category fromDto(CreateCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());

        return category;
    }
}
