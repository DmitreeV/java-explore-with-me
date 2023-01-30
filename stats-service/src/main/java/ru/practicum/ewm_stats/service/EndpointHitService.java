package ru.practicum.ewm_stats.service;

import ru.practicum.ewm_stats.dto.EndpointHitDto;
import ru.practicum.ewm_stats.dto.ViewStatsDto;

import java.util.List;

public interface EndpointHitService {

    EndpointHitDto saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, Boolean unique);
}
