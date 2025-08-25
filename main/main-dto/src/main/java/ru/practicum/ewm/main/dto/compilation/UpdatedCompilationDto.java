package ru.practicum.ewm.main.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.ewm.exception.validator.NullableNotBlank;

import java.util.List;

@Data
public class UpdatedCompilationDto {

    private Boolean pinned;

    @NullableNotBlank
    @Size(min = 1, max = 50, message = "Допустимая длина текста - от 1 до 50 символов")
    private String title;

    private List<Integer> events;

}
