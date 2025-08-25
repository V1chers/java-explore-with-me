package ru.practicum.ewm.main.dto.event;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.ewm.exception.validator.NullableNotBlank;

@Data
public class AdminCommentDto {

    @NullableNotBlank
    @Size(max = 7000, message = "Допустимая длина текста - до 7000 символов")
    private String comment;
}
