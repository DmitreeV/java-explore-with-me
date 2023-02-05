package ru.practicum.ewm_main.location.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    private float lat;

    private float lon;
}
