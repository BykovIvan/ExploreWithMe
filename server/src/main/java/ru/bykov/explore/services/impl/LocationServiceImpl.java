package ru.bykov.explore.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bykov.explore.model.Location;
import ru.bykov.explore.model.dto.LocationDto;
import ru.bykov.explore.repositories.LocationRepository;
import ru.bykov.explore.services.LocationService;
import ru.bykov.explore.utils.mapperForDto.LocationMapper;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location createLocation(LocationDto locationDto) {
        Location location = LocationMapper.toLocation(locationDto);
        return locationRepository.save(location);
    }
}
