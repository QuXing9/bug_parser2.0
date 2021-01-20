package com.ty.bugparser.service;

import com.ty.bugparser.mapper.UserMapper;
import com.ty.bugparser.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userService;

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        return userService.getUserByUsernameAndPassword(username, password);
    }

    @Override
    public int updateUser(User user) {
        return userService.updateUser(user);
    }

}
