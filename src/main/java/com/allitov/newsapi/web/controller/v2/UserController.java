package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.security.UserDetailsImpl;
import com.allitov.newsapi.web.dto.request.user.UserRequest;
import com.allitov.newsapi.web.dto.response.user.UserListResponse;
import com.allitov.newsapi.web.dto.response.user.UserResponse;
import com.allitov.newsapi.web.filter.UserFilter;
import com.allitov.newsapi.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
@Tag(name = "User controller", description = "User API version 2.0")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserListResponse> filterBy(@Valid UserFilter filter) {
        return ResponseEntity.ok(userMapper.userListToUserResponseList(userService.filterBy(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") Long id,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> create(@Valid @RequestBody UserRequest request) {
        User user = userService.createNewAccount(userMapper.requestToUser(request));

        return ResponseEntity.created(URI.create("/api/v2/user/" + user.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable("id") Long id,
                                           @RequestBody UserRequest request) {
        userService.update(userMapper.requestToUser(id, request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
