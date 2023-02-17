package ru.practicum.ewm_main.event.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import ru.practicum.ewm_main.event.dto.EventFullDto;
import ru.practicum.ewm_main.event.dto.EventShortDto;
import ru.practicum.ewm_main.event.dto.NewEventDto;
import ru.practicum.ewm_main.event.model.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm_main.Constant.DATE_TIME_FORMATTER;
import static ru.practicum.ewm_main.category.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm_main.event.model.State.PENDING;
import static ru.practicum.ewm_main.location.mapper.LocationMapper.toLocationDto;
import static ru.practicum.ewm_main.user.mapper.UserMapper.toUserShortDto;

@UtilityClass
public class EventMapper {

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
                .views(event.getViews())
                .confirmedRequests(event.getConfirmedRequests())
                .comments(new ArrayList<>())
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

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(DATE_TIME_FORMATTER))
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .comments(new ArrayList<>())
                .build();
    }

    public static List<EventShortDto> toEventsDto(Page<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
