package com.allitov.newsapi.model.service.impl;

import com.allitov.newsapi.model.data.User;
import com.allitov.newsapi.model.repository.UserRepository;
import com.allitov.newsapi.model.service.UserService;
import com.allitov.newsapi.util.BeanUtils;
import com.allitov.newsapi.web.filter.UserFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(MessageFormat.format(
                        "User with id {0} not found", id))
        );
    }

    @Override
    public List<User> filterBy(UserFilter filter) {
        return userRepository
                .findAll(PageRequest.of(filter.getPageNumber(), filter.getPageSize()))
                .getContent();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User foundUser = findById(user.getId());
        BeanUtils.copyNonNullProperties(user, foundUser);

        return userRepository.save(foundUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User createNewAccount(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public User findUserByName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("User with username {0} not found", username))
                );
    }
}
