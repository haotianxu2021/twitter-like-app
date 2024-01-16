package com.haotianxu.twitterlike.dao;

import com.haotianxu.twitterlike.entity.Auth;
import com.haotianxu.twitterlike.entity.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Auth, RoleId> {
    List<Auth> findAuthsByUsername(String username);
    List<Auth> findAuthsByAuthority(String authority);
}
