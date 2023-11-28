package com.allitov.newsapi.web.filter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentFilter {

    @NotNull(message = "News id must be specified")
    @Positive(message = "News id must be > 0")
    private Long newsId;
}
