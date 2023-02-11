package ru.practicum.ewm_main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.category.model.Category;
import ru.practicum.ewm_main.category.repository.CategoryRepository;
import ru.practicum.ewm_main.error.exception.BadRequestException;
import ru.practicum.ewm_main.error.exception.ConflictException;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.event.dto.*;
import ru.practicum.ewm_main.event.mapper.EventMapper;
import ru.practicum.ewm_main.event.model.Event;
import ru.practicum.ewm_main.event.model.State;
import ru.practicum.ewm_main.event.repository.EventRepository;
import ru.practicum.ewm_main.location.model.Location;
import ru.practicum.ewm_main.location.repository.LocationRepository;
import ru.practicum.ewm_main.participationRequest.repository.ParticipationRequestRepository;
import ru.practicum.ewm_main.user.model.User;
import ru.practicum.ewm_main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm_main.event.mapper.EventMapper.*;
import static ru.practicum.ewm_main.event.model.State.*;
import static ru.practicum.ewm_main.location.LocationMapper.toLocation;
import static ru.practicum.ewm_main.participationRequest.model.StatusRequest.CONFIRMED;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        return toEventsDto(eventRepository.findAllByInitiatorId(userId, PageRequest.of(from / size, size)));
    }

    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        User user = getUser(userId);
        Event event = toEvent(newEventDto);

        validateDate(LocalDateTime.parse(newEventDto.getEventDate(), DATE_TIME_FORMATTER));
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
            throw new BadRequestException("Невозможно получить полную информацию о событии.");
        }
        return setConfirmedRequests(toEventDto(event));
    }

    @Override
    public EventFullDto updateEventByUserId(Long eventId, Long userId, PrivateUpdateEventDto eventDto) {
        if (eventDto.getEventDate() != null) {
            validateDate(LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMATTER));
        }
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Невозможно получить полную информацию о событии.");
        }
        if (event.getState() == (State.PUBLISHED)) {
            throw new ConflictException("Изменить можно только отмененные события.");
        }
        switch (eventDto.getStateAction()) {
            case SEND_TO_REVIEW:
                event.setState(PENDING);
                break;
            case CANCEL_REVIEW:
                event.setState(CANCELED);
                break;
        }
        log.info("Данные события добавленного текущим пользователем изменены.");
        return setConfirmedRequests(toEventDto(eventRepository.save(event)));
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categoryIds, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {

        List<EventShortDto> events = eventRepository.searchEvents(text, categoryIds, paid, PUBLISHED,
                        PageRequest.of(from / size, size))
                .stream()
                .filter(event -> rangeStart != null ?
                        event.getEventDate().isAfter(LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER)) :
                        event.getEventDate().isAfter(LocalDateTime.now())
                                && rangeEnd != null ? event.getEventDate().isBefore(LocalDateTime.parse(rangeEnd,
                                DATE_TIME_FORMATTER)) :
                                event.getEventDate().isBefore(LocalDateTime.MAX))
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList());
        if (Boolean.TRUE.equals(onlyAvailable)) {
            events = events.stream().filter(shortEventDto ->
                    shortEventDto.getConfirmedRequests() < eventRepository
                            .findById(shortEventDto.getId()).get().getParticipantLimit() ||
                            eventRepository.findById(shortEventDto.getId()).get().getParticipantLimit() == 0
            ).collect(Collectors.toList());
        }
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    events = events
                            .stream()
                            .sorted(Comparator.comparing(EventShortDto::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case "VIEWS":
                    events = events
                            .stream()
                            .sorted(Comparator.comparing(EventShortDto::getViews))
                            .collect(Collectors.toList());
                    break;
                default:
                    throw new BadRequestException("Сортировкавозможна только по просмотрам или дате события.");
            }
        }
        return events;
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = getEvent(eventId);
        if (!event.getState().equals(PUBLISHED)) {
            throw new BadRequestException("Событие должно быть опубликовано.");
        }
        viewCounter(eventId);
        return setConfirmedRequests(toEventDto(event));
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               String rangeStart, String rangeEnd, int from, int size) {
        List<State> stateList = states == null ? null : states
                .stream()
                .map(State::valueOf)
                .collect(Collectors.toList());
        return eventRepository.searchEventsByAdmin(users, stateList, categories, PageRequest.of(from / size, size))
                .stream()
                .filter(event -> rangeStart != null ?
                        event.getEventDate().isAfter(LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER)) :
                        event.getEventDate().isAfter(LocalDateTime.now())
                                && rangeEnd != null ? event.getEventDate().isBefore(LocalDateTime.parse(rangeEnd,
                                DATE_TIME_FORMATTER)) : event.getEventDate().isBefore(LocalDateTime.MAX))
                .map(EventMapper::toEventDto)
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventDto eventDto) {

        if (eventDto.getEventDate() != null) {
            validateDate(LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMATTER));
        }
        Event event = getEvent(eventId);
        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getCategory() != null) {
            Category category = getCategory(eventDto.getCategory());
            event.setCategory(category);
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMATTER));
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(event.getLocation());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        switch (eventDto.getStateAction()) {
            case PUBLISH_EVENT:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new ConflictException("Событие можно публиковать, только если оно в состоянии ожидания.");
                }
                break;
            case REJECT_EVENT:
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.CANCELED);
                } else {
                    throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано.");
                }
                break;
        }
        log.info("Данные события изменены администратором.");
        return toEventDto(eventRepository.save(event));
    }

    private void validateDate(LocalDateTime eventDate) {
        LocalDateTime date = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(date)) {
            throw new ConflictException("Дата события должна быть не менее чем через два часа.");
        }
    }

    private EventFullDto setConfirmedRequests(EventFullDto eventDto) {
        eventDto.setConfirmedRequests(requestRepository.countParticipationByEventIdAndStatus(eventDto.getId(),
                CONFIRMED));
        return eventDto;
    }

    private EventShortDto setConfirmedRequests(EventShortDto eventDto) {
        eventDto.setConfirmedRequests(requestRepository.countParticipationByEventIdAndStatus(eventDto.getId(),
                CONFIRMED));
        return eventDto;
    }

    private void viewCounter(Long id) {
        Event event = getEvent(id);
        long views = event.getViews() + 1;
        event.setViews(views);
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
