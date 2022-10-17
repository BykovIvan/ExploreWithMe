package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Request;
import ru.bykov.explore.utils.RequestState;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long userId);

    Long countByEventId(Long eventId);

    @Modifying
    @Query("update Request r set r.status = :newStatus " +
            "where r.status IN (:oldStatus) " +
            "AND r.event IN (SELECT e FROM Event e " +
            "WHERE e.id  = :eventId)")
    void setStatusCanselWhereByStatusAndEventId(@Param("newStatus") RequestState newStatus,
                                                @Param("oldStatus") RequestState oldStatus,
                                                @Param("eventId") Long eventId);

    List<Request> findAllByEventId(Long eventId);

    Optional<Request> findByIdAndEventId(Long reqId, Long eventId);

    Request findByIdAndRequesterId(Long requestId, Long userId);
}
