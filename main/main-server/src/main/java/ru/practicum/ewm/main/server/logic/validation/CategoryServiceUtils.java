package ru.practicum.ewm.main.server.logic.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.main.dto.category.CategoryDto;
import ru.practicum.ewm.main.server.dal.category.Category;
import ru.practicum.ewm.main.server.dal.category.CategoryRepository;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceUtils {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    public void isNameExists(String name) {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            throw new ConflictException("Категория с данным именем уже существует");
        }
    }

    public void isCategoryHaveUses(int catId) {
        Category category = new Category();
        category.setId(catId);

        List<Event> eventList = eventRepository.findByCategory(category, PageRequest.of(0, 1)).toList();

        if (!eventList.isEmpty()) {
            throw new ConflictException("Категория уже используется");
        }
    }

    public boolean isNameNotChanged(Category category, CategoryDto categoryDto) {
        return category.getName().equals(categoryDto.getName());
    }
}
