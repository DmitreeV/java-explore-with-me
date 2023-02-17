package ru.practicum.ewm_main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_main.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments/{commentId}")
public class AdminCommentController {

    private final CommentService commentService;

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void adminDeleteComment(@PathVariable Long commentId) {
        commentService.adminDeleteComment(commentId);
    }
}
