package com.allitov.newsapi.web.controller.v1;

import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.model.service.NewsCategoryService;
import com.allitov.newsapi.web.dto.request.newscategory.NewsCategoryRequest;
import com.allitov.newsapi.web.dto.response.error.ErrorResponse;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryListResponse;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryResponse;
import com.allitov.newsapi.web.filter.NewsCategoryFilter;
import com.allitov.newsapi.web.mapper.NewsCategoryMapper;
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
@RequestMapping("/api/v1/news_category")
@RequiredArgsConstructor
@Tag(name = "News category controller", description = "News category API version 1.0")
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryService;

    private final NewsCategoryMapper newsCategoryMapper;

    @Operation(
            summary = "Get news categories by filter",
            description = "Get news categories by filter. Return a list of news categories matching filter parameters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsCategoryListResponse.class),
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
    public ResponseEntity<NewsCategoryListResponse> filterBy(@ParameterObject @Valid NewsCategoryFilter filter) {
        return ResponseEntity.ok(newsCategoryMapper.newsCategoryListToNewsCategoryListResponse(
                newsCategoryService.filterBy(filter))
        );
    }

    @Operation(
            summary = "Get news category by ID",
            description = "Get news category by ID. Return id and name of found news category",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsCategoryResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if news category with specified id not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")
                    }
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsCategoryResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsCategoryMapper.newsCategoryToResponse(newsCategoryService.findById(id)));
    }

    @Operation(
            summary = "Create news category",
            description = "Create news category. Return id and name of created news category"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = NewsCategoryResponse.class),
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
    public ResponseEntity<NewsCategoryResponse> create(@RequestBody @Valid NewsCategoryRequest request) {
        NewsCategory category = newsCategoryService.save(newsCategoryMapper.requestToNewsCategory(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(newsCategoryMapper.newsCategoryToResponse(category));
    }

    @Operation(
            summary = "Update news category by ID",
            description = "Update news category by ID. Return id and name of updated news category",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = NewsCategoryResponse.class),
                                    mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Return status 404 and error message if news category with specified id not found",
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
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsCategoryResponse> update(@PathVariable("id") Long id,
                                                       @RequestBody @Valid NewsCategoryRequest request) {
        NewsCategory category = newsCategoryService.update(newsCategoryMapper.requestToNewsCategory(id, request));

        return ResponseEntity.ok(newsCategoryMapper.newsCategoryToResponse(category));
    }

    @Operation(
            summary = "Delete news category by ID",
            description = "Delete news category by ID. Return status 204",
            parameters = {
                    @Parameter(name = "id", example = "1")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        newsCategoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
