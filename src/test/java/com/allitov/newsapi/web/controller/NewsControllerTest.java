package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.News;
import com.allitov.newsapi.model.service.NewsService;
import com.allitov.newsapi.utils.TestUtils;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
import com.allitov.newsapi.web.dto.response.comment.CommentResponse;
import com.allitov.newsapi.web.dto.response.news.NewsListResponse;
import com.allitov.newsapi.web.dto.response.news.NewsResponse;
import com.allitov.newsapi.web.dto.response.news.NewsWithCommentsCount;
import com.allitov.newsapi.web.filter.NewsFilter;
import com.allitov.newsapi.web.mapper.NewsMapper;
import jakarta.persistence.EntityNotFoundException;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NewsControllerTest extends AbstractControllerTest {

    @MockBean
    private NewsService newsService;

    @MockBean
    private NewsMapper newsMapper;

    @Test
    public void whenFindById_thenReturnNewsById() throws Exception {
        News news = createNews(1L, "content");
        List<CommentResponse> commentResponses = new ArrayList<>();
        commentResponses.add(createCommentResponse(1L, "content1", 1L, 1L));
        commentResponses.add(createCommentResponse(2L, "content2", 2L, 1L));
        NewsResponse response = createNewsResponse(1L, "content", 1L, 1L, commentResponses);

        Mockito.when(newsService.findById(1L)).thenReturn(news);
        Mockito.when(newsMapper.newsToResponse(news)).thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(newsService, Mockito.times(1)).findById(1L);
        Mockito.verify(newsMapper, Mockito.times(1)).newsToResponse(news);

        String expectedResponse = TestUtils.readStringFromResource("response/news/method/find_news_by_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFilterBy_thenReturnNews() throws Exception {
        List<News> news = new ArrayList<>();
        news.add(createNews(1L, "content1"));
        news.add(createNews(2L, "content2"));
        List<NewsWithCommentsCount> newsWithCommentsCounts = new ArrayList<>();
        newsWithCommentsCounts.add(createNewsWithCommentsCount(1L, "content1", 1L, 1L, 1));
        newsWithCommentsCounts.add(createNewsWithCommentsCount(2L, "content2", 2L, 2L, 2));
        NewsListResponse response = new NewsListResponse();
        response.setNews(newsWithCommentsCounts);
        NewsFilter filter = new NewsFilter();
        filter.setPageNumber(0);
        filter.setPageSize(10);

        Mockito.when(newsService.filterBy(filter)).thenReturn(news);
        Mockito.when(newsMapper.newsListToNewsListResponse(news)).thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news/filter?pageNumber=0&pageSize=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(newsService, Mockito.times(1)).filterBy(filter);
        Mockito.verify(newsMapper, Mockito.times(1)).newsListToNewsListResponse(news);

        String expectedResponse = TestUtils.readStringFromResource("response/news/method/filter_by_news_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreate_thenReturnCreatedNews() throws Exception {
        News news = createNews(2L, "created content");
        NewsResponse response = createNewsResponse(2L, "created content", 2L, 2L, new ArrayList<>());
        NewsRequest request = new NewsRequest();
        request.setContent("created content");
        request.setAuthorId(2L);
        request.setCategoryId(2L);

        Mockito.when(newsMapper.requestToNews(request)).thenReturn(news);
        Mockito.when(newsMapper.newsToResponse(news)).thenReturn(response);
        Mockito.when(newsService.save(news)).thenReturn(news);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(newsMapper, Mockito.times(1)).requestToNews(request);
        Mockito.verify(newsMapper, Mockito.times(1)).newsToResponse(news);
        Mockito.verify(newsService, Mockito.times(1)).save(news);

        String expectedResponse = TestUtils.readStringFromResource("response/news/method/create_news_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdate_thenReturnUpdatedNews() throws Exception {
        News news = createNews(1L, "updated content");
        NewsResponse response = createNewsResponse(1L, "updated content", 1L, 1L, new ArrayList<>());
        NewsRequest request = new NewsRequest();
        request.setContent("updated content");
        request.setAuthorId(1L);
        request.setCategoryId(1L);

        Mockito.when(newsMapper.requestToNews(1L, request)).thenReturn(news);
        Mockito.when(newsMapper.newsToResponse(news)).thenReturn(response);
        Mockito.when(newsService.update(news)).thenReturn(news);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/news/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(newsMapper, Mockito.times(1)).requestToNews(1L, request);
        Mockito.verify(newsMapper, Mockito.times(1)).newsToResponse(news);
        Mockito.verify(newsService, Mockito.times(1)).update(news);

        String expectedResponse = TestUtils.readStringFromResource("response/news/method/update_news_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/news/500"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(newsService, Mockito.times(1)).deleteById(500L);
    }

    @Test
    public void whenFindByIdNotExistedNews_thenReturnError() throws Exception {
        Mockito.when(newsService.findById(500L)).thenThrow(new EntityNotFoundException("News with id 500 not found"));

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news/500"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(newsService, Mockito.times(1)).findById(500L);

        String expectedResponse = TestUtils.readStringFromResource("response/news/method/news_by_id_not_found_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("blankContent")
    public void whenCreateNewsWithBlankContent_thenReturnError(String content) throws Exception {
        NewsRequest request = new NewsRequest();
        request.setContent(content);
        request.setAuthorId(1L);
        request.setCategoryId(1L);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/news/request/blank_news_content_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateNewsWithNullAuthorId_thenReturnError() throws Exception {
        NewsRequest request = new NewsRequest();
        request.setContent("content");
        request.setAuthorId(null);
        request.setCategoryId(1L);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/news/request/null_news_author_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateNewsWithNullCategoryId_thenReturnError() throws Exception {
        NewsRequest request = new NewsRequest();
        request.setContent("content");
        request.setAuthorId(1L);
        request.setCategoryId(null);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/news/request/null_news_category_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidAuthorId")
    public void whenCreateNewsWithInvalidAuthorId_thenReturnError(Long authorId) throws Exception {
        NewsRequest request = new NewsRequest();
        request.setContent("content");
        request.setAuthorId(authorId);
        request.setCategoryId(1L);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/news/request/invalid_news_author_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidCategoryId")
    public void whenCreateNewsWithInvalidCategoryId_thenReturnError(Long categoryId) throws Exception {
        NewsRequest request = new NewsRequest();
        request.setContent("content");
        request.setAuthorId(1L);
        request.setCategoryId(categoryId);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource("response/news/request/invalid_news_category_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    private static Stream<Arguments> blankContent() {
        return Stream.of(
                null,
                Arguments.of(""),
                Arguments.of(" ")
        );
    }

    private static Stream<Arguments> invalidAuthorId() {
        return Stream.of(
                Arguments.of(0L),
                Arguments.of(-10L)
        );
    }

    private static Stream<Arguments> invalidCategoryId() {
        return Stream.of(
                Arguments.of(0L),
                Arguments.of(-40L)
        );
    }
}
