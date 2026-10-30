package com.example.campus_parking_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_parking_system.annotation.RequireAdmin;
import com.example.campus_parking_system.common.Result;
import com.example.campus_parking_system.entity.ParkingSpot;
import com.example.campus_parking_system.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spot")
@RequiredArgsConstructor
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;

    // 所有人：车位列表（状态大屏用，支持按区域筛选）
    @GetMapping("/all")
    public Result<List<ParkingSpot>> all(@RequestParam(required = false) Integer areaId) {
        return Result.ok(parkingSpotService.listAll(areaId));
    }

    // 所有人：各区统计（空闲/占用/预约数量）
    @GetMapping("/stats")
    public Result<List<Map<String,Object>>> stats() {
        return Result.ok(parkingSpotService.stats());
    }

    // 管理员：分页+筛选
    @RequireAdmin
    @GetMapping("/page")
    public Result<Page<ParkingSpot>> page(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) Integer areaId,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) String spotCode) {
        return Result.ok(parkingSpotService.page(pageNum,pageSize,areaId,status,spotCode));
    }

    @RequireAdmin
    @PostMapping
    public Result<Void> add(@RequestBody ParkingSpot parkingSpot) {
        parkingSpotService.add(parkingSpot);
        return Result.ok();
    }

    @RequireAdmin
    @PutMapping Result<Void> update(@RequestBody ParkingSpot parkingSpot) {
        parkingSpotService.update(parkingSpot);
        return Result.ok();
    }

    @RequireAdmin
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        parkingSpotService.delete(id);
        return Result.ok();
    }
}
