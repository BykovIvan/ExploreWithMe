package ru.bykov.explore.services;

import ru.bykov.explore.model.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> findByUserId(Long userId);
}
