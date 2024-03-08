package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.model.service.NewsCategoryService;
import com.allitov.newsapi.web.dto.request.newscategory.NewsCategoryRequest;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryListResponse;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryResponse;
import com.allitov.newsapi.web.filter.NewsCategoryFilter;
import com.allitov.newsapi.web.mapper.NewsCategoryMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v2/news-category")
@RequiredArgsConstructor
@Tag(name = "News category controller", description = "News category API version 2.0")
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryService;

    private final NewsCategoryMapper newsCategoryMapper;

    @GetMapping("/filter")
    public ResponseEntity<NewsCategoryListResponse> filterBy(@ParameterObject @Valid NewsCategoryFilter filter) {
        return ResponseEntity.ok(newsCategoryMapper.newsCategoryListToNewsCategoryListResponse(
                newsCategoryService.filterBy(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsCategoryResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsCategoryMapper.newsCategoryToResponse(newsCategoryService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody NewsCategoryRequest request) {
        NewsCategory newsCategory = newsCategoryService.save(newsCategoryMapper.requestToNewsCategory(request));

        return ResponseEntity.created(URI.create("/api/v2/news-category/" + newsCategory.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable("id") Long id, @RequestBody NewsCategoryRequest request) {
        newsCategoryService.update(newsCategoryMapper.requestToNewsCategory(id, request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        newsCategoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
