package ru.practicum.ewm.main.server.logic.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.models.ConflictException;
import ru.practicum.ewm.exception.models.NotFoundException;
import ru.practicum.ewm.main.dto.event.PatchEventDto;
import ru.practicum.ewm.main.dto.location.LocationDto;
import ru.practicum.ewm.main.server.dal.category.Category;
import ru.practicum.ewm.main.server.dal.category.CategoryRepository;
import ru.practicum.ewm.main.server.dal.event.Event;
import ru.practicum.ewm.main.server.dal.event.StateAction;
import ru.practicum.ewm.main.server.dal.location.Location;
import ru.practicum.ewm.main.server.dal.location.LocationMapper;
import ru.practicum.ewm.main.server.dal.location.LocationRepository;
import ru.practicum.ewm.main.server.dal.request.Request;
import ru.practicum.ewm.main.server.dal.request.RequestStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceUtils {

    private final LocationRepository locationRepository;

    private final CategoryRepository categoryRepository;

    public void isWaitingOrCancel(StateAction stateAction) {
        if (stateAction != StateAction.PENDING && stateAction != StateAction.CANCELED) {
            throw new ConflictException("Событие должно ждать проверки или быть отменено");
        }
    }

    public void isEventDateTooEarly(Instant eventDate) {
        if (Instant.now().plusSeconds(7200).isAfter(eventDate)) {
            throw new ConflictException("Событие должно начаться не раньше чем через 2 часа");
        }
    }

    public void isAdminEventDateTooEarly(Instant eventDate) {
        if (Instant.now().plusSeconds(3600).isAfter(eventDate)) {
            throw new ConflictException("Событие должно начаться не раньше чем через 2 часа");
        }
    }

    public Location getLocation(LocationDto locationDto) {
        if (locationDto == null) {
            return null;
        }

        Optional<Location> location = locationRepository.findByLatitudeAndLongitude(locationDto.getLat(),
                locationDto.getLon());

        if (location.isPresent()) {
            return location.get();
        } else {
            Location newLocation = LocationMapper.fromDto(locationDto);
            newLocation = locationRepository.save(newLocation);
            return newLocation;
        }
    }

    public void isOrganizer(int userId, Event event) {
        if (event.getUser().getId() != userId) {
            throw new ConflictException("пользователь не является организатором");
        }
    }

    public void isRequestsPending(List<Request> requestList) {
        requestList.forEach(request -> {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Заявки с id = " + request.getId() + " не в статусе ожидания");
            }
        });
    }

    public void isModerationRequired(Event event) {
        if (!event.getRequestModeration()) {
            throw new ConflictException("Модерация заявок у события отключена");
        }
    }

    public void isEventPublished(Event event) {
        if (event.getStateAction() != StateAction.PUBLISHED) {
            throw new NotFoundException("Событие не найдено");
        }
    }

    public Instant stringToInstant(String dateTime) {
        if (dateTime == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public void setUpdatingEventFields(PatchEventDto patchEventDto, Event newEvent, Event event) {
        if (patchEventDto.getCategory() != null) {
            Category category = ServiceUtils.getIfExist(categoryRepository, patchEventDto.getCategory(),
                    "Категория с данным id не найдено");
            newEvent.setCategory(category);
        } else {
            newEvent.setCategory(event.getCategory());
        }
        if (patchEventDto.getLocation() != null) {
            newEvent.setLocation(getLocation(patchEventDto.getLocation()));
        } else {
            newEvent.setLocation(event.getLocation());
        }
    }
}
