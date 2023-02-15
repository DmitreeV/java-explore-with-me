package ru.practicum.ewm_main.compilation.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationUpdateDto {

    private String title;

    private Boolean pinned;

    private List<Long> events;
}
