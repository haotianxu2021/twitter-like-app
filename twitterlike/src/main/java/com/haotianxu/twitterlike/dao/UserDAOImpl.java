package com.haotianxu.twitterlike.dao;

import com.haotianxu.twitterlike.entity.User;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class UserDAOImpl implements UserDAO {
    private EntityManager entityManager;
    @Autowired
    public UserDAOImpl(EntityManager theManager) {
        entityManager = theManager;
    }
    @Override
    public boolean login(String name, String pwd) {
        User theUser = entityManager.find(User.class, name);
        return theUser != null && Objects.equals(theUser.getPassword(), pwd);
    }
}
