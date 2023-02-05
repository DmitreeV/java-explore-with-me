package ru.practicum.ewm_main.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    @NotNull
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
