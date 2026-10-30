package com.example.campus_parking_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("parking_spot")
public class ParkingSpot {
    @TableId(type = IdType.AUTO)
    private  Integer id;
    private Integer areaId;
    private String spotCode;
    private Integer status; // 0空闲,1占用,2已预约,3停用

    // 定义一个“临时扩展字段”，用来接收联表查询出来的额外数据   非数据库字段，联表显示区域名用
    @TableField(exist = false)
    private String areaName;
}
