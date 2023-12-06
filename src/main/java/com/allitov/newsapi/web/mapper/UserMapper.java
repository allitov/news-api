package com.allitov.newsapi.web.mapper;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.web.dto.request.user.UserRequest;
import com.allitov.newsapi.web.dto.response.user.UserListResponse;
import com.allitov.newsapi.web.dto.response.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "userName", target = "name")
    User requestToUser(UserRequest request);

    default User requestToUser(Long userId, UserRequest request) {
        User user = requestToUser(request);
        user.setId(userId);

        return user;
    }

    @Mapping(source = "registrationDate", target = "regDate")
    UserResponse userToResponse(User user);

    List<UserResponse> userListToResponseList(List<User> users);

    default UserListResponse userListToUserResponseList(List<User> users) {
        UserListResponse response = new UserListResponse();
        response.setUsers(userListToResponseList(users));

        return response;
    }
}
