package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.aop.Changeable;
import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
import com.allitov.newsapi.web.dto.response.error.ErrorResponse;
import com.allitov.newsapi.web.dto.response.news.NewsListResponse;
import com.allitov.newsapi.web.dto.response.news.NewsResponse;
import com.allitov.newsapi.web.filter.NewsFilter;
import com.allitov.newsapi.web.mapper.NewsMapper;
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
@RequestMapping("/api/news")
@RequiredArgsConstructor
@Tag(name = "News controller", description = "News API version 1.0")
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @Operation(
            summary = "Get news by filter",
            description = "Get news by filter. Return a list of news matching the filter parameters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsListResponse.class),
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
    public ResponseEntity<NewsListResponse> filterBy(@ParameterObject @Valid NewsFilter filter) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponse(newsService.filterBy(filter)));
    }

    @Operation(
            summary = "Get news by ID",
            description = "Get news by ID. Return id, content, author id, category id, creation date, " +
                    "last update time and comments list of found news",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if news with specified id not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(newsService.findById(id)));
    }

    @Operation(
            summary = "Create news",
            description = "Create news. Return id, content, author id, category id, creation date," +
                    " last update time and comments list of created news"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = NewsResponse.class),
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
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid NewsRequest request) {
        News news = newsService.save(newsMapper.requestToNews(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.newsToResponse(news));
    }

    @Operation(
            summary = "Update news by ID",
            description = "Update news by ID. Return id, content, author id, category id, creation date, " +
                    "last update time and comments list of updated news",
            parameters = {
                    @Parameter(name = "id", example = "1"),
                    @Parameter(name = "userId", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if news with specified id not found",
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
            ),
            @ApiResponse(
                    description = "Return status 403 and error message " +
                            "if news author id and user id are different",
                    responseCode = "403",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @PutMapping("/{id}")
    @Changeable
    public ResponseEntity<NewsResponse> update(@PathVariable("id") Long id,
                                               @RequestParam("userId") Long userId,
                                               @RequestBody @Valid NewsRequest request) {
        News news = newsService.update(newsMapper.requestToNews(id, request));

        return ResponseEntity.ok(newsMapper.newsToResponse(news));
    }

    @Operation(
            summary = "Delete news by ID",
            description = "Delete news by ID. Return status 204",
            parameters = {
                    @Parameter(name = "id", example = "1"),
                    @Parameter(name = "userId", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    description = "Return status 403 and error message " +
                            "if news author id and user id are different",
                    responseCode = "403",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @DeleteMapping("/{id}")
    @Changeable
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id,
                                           @RequestParam("userId") Long userId) {
        newsService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
