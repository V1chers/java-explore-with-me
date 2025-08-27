package ru.practicum.ewm.statistics.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.statistics.server.model.GetStats;
import ru.practicum.ewm.statistics.server.model.Stats;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public interface StatisticsRepository extends JpaRepository<Stats, Integer> {

    @Query("select s.app as app, s.uriPath as uriPath, s.uriId as uriId, COUNT(s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "group by s.app, s.uriPath, s.uriId " +
            "order by totalViews desc")
    List<GetStats> getAllStats(Instant start, Instant end);

    @Query("select s.app as app,s.uriPath as uriPath, s.uriId as uriId, COUNT(s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "group by s.app, s.uriPath, s.uriId " +
            "order by totalViews desc")
    List<GetStats> getAllUniqueStats(Instant start, Instant end);

    @Query("select s.app as app,s.uriPath as uriPath, s.uriId as uriId, COUNT(s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "and s.uriPath = ?3 " +
            "and (-1 in (?4) or s.uriId in (?4)) " +
            "group by s.app, s.uriPath, s.uriId " +
            "order by totalViews desc")
    List<GetStats> getStats(Instant start, Instant end, String uriPath, Set<Integer> uriIdList);

    @Query("select s.app as app,s.uriPath as uriPath, s.uriId as uriId, COUNT(distinct s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "and s.uriPath = ?3 " +
            "and (-1 in (?4) or s.uriId in (?4)) " +
            "group by s.app, s.uriPath, s.uriId " +
            "order by totalViews desc")
    List<GetStats> getUniqueStats(Instant start, Instant end, String uriPath, Set<Integer> uriIdList);
}
