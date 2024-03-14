package com.allitov.newsapi.web.dto.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotNull(message = "News id must be specified")
    @Schema(example = "1", minimum = "1")
    private Long newsId;

    @NotBlank(message = "Content must be specified")
    @Schema(example = "comment content", minLength = 1)
    private String content;
}
