package ru.practicum.ewm_main.event;

import org.springframework.data.domain.Page;
import ru.practicum.ewm_main.event.dto.AdminUpdateEventDto;
import ru.practicum.ewm_main.event.dto.EventFullDto;
import ru.practicum.ewm_main.event.dto.EventShortDto;
import ru.practicum.ewm_main.event.dto.NewEventDto;
import ru.practicum.ewm_main.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm_main.category.CategoryMapper.toCategory;
import static ru.practicum.ewm_main.category.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm_main.event.model.State.PENDING;
import static ru.practicum.ewm_main.location.LocationMapper.toLocationDto;
import static ru.practicum.ewm_main.user.UserMapper.toUserShortDto;

public class EventMapper {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn().format(DATE_TIME_FORMATTER))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .initiator(toUserShortDto(event.getInitiator()))
                .location(toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static Event toEvent(EventFullDto eventDto) {
        return Event.builder()
                .id(eventDto.getId())
                .annotation(eventDto.getAnnotation())
                .category(toCategory(eventDto.getCategory()))
                .createdOn(LocalDateTime.parse(eventDto.getCreatedOn(), DATE_TIME_FORMATTER))
                .description(eventDto.getDescription())
                .eventDate(LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMATTER))
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .publishedOn(eventDto.getPublishedOn())
                .requestModeration(eventDto.getRequestModeration())
                .state(eventDto.getState())
                .title(eventDto.getTitle())
                .build();
    }

    public static Event toEvent(EventShortDto eventShortDto) {
        return Event.builder()
                .id(eventShortDto.getId())
                .annotation(eventShortDto.getAnnotation())
                .category(toCategory(eventShortDto.getCategory()))
                .eventDate(LocalDateTime.parse(eventShortDto.getEventDate(), DATE_TIME_FORMATTER))
                .paid(eventShortDto.getPaid())
                .title(eventShortDto.getTitle())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), DATE_TIME_FORMATTER))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .state(PENDING)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static Event toEvent(AdminUpdateEventDto adminUpdateEventDto) {
        return Event.builder()
                .annotation(adminUpdateEventDto.getAnnotation())
                .description(adminUpdateEventDto.getDescription())
                .eventDate(LocalDateTime.parse(adminUpdateEventDto.getEventDate(), DATE_TIME_FORMATTER))
                .paid(adminUpdateEventDto.getPaid())
                .participantLimit(adminUpdateEventDto.getParticipantLimit())
                .requestModeration(adminUpdateEventDto.getRequestModeration())
                .title(adminUpdateEventDto.getTitle())
                .state(PENDING)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static List<EventShortDto> toEventsDto(Page<Event> events){
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
