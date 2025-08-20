package ru.practicum.ewm.main.dto.compilation;

import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.ewm.main.dto.event.GetShortEventDto;

import java.util.List;

@Data
public class CompilationDto {

    @Null
    private int id;

    private boolean pinned;

    private String title;

    private List<GetShortEventDto> events;
}
