package ru.practicum.ewm.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.statistics.model.GetStats;
import ru.practicum.ewm.statistics.model.Stats;

import java.time.Instant;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Stats, Integer> {

    @Query("select s.app as app, s.uri as uri, COUNT(s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by totalViews desc")
    List<GetStats> getStats(Instant start, Instant end, List<String> uris);

    @Query("select s.app as app, s.uri as uri, COUNT(s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "group by s.app, s.uri " +
            "order by totalViews desc")
    List<GetStats> getAllStats(Instant start, Instant end);

    @Query("select s.app as app, s.uri as uri, COUNT(distinct s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by totalViews desc")
    List<GetStats> getUniqueStats(Instant start, Instant end, List<String> uris);

    @Query("select s.app as app, s.uri as uri, COUNT(distinct s.ip) as totalViews " +
            "from Stats as s " +
            "where s.created > ?1 " +
            "and s.created < ?2 " +
            "group by s.app, s.uri " +
            "order by totalViews desc")
    List<GetStats> getAllUniqueStats(Instant start, Instant end);
}
