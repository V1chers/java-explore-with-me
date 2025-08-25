package ru.practicum.ewm.main.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {

    @NotBlank(message = "переменная не может быть пустым или содержать пробелы")
    @Size(min = 2, max = 250, message = "Допустимая длина текста - от 2 до 250 символов")
    private String name;

    @Email(message = "переменная должна быть имейлом")
    @NotBlank(message = "переменная не может быть пустым или содержать пробелы")
    @Size(min = 6, max = 254, message = "Допустимая длина текста - от 6 до 254 символов")
    private String email;
}
