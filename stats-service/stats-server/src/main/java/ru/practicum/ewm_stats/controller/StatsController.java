package ru.practicum.ewm_stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_stats.dto.EndpointHitDto;
import ru.practicum.ewm_stats.dto.GetStatsDto;
import ru.practicum.ewm_stats.dto.ViewStatsDto;
import ru.practicum.ewm_stats.service.EndpointHitService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EndpointHitDto saveHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        return endpointHitService.saveHit(endpointHitDto);
    }

    @GetMapping("stats")
    public List<ViewStatsDto> getViewStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return endpointHitService.getViewStats(new GetStatsDto(start, end, uris, unique));
    }
}
