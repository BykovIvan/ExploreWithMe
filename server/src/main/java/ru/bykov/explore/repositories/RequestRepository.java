package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bykov.explore.model.Request;
import ru.bykov.explore.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(Long userId);

    List<Request> findByEventAndRequester(Long eventId, Long userId);
}
