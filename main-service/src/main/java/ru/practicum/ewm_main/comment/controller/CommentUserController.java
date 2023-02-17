package ru.practicum.ewm_main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.comment.dto.CommentDto;
import ru.practicum.ewm_main.comment.dto.CommentUpdateDto;
import ru.practicum.ewm_main.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class CommentUserController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto saveComment(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody CommentDto commentDto) {
        return commentService.saveComment(commentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Long commentId,
                                    @PathVariable Long userId,
                                    @RequestBody CommentUpdateDto commentDto) {
        return commentService.updateComment(commentId, userId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void userDeleteComment(@PathVariable Long userId,
                                  @PathVariable Long commentId) {
        commentService.userDeleteComment(commentId, userId);
    }

    @GetMapping
    public List<CommentDto> getAllCommentsByUser(@PathVariable Long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getAllCommentsByUser(userId, from, size);
    }
}
