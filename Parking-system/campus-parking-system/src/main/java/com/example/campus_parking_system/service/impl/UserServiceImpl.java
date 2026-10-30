package com.example.campus_parking_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.campus_parking_system.entity.User;
import com.example.campus_parking_system.mapper.UserMapper;
import com.example.campus_parking_system.service.UserService;
import com.example.campus_parking_system.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    // BCrypt 单例即可
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void register(User user) {
        // 1. 用户名唯一性校验
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername,user.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 密码加密（默认角色：普通用户）
        user.setPassword(encoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(0);
        }
        userMapper.insert(user);
    }

    @Override
    public Map<String,Object> login(String username,String password) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername,username));

        // 用户不存在 或 密码错误，统一提示（避免暴露用户是否存在）
        if (user == null || !encoder.matches(password,user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成 token 并返回
        String token = jwtUtil.generateToken(user.getId(),user.getRole());
        Map<String,Object> result =new HashMap<>();
        result.put("token",token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }
}
