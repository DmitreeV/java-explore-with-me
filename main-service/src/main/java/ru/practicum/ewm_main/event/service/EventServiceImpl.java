package ru.practicum.ewm_main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.category.model.Category;
import ru.practicum.ewm_main.category.repository.CategoryRepository;
import ru.practicum.ewm_main.error.exception.BadRequestException;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.event.EventMapper;
import ru.practicum.ewm_main.event.dto.AdminUpdateEventDto;
import ru.practicum.ewm_main.event.dto.EventFullDto;
import ru.practicum.ewm_main.event.dto.EventShortDto;
import ru.practicum.ewm_main.event.dto.NewEventDto;
import ru.practicum.ewm_main.event.model.Event;
import ru.practicum.ewm_main.event.repository.EventRepository;
import ru.practicum.ewm_main.location.model.Location;
import ru.practicum.ewm_main.location.repository.LocationRepository;
import ru.practicum.ewm_main.user.model.User;
import ru.practicum.ewm_main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm_main.event.EventMapper.*;
import static ru.practicum.ewm_main.event.model.State.CANCELED;
import static ru.practicum.ewm_main.event.model.State.PENDING;
import static ru.practicum.ewm_main.location.LocationMapper.toLocation;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        return toEventsDto(eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)));
    }

    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        User user = getUser(userId);
        Event event = toEvent(newEventDto);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Дата события не может быть в прошлом");
        }
        Location location = locationRepository.save(toLocation(newEventDto.getLocation()));
        event.setCategory(getCategory(newEventDto.getCategory()));
        event.setLocation(location);
        event.setInitiator(user);
        log.info("Событие сохранено.");
        return toEventDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventByUserId(Long eventId, Long userId) {
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Невозможно получить полную информацию о событии");
        }
        return toEventDto(event);
    }

    @Override
    public EventFullDto updateEventByUserId(Long eventId, Long userId) {
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("Невозможно получить полную информацию о событии");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Дата события не может быть в прошлом");
        }
        if (!event.getState().equals(PENDING)) {
            throw new BadRequestException("Изменить можно только отмененные события");
        }
        event.setState(CANCELED);
        log.info("Данные события добавленного текущим пользователем изменены.");
        return toEventDto(eventRepository.save(event));
    }

    //soon
    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categoryIds, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {

        return null;
    }

    //soon
    @Override
    public EventFullDto getEventById(Long eventId) {
        return null;
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, int from, int size) {
        return eventRepository.searchEventsByAdmin(users, states, categories, PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventDto eventDto) {
        Event event = getEvent(eventId);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Дата события не может быть в прошлом");
        }
        if (!event.getState().equals(PENDING)) {
            throw new BadRequestException("Изменить можно только отмененные события");
        }
        log.info("Данные события изменены.");
        return toEventDto(eventRepository.save(event));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Неверный ID пользователя."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Неверный ID события."));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Неверный ID категории."));
    }
}
