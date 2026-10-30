package com.example.campus_parking_system.controller;

import com.example.campus_parking_system.annotation.RequireAdmin;
import com.example.campus_parking_system.common.Result;
import com.example.campus_parking_system.common.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    // 需要登录
    @GetMapping("/info")
    public Result<String> info() {
        return Result.ok("当前用户ID: " + UserContext.getUserId());
    }

    // 需要管理员
    @RequireAdmin
    @GetMapping("/admin")
    public Result<String> admin() {
        return Result.ok("管理员接口，仅 role=1 可访问");
    }
}
