package ru.practicum.ewm_main.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_main.comment.dto.CommentUpdateDto;
import ru.practicum.ewm_main.comment.dto.CommentDto;
import ru.practicum.ewm_main.comment.model.Comment;
import ru.practicum.ewm_main.comment.repository.CommentRepository;
import ru.practicum.ewm_main.error.exception.ConflictException;
import ru.practicum.ewm_main.error.exception.NotFoundException;
import ru.practicum.ewm_main.event.model.Event;
import ru.practicum.ewm_main.event.model.State;
import ru.practicum.ewm_main.event.repository.EventRepository;
import ru.practicum.ewm_main.user.model.User;
import ru.practicum.ewm_main.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

import static ru.practicum.ewm_main.comment.mapper.CommentMapper.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto saveComment(CommentDto commentDto, Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Комментарий можно оставить только под опубликованным событием.");
        }
        Comment comment = toComment(commentDto, user, event);
        log.info("Комментарий добавлен.");
        return toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(Long commentId, Long userId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new ConflictException("Только автор может изменить комментарий."));

        comment.setText(commentUpdateDto.getText());
        log.info("Комментарий изменен.");
        return toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByUser(Long userId, Integer from, Integer size) {
        User user = getUser(userId);
        log.info("Получен список всех комментариев пользователя.");
        return toCommentsDto(commentRepository.findAllByUser(user, PageRequest.of(from / size, size)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByEvent(Long eventId, Integer from, Integer size) {
        Event event = getEvent(eventId);
        log.info("Получен список всех комментариев события.");
        return toCommentsDto(commentRepository.findAllByEvent(event, PageRequest.of(from / size, size)));
    }

    @Override
    public void userDeleteComment(Long commentId, Long userId) {
        getUser(userId);
        Comment comment = getComment(commentId);

        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new ConflictException("Только автор может удалить комментарий.");
        }
        commentRepository.deleteById(commentId);
        log.info("Комментарий удален пользователем.");
    }

    @Override
    public void adminDeleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NotFoundException("Комментарий не найден.");
        }
        commentRepository.deleteById(commentId);
        log.info("Комментарий удален администратором.");
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Неверный ID пользователя."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Неверный ID события."));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Неверный ID комментария."));
    }
}
