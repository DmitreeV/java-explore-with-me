package ru.practicum.ewm_main.compilation.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.ewm_main.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm_main.compilation.dto.CompilationDto;
import ru.practicum.ewm_main.compilation.model.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents())
                .build();
    }

    public static Compilation toCompilation(CompilationCreateDto compilationCreateDto) {
        return Compilation.builder()
                .title(compilationCreateDto.getTitle())
                .pinned(compilationCreateDto.getPinned())
                .build();
    }

    public static List<CompilationDto> toCompilationsDto(Page<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
