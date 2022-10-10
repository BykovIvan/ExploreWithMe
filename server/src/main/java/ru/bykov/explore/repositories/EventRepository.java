package ru.bykov.explore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Event;
import ru.bykov.explore.utils.StateOfEventAndReq;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    //TODO
    @Query("select e from Event e" +
            "")
    Page<Event> findByParamFromUser(@Param("text") String text,
                                    @Param("categories") String[] categories,
                                    @Param("paid") Boolean paid,
                                    @Param("rangeStart") String rangeStart,
                                    @Param("rangeEnd") String rangeEnd,
                                    @Param("onlyAvailable") Boolean onlyAvailable,
                                    Pageable pageable);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    //TODO

    @Query("select e from Event e " +
            "JOIN User u on e.initiator.id = u.id " +
            "JOIN Category c on e.category.id = c.id " +
            "WHERE (e.initiator.id is null or e.initiator.id IN (:users)) " +
            "AND (e.state is null or e.state IN (:states)) " +
            "AND (e.category.id  is null or e.category.id IN (:categories)) " +
            "AND (e.eventDate is null or e.eventDate > (:rangeStart)) " +
            "AND (e.eventDate is null or e.eventDate < (:rangeEnd))")
    Page<Event> findByParamFromAdmin(@Param("users") Long[] users,
                                     @Param("states") StateOfEventAndReq[] states,
                                     @Param("categories") Long[] categories,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     Pageable pageable);


//@Query("select u from User u " +
//            "where u.id IN (:ids)")
//    Page<User> findByIdIn(@Param("ids")Long[] ids, Pageable pageable);

//    @Query(" select b from Booking b " +
//            "JOIN Item i on b.item.id = i.id " +
//            "where b.booker.id = ?1 " +
//            "and b.start < ?2 " +
//            "and b.end > ?2 ")
//    Page<Booking> findByBookerIdByUserId(Long bookerId, Timestamp timestamp, Pageable pageable);

//    @Query("select u from User u " +
//            "where u.id IN (:ids)")


    //@Query("select e from Event e " +
    //            "JOIN User u on e.initiator.id = u.id " +
    //            "JOIN Category c on e.category.id = c.id " +
    //            "WHERE (:users is null or e.initiator.id IN (:users)) AND " +
    //            "(:states is null or e.state IN (:states)) AND " +
    //            "(:categories is null or e.category.id IN (:categories)) AND " +
    //            "(:rangeStart is null or e.eventDate > (:rangeStart)) AND " +
    //            "(:rangeEnd is null or e.eventDate < (:rangeEnd))")
    //    Page<Event> findByParamFromAdmin(@Param("users")Long[] users,
    //                                     @Param("states")String[] states,
    //                                     @Param("categories")Long[] categories,
    //                                     @Param("rangeStart")String rangeStart,
    //                                     @Param("rangeEnd")String rangeEnd,
    //                                     Pageable pageable);
}
