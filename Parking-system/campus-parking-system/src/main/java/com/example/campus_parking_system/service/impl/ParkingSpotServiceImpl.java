package com.example.campus_parking_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_parking_system.entity.ParkingArea;
import com.example.campus_parking_system.entity.ParkingSpot;
import com.example.campus_parking_system.mapper.ParkingAreaMapper;
import com.example.campus_parking_system.mapper.ParkingSpotMapper;
import com.example.campus_parking_system.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl implements ParkingSpotService {
    private final ParkingSpotMapper spotMapper;
    private final ParkingAreaMapper areaMapper;

    @Override
    public Page<ParkingSpot> page(int pageNum, int pageSize,Integer areaId,Integer status,String spotCode) {
        LambdaQueryWrapper<ParkingSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(areaId != null,ParkingSpot::getAreaId,areaId)
                .eq(status != null,ParkingSpot::getStatus,status)
                .like(spotCode != null && !spotCode.isEmpty(),ParkingSpot::getSpotCode,spotCode);
        Page<ParkingSpot> page = spotMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);
        fillAreaName(page.getRecords());
        return page;
    }

    @Override
    public List<ParkingSpot> listAll(Integer areaId) {
        LambdaQueryWrapper<ParkingSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(areaId != null,ParkingSpot::getAreaId,areaId)
                .orderByAsc(ParkingSpot::getSpotCode);
        List<ParkingSpot> spots = spotMapper.selectList(wrapper);
        fillAreaName(spots);
        return spots;
    }

    @Override
    public List<Map<String,Object>> stats() {
        List<ParkingArea> areas = areaMapper.selectList(null);
        List<ParkingSpot> spots = spotMapper.selectList(null);

        // 按区域分组统计
        Map<Integer,List<ParkingSpot>> group = spots.stream()
                .collect(Collectors.groupingBy(ParkingSpot::getAreaId));

        List<Map<String,Object>> result = new ArrayList<>();
        for (ParkingArea area : areas) {
            Map<String,Object> item = new HashMap<>();
            item.put("areaId", area.getId());
            item.put("areaName", area.getAreaName());
            List<ParkingSpot> list = group.getOrDefault(area.getId(), Collections.emptyList());
            item.put("total", list.size());
            item.put("free", list.stream().filter(s -> s.getStatus() == 0).count());
            item.put("occupied", list.stream().filter(s -> s.getStatus() == 1).count());
            item.put("reserved", list.stream().filter(s -> s.getStatus() == 2).count());
            result.add(item);
        }
        return result;
    }

    @Override
    public void add(ParkingSpot spot) {
        checkCodeUnique(spot.getSpotCode(),null);
        if (spot.getStatus() == null) spot.setStatus(0);
        // 校验区域存在
        if (areaMapper.selectById(spot.getAreaId()) == null) {
            throw new RuntimeException("所属区域不存在");
        }
        spotMapper.insert(spot);
    }

    @Override
    public void update(ParkingSpot spot) {
        checkCodeUnique(spot.getSpotCode(),spot.getId());
        spotMapper.updateById(spot);
    }

    @Override
    public void delete(Integer id) {
        ParkingSpot spot = spotMapper.selectById(id);
        if (spot == null) throw new RuntimeException("车位不存在");
        // 占用/已预约的车位不允许直接删
        if (spot.getStatus() == 1 || spot.getStatus() == 2) {
            throw new RuntimeException("车位使用中或已预约，无法删除，可先停用");
        }
        spotMapper.deleteById(id);
    }

    // 批量填充区域名称（避免N+1查询：先收集id再一次性查）
    private void fillAreaName(List<ParkingSpot> spots) {
        if (spots.isEmpty()) return;
        Set<Integer> areaIds = spots.stream().map(ParkingSpot::getAreaId).collect(Collectors.toSet());
        Map<Integer,String> areaMap = areaMapper.selectBatchIds(areaIds).stream()
                .collect(Collectors.toMap(ParkingArea::getId,ParkingArea::getAreaName));
        spots.forEach(s -> s.setAreaName(areaMap.get(s.getAreaId())));
    }

    // 车位编号唯一性校验（excludeId：编辑时排除自身）
    private void checkCodeUnique(String code,Integer excludeId) {   //excludeId 当前车位
        LambdaQueryWrapper<ParkingSpot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkingSpot::getSpotCode,code);
        if (excludeId != null) wrapper.ne(ParkingSpot::getId,excludeId);
        if (spotMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("车位编号已存在");
        }
    }
}
