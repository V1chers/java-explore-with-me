package ru.practicum.ewm.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.practicum.ewm.main.dto.location.LocationDto;

import java.time.LocalDateTime;

@Data
public class CreateEventDto {

    @Positive(message = "не может быть нулем или меньше нуля")
    @NotNull
    private Integer category;

    @NotNull
    private LocationDto location;

    @NotBlank
    @Size(min = 20, max = 2000, message = "Допустимая длина текста - от 20 до 2000 символов")
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Допустимая длина текста - от 20 до 7000 символов")
    private String description;

    @NotBlank
    @Size(min = 3, max = 120, message = "Допустимая длина текста - от 3 до 120 символов")
    private String title;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;

    private Boolean paid;

    @PositiveOrZero(message = "число не может меньше нуля")
    private Integer participantLimit;

    private Boolean requestModeration;

}
