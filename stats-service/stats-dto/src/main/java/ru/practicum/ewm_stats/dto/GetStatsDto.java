package ru.practicum.ewm_stats.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStatsDto {

    @NotNull
    private String start;
    @NotNull
    private String end;
    private List<String> uris;
    private Boolean unique;
}
