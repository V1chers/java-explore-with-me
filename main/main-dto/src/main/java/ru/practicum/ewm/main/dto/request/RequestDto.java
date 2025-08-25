package ru.practicum.ewm.main.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {

    private int id;

    private int requester;

    private int event;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSS")
    private LocalDateTime created;
}
