package com.example.campus_parking_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_parking_system.entity.ParkingArea;
import com.example.campus_parking_system.entity.ParkingSpot;
import com.example.campus_parking_system.mapper.ParkingAreaMapper;
import com.example.campus_parking_system.mapper.ParkingSpotMapper;
import com.example.campus_parking_system.service.ParkingAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingAreaServiceImpl implements ParkingAreaService {
    private final ParkingAreaMapper parkingAreaMapper;
    private final ParkingSpotMapper parkingSpotMapper;

    @Override
    public Page<ParkingArea> page(int pageNum,int pageSize) {
        return parkingAreaMapper.selectPage(new Page<>(pageNum,pageSize),null);
    }

    @Override
    public List<ParkingArea> list() {
        // 简单查全部；需要排序就加 wrapper
        return parkingAreaMapper.selectList(
                new LambdaQueryWrapper<ParkingArea>()
                        .orderByAsc(ParkingArea::getId)
        );
    }

    @Override
    public  void add(ParkingArea parkingArea) {
        Long count = parkingAreaMapper.selectCount(
                new LambdaQueryWrapper<ParkingArea>().eq(ParkingArea::getAreaName,parkingArea.getAreaName()));
        if (count > 0) throw new RuntimeException("区域名称已存在");
        parkingAreaMapper.insert(parkingArea);
    }

    @Override
    public void update(ParkingArea parkingArea) {
        // 排除自己后查重  即查一下数据库里，有没有「名字跟我一样、但又不是我自己」的其他区域
        Long count = parkingAreaMapper.selectCount(
                new LambdaQueryWrapper<ParkingArea>()
                        .eq(ParkingArea::getAreaName,parkingArea.getAreaName())  // eq = equals（等于）
                        .ne(ParkingArea::getId,parkingArea.getId()));  // ne = not equals（不等于）
        if (count > 0) throw new RuntimeException("区域名称已存在");
        parkingAreaMapper.updateById(parkingArea);
    }

    @Override
    public void delete(Integer id) {
        // 区下还有车位则不允许删
        Long spotCount = parkingSpotMapper.selectCount(
                new LambdaQueryWrapper<ParkingSpot>().eq(ParkingSpot::getAreaId,id));
        if (spotCount > 0) throw new RuntimeException("该区域下还有车位，无法删除");
        parkingAreaMapper.deleteById(id);
    }
}