package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.model.service.NewsCategoryService;
import com.allitov.newsapi.web.dto.request.newscategory.NewsCategoryRequest;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryListResponse;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryResponse;
import com.allitov.newsapi.web.filter.NewsCategoryFilter;
import com.allitov.newsapi.web.mapper.NewsCategoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = NewsCategoryController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class NewsCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsCategoryService newsCategoryService;

    @MockBean
    private NewsCategoryMapper newsCategoryMapper;

    // Methods tests

    @Test
    @DisplayName("Test filterBy() status 200")
    public void givenNewsCategoryFilter_whenFilterBy_thenNewsCategoryListResponse() throws Exception {
        NewsCategoryFilter filter = new NewsCategoryFilter();
        filter.setPageNumber(1);
        filter.setPageSize(1);
        List<NewsCategory> foundNewsCategories = Collections.emptyList();
        NewsCategoryListResponse response = new NewsCategoryListResponse();

        Mockito.when(newsCategoryService.filterBy(filter))
                .thenReturn(foundNewsCategories);
        Mockito.when(newsCategoryMapper.newsCategoryListToNewsCategoryListResponse(foundNewsCategories))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/news-category/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                        filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'newsCategories': []}"));

        Mockito.verify(newsCategoryService, Mockito.times(1))
                .filterBy(filter);
        Mockito.verify(newsCategoryMapper, Mockito.times(1))
                .newsCategoryListToNewsCategoryListResponse(foundNewsCategories);
    }

    @Test
    @DisplayName("Test findById() status 200")
    public void givenId_whenFindById_thenNewsCategoryResponse() throws Exception {
        Long id = 1L;
        NewsCategory newsCategory = new NewsCategory();
        NewsCategoryResponse response = createNewsCategoryResponse();

        Mockito.when(newsCategoryService.findById(id))
                .thenReturn(newsCategory);
        Mockito.when(newsCategoryMapper.newsCategoryToResponse(newsCategory))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/news-category/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'id': 1, 'name': 'News category'}"));

        Mockito.verify(newsCategoryService, Mockito.times(1))
                .findById(id);
        Mockito.verify(newsCategoryMapper, Mockito.times(1))
                .newsCategoryToResponse(newsCategory);
    }

    @Test
    @DisplayName("Test findById() with nonexistent id")
    public void givenNonexistentId_whenFindById_thenErrorResponse() throws Exception {
        Long id = 1L;

        Mockito.when(newsCategoryService.findById(id))
                .thenThrow(new EntityNotFoundException(String.format("News category with id = '%d' not found", id)));

        mockMvc.perform(get("/api/v2/news-category/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        String.format("{'errorMessage': \"News category with id = '%d' not found\"}", id)));

        Mockito.verify(newsCategoryService, Mockito.times(1))
                .findById(id);
    }

    @Test
    @DisplayName("Test create() status 201")
    public void givenNewsCategoryRequest_whenCreate_thenVoid() throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        Long id = 1L;
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setId(id);

        Mockito.when(newsCategoryMapper.requestToNewsCategory(request))
                .thenReturn(newsCategory);
        Mockito.when(newsCategoryService.save(newsCategory))
                .thenReturn(newsCategory);

        mockMvc.perform(post("/api/v2/news-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/news-category/" + newsCategory.getId()));

        Mockito.verify(newsCategoryMapper, Mockito.times(1))
                .requestToNewsCategory(request);
        Mockito.verify(newsCategoryService, Mockito.times(1))
                .save(newsCategory);
    }

    @Test
    @DisplayName("Test updateById() status 204")
    public void givenIdAndNewsCategoryRequest_whenUpdateById_thenVoid() throws Exception {
        Long id = 1L;
        NewsCategoryRequest request = createNewsCategoryRequest();
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setId(id);

        Mockito.when(newsCategoryMapper.requestToNewsCategory(id, request))
                .thenReturn(newsCategory);
        Mockito.when(newsCategoryService.update(newsCategory))
                .thenReturn(newsCategory);

        mockMvc.perform(put("/api/v2/news-category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(newsCategoryMapper, Mockito.times(1))
                .requestToNewsCategory(id, request);
        Mockito.verify(newsCategoryService, Mockito.times(1))
                .update(newsCategory);
    }

    @Test
    @DisplayName("Test updateById() with nonexistent id")
    public void givenNonexistentIdAndNewsCategoryRequest_whenUpdateById_thenErrorResponse() throws Exception {
        Long id = 1L;
        NewsCategoryRequest request = createNewsCategoryRequest();
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setId(id);

        Mockito.when(newsCategoryMapper.requestToNewsCategory(id, request))
                .thenReturn(newsCategory);
        Mockito.when(newsCategoryService.update(newsCategory))
                .thenThrow(new EntityNotFoundException(String.format("News category with id = '%d' not found", id)));

        mockMvc.perform(put("/api/v2/news-category/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        String.format("{'errorMessage': \"News category with id = '%d' not found\"}", id)));

        Mockito.verify(newsCategoryMapper, Mockito.times(1))
                .requestToNewsCategory(id, request);
        Mockito.verify(newsCategoryService, Mockito.times(1))
                .update(newsCategory);
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    public void givenId_whenDeleteById_thenVoid() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v2/news-category/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(newsCategoryService, Mockito.times(1))
                .deleteById(id);
    }

    // Validation tests

    @Test
    @DisplayName("Test NewsCategoryFilter validation with null filter page size")
    public void givenNullNewsCategoryFilterPageSize_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter?pageNumber={pageNumber}", 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page size must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidNewsCategoryFilterPageSize")
    @DisplayName("Test NewsCategoryFilter validation with invalid filter page size")
    public void givenInvalidNewsCategoryFilterPageSize_whenFilterBy_thenErrorResponse(
            NewsCategoryFilter filter) throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page size must be > 0'}"));
    }

    @Test
    @DisplayName("Test NewsCategoryFilter validation with null filter page number")
    public void givenNullNewsCategoryFilterPageNumber_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter?pageSize={pageSize}", 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page number must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidNewsCategoryFilterPageNumber")
    @DisplayName("Test NewsCategoryFilter validation with invalid filter page number")
    public void givenInvalidNewsCategoryFilterPageNumber_whenFilterBy_thenErrorResponse(
            NewsCategoryFilter filter) throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                        filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page number must be >= 0'}"));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    @DisplayName("Test NewsCategoryRequest validation with blank name")
    public void givenBlankNewsCategoryRequestName_whenCreate_thenErrorResponse(String name) throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        request.setName(name);

        mockMvc.perform(post("/api/v2/news-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Name must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidStrings")
    @DisplayName("Test NewsCategoryRequest validation with invalid name")
    public void givenInvalidNewsCategoryRequestName_whenCreate_thenErrorResponse(String name) throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        request.setName(name);

        mockMvc.perform(post("/api/v2/news-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{'errorMessage': 'News category name length must be 1 <= length <= 50'}"));
    }

    private NewsCategoryResponse createNewsCategoryResponse() {
        return NewsCategoryResponse.builder()
                .id(1L)
                .name("News category")
                .build();
    }

    private NewsCategoryRequest createNewsCategoryRequest() {
        return new NewsCategoryRequest("News category");
    }

    private static Stream<Arguments> invalidNewsCategoryFilterPageSize() {
        return Stream.of(
                Arguments.of(new NewsCategoryFilter(0, 1)),
                Arguments.of(new NewsCategoryFilter(-1, 1))
        );
    }

    private static Stream<Arguments> invalidNewsCategoryFilterPageNumber() {
        return Stream.of(
                Arguments.of(new NewsCategoryFilter(1, -1)),
                Arguments.of(new NewsCategoryFilter(1, -1000))
        );
    }

    private static Stream<Arguments> blankStrings() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("\n\n "),
                Arguments.of("     ")
        );
    }

    private static Stream<Arguments> invalidStrings() {
        return Stream.of(
                Arguments.of(RandomString.make(51)),
                Arguments.of(RandomString.make(100))
        );
    }
}
