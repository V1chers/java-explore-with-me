package ru.practicum.ewm.main.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class StatusUpdateResultDto {

    List<RequestDto> confirmedRequests;

    List<RequestDto> rejectedRequests;
}
