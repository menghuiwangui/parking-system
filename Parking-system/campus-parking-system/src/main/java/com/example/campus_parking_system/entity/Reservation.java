package com.example.campus_parking_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reservation")
public class Reservation {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private Integer spotId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Integer status;   // 0已预约 1已完成 2已取消
    private LocalDateTime createTime;

    // 联表展示字段，非数据库字段
    @TableField(exist = false)
    private String spotCode;
    @TableField(exist = false)
    private String areaName;
    @TableField(exist = false)
    private String username;
}
