package com.allitov.newsapi.web.dto.response.user;

import lombok.Data;

import java.time.Instant;

@Data
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Instant regDate;
}
