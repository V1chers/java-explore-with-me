package ru.practicum.ewm.main.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryDto {

    @NotBlank(message = "переменная не может быть пустым или содержать пробелы")
    @Size(min = 1, max = 50, message = "Допустимая длина текста - от 1 до 50 символов")
    private String name;
}
