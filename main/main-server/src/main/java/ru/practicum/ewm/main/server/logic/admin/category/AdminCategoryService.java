package ru.practicum.ewm.main.server.logic.admin.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.category.CategoryDto;
import ru.practicum.ewm.main.dto.category.CreateCategoryDto;
import ru.practicum.ewm.main.server.dal.category.Category;
import ru.practicum.ewm.main.server.dal.category.CategoryMapper;
import ru.practicum.ewm.main.server.dal.category.CategoryRepository;
import ru.practicum.ewm.main.server.logic.utils.CategoryServiceUtils;
import ru.practicum.ewm.main.server.logic.utils.ServiceUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryServiceUtils categoryServiceUtils;

    @Transactional
    public CategoryDto createCategory(CreateCategoryDto categoryDto) {
        Category category = CategoryMapper.fromDto(categoryDto);
        categoryServiceUtils.isNameExists(categoryDto.getName());

        category = categoryRepository.save(category);

        return CategoryMapper.toDto(category);
    }

    @Transactional
    public void deleteCategory(Integer catId) {
        ServiceUtils.isExist(categoryRepository, catId, "Категория с таким id не найдена");
        categoryServiceUtils.isCategoryHaveUses(catId);

        categoryRepository.deleteById(catId);
    }

    @Transactional
    public CategoryDto changeCategory(Integer catId, CategoryDto categoryDto) {
        Category oldCategory = ServiceUtils.getIfExist(categoryRepository, catId, "Категория с таким id не найдена");
        if (categoryServiceUtils.isNameNotChanged(oldCategory, categoryDto)) {
            return CategoryMapper.toDto(oldCategory);
        }
        categoryServiceUtils.isNameExists(categoryDto.getName());

        Category category = CategoryMapper.fromDto(categoryDto);
        category.setId(catId);

        category = categoryRepository.save(category);

        return CategoryMapper.toDto(category);
    }
}
