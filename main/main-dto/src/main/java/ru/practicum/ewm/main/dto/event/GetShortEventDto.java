package ru.practicum.ewm.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.main.dto.category.CategoryDto;
import ru.practicum.ewm.main.dto.user.UserDto;

import java.time.LocalDateTime;

@Data
public class GetShortEventDto {

    private Integer id;

    private UserDto initiator;

    private CategoryDto category;

    private String annotation;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;

    private Integer views;

    private Integer confirmedRequests;
}
