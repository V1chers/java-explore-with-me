package ru.practicum.ewm.main.server.logic.open.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.ewm.main.dto.category.CategoryDto;
import ru.practicum.ewm.main.server.dal.category.Category;
import ru.practicum.ewm.main.server.dal.category.CategoryMapper;
import ru.practicum.ewm.main.server.dal.category.CategoryRepository;
import ru.practicum.ewm.main.server.logic.validation.ServiceUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenCategoryService {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<Category> categoryList = categoryRepository.findAll(PageRequest.of(from, size)).toList();

        return CategoryMapper.toDto(categoryList);
    }

    @GetMapping(path = "/{catId}")
    public CategoryDto getCategory(@PathVariable int catId) {
        Category category = ServiceUtils.getIfExist(categoryRepository, catId,
                "Категория с заданным id не найдена");

        return CategoryMapper.toDto(category);
    }
}
