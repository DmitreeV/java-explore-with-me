package ru.practicum.ewm_main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.compilation.dto.CompilationDto;
import ru.practicum.ewm_main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm_main.compilation.model.Compilation;
import ru.practicum.ewm_main.compilation.repository.CompilationRepository;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.event.model.Event;
import ru.practicum.ewm_main.event.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm_main.compilation.CompilationMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = toCompilation(newCompilationDto);
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        compilation.setEvents(events);
        log.info("Подборка сохранена.");
        return toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(CompilationDto compilationDto, Long compId) {
        Compilation compilationToUpdate = getCompilation(compId);
        Compilation compilation = toCompilation(compilationDto);
        if (compilation.getTitle() != null) {
            compilationToUpdate.setTitle(compilation.getTitle());
        }
        log.info("Данные подборки обновлены.");
        return toCompilationDto(compilationRepository.save(compilationToUpdate));
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        if (!pinned) {
            return new ArrayList<>();
        }
        log.info("Получен список всех подборок.");
        return toCompilationsDto(compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size)));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return toCompilationDto(getCompilation(compId));
    }

    @Override
    public void deleteCompilation(Long compId) {
        log.info("Подборка удалена.");
        compilationRepository.deleteById(compId);
    }

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Неверный ID подборки."));
    }
}
