package ru.practicum.ewm_main.event.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.client.EventClient;
import ru.practicum.ewm_main.event.dto.EventFullDto;
import ru.practicum.ewm_main.event.dto.EventShortDto;
import ru.practicum.ewm_main.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;
    private final EventClient client;

    public PublicEventController(EventService eventService, EventClient client) {
        this.eventService = eventService;
        this.client = client;
    }


    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categoryIds,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest httpServletRequest) {
        client.createHit(httpServletRequest);
        return eventService.getEvents(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        client.createHit(httpServletRequest);
        return eventService.getEventById(id);
    }
}
