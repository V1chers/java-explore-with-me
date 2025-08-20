package ru.practicum.ewm.main.server.dal.location;

import ru.practicum.ewm.main.dto.location.LocationDto;

public class LocationMapper {

    public static LocationDto toDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setLat(location.getLatitude());
        locationDto.setLon(location.getLongitude());

        return locationDto;
    }

    public static Location fromDto(LocationDto locationDto) {
        Location location = new Location();
        location.setLatitude(locationDto.getLat());
        location.setLongitude(locationDto.getLon());

        return location;
    }
}
