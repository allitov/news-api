package com.allitov.newsapi.web.controller.v2;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.web.dto.request.user.UserRequest;
import com.allitov.newsapi.web.dto.response.user.UserListResponse;
import com.allitov.newsapi.web.dto.response.user.UserResponse;
import com.allitov.newsapi.web.filter.UserFilter;
import com.allitov.newsapi.web.mapper.UserMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserService userService;

    // Methods tests

    @Test
    @DisplayName("Test filterBy() status 200")
    public void givenUserFilter_whenFilterBy_thenUserListResponse() throws Exception {
        UserFilter filter = new UserFilter();
        filter.setPageSize(1);
        filter.setPageNumber(0);
        List<User> foundUsers = Collections.emptyList();
        UserListResponse response = new UserListResponse();

        Mockito.when(userService.filterBy(filter))
                .thenReturn(foundUsers);
        Mockito.when(userMapper.userListToUserResponseList(foundUsers))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/user/filter?pageSize=1&pageNumber=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'users': []}"));

        Mockito.verify(userService, Mockito.times(1))
                .filterBy(filter);
        Mockito.verify(userMapper, Mockito.times(1))
                .userListToUserResponseList(foundUsers);
    }

    @Test
    @DisplayName("Test findById() status 200")
    public void givenId_whenFindById_thenUserResponse() throws Exception {
        Long id = 1L;
        User user = new User();
        UserResponse response = createUserResponse();

        Mockito.when(userService.findById(id))
                .thenReturn(user);
        Mockito.when(userMapper.userToResponse(user))
                .thenReturn(response);

        mockMvc.perform(get("/api/v2/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{'id': 1, " +
                                "'username': 'username', " +
                                "'email': 'email@email.com', " +
                                "'regDate': '1970-01-01T00:00:00Z'}")
                );

        Mockito.verify(userService, Mockito.times(1))
                .findById(id);
        Mockito.verify(userMapper, Mockito.times(1))
                .userToResponse(user);
    }

    @Test
    @DisplayName("Test findById() with nonexistent id")
    public void givenNonexistentId_whenFindById_thenErrorResponse() throws Exception {
        Long id = 1L;

        Mockito.when(userService.findById(id))
                .thenThrow(new EntityNotFoundException(String.format("User with id = '%d' not found", id)));

        mockMvc.perform(get("/api/v2/user/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format("{'errorMessage': \"User with id = '%d' not found\"}", id)));

        Mockito.verify(userService, Mockito.times(1))
                .findById(id);
    }

    @Test
    @DisplayName("Test create() status 201")
    public void givenUserRequest_whenCreate_thenVoid() throws Exception {
        UserRequest request = createUserRequest();
        Long id = 1L;
        User user = new User();
        user.setId(id);

        Mockito.when(userMapper.requestToUser(request))
                .thenReturn(user);
        Mockito.when(userService.createNewAccount(user))
                .thenReturn(user);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v2/user/" + user.getId()));

        Mockito.verify(userMapper, Mockito.times(1))
                .requestToUser(request);
        Mockito.verify(userService, Mockito.times(1))
                .createNewAccount(user);
    }

    @Test
    @DisplayName("Test updateById() status 204")
    public void givenIdAndUserRequest_whenUpdateById_thenVoid() throws Exception {
        Long id = 1L;
        UserRequest request = createUserRequest();
        User user = new User();
        user.setId(id);

        Mockito.when(userMapper.requestToUser(id, request))
                .thenReturn(user);
        Mockito.when(userService.update(user))
                .thenReturn(user);

        mockMvc.perform(put("/api/v2/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(userMapper, Mockito.times(1))
                .requestToUser(id, request);
        Mockito.verify(userService, Mockito.times(1))
                .update(user);
    }

    @Test
    @DisplayName("Test updateById() with nonexistent id")
    public void givenNonexistentIdAndUserRequest_whenUpdateById_thenErrorResponse() throws Exception {
        Long id = 1L;
        UserRequest request = createUserRequest();
        User user = new User();
        user.setId(id);

        Mockito.when(userMapper.requestToUser(id, request))
                .thenReturn(user);
        Mockito.when(userService.update(user))
                .thenThrow(new EntityNotFoundException(String.format("User with id = '%d' not found", id)));

        mockMvc.perform(put("/api/v2/user/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format("{'errorMessage': \"User with id = '%d' not found\"}", id)));

        Mockito.verify(userMapper, Mockito.times(1))
                .requestToUser(id, request);
        Mockito.verify(userService, Mockito.times(1))
                .update(user);
    }

    @Test
    @DisplayName("Test deleteById() status 204")
    public void givenId_whenDeleteById_thenVoid() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v2/user/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(userService, Mockito.times(1))
                .deleteById(id);
    }

    // Validation tests

    @Test
    @DisplayName("Test UserFilter validation with null filter page size")
    public void givenNullUserFilterPageSize_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/filter?pageNumber={pageNumber}", 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page size must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidUserFilterPageSize")
    @DisplayName("Test UserFilter validation with invalid filter page size")
    public void givenInvalidUserFilterPageSize_whenFilterBy_thenErrorResponse(UserFilter filter) throws Exception {
        mockMvc.perform(get("/api/v2/user/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                        filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page size must be > 0'}"));
    }

    @Test
    @DisplayName("Test UserFilter validation with null filter page number")
    public void givenNullUserFilterPageNumber_whenFilterBy_thenErrorResponse() throws Exception {
        mockMvc.perform(get("/api/v2/user/filter?pageSize={pageSize}", 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page number must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidUserFilterPageNumber")
    @DisplayName("Test UserFilter validation with invalid filter page number")
    public void givenInvalidUserFilterPageNumber_whenFilterBy_thenErrorResponse(UserFilter filter) throws Exception {
        mockMvc.perform(get("/api/v2/user/filter?pageSize={pageSize}&pageNumber={pageNumber}",
                        filter.getPageSize(), filter.getPageNumber()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Page number must be >= 0'}"));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    @DisplayName("Test UserRequest validation with blank username")
    public void givenBlankUserRequestUsername_whenCreate_thenErrorResponse(String username) throws Exception {
        UserRequest request = createUserRequest();
        request.setUsername(username);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Username must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidStrings")
    @DisplayName("Test UserRequest validation with invalid username")
    public void givenInvalidUserRequestUsername_whenCreate_thenErrorResponse(String username) throws Exception {
        UserRequest request = createUserRequest();
        request.setUsername(username);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Username length must be 3 <= length <= 50'}"));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    @DisplayName("Test UserRequest validation with blank email")
    public void givenBlankUserRequestEmail_whenCreate_thenErrorResponse(String email) throws Exception {
        UserRequest request = createUserRequest();
        request.setEmail(email);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Email must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidStrings")
    @DisplayName("Test UserRequest validation with invalid email")
    public void givenInvalidUserRequestEmail_whenCreate_thenErrorResponse(String email) throws Exception {
        UserRequest request = createUserRequest();
        request.setEmail(email);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Email length must be 3 <= length <= 256'}"));
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    @DisplayName("Test UserRequest validation with blank password")
    public void givenBlankUserRequestPassword_whenCreate_thenErrorMessage(String password) throws Exception {
        UserRequest request = createUserRequest();
        request.setPassword(password);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Password must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidStrings")
    @DisplayName("Test UserRequest validation with invalid password")
    public void givenInvalidUserRequestPassword_whenCreate_thenErrorResponse(String password) throws Exception {
        UserRequest request = createUserRequest();
        request.setPassword(password);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Password length must be 3 <= length <= 256'}"));
    }

    @ParameterizedTest
    @MethodSource("blankUserRules")
    @DisplayName("Test UserRequest validation with blank user roles")
    public void givenBlankUserRequestRoles_whenCreate_thenErrorResponse(Set<String> roles) throws Exception {
        UserRequest request = createUserRequest();
        request.setRoles(roles);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': 'Roles must be specified'}"));
    }

    @ParameterizedTest
    @MethodSource("invalidUserRoles")
    @DisplayName("Test UserRequest validation with blank user roles")
    public void givenInvalidUserRequestRoles_whenCreate_thenErrorResponse(Set<String> roles) throws Exception {
        UserRequest request = createUserRequest();
        request.setRoles(roles);

        mockMvc.perform(post("/api/v2/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'errorMessage': " +
                        "\"User roles must be any of ['USER', 'MODERATOR', 'ADMIN']\"}"));
    }

    private static Stream<Arguments> invalidUserFilterPageSize() {
        return Stream.of(
                Arguments.of(new UserFilter(0, 1)),
                Arguments.of(new UserFilter(-1, 1))
        );
    }

    private static Stream<Arguments> invalidUserFilterPageNumber() {
        return Stream.of(
                Arguments.of(new UserFilter(1, -1)),
                Arguments.of(new UserFilter(1, -1000))
        );
    }

    private static Stream<Arguments> blankStrings() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("\n\n "),
                Arguments.of("    ")
        );
    }

    private static Stream<Arguments> invalidStrings() {
        return Stream.of(
                Arguments.of("a"),
                Arguments.of("ab"),
                Arguments.of(RandomString.make(257))
        );
    }

    private static Stream<Arguments> blankUserRules() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(Collections.emptySet())
        );
    }

    private static Stream<Arguments> invalidUserRoles() {
        return Stream.of(
                Arguments.of(Set.of("admin")),
                Arguments.of(Set.of("ADMIN", "BEST ADMIN"))
        );
    }

    private UserResponse createUserResponse() {
        return UserResponse.builder()
                .id(1L)
                .username("username")
                .email("email@email.com")
                .regDate(Instant.parse("1970-01-01T00:00:00Z"))
                .build();
    }

    private UserRequest createUserRequest() {
        return UserRequest.builder()
                .username("username")
                .email("email@email.com")
                .password("password")
                .roles(Set.of("ADMIN", "MODERATOR", "USER"))
                .build();
    }
}
