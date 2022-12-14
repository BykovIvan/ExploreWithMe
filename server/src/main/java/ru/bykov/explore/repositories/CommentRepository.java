package ru.bykov.explore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Comment;
import ru.bykov.explore.utils.CommentState;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndOwnerIdAndEventId(Long comId, Long ownerId, Long eventId);

    void deleteByIdAndOwnerIdAndEventId(Long comId, Long ownerId, Long eventId);

    @Query("select c from Comment c " +
            "JOIN User u on c.event.id = u.id " +
            "JOIN Event e on c.event.id = e.id " +
            "WHERE (UPPER(c.text) like UPPER(concat('%', :text, '%'))) " +
            "AND c.event.id = (:eventId)")
    Page<Comment> findByParamFromUser(@Param("eventId") Long eventId,
                                      @Param("text") String text,
                                      Pageable pageable);

    @Query("select c from Comment c " +
            "JOIN Event e on c.event.id = e.id " +
            "WHERE c.status IN (:status) " +
            "AND c.event.id = (:eventId)")
    List<Comment> findByEventIdAndStatus(@Param("eventId") Long eventId,
                                         @Param("status") CommentState status,
                                         Pageable pageable);
}
