package ru.practicum.ewm_stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_stats.dto.EndpointHitDto;
import ru.practicum.ewm_stats.dto.GetStatsDto;
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
    public List<ViewStatsDto> getViewStats(GetStatsDto getStatsDto) {

        LocalDateTime startDate = LocalDateTime.parse(getStatsDto.getStart(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(getStatsDto.getEnd(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<ViewStatsDto> viewStats;
        List<String> uris = getStatsDto.getUris();

        if (getStatsDto.getUris().isEmpty()) {
            viewStats = (getStatsDto.getUnique() ? endpointHitRepository.getStatsUniqueByTime(startDate, endDate)
                    : endpointHitRepository.getAllStatsByTime(startDate, endDate));
        } else {
            viewStats = (getStatsDto.getUnique() ? endpointHitRepository.getStatsUniqueByTimeAndUris(startDate, endDate, uris)
                    : endpointHitRepository.getStatsByTimeAndUris(startDate, endDate, uris));
        }
        return viewStats;
    }
}
