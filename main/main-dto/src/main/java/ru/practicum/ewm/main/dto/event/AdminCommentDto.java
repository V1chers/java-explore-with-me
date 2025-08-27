package ru.practicum.ewm.main.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCommentDto {

    @NotBlank
    @Size(min = 5, max = 7000, message = "Допустимая длина текста - от 5 до 7000 символов")
    private String comment;
}
