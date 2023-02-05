package ru.practicum.ewm_main.event.dto;

import lombok.*;
import ru.practicum.ewm_main.event.model.State;
import ru.practicum.ewm_main.location.dto.LocationDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateEventDto {

    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private State state;

    private String title;
}
