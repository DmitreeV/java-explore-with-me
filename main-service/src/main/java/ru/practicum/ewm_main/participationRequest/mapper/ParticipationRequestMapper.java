package ru.practicum.ewm_main.participationRequest.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.ewm_main.participationRequest.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm_main.Constant.DATE_TIME_FORMATTER;

@UtilityClass
public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated().format(DATE_TIME_FORMATTER))
                .build();
    }

    public static List<ParticipationRequestDto> toParticipationRequestsDto(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
