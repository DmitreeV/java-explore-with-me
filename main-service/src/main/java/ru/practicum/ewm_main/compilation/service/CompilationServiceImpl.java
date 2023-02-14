package ru.practicum.ewm_main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm_main.compilation.dto.CompilationDto;
import ru.practicum.ewm_main.compilation.dto.CompilationUpdateDto;
import ru.practicum.ewm_main.compilation.model.Compilation;
import ru.practicum.ewm_main.compilation.repository.CompilationRepository;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.event.model.Event;
import ru.practicum.ewm_main.event.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm_main.compilation.mapper.CompilationMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(CompilationCreateDto compilationCreateDto) {
        Compilation compilation = toCompilation(compilationCreateDto);
        List<Event> events = eventRepository.findAllById(compilationCreateDto.getEvents());
        compilation.setEvents(events);
        log.info("Подборка сохранена.");
        return toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilationToUpdate = getCompilation(compId);

        if (compilationToUpdate.getEvents() != null) {
            List<Event> events = new ArrayList<>();
            if (compilationUpdateDto.getEvents().size() != 0) {
                events = eventRepository.findEventsByIds(compilationUpdateDto.getEvents());
            }
            compilationToUpdate.setEvents(events);
        }
        compilationToUpdate.setPinned(compilationUpdateDto.getPinned());
        compilationToUpdate.setTitle(compilationUpdateDto.getTitle());

        log.info("Данные подборки обновлены.");
        return toCompilationDto(compilationRepository.save(compilationToUpdate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            return toCompilationsDto(compilationRepository.findAll(PageRequest.of(from / size, size)));
        }
        log.info("Получен список всех подборок.");
        return toCompilationsDto(compilationRepository.findAllByPinned(true, PageRequest.of(from / size, size)));
    }

    @Override
    @Transactional(readOnly = true)
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
