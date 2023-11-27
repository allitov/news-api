package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.web.dto.request.user.UserRequest;
import com.allitov.newsapi.web.dto.response.user.UserListResponse;
import com.allitov.newsapi.web.dto.response.user.UserResponse;
import com.allitov.newsapi.web.filter.UserFilter;
import com.allitov.newsapi.web.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/filter")
    public ResponseEntity<UserListResponse> filterBy(UserFilter filter) {
        return ResponseEntity.ok(userMapper.userListToUserResponseList(userService.filterBy(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        User user = userService.save(userMapper.requestToUser(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long id, @RequestBody @Valid UserRequest request) {
        User user = userService.update(userMapper.requestToUser(id, request));

        return ResponseEntity.ok(userMapper.userToResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
