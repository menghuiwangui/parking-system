package com.example.campus_parking_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_parking_system.entity.ParkingSpot;

import java.util.List;
import java.util.Map;

public interface ParkingSpotService {
    Page<ParkingSpot> page(int pageNum,int pageSize,Integer areaId,Integer status,String spotCode);
    List<ParkingSpot> listAll(Integer areaId);
    List<Map<String,Object>> stats();
    void add(ParkingSpot parkingSpot);
    void update(ParkingSpot parkingSpot);
    void delete(Integer id);
}
