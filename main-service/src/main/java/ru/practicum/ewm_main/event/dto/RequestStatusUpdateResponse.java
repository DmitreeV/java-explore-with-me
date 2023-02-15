package ru.practicum.ewm_main.event.dto;

import lombok.*;
import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusUpdateResponse {

    List<ParticipationRequestDto> confirmedRequests;

    List<ParticipationRequestDto> rejectedRequests;
}
