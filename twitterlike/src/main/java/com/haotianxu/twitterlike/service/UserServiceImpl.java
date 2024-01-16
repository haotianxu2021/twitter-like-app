package com.haotianxu.twitterlike.service;

import com.haotianxu.twitterlike.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    @Autowired
    public UserServiceImpl(UserDAO theDAO) {
        userDAO = theDAO;
    }
    @Override
    public boolean login(String name, String pwd) {
        return userDAO.login(name, pwd);
    }
}
