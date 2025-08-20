package ru.practicum.ewm.main.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateCompilationDto {

    private boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50, message = "Допустимая длина текста - от 1 до 50 символов")
    private String title;

    private List<Integer> events;
}
