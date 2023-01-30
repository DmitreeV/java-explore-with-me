package ru.practicum.ewm_stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_stats.dto.EndpointHitDto;
import ru.practicum.ewm_stats.dto.ViewStatsDto;
import ru.practicum.ewm_stats.model.EndpointHit;
import ru.practicum.ewm_stats.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewm_stats.EndpointHitMapper.toEndpointHit;
import static ru.practicum.ewm_stats.EndpointHitMapper.toEndpointHitDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    @Override
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = toEndpointHit(endpointHitDto);
        log.info("Save endpoint hit.");
        return toEndpointHitDto(endpointHitRepository.save(endpointHit));
    }

    @Override
    public List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, Boolean unique) {

        LocalDateTime startDate = convertStart(start);
        LocalDateTime endDate = convertEnd(end);
        List<ViewStatsDto> viewStats;
        if (uris.isEmpty()) {
            viewStats = (unique ? endpointHitRepository.getStatsUniqueByTime(startDate, endDate)
                    : endpointHitRepository.getAllStatsByTime(startDate, endDate));
        } else {
            viewStats = (unique ? endpointHitRepository.getStatsUniqueByTimeAndUris(startDate, endDate, uris)
                    : endpointHitRepository.getStatsByTimeAndUris(startDate, endDate, uris));
        }
        return viewStats;
    }

    private LocalDateTime convertStart(String start) {
        if (start == null) {
            return LocalDateTime.now().minusYears(10);
        } else {
            return LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    private LocalDateTime convertEnd(String end) {
        if (end == null) {
            return LocalDateTime.now();
        } else {
            return LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
