package com.korit.backend_mini.mapper;

import com.korit.backend_mini.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> getUserByUserId(Integer userId);
    Optional<User> getUserByUserEmail(String email);
    Optional<User> getUserByUsername(String username);
    int addUser(User user);
    int changePassword(User user);
    int changeUsername(User user);
    int changeProfileImg(User user);
    List<User> getUserList();
    int withdraw(Integer userId);
    void deleteUser();
}
