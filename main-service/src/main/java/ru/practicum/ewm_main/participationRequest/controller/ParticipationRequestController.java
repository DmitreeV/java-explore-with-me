package ru.practicum.ewm_main.participationRequest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.ewm_main.participationRequest.service.ParticipationRequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class ParticipationRequestController {

    private final ParticipationRequestService requestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto saveParticipationRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestService.saveParticipationRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable Long userId) {
        return requestService.getParticipationRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId,
                                                              @PathVariable Long requestId) {
        return requestService.cancelParticipationRequest(userId, requestId);
    }
}
