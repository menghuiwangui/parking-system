package com.example.campus_parking_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_parking_system.entity.ParkingArea;

import java.util.List;

public interface ParkingAreaService {
    Page<ParkingArea> page(int pageNum,int pageSize);
    List<ParkingArea> list();  // 下拉框用
    void add(ParkingArea parkingArea);
    void update(ParkingArea parkingArea);
    void delete(Integer id);
}
