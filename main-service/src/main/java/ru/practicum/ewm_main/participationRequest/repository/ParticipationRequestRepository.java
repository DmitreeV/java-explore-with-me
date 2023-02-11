package ru.practicum.ewm_main.participationRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_main.participationRequest.model.ParticipationRequest;
import ru.practicum.ewm_main.participationRequest.model.StatusRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findByEventIdAndRequesterId(Long eventId, Long userId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long userId);

    List<ParticipationRequest> findAllByEventIdAndEventInitiatorId(Long eventId, Long userId);

    Integer countParticipationByEventIdAndStatus(Long eventId, StatusRequest confirmed);

    @Query("SELECT r FROM ParticipationRequest AS r " +
            "WHERE r.event.id = :eventId " +
            "AND r.event.initiator.id = :initiatorId " +
            "AND r.id in (:requestsId)")
    List<ParticipationRequest> findRequestsByInitiatorIdIdAndEventId(Long initiatorId, Long eventId, List<Long> requestsId);
}
