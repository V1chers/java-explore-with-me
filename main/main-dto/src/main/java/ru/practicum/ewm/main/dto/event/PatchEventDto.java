package ru.practicum.ewm.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.ewm.exception.validator.NullableNotBlank;
import ru.practicum.ewm.main.dto.location.LocationDto;

import java.time.LocalDateTime;

@Data
public class PatchEventDto {

    @NullableNotBlank
    @Size(min = 20, max = 2000, message = "Допустимая длина текста - от 20 до 2000 символов")
    private String annotation;

    private Integer category;

    @NullableNotBlank
    @Size(min = 20, max = 7000, message = "Допустимая длина текста - от 20 до 7000 символов")
    private String description;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "число не может меньше нуля")
    private Integer participantLimit;

    private Boolean requestModeration;

    @NullableNotBlank
    private String stateAction;

    @NullableNotBlank
    @Size(min = 3, max = 120, message = "Допустимая длина текста - от 3 до 120 символов")
    private String title;
}
