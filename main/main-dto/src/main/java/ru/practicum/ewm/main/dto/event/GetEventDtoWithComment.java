package ru.practicum.ewm.main.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEventDtoWithComment {

    private GetEventDto event;

    private AdminCommentDto comment;
}
