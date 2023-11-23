package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
import com.allitov.newsapi.web.dto.response.news.NewsListResponse;
import com.allitov.newsapi.web.dto.response.news.NewsResponse;
import com.allitov.newsapi.web.filter.NewsFilter;
import com.allitov.newsapi.web.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(NewsFilter filter) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponse(newsService.filterBy(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(newsService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody NewsRequest request) {
        News news = newsService.save(newsMapper.requestToNews(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.newsToResponse(news));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(@PathVariable("id") Long id, @RequestBody NewsRequest request) {
        News news = newsService.update(newsMapper.requestToNews(id, request));

        return ResponseEntity.ok(newsMapper.newsToResponse(news));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        newsService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
