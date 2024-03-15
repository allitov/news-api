package com.allitov.newsapi.integration;

import com.allitov.newsapi.model.repository.UserRepository;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@EnableTestcontainers
@AutoConfigureMockMvc
@Transactional
@Sql("classpath:db/init.sql")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static final String USER_DETAILS_SERVICE_BEAN_NAME = "userDetailsServiceImpl";

    @Test
    @DisplayName("Test filterBy() status 200")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenUserFilterAndRoleAdmin_whenFilterBy_thenUserListResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{'users': [" +
                                "{'id': 1, " +
                                "'username': 'Garek Simper', " +
                                "'email': 'gsimper0@dropbox.com', " +
                                "'regDate': '2023-06-30T15:03:20Z'}" +
                                "]}"));
    }

    @Test
    @DisplayName("Test filterBy() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenInvalidUserFilterAndRoleAdmin_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/filter"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test filterBy() status 401")
    public void givenUserFilterAndAnonymousUser_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));
    }

    @Test
    @DisplayName("Test filterBy() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenUserFilterAndRoleUser_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/filter?pageSize=1&pageNumber=0"))
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
        mockMvc.perform(get("/api/v2/user/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{'id': 5, " +
                                "'username': 'Fina Sugden', " +
                                "'email': 'fsugden4@nymag.com', " +
                                "'regDate': '2023-04-14T06:02:56Z'}"));
    }

    @Test
    @DisplayName("Test findById() status 401")
    public void givenIdAndAnonymousUser_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/1"))
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
    public void givenIdAndDifferentUser_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User with id = '5' cannot get or change data of user with id = '1'\"}"));
    }

    @Test
    @DisplayName("Test findById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndRoleAdmin_whenFindById_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"User with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test create() status 201")
    public void givenUserRequest_whenCreate_thenVoid() throws Exception {
        assertEquals(5, userRepository.findAll().size());

        UserRequest request = createUserRequest();
        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/user/6"));

        assertEquals(6, userRepository.findAll().size());
    }

    @Test
    @DisplayName("Test create() status 400")
    public void givenInvalidUserRequest_whenCreate_thenErrorResponse() throws Exception {
        assertEquals(5, userRepository.findAll().size());

        UserRequest request = createUserRequest();
        request.setRoles(null);
        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(5, userRepository.findAll().size());
    }

    @Test
    @DisplayName("Test updateById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndUserRequestAndRoleUser_whenUpdateById_thenVoid() throws Exception {
        UserRequest request = createUserRequest();
        mockMvc.perform(put("/api/v2/user/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        assertEquals(request.getUsername(), userRepository.findById(5L).get().getUsername());
    }

    @Test
    @DisplayName("Test updateById() status 400")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndInvalidUserRequestAndRoleUser_whenUpdateById_thenErrorResponse() throws Exception {
        UserRequest request = createUserRequest();
        request.setUsername(" ");
        mockMvc.perform(put("/api/v2/user/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertNotEquals(request.getUsername(), userRepository.findById(5L).get().getUsername());
    }

    @Test
    @DisplayName("Test findById() status 401")
    public void givenIdAndUserRequestAndAnonymousUser_whenUpdateById_thenErrorResponse() throws Exception {
        UserRequest request = createUserRequest();
        mockMvc.perform(put("/api/v2/user/5")
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
    public void givenIdAndUserRequestAndDifferentUser_whenUpdateById_thenErrorResponse() throws Exception {
        UserRequest request = createUserRequest();
        mockMvc.perform(put("/api/v2/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User with id = '5' cannot get or change data of user with id = '1'\"}"));
    }

    @Test
    @DisplayName("Test updateById() status 404")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Garek Simper",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenNonexistentIdAndUserRequestAndRoleAdmin_whenFindById_thenErrorResponse() throws Exception {
        UserRequest request = createUserRequest();
        mockMvc.perform(put("/api/v2/user/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': \"User with id = '10' not found\"}"));
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndROleUser_whenDeleteById_thenVoid() throws Exception {
        assertEquals(5, userRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/user/5"))
                .andExpect(status().isNoContent());

        assertEquals(4, userRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 401")
    public void givenIdAndAnonymousUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, userRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/user/5"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Authentication failure'}"));

        assertEquals(5, userRepository.findAll().size());
    }

    @Test
    @DisplayName("Test deleteById() status 403")
    @WithUserDetails(
            userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME,
            value = "Fina Sugden",
            setupBefore = TestExecutionEvent.TEST_METHOD
    )
    public void givenIdAndDifferentUser_whenDeleteById_thenErrorResponse() throws Exception {
        assertEquals(5, userRepository.findAll().size());

        mockMvc.perform(delete("/api/v2/user/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User with id = '5' cannot get or change data of user with id = '1'\"}"));

        assertEquals(5, userRepository.findAll().size());
    }

    private UserRequest createUserRequest() {
        return UserRequest.builder()
                .username("Username")
                .email("email@email.com")
                .password("password")
                .roles(Set.of("USER", "MODERATOR"))
                .build();
    }
}
