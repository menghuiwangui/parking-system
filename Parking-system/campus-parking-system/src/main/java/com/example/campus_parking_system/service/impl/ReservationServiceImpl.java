package com.example.campus_parking_system.service.impl;

import com.example.campus_parking_system.mapper.ParkingAreaMapper;
import com.example.campus_parking_system.mapper.ParkingSpotMapper;
import com.example.campus_parking_system.mapper.ReservationMapper;
import com.example.campus_parking_system.mapper.UserMapper;
import com.example.campus_parking_system.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;
    private final ParkingSpotMapper parkingSpotMapper;
    private final ParkingAreaMapper parkingAreaMapper;
    private final UserMapper userMapper;

    // 查询某时间段内可预约的车位
}
