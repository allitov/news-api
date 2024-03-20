package com.allitov.newsapi.integration;

import com.allitov.newsapi.model.repository.NewsCategoryRepository;
import com.allitov.newsapi.web.dto.request.newscategory.NewsCategoryRequest;
import com.allitov.testutils.EnableTestcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EnableTestcontainers
@AutoConfigureMockMvc
@Transactional
@Sql("classpath:db/init.sql")
public class NewsCategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    private static final String USER_DETAILS_SERVICE_BEAN_NAME = "userDetailsServiceImpl";

    @Test
    @DisplayName("Test filterBy() status 200")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNewsCategoryFilterAndRoleAdmin_whenFilterBy_thenNewsCategoryListResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'newsCategories': [{'id': 1, 'name': 'CDL'}]}"));
    }

    @Test
    @DisplayName("Test filterBy() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidNewsCategoryFilterAndRoleAdmin_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test filterBy() status 401")
    public void givenNewsCategoryFilterAndAnonymousUser_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test filterBy() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenNewsCategoryFilterAndInvalidRole_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'No required authorities'}"));
    }

    @Test
    @DisplayName("Test findById() status 200")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndRoleUser_whenFindById_thenNewsCategoryResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'id': 1, 'name': 'CDL'}"));
    }

    @Test
    @DisplayName("Test findById() status 401")
    public void givenIdAndAnonymousUser_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test findById() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenIdAndInvalidRole_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'No required authorities'}"));
    }

    @Test
    @DisplayName("Test findById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndRoleAdmin_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news-category/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"News category with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test create() status 201")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNewsCategoryRequestAndRoleAdmin_whenCreate_thenVoid() throws Exception {
        assertEquals(5, newsCategoryRepository.findAll().size());

        NewsCategoryRequest request = createNewsCategoryRequest();
        mockMvc.perform(post("/api/v2/news-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/news-category/6"));

        assertEquals(6, newsCategoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidNewsCategoryRequestAndRoleAdmin_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, newsCategoryRepository.findAll().size());

        NewsCategoryRequest request = createNewsCategoryRequest();
        request.setName(null);
        mockMvc.perform(post("/api/v2/news-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(5, newsCategoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 401")
    public void givenNewsCategoryRequestAndAnonymousUser_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, newsCategoryRepository.findAll().size());

        NewsCategoryRequest request = createNewsCategoryRequest();
        mockMvc.perform(post("/api/v2/news-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));

        assertEquals(5, newsCategoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNewsCategoryRequestAndInvalidRole_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, newsCategoryRepository.findAll().size());

        NewsCategoryRequest request = createNewsCategoryRequest();
        mockMvc.perform(post("/api/v2/news-category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'No required authorities'}"));

        assertEquals(5, newsCategoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Test updateById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndNewsCategoryRequestAndRoleAdmin_whenUpdateById_thenVoid() throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        mockMvc.perform(put("/api/v2/news-category/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        assertEquals(request.getName(), newsCategoryRepository.findById(3L).get().getName());
    }

    @Test
    @DisplayName("Test updateById() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndInvalidNewsCategoryRequestAndRoleAdmin_whenUpdateById_thenErrorResponse() throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        request.setName(null);
        mockMvc.perform(put("/api/v2/news-category/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertNotEquals(request.getName(), newsCategoryRepository.findById(5L).get().getName());
    }

    @Test
    @DisplayName("Test updateById() status 401")
    public void givenIdAndNewsCategoryRequestAndAnonymousUser_whenUpdateById_thenErrorResponse() throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        mockMvc.perform(put("/api/v2/news-category/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test updateById() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndNewsCategoryRequestAndInvalidRole_whenUpdateById_thenErrorResponse() throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        mockMvc.perform(put("/api/v2/news-category/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'No required authorities'}"));
    }

    @Test
    @DisplayName("Test updateById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndNewsCategoryRequestAndRoleAdmin_whenFindById_thenErrorResponse() throws Exception {
        NewsCategoryRequest request = createNewsCategoryRequest();
        mockMvc.perform(put("/api/v2/news-category/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"News category with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndRoleAdmin_whenDeleteById_thenVoid() throws Exception {
        assertEquals(5, newsCategoryRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/news-category/3"))
                .andExpect(status().isNoContent());

        assertEquals(4, newsCategoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 401")
    public void givenIdAndAnonymousUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, newsCategoryRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/news-category/5"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));

        assertEquals(5, newsCategoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndInvalidRole_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, newsCategoryRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/news-category/5"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'No required authorities'}"));

        assertEquals(5, newsCategoryRepository.findAll().size());
    }

    private NewsCategoryRequest createNewsCategoryRequest() {
        return NewsCategoryRequest.builder()
                .name("category name")
                .build();
    }
}
