package ru.practicum.ewm_main.participationRequest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_main.error.exception.BadRequestException;
import ru.practicum.ewm_main.error.exception.ConflictException;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.event.dto.EventReqStatusUpdateReqDto;
import ru.practicum.ewm_main.event.dto.RequestStatusUpdateResponse;
import ru.practicum.ewm_main.event.model.Event;
import ru.practicum.ewm_main.event.model.State;
import ru.practicum.ewm_main.event.repository.EventRepository;
import ru.practicum.ewm_main.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.ewm_main.participationRequest.mapper.ParticipationRequestMapper;
import ru.practicum.ewm_main.participationRequest.model.ParticipationRequest;
import ru.practicum.ewm_main.participationRequest.model.StatusRequest;
import ru.practicum.ewm_main.participationRequest.repository.ParticipationRequestRepository;
import ru.practicum.ewm_main.user.model.User;
import ru.practicum.ewm_main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm_main.participationRequest.mapper.ParticipationRequestMapper.toParticipationRequestDto;
import static ru.practicum.ewm_main.participationRequest.mapper.ParticipationRequestMapper.toParticipationRequestsDto;
import static ru.practicum.ewm_main.participationRequest.model.StatusRequest.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId) {
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId) != null) {
            throw new ConflictException("Нельзя добавить повторный запрос.");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setRequester(getUser(userId));
        request.setEvent(getEvent(eventId));
        request.setStatus(CONFIRMED);

        if (userId.equals(request.getEvent().getInitiator().getId())) {
            throw new ConflictException("Нельзя добавить запрос от инициализатора события.");
        }
        if (!request.getEvent().getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Нельзя добавить запрос на не опубликованное событие.");
        }
        if (request.getEvent().getParticipantLimit() <= requestRepository
                .countParticipationByEventIdAndStatus(eventId, CONFIRMED)) {
            throw new ConflictException("Лимит участников уже заполнен.");
        }
        if (Boolean.TRUE.equals(request.getEvent().getRequestModeration())) {
            request.setStatus(StatusRequest.PENDING);
        }
        log.info("Запрос на участие сохранен.");
        return toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        log.info("Получена информация о заявках пользователя.");
        return toParticipationRequestsDto(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long eventId, Long userId) {
        getUser(userId);
        getEvent(eventId);
        log.info("Получена информация о запросах на участие в событии пользователя.");
        return toParticipationRequestsDto(requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId));
    }

    @Override
    public RequestStatusUpdateResponse updateParticipationRequest(Long userId, Long eventId, EventReqStatusUpdateReqDto updateReqDto) {
        Event event = getEvent(eventId);

        if (event.getParticipantLimit() <= requestRepository
                .countParticipationByEventIdAndStatus(eventId, CONFIRMED)) {
            throw new ConflictException("Лимит участников уже заполнен.");
        }
        List<ParticipationRequestDto> confirmedList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedList = new ArrayList<>();

        requestRepository.findRequestsByInitiatorIdIdAndEventId(userId, eventId, updateReqDto.getRequestIds())
                .stream()
                .peek(req -> {
                    if (req.getStatus().equals(StatusRequest.PENDING)) {
                        if (updateReqDto.getStatus().equals(StatusRequest.CONFIRMED)) {
                            if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                                req.setStatus(StatusRequest.CONFIRMED);
                                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                            } else {
                                req.setStatus(StatusRequest.REJECTED);
                            }
                        } else {
                            req.setStatus(StatusRequest.REJECTED);
                        }
                    } else {
                        throw new ConflictException("Нельзя подтвердить запрос.");
                    }
                })
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .forEach(req -> {
                    if (req.getStatus().equals(StatusRequest.CONFIRMED))
                        confirmedList.add(req);
                    else
                        rejectedList.add(req);
                });
        log.info("Статус заявки изменен.");
        return new RequestStatusUpdateResponse(confirmedList, rejectedList);
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new BadRequestException("Только организатор может отменить запрос."));
        request.setStatus(StatusRequest.CANCELED);
        log.info("Запрос отменен.");
        return toParticipationRequestDto(requestRepository.save(request));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Неверный ID пользователя."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Неверный ID события."));
    }
}
