package com.example.SpringBookStore.service;

import com.example.SpringBookStore.dto.UserDto;
import com.example.SpringBookStore.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
