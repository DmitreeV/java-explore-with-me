package ru.practicum.ewm_main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.event.dto.*;
import ru.practicum.ewm_main.event.service.EventService;
import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.ewm_main.participationRequest.service.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventUserController {

    private final EventService eventService;
    private final ParticipationRequestService requestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.saveEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Positive @RequestParam(defaultValue = "10") int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByUserId(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventService.getEventByUserId(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUserId(@PathVariable Long eventId, @PathVariable Long userId,
                                            @RequestBody PrivateUpdateEventDto eventDto) {
        return eventService.updateEventByUserId(eventId, userId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable Long userId,
                                                                  @PathVariable Long eventId) {
        return requestService.getParticipationRequests(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResponse updateParticipationRequest(@PathVariable Long userId,
                                                                  @PathVariable Long eventId,
                                                                  @RequestBody EventReqStatusUpdateReqDto updateReqDto) {
        return requestService.updateParticipationRequest(userId, eventId, updateReqDto);
    }
}
