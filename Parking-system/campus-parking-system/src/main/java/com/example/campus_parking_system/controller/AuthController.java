package com.example.campus_parking_system.controller;

import com.example.campus_parking_system.common.Result;
import com.example.campus_parking_system.entity.User;
import com.example.campus_parking_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    // 注册：POST /api/auth/register
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        userService.register(user);
        return Result.ok();
    }

    // 登录：POST /api/auth/login
    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user) {
        Map<String,Object> data = userService.login(user.getUsername(),user.getPassword());
        return Result.ok(data);
    }
}
