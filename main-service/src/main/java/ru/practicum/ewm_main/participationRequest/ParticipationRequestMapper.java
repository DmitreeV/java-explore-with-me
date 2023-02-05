package ru.practicum.ewm_main.participationRequest;

import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.ewm_main.participationRequest.model.ParticipationRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParticipationRequestMapper {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(DATE_TIME_FORMATTER))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static ParticipationRequest toParticipationRequest(ParticipationRequestDto requestDto) {
        return ParticipationRequest.builder()
                .id(requestDto.getId())
                .created(LocalDateTime.parse(requestDto.getCreated(), DATE_TIME_FORMATTER))
                .status(requestDto.getStatus())
                .build();
    }
}
