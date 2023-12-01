package com.allitov.newsapi.web.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "User name must be specified")
    @Size(min = 3, max = 50, message = "Username length must be {min} <= length <= {max}")
    private String userName;

    @NotBlank(message = "Email must be specified")
    @Size(min = 3, max = 255, message = "Email length must be {min} <= length <= {max}")
    private String email;
}
