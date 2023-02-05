package ru.practicum.ewm_main.participationRequest.service;

import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
