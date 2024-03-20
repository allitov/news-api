package com.allitov.newsapi.integration;

import com.allitov.newsapi.model.repository.NewsRepository;
import com.allitov.newsapi.web.dto.request.news.NewsRequest;
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
public class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NewsRepository newsRepository;

    private static final String USER_DETAILS_SERVICE_BEAN_NAME = "userDetailsServiceImpl";

    @Test
    @DisplayName("Test filterBy() status 200")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNewsFilterAndRoleAdmin_whenFilterBy_thenNewsListResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'news': [" +
                        "{'id': 1, " +
                        "'content': 'Sed sagittis. Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci. Nullam molestie nibh in lectus. Pellentesque at nulla. Suspendisse potenti. Cras in purus eu magna vulputate luctus.', " +
                        "'authorId': 4, " +
                        "'categoryId': 2, " +
                        "'creationDate': '2023-10-03T03:52:13Z', " +
                        "'lastUpdate': '2023-04-10T11:34:31Z', " +
                        "'commentsCount': 2}" +
                        "]}"));
    }

    @Test
    @DisplayName("Test filterBy() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidNewsFilterAndRoleAdmin_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/filter"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test filterBy() status 401")
    public void givenNewsFilterAndAnonymousUser_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test filterBy() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenNewsFilterAndInvalidRole_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/filter?pageSize=1&pageNumber=0"))
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
    public void givenIdAndRoleUser_whenFindById_thenNewsResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{" +
                        "'id': 3, " +
                        "'content': 'Fusce consequat. Nulla nisl. Nunc nisl. Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa. Donec dapibus. Duis at velit eu est congue elementum. In hac habitasse platea dictumst. Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.', " +
                        "'authorId': 3, " +
                        "'categoryId': 2, " +
                        "'creationDate': '2023-02-08T04:35:43Z', " +
                        "'lastUpdate': '2023-06-07T19:19:06Z', " +
                        "'comments': []" +
                        "}"));
    }

    @Test
    @DisplayName("Test findById() status 401")
    public void givenIdAndAnonymousUser_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test findById() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenIdAndInvalidRole_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/news/1"))
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
        mockMvc.perform(get("/api/v2/news/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"News with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test create() status 201")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNewsRequestAndRoleAdmin_whenCreate_thenVoid() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        NewsRequest request = createNewsRequest();
        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/news/6"));

        assertEquals(6, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidNewsRequestAndRoleAdmin_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        NewsRequest request = createNewsRequest();
        request.setContent(null);
        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(5, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 401")
    public void givenNewsRequestAndAnonymousUser_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        NewsRequest request = createNewsRequest();
        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));

        assertEquals(5, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenCommentRequestAndDifferentUser_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        NewsRequest request = createNewsRequest();
        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'No required authorities'}"));

        assertEquals(5, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidNewsRequestNewsCategoryIdAndRoleAdmin_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        NewsRequest request = createNewsRequest();
        request.setCategoryId(10L);
        mockMvc.perform(post("/api/v2/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"News category with id = '10' not found\"}"));

        assertEquals(5, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test updateById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Emmey Crossland",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndNewsRequestAndRoleUser_whenUpdateById_thenVoid() throws Exception {
        NewsRequest request = createNewsRequest();
        mockMvc.perform(put("/api/v2/news/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        assertEquals(request.getContent(), newsRepository.findById(3L).get().getContent());
    }

    @Test
    @DisplayName("Test updateById() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Emmey Crossland",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndInvalidNewsRequestAndRoleUser_whenUpdateById_thenErrorResponse() throws Exception {
        NewsRequest request = createNewsRequest();
        request.setContent(null);
        mockMvc.perform(put("/api/v2/news/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertNotEquals(request.getContent(), newsRepository.findById(5L).get().getContent());
    }

    @Test
    @DisplayName("Test updateById() status 401")
    public void givenIdAndNewsRequestAndAnonymousUser_whenUpdateById_thenErrorResponse() throws Exception {
        NewsRequest request = createNewsRequest();
        mockMvc.perform(put("/api/v2/news/5")
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
    public void givenIdAndNewsRequestAndDifferentUser_whenUpdateById_thenErrorResponse() throws Exception {
        NewsRequest request = createNewsRequest();
        mockMvc.perform(put("/api/v2/news/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User with id = '5' cannot get or change data of news with id = '3'\"}"));
    }

    @Test
    @DisplayName("Test updateById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndNewsRequestAndRoleAdmin_whenFindById_thenErrorResponse() throws Exception {
        NewsRequest request = createNewsRequest();
        mockMvc.perform(put("/api/v2/news/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"News with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndRoleAdmin_whenDeleteById_thenVoid() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/news/3"))
                .andExpect(status().isNoContent());

        assertEquals(4, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 401")
    public void givenIdAndAnonymousUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/news/5"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));

        assertEquals(5, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndDifferentUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/news/3"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User with id = '5' cannot get or change data of news with id = '3'\"}"));

        assertEquals(5, newsRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndRoleUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, newsRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/news/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"News with id = '10' not found\"}"));

        assertEquals(5, newsRepository.findAll().size());
    }

    private NewsRequest createNewsRequest() {
        return NewsRequest.builder()
                .content("news content")
                .categoryId(1L)
                .build();
    }
}
