package ru.bykov.explore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
