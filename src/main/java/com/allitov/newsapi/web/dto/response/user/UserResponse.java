package com.allitov.newsapi.web.dto.response.user;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Instant regDate;
}
