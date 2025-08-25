package ru.practicum.ewm.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HitStatsDto {

    @NotBlank(message = "поле app не может быть пустым или только содержать пробелы")
    private String app;

    @NotBlank(message = "поле uri не может быть пустым или только содержать пробелы")
    private String uri;

    @NotBlank(message = "поле ip не может быть пустым или только содержать пробелы")
    private String ip;

    @NotNull(message = "поле timestamp не может быть пустым")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
