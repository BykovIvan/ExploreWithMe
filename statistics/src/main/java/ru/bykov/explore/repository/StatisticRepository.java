package ru.bykov.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Statistic;
import ru.bykov.explore.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @Query("SELECT new ru.bykov.explore.model.dto.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) FROM Statistic s " +
            "WHERE s.timestamp > :start " +
            "AND s.timestamp < :end " +
            "AND (s.uri is null or s.uri IN (:uris)) " +
            "GROUP BY s.app, s.uri")
    List<ViewStats> findByParamByUniqueIp(@Param("start") LocalDateTime timeOfStart,
                                          @Param("end") LocalDateTime timeOfEnd,
                                          @Param("uris") String[] uris);

    @Query("SELECT new ru.bykov.explore.model.dto.ViewStats(s.app, s.uri, COUNT(s.ip)) FROM Statistic s " +
            "WHERE s.timestamp > :start " +
            "AND s.timestamp < :end " +
            "AND (s.uri is null or s.uri IN (:uris)) " +
            "GROUP BY s.app, s.uri")
    List<ViewStats> findByParam(@Param("start") LocalDateTime timeOfStart,
                                @Param("end") LocalDateTime timeOfEnd,
                                @Param("uris") String[] uris);

}
