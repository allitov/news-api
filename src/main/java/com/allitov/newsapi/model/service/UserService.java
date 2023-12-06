package com.allitov.newsapi.model.service;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.web.filter.UserFilter;

import java.util.List;

public interface UserService {

    User findById(Long id);

    List<User> filterBy(UserFilter filter);

    User save(User user);

    User update(User user);

    void deleteById(Long id);
}
