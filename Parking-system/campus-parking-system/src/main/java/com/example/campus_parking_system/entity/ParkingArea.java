package com.example.campus_parking_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("parking_area")
public class ParkingArea {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String areaName;
    private String location;
    private String description;
}
