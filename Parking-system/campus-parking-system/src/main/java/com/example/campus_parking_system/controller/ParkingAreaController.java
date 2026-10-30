package com.example.campus_parking_system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_parking_system.annotation.RequireAdmin;
import com.example.campus_parking_system.common.Result;
import com.example.campus_parking_system.entity.ParkingArea;
import com.example.campus_parking_system.service.ParkingAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/area")
@RequiredArgsConstructor
public class ParkingAreaController {
    private final ParkingAreaService parkingAreaService;


    //  所有人可查：不分页列表（下拉框用）
    @GetMapping("/list")
    public Result<List<ParkingArea>> list() {
        return Result.ok(parkingAreaService.list());
    }

    //  管理员：分页列表
    @RequireAdmin
    @GetMapping("/page")
    public Result<Page<ParkingArea>> page(@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize) {
        return Result.ok(parkingAreaService.page(pageNum,pageSize));
    }

    @RequireAdmin
    @PostMapping
    public Result<Void> add(@RequestBody ParkingArea parkingArea) {
        parkingAreaService.add(parkingArea);
        return Result.ok();
    }

    @RequireAdmin
    @PutMapping
    public Result<Void> update(@RequestBody ParkingArea parkingArea) {
        parkingAreaService.update(parkingArea);
        return Result.ok();
    }

    @RequireAdmin
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        parkingAreaService.delete(id);
        return Result.ok();
    }
}
