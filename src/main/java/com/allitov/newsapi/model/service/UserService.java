package com.allitov.newsapi.model.service;

import com.allitov.newsapi.model.data.RoleType;
import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.web.filter.UserFilter;

import java.util.List;
import java.util.Set;

public interface UserService {

    User findById(Long id);

    List<User> filterBy(UserFilter filter);

    User save(User user);

    User update(User user);

    void deleteById(Long id);

    User createNewAccount(User user, Set<RoleType> roles);

    User findUserByName(String username);
}
