package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.security.UserDetailsImpl;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
import com.allitov.newsapi.web.dto.response.news.NewsListResponse;
import com.allitov.newsapi.web.dto.response.news.NewsResponse;
import com.allitov.newsapi.web.filter.NewsFilter;
import com.allitov.newsapi.web.mapper.NewsMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v2/news")
@RequiredArgsConstructor
@Tag(name = "News controller", description = "News API version 2.0")
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(@Valid NewsFilter filter) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponse(newsService.filterBy(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(newsService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody NewsRequest request,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        News newsToSave = newsMapper.requestToNews(request);
        newsToSave.setAuthor(userDetails.getUser());
        News savedNews =  newsService.save(newsToSave);

        return ResponseEntity.created(URI.create("/api/v2/news/" + savedNews.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @Valid @RequestBody NewsRequest request) {
        newsService.update(newsMapper.requestToNews(id, request));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        newsService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
