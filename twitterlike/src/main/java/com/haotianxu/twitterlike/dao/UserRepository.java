package com.haotianxu.twitterlike.dao;

import com.haotianxu.twitterlike.entity.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
//    Optional<User> findByUsername(String username);

}
