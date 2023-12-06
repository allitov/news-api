package com.allitov.newsapi.web.controller;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.utils.TestUtils;
import com.allitov.newsapi.web.dto.request.user.UserRequest;
import com.allitov.newsapi.web.dto.response.user.UserListResponse;
import com.allitov.newsapi.web.dto.response.user.UserResponse;
import com.allitov.newsapi.web.filter.UserFilter;
import com.allitov.newsapi.web.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
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

public class UserControllerTest extends AbstractControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    public void whenFindById_thenReturnClientById() throws Exception {
        User user = createUser(1L, "Ivan", "email@example");
        UserResponse response = createUserResponse(1L, "Ivan", "email@example");

        Mockito.when(userService.findById(1L))
                .thenReturn(user);
        Mockito.when(userMapper.userToResponse(user))
                .thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, Mockito.times(1))
                .findById(1L);
        Mockito.verify(userMapper, Mockito.times(1))
                .userToResponse(user);

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/method/find_user_by_id_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFilterBy_thenReturnClients() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(createUser(1L, "Nadya", "nadya@email"));
        users.add(createUser(2L, "Ivan", "ivan@email"));
        List<UserResponse> responses = new ArrayList<>();
        responses.add(createUserResponse(1L, "Nadya", "nadya@email"));
        responses.add(createUserResponse(2L, "Ivan", "ivan@email"));
        UserListResponse response = new UserListResponse();
        response.setUsers(responses);
        UserFilter filter = new UserFilter();
        filter.setPageNumber(0);
        filter.setPageSize(10);

        Mockito.when(userService.filterBy(filter))
                .thenReturn(users);
        Mockito.when(userMapper.userListToUserResponseList(users))
                .thenReturn(response);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/filter?pageNumber=0&pageSize=10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, Mockito.times(1))
                .filterBy(filter);
        Mockito.verify(userMapper, Mockito.times(1))
                .userListToUserResponseList(users);

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/method/filter_by_users_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateUser_thenReturnNewUser() throws Exception {
        User user = createUser(1L, "New User", "user@email");
        User userFromRequest = createUser(null, "New User", "user@email");
        UserResponse response = createUserResponse(1L, "New User", "user@email");
        UserRequest request = new UserRequest();
        request.setUserName("New User");
        request.setEmail("user@email");

        Mockito.when(userMapper.requestToUser(request))
                .thenReturn(userFromRequest);
        Mockito.when(userMapper.userToResponse(user))
                .thenReturn(response);
        Mockito.when(userService.save(userFromRequest))
                .thenReturn(user);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userMapper, Mockito.times(1))
                .requestToUser(request);
        Mockito.verify(userMapper, Mockito.times(1))
                .userToResponse(user);
        Mockito.verify(userService, Mockito.times(1))
                .save(userFromRequest);

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/method/create_user_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        User user = createUser(1L, "Updated User", "user@email");
        User userFromRequest = createUser(null, "Updated User", "user@emial");
        UserResponse response = createUserResponse(1L, "Updated User", "user@email");
        UserRequest request = new UserRequest();
        request.setUserName("Updated User");
        request.setEmail("user@email");

        Mockito.when(userMapper.requestToUser(1L, request))
                .thenReturn(userFromRequest);
        Mockito.when(userMapper.userToResponse(user))
                .thenReturn(response);
        Mockito.when(userService.update(userFromRequest))
                .thenReturn(user);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userMapper, Mockito.times(1))
                .requestToUser(1L, request);
        Mockito.verify(userMapper, Mockito.times(1))
                .userToResponse(user);
        Mockito.verify(userService, Mockito.times(1))
                .update(userFromRequest);

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/method/update_user_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenDeleteUserById_thenReturnStatusNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/user/500"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(userService, Mockito.times(1))
                .deleteById(500L);
    }

    @Test
    public void whenFindByIdNotExistedUser_thenReturnError() throws Exception {
        Mockito.when(userService.findById(500L))
                .thenThrow(new EntityNotFoundException("User with id 500 not found"));

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/500"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(userService, Mockito.times(1))
                .findById(500L);

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/method/user_by_id_not_found_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("blankEmail")
    public void whenCreateUserWithBlankEmail_thenReturnError(String email) throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName("name");
        request.setEmail(email);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/request/blank_user_email_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("blankUserName")
    public void whenCreateUserWithBlankUserName_thenReturnError(String userName) throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName(userName);
        request.setEmail("email@email");

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/request/blank_username_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidUserNameSize")
    public void whenCreateUserWithInvalidUserNameSize_thenReturnError(String userName) throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName(userName);
        request.setEmail("email@email");

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/request/invalid_username_size_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidEmailSize")
    public void whenCreateUserWithInvalidEmailSize_then_ReturnError(String email) throws Exception {
        UserRequest request = new UserRequest();
        request.setUserName("User");
        request.setEmail(email);

        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/request/invalid_user_email_size_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFilterWithNullPageSize_thenReturnError() throws Exception {
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/filter?pageNumber=10"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/filter/null_user_filter_page_size_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenFilterWithNullPageNumber_thenReturnError() throws Exception {
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/user/filter?pageSize=10"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/filter/null_user_filter_page_number_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidPageSize")
    public void whenFilterWithInvalidPageSize_thenReturnError(Integer pageSize) throws Exception {
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/user/filter?pageNumber=10&pageSize=%d", pageSize)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/filter/invalid_user_filter_page_size_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidPageNumber")
    public void whenFilterByInvalidPageNumber_thenReturnError(Integer pageNumber) throws Exception {
        String actualResponse = mockMvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/user/filter?pageSize=10&pageNumber=%d", pageNumber)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = TestUtils.readStringFromResource(
                "response/user/filter/invalid_user_filter_page_number_response.json");

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    private static Stream<Arguments> invalidUserNameSize() {
        return Stream.of(
                Arguments.of(RandomString.make(2)),
                Arguments.of(RandomString.make(51))
        );
    }

    private static Stream<Arguments> invalidEmailSize() {
        return Stream.of(
                Arguments.of(RandomString.make(2)),
                Arguments.of(RandomString.make(256))
        );
    }

    private static Stream<Arguments> invalidPageSize() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(0)
        );
    }

    private static Stream<Arguments> invalidPageNumber() {
        return Stream.of(
                Arguments.of(-1),
                Arguments.of(-10000)
        );
    }

    private static Stream<Arguments> blankEmail() {
        return Stream.of(
                null,
                Arguments.of("   "),
                Arguments.of("      ")
        );
    }

    private static Stream<Arguments> blankUserName() {
        return Stream.of(
                null,
                Arguments.of("    "),
                Arguments.of("\n   ")
        );
    }
}
