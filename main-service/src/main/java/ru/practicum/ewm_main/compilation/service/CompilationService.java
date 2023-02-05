package ru.practicum.ewm_main.compilation.service;

import ru.practicum.ewm_main.compilation.dto.CompilationDto;
import ru.practicum.ewm_main.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(CompilationDto compilationDto, Long compId);

    List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compId);

    void deleteCompilation(Long compId);
}
