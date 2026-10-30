package com.example.campus_parking_system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_parking_system.dto.ReservationDTO;
import com.example.campus_parking_system.entity.ParkingSpot;
import com.example.campus_parking_system.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<ParkingSpot> availableSpots(Integer areaId, LocalDateTime start,LocalDateTime end);
    void create(Integer userId, ReservationDTO dto);
    Page<Reservation> myPage(int pageNum,int pageSize,Integer status);
    void cancel(Integer id,Integer userId,Integer role);
}
