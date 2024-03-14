package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.Comment;
import com.allitov.newsapi.model.service.CommentService;
import com.allitov.newsapi.security.UserDetailsImpl;
import com.allitov.newsapi.web.dto.request.comment.CommentRequest;
import com.allitov.newsapi.web.dto.response.comment.CommentListResponse;
import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import com.allitov.newsapi.web.dto.response.error.ErrorResponse;
import com.allitov.newsapi.web.filter.CommentFilter;
import com.allitov.newsapi.web.mapper.CommentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Get comments by filter",
            description = "Get comments by filter. " +
                    "Returns a list of comments matching the filter parameters. " +
                    "Requires any of the authorities: ['ADMIN', 'MODERATOR', 'USER']",
            security = @SecurityRequirement(name = "Basic authorisation")
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Returns status 200 and comments list if everything completed successfully",
                    responseCode = "200",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = CommentListResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 400 and error message if filter has invalid values",
                    responseCode = "400",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 401 and error message if user is not authorized",
                    responseCode = "401",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 403 and error message if user has no required authorities",
                    responseCode = "403",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @GetMapping("/filter")
    public ResponseEntity<CommentListResponse> filterBy(@ParameterObject @Valid CommentFilter filter) {
        return ResponseEntity.ok(commentMapper.commentListToCommentListResponse(commentService.filterBy(filter)));
    }

    @Operation(
            summary = "Get comment by id",
            description = "Get comment by id. Returns comment with requested id. " +
                    "Requires any of the authorities: ['ADMIN', 'MODERATOR', 'USER']",
            parameters = {
                    @Parameter(name = "id", example = "1")
            },
            security = @SecurityRequirement(name = "Basic authorisation")
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Returns status 200 and comment if everything completed successfully",
                    responseCode = "200",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = CommentResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 401 and error message if user is not authorized",
                    responseCode = "401",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 403 and error message if user has no required authorities",
                    responseCode = "403",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 404 and error message if comment with requested id was not found",
                    responseCode = "404",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(commentMapper.commentToResponse(commentService.findById(id)));
    }

    @Operation(
            summary = "Create comment",
            description = "Create comment. Returns status 201 and created comment location. " +
                    "Requires any of the authorities: ['ADMIN', 'MODERATOR', 'USER']",
            security = @SecurityRequirement(name = "Basic authorisation")
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Returns status 201 and created comment location " +
                            "if everything completed successfully",
                    responseCode = "201",
                    headers = {
                            @Header(name = "Location", description = "Created comment location")
                    }
            ),
            @ApiResponse(
                    description = "Returns status 400 and error message if request has invalid values",
                    responseCode = "400",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 401 and error message if user is not authorized",
                    responseCode = "401",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 403 and error message if user has no required authorities",
                    responseCode = "403",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 404 and error message if request has nonexistent news category id",
                    responseCode = "404",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CommentRequest request,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Comment commentToSave = commentMapper.requestToComment(request);
        commentToSave.setAuthor(userDetails.getUser());
        Comment savedComment = commentService.save(commentToSave);

        return ResponseEntity.created(URI.create("/api/v2/comment/" + savedComment.getId())).build();
    }

    @Operation(
            summary = "Update comment by id",
            description = "Update comment by id. Returns status 204. " +
                    "Requires any of the authorities: ['ADMIN', 'MODERATOR', 'USER']",
            parameters = {
                    @Parameter(name = "id", example = "1")
            },
            security = @SecurityRequirement(name = "Basic authorisation")
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Returns status 204 if everything completed successfully",
                    responseCode = "204"
            ),
            @ApiResponse(
                    description = "Returns status 400 and error message if request has invalid values",
                    responseCode = "400",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 401 and error message if user is not authorized",
                    responseCode = "401",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 403 and error message if user has no required authorities",
                    responseCode = "403",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 404 and error message if comment with requested id was not found " +
                            "or request has nonexistent news category id",
                    responseCode = "404",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @Valid @RequestBody CommentRequest request) {
        commentService.update(commentMapper.requestToComment(id, request));

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete comment by id",
            description = "Delete comment by id. Returns status 204. " +
                    "Requires any of the authorities: ['ADMIN', 'MODERATOR', 'USER']",
            parameters = {
                    @Parameter(name = "id", example = "1")
            },
            security = @SecurityRequirement(name = "Basic authorisation")
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Returns status 204 if everything completed successfully",
                    responseCode = "204"
            ),
            @ApiResponse(
                    description = "Returns status 401 and error message if user is not authorized",
                    responseCode = "401",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 403 and error message if user has no required authorities",
                    responseCode = "403",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            ),
            @ApiResponse(
                    description = "Returns status 404 and error message if comment with requested id was not found",
                    responseCode = "404",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
