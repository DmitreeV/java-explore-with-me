package ru.practicum.ewm_stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_stats.dto.ViewStatsDto;
import ru.practicum.ewm_stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.ewm_stats.dto.ViewStatsDto(e.app, e.uri, count(e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "and e.uri in ?3 " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewStatsDto> getStatsByTimeAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm_stats.dto.ViewStatsDto(e.app, e.uri, count(e.ip))" +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewStatsDto> getAllStatsByTime(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm_stats.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "and e.uri in ?3 " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewStatsDto> getStatsUniqueByTimeAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm_stats.dto.ViewStatsDto(e.app, e.uri, count(distinct e.ip)) " +
            "from EndpointHit as e " +
            "where e.timestamp between ?1 and ?2 " +
            "group by e.uri, e.app " +
            "order by count(e.ip) desc ")
    List<ViewStatsDto> getStatsUniqueByTime(LocalDateTime start, LocalDateTime end);
}
