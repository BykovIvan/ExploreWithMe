package ru.bykov.explore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.utils.EventState;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "WHERE (UPPER(e.annotation) like UPPER(concat('%', :text, '%')) " +
            "or UPPER(e.description) like UPPER(concat('%', :text, '%'))) " +
            "AND (e.category.id is null or e.category.id IN (:categories)) " +
            "AND (e.paid is null or e.paid = (:paid)) " +
            "AND (e.state is null or e.state IN (:state)) " +
            "AND (e.eventDate is null or e.eventDate > (:rangeStart)) " +
            "AND (e.eventDate is null or e.eventDate < (:rangeEnd))")
    Page<Event> findByParamFromUser(@Param("text") String text,
                                    @Param("categories") Long[] categories,
                                    @Param("paid") Boolean paid,
                                    @Param("state") EventState state,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    Pageable pageable);


    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("select e from Event e " +
            "JOIN User u on e.initiator.id = u.id " +
            "JOIN Category c on e.category.id = c.id " +
            "WHERE (e.initiator.id is null or e.initiator.id IN (:users)) " +
            "AND (e.state is null or e.state IN (:states)) " +
            "AND (e.category.id is null or e.category.id IN (:categories)) " +
            "AND (e.eventDate is null or e.eventDate > (:rangeStart)) " +
            "AND (e.eventDate is null or e.eventDate < (:rangeEnd))")
    Page<Event> findByParamFromAdmin(@Param("users") Long[] users,
                                     @Param("states") EventState[] states,
                                     @Param("categories") Long[] categories,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     Pageable pageable);

    @Modifying
    @Query("update Event e set e.confirmedRequests = e.confirmedRequests + 1 where e.id = :id ")
    void setNewConfirmedRequestsPlusOne(@Param("id") Long eventId);

    @Modifying
    @Query("update Event e set e.confirmedRequests = e.confirmedRequests - 1 where e.id = :id ")
    void setNewConfirmedRequestsMinusOne(@Param("id") Long eventId);
}
