package ru.bykov.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Statistic;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
