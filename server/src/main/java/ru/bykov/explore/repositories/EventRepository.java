package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
