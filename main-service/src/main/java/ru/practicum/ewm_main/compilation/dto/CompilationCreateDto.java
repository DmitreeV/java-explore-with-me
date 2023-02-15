package ru.practicum.ewm_main.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreateDto {

    @NotBlank
    private String title;

    @NotNull
    private Boolean pinned;

    @NotNull
    private List<Long> events;
}
