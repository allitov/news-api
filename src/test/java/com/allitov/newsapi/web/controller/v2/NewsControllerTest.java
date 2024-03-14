package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
import com.allitov.newsapi.web.dto.response.news.NewsListResponse;
import com.allitov.newsapi.web.dto.response.news.NewsResponse;
import com.allitov.newsapi.web.filter.NewsFilter;
import com.allitov.newsapi.web.mapper.NewsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = NewsController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsService newsService;

    @MockBean
    private NewsMapper newsMapper;

    // Methods tests

    @Test
    @DisplayName("Test filterBy() status 200")
    public void givenNewsFilter_whenFilterBy_thenNewsListResponse() throws Exception {
        NewsFilter filter = new NewsFilter();
        filter.setPageSize(1);
        filter.setPageNumber(1);
        List<News> foundNews = Collections.emptyList();
        NewsListResponse response = new NewsListResponse();

        Mockito.when(newsService.filterBy(filter))
                .thenReturn(foundNews);
        Mockito.when(newsMapper.newsListToNewsListResponse(foundNews))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/news/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'news': []}"));

        Mockito.verify(newsService, Mockito.times(1))
                .filterBy(filter);
        Mockito.verify(newsMapper, Mockito.times(1))
                .newsListToNewsListResponse(foundNews);
    }

    @Test
    @DisplayName("Test findById() status 200")
    public void givenId_whenFindById_thenNewsResponse() throws Exception {
        Long id = 1L;
        News news = new News();
        NewsResponse response = createNewsResponse();

        Mockito.when(newsService.findById(id))
                .thenReturn(news);
        Mockito.when(newsMapper.newsToResponse(news))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/news/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{'id': 1, " +
                                "'content': 'content', " +
                                "'authorId': 1, " +
                                "'categoryId': 1," +
                                "'creationDate': '1970-01-01T00:00:00Z'," +
                                "'lastUpdate': '1970-01-01T00:00:00Z', " +
                                "'comments': []}")
                );

        Mockito.verify(newsService, Mockito.times(1))
                .findById(id);
        Mockito.verify(newsMapper, Mockito.times(1))
                .newsToResponse(news);
    }

    @Test
    @DisplayName("Test findById() with nonexistent id")
    public void givenNonexistentId_whenFindById_thenErrorResponse() throws Exception {
        Long id = 1L;

        Mockito.when(newsService.findById(id))
                .thenThrow(new EntityNotFoundException(String.format("News with id = '%d' not found", id)));

        mockMvc.perform(get("/api/v2/news/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format("{'errorMessage': \"News with id = '%d' not found\"}", id)));

        Mockito.verify(newsService, Mockito.times(1))
                .findById(id);
    }

    @Test
    @DisplayName("Test create() status 201")
    public void givenUserRequest_whenCreate_thenVoid() throws Exception {
        NewsRequest request = createNewsRequest();
        Long id = 1L;
        News news = new News();
        news.setId(id);

        Mockito.when(newsMapper.requestToNews(request))
                .thenReturn(news);
        Mockito.when(newsService.save(news))
                .thenReturn(news);

        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/news/" + news.getId()));

        Mockito.verify(newsMapper, Mockito.times(1))
                .requestToNews(request);
        Mockito.verify(newsService, Mockito.times(1))
                .save(news);
    }

    @Test
    @DisplayName("Test updateById() status 204")
    public void givenIdAndNewsRequest_whenUpdateById_thenVoid() throws Exception {
        Long id = 1L;
        NewsRequest request = createNewsRequest();
        News news = new News();
        news.setId(id);

        Mockito.when(newsMapper.requestToNews(id, request))
                .thenReturn(news);
        Mockito.when(newsService.update(news))
                .thenReturn(news);

        mockMvc.perform(put("/api/v2/news/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(newsMapper, Mockito.times(1))
                .requestToNews(id, request);
        Mockito.verify(newsService, Mockito.times(1))
                .update(news);
    }

    @Test
    @DisplayName("Test updateById() with nonexistent id")
    public void givenNonexistentIdAndNewsRequest_whenUpdateById_thenErrorResponse() throws Exception {
        Long id = 1L;
        NewsRequest request = createNewsRequest();
        News news = new News();
        news.setId(id);

        Mockito.when(newsMapper.requestToNews(id, request))
                .thenReturn(news);
        Mockito.when(newsService.update(news))
                .thenThrow(new EntityNotFoundException(String.format("News with id = '%d' not found", id)));

        mockMvc.perform(put("/api/v2/news/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format("{'errorMessage': \"News with id = '%d' not found\"}", id)));

        Mockito.verify(newsMapper, Mockito.times(1))
                .requestToNews(id, request);
        Mockito.verify(newsService, Mockito.times(1))
                .update(news);
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    public void givenId_whenDeleteById_thenVoid() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v2/news/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(newsService, Mockito.times(1))
                .deleteById(id);
    }

    // Validation tests

    @Test
    @DisplayName("Test NewsFilter validation with null filter page size")
    public void givenNullNewsFilterPageSize_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/filter?pageNumber={pageNumber}", 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page size must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidNewsFilterPageSize")
    @DisplayName("Test NewsFilter validation with invalid filter page size")
    public void givenInvalidNewsFilterPageSize_whenFilterBy_thenErrorResponse(NewsFilter filter) throws Exception {
        mockMvc.perform(get("/api/v2/news/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                        filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page size must be > 0'}"));
    }

    @Test
    @DisplayName("Test NewsFilter validation with null filter page number")
    public void givenNullNewsFilterPageNumber_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/filter?pageSize={pageSize}", 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page number must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidNewsFilterPageNumber")
    @DisplayName("Test NewsFilter validation with invalid filter page number")
    public void givenInvalidNewsFilterPageNumber_whenFilterBy_thenErrorResponse(NewsFilter filter) throws Exception {
        mockMvc.perform(get("/api/v2/news/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                        filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page number must be >= 0'}"));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    @DisplayName("Tests NewsRequest validation with blank content")
    public void givenBlankNewsRequestContent_whenCreate_thenErrorResponse(String content) throws Exception {
        NewsRequest request = createNewsRequest();
        request.setContent(content);

        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Content must be specified'}"));
    }

    @Test
    @DisplayName("Test NewsRequest validation with null category id")
    public void givenNullNewsRequestCategoryId_whenCreate_thenErrorResponse() throws Exception {
        NewsRequest request = createNewsRequest();
        request.setCategoryId(null);

        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Category id must be specified'}"));
    }

    private NewsResponse createNewsResponse() {
        return NewsResponse.builder()
                .id(1L)
                .content("content")
                .authorId(1L)
                .categoryId(1L)
                .creationDate(Instant.parse("1970-01-01T00:00:00Z"))
                .lastUpdate(Instant.parse("1970-01-01T00:00:00Z"))
                .comments(Collections.emptyList())
                .build();
    }

    private NewsRequest createNewsRequest() {
        return NewsRequest.builder()
                .content("content")
                .categoryId(1L)
                .build();
    }

    private static Stream<Arguments> invalidNewsFilterPageSize() {
        return Stream.of(
                Arguments.of(new NewsFilter(0, 1, "category", "author")),
                Arguments.of(new NewsFilter(-1, 1, "category", "author"))
        );
    }

    private static Stream<Arguments> invalidNewsFilterPageNumber() {
        return Stream.of(
                Arguments.of(new NewsFilter(1, -1, "category", "author")),
                Arguments.of(new NewsFilter(1, -1000, "category", "author"))
        );
    }

    private static Stream<Arguments> blankStrings() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("\n\n "),
                Arguments.of("      ")
        );
    }
}
