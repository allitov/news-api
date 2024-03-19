package com.allitov.newsapi.integration;

import com.allitov.newsapi.model.repository.CommentRepository;
import com.allitov.newsapi.web.dto.request.comment.CommentRequest;
import com.allitov.newsapi.web.dto.request.user.UserRequest;
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
public class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    private static final String USER_DETAILS_SERVICE_BEAN_NAME = "userDetailsServiceImpl";

    @Test
    @DisplayName("Test filterBy() status 200")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenCommentFilterAndRoleAdmin_whenFilterBy_thenUserListResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/filter?newsId=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'comments': []}"));
    }

    @Test
    @DisplayName("Test filterBy() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidCommentFilterAndRoleAdmin_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/filter"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test filterBy() status 401")
    public void givenCommentFilterAndAnonymousUser_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/filter?newsId=1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test filterBy() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenCommentFilterAndInvalidRole_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/filter?newsId=1"))
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
    public void givenIdAndRoleUser_whenFindById_thenUserResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{" +
                        "'id': 1, " +
                        "'content': 'Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat. Vestibulum sed magna at nunc commodo placerat.', " +
                        "'authorId': 3, " +
                        "'newsId': 4, " +
                        "'creationDate': '2023-05-29T21:54:09Z', " +
                        "'lastUpdate': '2022-11-30T15:59:01Z'" +
                        "}"));
    }

    @Test
    @DisplayName("Test findById() status 401")
    public void givenIdAndAnonymousUser_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test findById() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenIdAndInvalidRole_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/comment/1"))
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
        mockMvc.perform(get("/api/v2/comment/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"Comment with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test create() status 201")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenCommentRequestAndRoleAdmin_whenCreate_thenVoid() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        CommentRequest request = createCommentRequest();
        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/comment/6"));

        assertEquals(6, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidCommentRequestAndRoleAdmin_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        CommentRequest request = createCommentRequest();
        request.setNewsId(null);
        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(5, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 401")
    public void givenCommentRequestAndAnonymousUser_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        CommentRequest request = createCommentRequest();
        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));

        assertEquals(5, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 403")
    @WithMockUser(username = "user", authorities = {"INVALID_AUTHORITY"})
    public void givenCommentRequestAndInvalidRole_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        CommentRequest request = createCommentRequest();
        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'No required authorities'}"));

        assertEquals(5, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidCommentRequestNewsIdAndRoleAdmin_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        CommentRequest request = createCommentRequest();
        request.setNewsId(10L);
        mockMvc.perform(post("/api/v2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"News with id = '10' not found\"}"));

        assertEquals(5, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test updateById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Emmey Crossland",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndCommentRequestAndRoleUser_whenUpdateById_thenVoid() throws Exception {
        CommentRequest request = createCommentRequest();
        mockMvc.perform(put("/api/v2/comment/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        assertEquals(request.getContent(), commentRepository.findById(3L).get().getContent());
    }

    @Test
    @DisplayName("Test updateById() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndInvalidCommentRequestAndRoleUser_whenUpdateById_thenErrorResponse() throws Exception {
        CommentRequest request = createCommentRequest();
        request.setContent(null);
        mockMvc.perform(put("/api/v2/comment/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertNotEquals(request.getContent(), commentRepository.findById(5L).get().getContent());
    }

    @Test
    @DisplayName("Test findById() status 401")
    public void givenIdAndCommentRequestAndAnonymousUser_whenUpdateById_thenErrorResponse() throws Exception {
        CommentRequest request = createCommentRequest();
        mockMvc.perform(put("/api/v2/comment/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test findById() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndCommentRequestAndDifferentUser_whenUpdateById_thenErrorResponse() throws Exception {
        CommentRequest request = createCommentRequest();
        mockMvc.perform(put("/api/v2/comment/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User with id = '5' cannot get or change data of comment with id = '5'\"}"));
    }

    @Test
    @DisplayName("Test updateById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndCommentRequestAndRoleAdmin_whenFindById_thenErrorResponse() throws Exception {
        CommentRequest request = createCommentRequest();
        mockMvc.perform(put("/api/v2/comment/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"Comment with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Emmey Crossland",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndRoleUser_whenDeleteById_thenVoid() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/comment/3"))
                .andExpect(status().isNoContent());

        assertEquals(4, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 401")
    public void givenIdAndAnonymousUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/comment/5"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));

        assertEquals(5, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndDifferentUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/comment/5"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User with id = '5' cannot get or change data of comment with id = '5'\"}"));

        assertEquals(5, commentRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndRoleUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, commentRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/comment/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"Comment with id = '10' not found\"}"));

        assertEquals(5, commentRepository.findAll().size());
    }

    private CommentRequest createCommentRequest() {
        return CommentRequest.builder()
                .newsId(1L)
                .content("content")
                .build();
    }
}
