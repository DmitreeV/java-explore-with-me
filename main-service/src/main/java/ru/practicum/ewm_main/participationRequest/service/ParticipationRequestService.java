package ru.practicum.ewm_main.participationRequest.service;

import ru.practicum.ewm_main.event.dto.EventReqStatusUpdateReqDto;
import ru.practicum.ewm_main.event.dto.RequestStatusUpdateResponse;
import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequests(Long userId);

    List<ParticipationRequestDto> getParticipationRequests(Long eventId, Long userId);

    RequestStatusUpdateResponse updateParticipationRequest(Long eventId, Long userId, EventReqStatusUpdateReqDto updateReqDto);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);
}
