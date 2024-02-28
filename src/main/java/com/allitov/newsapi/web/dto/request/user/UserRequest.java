package com.allitov.newsapi.web.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "User name must be specified")
    @Size(min = 3, max = 50, message = "Username length must be {min} <= length <= {max}")
    @Schema(example = "Ivan Ivanov")
    private String username;

    @NotBlank(message = "Email must be specified")
    @Size(min = 3, max = 256, message = "Email length must be {min} <= length <= {max}")
    @Schema(example = "email@example")
    private String email;
}
