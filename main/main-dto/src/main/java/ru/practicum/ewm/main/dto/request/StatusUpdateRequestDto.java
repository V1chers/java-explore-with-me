package ru.practicum.ewm.main.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class StatusUpdateRequestDto {

    List<Integer> requestIds;

    String status;
}
