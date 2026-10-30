package com.example.campus_parking_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;     // BCrypt 加密存储
    private String realName;
    private String phone;
    private String carPlate;
    private Integer role;        // 0普通用户, 1管理员
    private LocalDateTime createTime;
}
