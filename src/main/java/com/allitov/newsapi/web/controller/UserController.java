package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.web.dto.request.user.UserRequest;
import com.allitov.newsapi.web.dto.response.error.ErrorResponse;
import com.allitov.newsapi.web.dto.response.user.UserListResponse;
import com.allitov.newsapi.web.dto.response.user.UserResponse;
import com.allitov.newsapi.web.filter.UserFilter;
import com.allitov.newsapi.web.mapper.UserMapper;
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User controller", description = "User API version 1.0")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(
            summary = "Get users by filter",
            description = "Get users by filter. Return a list of users matching the filter parameters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserListResponse.class),
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
    public ResponseEntity<UserListResponse> filterBy(@ParameterObject @Valid UserFilter filter) {
        return ResponseEntity.ok(userMapper.userListToUserResponseList(userService.filterBy(filter)));
    }

    @Operation(
            summary = "Get user by ID",
            description = "Get user by ID. Return id, name, email and registration date of found user",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if user with specified id not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @Operation(
            summary = "Create user",
            description = "Create user. Return id, name, email and registration date of created user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class),
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
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        User user = userService.save(userMapper.requestToUser(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponse(user));
    }

    @Operation(
            summary = "Update user by ID",
            description = "Update user by ID. Return id, name, email and registration date of updated user",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if user with specified id not found",
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
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long id, @RequestBody @Valid UserRequest request) {
        User user = userService.update(userMapper.requestToUser(id, request));

        return ResponseEntity.ok(userMapper.userToResponse(user));
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Delete user by ID. Return status 204",
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
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
