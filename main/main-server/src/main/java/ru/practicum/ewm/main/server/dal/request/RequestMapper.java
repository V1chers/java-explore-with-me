package ru.practicum.ewm.main.server.dal.request;

import ru.practicum.ewm.main.dto.request.RequestDto;
import ru.practicum.ewm.main.dto.request.StatusUpdateResultDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    public static RequestDto toDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setRequester(request.getUserId());
        requestDto.setEvent(request.getEventId());
        requestDto.setStatus(request.getStatus().toString());
        requestDto.setCreated(toLocalDateTime(request.getCreated()));

        return requestDto;
    }

    public static List<RequestDto> toDto(List<Request> requestList) {
        return requestList.stream().map(RequestMapper::toDto).toList();
    }

    public static StatusUpdateResultDto toStatusUpdateResultDto(List<Request> requestList) {
        StatusUpdateResultDto statusUpdateResultDto = new StatusUpdateResultDto();
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();

        requestList.forEach(request -> {
            if (request.getStatus() == RequestStatus.CONFIRMED) {
                confirmedRequests.add(RequestMapper.toDto(request));
            } else if (request.getStatus() == RequestStatus.REJECTED) {
                rejectedRequests.add(RequestMapper.toDto(request));
            }
        });

        statusUpdateResultDto.setConfirmedRequests(confirmedRequests);
        statusUpdateResultDto.setRejectedRequests(rejectedRequests);

        return statusUpdateResultDto;
    }

    private static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }

        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
