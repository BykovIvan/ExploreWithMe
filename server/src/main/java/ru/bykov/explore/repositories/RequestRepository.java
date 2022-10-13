package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Request;
import ru.bykov.explore.utils.StateOfEventAndReq;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long userId);

    List<Request> findByEventIdAndRequesterId(Long eventId, Long userId);

    Long countByEventId(Long eventId);

    @Modifying
    @Query("update Request r set r.status = ?1 where r.event = ?2 and r.status = ?3")
    void setStatusCanselWhereByStatusAndEventId(StateOfEventAndReq newStatus, StateOfEventAndReq oldStatus, Long eventId);
}
