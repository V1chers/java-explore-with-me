package ru.practicum.ewm.main.dto.event;

import lombok.Data;

@Data
public class GetEventDtoWithComment {

    private GetEventDto event;

    private AdminCommentDto comment;
}
