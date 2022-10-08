package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
