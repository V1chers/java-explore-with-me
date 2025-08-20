package ru.practicum.ewm.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.main.dto.category.CategoryDto;
import ru.practicum.ewm.main.dto.location.LocationDto;
import ru.practicum.ewm.main.dto.user.UserDto;

import java.time.LocalDateTime;

@Data
public class GetEventDto {

    private Integer id;

    private UserDto initiator;

    private CategoryDto category;

    private LocationDto location;

    private String state;

    private String annotation;

    private String description;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Integer views;

    private Integer confirmedRequests;
}
