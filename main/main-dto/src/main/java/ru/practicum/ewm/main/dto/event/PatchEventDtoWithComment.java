package ru.practicum.ewm.main.dto.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatchEventDtoWithComment {

    @NotNull
    @Valid
    PatchEventDto patchEventDto;

    @Valid
    AdminCommentDto adminCommentDto;
}
