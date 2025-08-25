package ru.practicum.ewm.main.dto.location;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class LocationDto {

    @Null
    private Integer id;

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;
}
