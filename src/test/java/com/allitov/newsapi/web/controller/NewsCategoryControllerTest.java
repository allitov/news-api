package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.NewsCategory;
import com.allitov.newsapi.model.service.NewsCategoryService;
import com.allitov.newsapi.utils.TestUtils;
import com.allitov.newsapi.web.dto.request.newscategory.NewsCategoryRequest;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryListResponse;
import com.allitov.newsapi.web.dto.response.newscategory.NewsCategoryResponse;
import com.allitov.newsapi.web.filter.NewsCategoryFilter;
import com.allitov.newsapi.web.mapper.NewsCategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

public class NewsCategoryControllerTest extends AbstractControllerTest {

    @MockBean
    private NewsCategoryService categoryService;

    @MockBean
    private NewsCategoryMapper categoryMapper;

    @Test
    public void whenFindById_thenReturnNewsCategoryById() throws Exception {
        NewsCategory category = createNewsCategory(1L, "Category");
        NewsCategoryResponse response = createNewsCategoryResponse(1L, "Category");

        Mockito.when(categoryService.findById(1L)).thenReturn(category);
        Mockito.when(categoryMapper.newsCategoryToResponse(category)).thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news_category/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(categoryService, Mockito.times(1)).findById(1L);
        Mockito.verify(categoryMapper, Mockito.times(1)).newsCategoryToResponse(category);

        String expectedResponse = TestUtils.readStringFromResource("response/newscategory/method/find_news_category_by_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFilterBy_thenReturnNewsCategories() throws Exception {
        List<NewsCategory> categories = new ArrayList<>();
        categories.add(createNewsCategory(1L, "Category1"));
        categories.add(createNewsCategory(2L, "Category2"));
        List<NewsCategoryResponse> responses = new ArrayList<>();
        responses.add(createNewsCategoryResponse(1L, "Category1"));
        responses.add(createNewsCategoryResponse(2L, "Category2"));
        NewsCategoryListResponse response = new NewsCategoryListResponse();
        response.setNewsCategories(responses);
        NewsCategoryFilter filter = new NewsCategoryFilter();
        filter.setPageNumber(0);
        filter.setPageSize(10);

        Mockito.when(categoryService.filterBy(filter)).thenReturn(categories);
        Mockito.when(categoryMapper.newsCategoryListToNewsCategoryListResponse(categories)).thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news_category/filter?pageNumber=0&pageSize=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(categoryService, Mockito.times(1)).filterBy(filter);
        Mockito.verify(categoryMapper, Mockito.times(1)).newsCategoryListToNewsCategoryListResponse(categories);

        String expectedResponse = TestUtils.readStringFromResource("response/newscategory/method/filter_by_news_category_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreate_thenReturnNewNewsCategory() throws Exception {
        NewsCategory category = createNewsCategory(1L, "Created Category");
        NewsCategoryResponse response = createNewsCategoryResponse(1L, "Created Category");
        NewsCategoryRequest request = new NewsCategoryRequest();
        request.setName("Created Category");

        Mockito.when(categoryMapper.requestToNewsCategory(request)).thenReturn(category);
        Mockito.when(categoryMapper.newsCategoryToResponse(category)).thenReturn(response);
        Mockito.when(categoryService.save(category)).thenReturn(category);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/news_category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(categoryMapper, Mockito.times(1)).requestToNewsCategory(request);
        Mockito.verify(categoryMapper, Mockito.times(1)).newsCategoryToResponse(category);
        Mockito.verify(categoryService, Mockito.times(1)).save(category);

        String expectedResponse = TestUtils.readStringFromResource("response/newscategory/method/create_news_category_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdate_thenReturnUpdatedNewsCategory() throws Exception {
        NewsCategory category = createNewsCategory(1L, "Updated Category");
        NewsCategoryResponse response = createNewsCategoryResponse(1L, "Updated Category");
        NewsCategoryRequest request = new NewsCategoryRequest();
        request.setName("Updated Category");

        Mockito.when(categoryMapper.requestToNewsCategory(1L, request)).thenReturn(category);
        Mockito.when(categoryMapper.newsCategoryToResponse(category)).thenReturn(response);
        Mockito.when(categoryService.update(category)).thenReturn(category);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/news_category/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(categoryMapper, Mockito.times(1)).requestToNewsCategory(1L, request);
        Mockito.verify(categoryMapper, Mockito.times(1)).newsCategoryToResponse(category);
        Mockito.verify(categoryService, Mockito.times(1)).update(category);

        String expectedResponse = TestUtils.readStringFromResource("response/newscategory/method/update_news_category_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/news_category/500"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(categoryService, Mockito.times(1)).deleteById(500L);
    }

    @Test
    public void whenFindByIdNotExistedNewsCategory_thenReturnError() throws Exception {
        Mockito.when(categoryService.findById(500L)).thenThrow(new EntityNotFoundException("News category with id 500 not found"));

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/news_category/500"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(categoryService, Mockito.times(1)).findById(500L);

        String expectedResponse = TestUtils.readStringFromResource("response/newscategory/method/news_category_by_id_not_found_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
