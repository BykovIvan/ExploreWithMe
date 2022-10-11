package ru.bykov.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bykov.explore.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("SELECT s from Statistic s " +
            "WHERE s.timestamp > :start " +
            "AND s.timestamp < :end " +
            "AND (s.uri is null or s.uri IN (:uris))")
    List<Statistic> getByParam(@Param("start") LocalDateTime timeOfStart,
                               @Param("end") LocalDateTime timeOfEnd,
                               @Param("uris") String[] uris);

    @Query("SELECT s FROM Statistic s " +
            "WHERE s.timestamp > :start " +
            "AND s.timestamp < :end " +
            "AND (s.uri is null or s.uri IN (:uris)) " +
            "AND s.ip in (SELECT s2.ip from Statistic s2 " +
            "WHERE s.timestamp > :start " +
            "AND s.timestamp < :end " +
            "AND (s.uri is null or s.uri IN (:uris)) " +
            "GROUP BY s2.ip " +
            "HAVING count(s2.ip) > 1)")
    List<Statistic> getByParamUniqIp(@Param("start") LocalDateTime timeOfStart,
                                     @Param("end") LocalDateTime timeOfEnd,
                                     @Param("uris") String[] uris);

    @Query("SELECT COUNT(s.uri) from Statistic s " +
            "where s.app = :app " +
            "AND s.uri = :uri")
    Long countByAppAndUri(@Param("app") String app,
                          @Param("uri") String uri);

}
