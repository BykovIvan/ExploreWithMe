package ru.bykov.explore.utils.mapperForDto;

import ru.bykov.explore.model.Location;
import ru.bykov.explore.model.dto.LocationDto;

public class LocationMapper {

    private LocationMapper(){
    }

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
