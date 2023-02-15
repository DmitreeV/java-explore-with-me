package ru.practicum.ewm_main.event.dto;

import lombok.*;
import ru.practicum.ewm_main.participationRequest.model.StatusRequest;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventReqStatusUpdateReqDto {

    private List<Long> requestIds;

    private StatusRequest status;
}
