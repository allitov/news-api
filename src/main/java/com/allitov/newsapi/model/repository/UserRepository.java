package com.allitov.newsapi.model.repository;

import com.allitov.newsapi.model.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
