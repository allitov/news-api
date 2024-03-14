package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.security.UserDetailsImpl;
import com.allitov.newsapi.web.dto.request.comment.CommentRequest;
import com.allitov.newsapi.web.dto.response.comment.CommentListResponse;
import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import com.allitov.newsapi.web.filter.CommentFilter;
import com.allitov.newsapi.web.mapper.CommentMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v2/comment")
@RequiredArgsConstructor
@Tag(name = "Comment controller", description = "Comment API version 2.0")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping("/filter")
    public ResponseEntity<CommentListResponse> filterBy(@ParameterObject @Valid CommentFilter filter) {
        return ResponseEntity.ok(commentMapper.commentListToCommentListResponse(commentService.filterBy(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentMapper.commentToResponse(commentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CommentRequest request,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Comment commentToSave = commentMapper.requestToComment(request);
        commentToSave.setAuthor(userDetails.getUser());
        Comment savedComment = commentService.save(commentToSave);

        return ResponseEntity.created(URI.create("/api/v2/comment/" + savedComment.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @Valid @RequestBody CommentRequest request) {
        commentService.update(commentMapper.requestToComment(id, request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
