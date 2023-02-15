package ru.practicum.ewm_main.compilation.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm_main.compilation.dto.CompilationDto;
import ru.practicum.ewm_main.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm_main.compilation.service.CompilationService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto saveCompilation(@Valid @RequestBody CompilationCreateDto compilationCreateDto) {
        return compilationService.saveCompilation(compilationCreateDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCategory(@PathVariable Long compId, @RequestBody CompilationUpdateDto compilationUpdateDto) {
        return compilationService.updateCompilation(compId, compilationUpdateDto);
    }
}
