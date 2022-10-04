package ru.bykov.explore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.model.dto.event.EventFullDto;

import java.sql.Timestamp;
import java.util.Arrays;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select e from Event e" +
            "")
    Page<Event> findByParam(String text, String[] categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, Pageable pageable);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

//    @Query(" select b from Booking b " +
//            "JOIN Item i on b.item.id = i.id " +
//            "where b.booker.id = ?1 " +
//            "and b.start < ?2 " +
//            "and b.end > ?2 ")
//    Page<Booking> findByBookerIdByUserId(Long bookerId, Timestamp timestamp, Pageable pageable);

//    @Query("select u from User u " +
//            "where u.id IN (:ids)")
}
