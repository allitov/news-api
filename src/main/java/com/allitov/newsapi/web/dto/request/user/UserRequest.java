package com.allitov.newsapi.web.dto.request.user;

import com.allitov.newsapi.exception.ExceptionMessage;
import com.allitov.newsapi.model.data.RoleType;
import com.allitov.newsapi.web.validation.ValuesOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {

    @NotBlank(message = ExceptionMessage.USER_BLANK_USERNAME)
    @Size(min = 3, max = 50, message = ExceptionMessage.USER_INVALID_USERNAME)
    @Schema(example = "Ivan Ivanov")
    private String username;

    @NotBlank(message = ExceptionMessage.USER_BLANK_EMAIL)
    @Size(min = 3, max = 256, message = ExceptionMessage.USER_INVALID_EMAIL)
    @Schema(example = "email@example")
    private String email;

    @NotBlank(message = ExceptionMessage.USER_BLANK_PASSWORD)
    @Size(min = 3, max = 256, message = ExceptionMessage.USER_INVALID_PASSWORD)
    @Schema(example = "12345")
    private String password;

    @NotEmpty(message = ExceptionMessage.USER_NULL_ROLES)
    @ValuesOfEnum(enumClass = RoleType.class, message = ExceptionMessage.USER_INVALID_ROLES)
    @Schema(example = "[\"USER\", \"MODERATOR\", \"ADMIN\"]", allowableValues = {"USER", "MODERATOR", "ADMIN"})
    private Set<String> roles;
}
