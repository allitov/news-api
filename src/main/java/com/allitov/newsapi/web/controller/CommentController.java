package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.web.dto.request.comment.CommentRequest;
import com.allitov.newsapi.web.dto.response.comment.CommentListResponse;
import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import com.allitov.newsapi.web.dto.response.error.ErrorResponse;
import com.allitov.newsapi.web.filter.CommentFilter;
import com.allitov.newsapi.web.mapper.CommentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Tag(name = "Comment controller", description = "Comment API version 1.0")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @Operation(
            summary = "Get comments by filter",
            description = "Get comments by filter. Return a list of comments matching the filter parameters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = CommentListResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 400 and error message if filter has invalid values",
                    responseCode = "400",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/filter")
    public ResponseEntity<CommentListResponse> filterBy(@ParameterObject @Valid CommentFilter filter) {
        return ResponseEntity.ok(commentMapper.commentListToCommentListResponse(commentService.filterBy(filter)));
    }

    @Operation(
            summary = "Get comment by id",
            description = "Get comment by id. Return id, content, author id, news id, " +
                    "creation date and last update time of found comment",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = CommentResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if comment with specified id not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentMapper.commentToResponse(commentService.findById(id)));
    }

    @Operation(
            summary = "Create comment",
            description = "Create comment. Return id, content, author id, news id, " +
                    "creation date and last update time of created comment"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = CommentResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 400 and error message if request body has invalid values",
                    responseCode = "400",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody @Valid CommentRequest request) {
        Comment comment = commentService.save(commentMapper.requestToComment(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.commentToResponse(comment));
    }

    @Operation(
            summary = "Update comment by ID",
            description = "Update comment by ID. Return id, content, author id, news id, " +
                    "creation date and last update time of updated comment",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = CommentResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if comment with specified id not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 400 and error message if request body has invalid values",
                    responseCode = "400",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable("id") Long id,
                                                  @RequestBody @Valid CommentRequest request) {
        Comment comment = commentService.update(commentMapper.requestToComment(id, request));

        return ResponseEntity.ok(commentMapper.commentToResponse(comment));
    }

    @Operation(
            summary = "Delete comment by ID",
            description = "Delete comment by ID. Return status 204",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        commentService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
