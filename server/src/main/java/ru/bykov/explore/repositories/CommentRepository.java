package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
