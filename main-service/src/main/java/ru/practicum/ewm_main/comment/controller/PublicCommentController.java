package ru.practicum.ewm_main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.comment.dto.CommentDto;
import ru.practicum.ewm_main.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAllCommentsByEvent(@PathVariable Long eventId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getAllCommentsByEvent(eventId, from, size);
    }
}
