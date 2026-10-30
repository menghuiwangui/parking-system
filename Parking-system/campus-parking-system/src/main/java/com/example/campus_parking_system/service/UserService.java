package com.example.campus_parking_system.service;

import com.example.campus_parking_system.entity.User;

import java.util.Map;

public interface UserService {
    void register(User user);
    Map<String,Object> login(String username,String password);
}
