package ru.practicum.ewm_main.compilation.dto;

import lombok.*;
import ru.practicum.ewm_main.event.model.Event;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;

    private String title;

    private Boolean pinned;

    private List<Event> events;
}
