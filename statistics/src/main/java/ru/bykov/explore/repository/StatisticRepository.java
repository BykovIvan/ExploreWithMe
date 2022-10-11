package ru.bykov.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Statistic;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("SELECT s from Statistic s " +
            "WHERE s.timestamp > :start " +
            "AND s.timestamp < :end " +
            "AND (s.uri is null or s.uri IN (:uris))")
    List<Statistic> getByParam(@Param("start") LocalDateTime timeOfStart,
                               @Param("end")LocalDateTime timeOfEnd,
                               @Param("uris") String[] uris);

    @Query("SELECT COUNT(s) from Statistic s " +
            "where s.app = :app " +
            "AND s.uri = :uri")
    Long countByAppAndUri(@Param("app") String app,
                          @Param("uri") String uri);

    @Query("SELECT s from Statistic s " +
            "WHERE s.timestamp > :start " +
            "AND s.timestamp < :end " +
            "AND (s.uri is null or s.uri IN (:uris)) " +
            "GROUP BY s.ip ")
    List<Statistic> getByParamUniqIp(@Param("start") LocalDateTime timeOfStart,
                                     @Param("end") LocalDateTime timeOfEnd,
                                     @Param("uris") String[] uris);


    //AND (e.category.id is null or e.category.id IN (:categories))
}
