package com.princeoo.blog.dao;

import com.princeoo.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
