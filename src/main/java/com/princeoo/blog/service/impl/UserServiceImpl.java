package com.princeoo.blog.service.impl;

import com.princeoo.blog.dao.UserRepository;
import com.princeoo.blog.pojo.User;
import com.princeoo.blog.service.UserService;
import com.princeoo.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
