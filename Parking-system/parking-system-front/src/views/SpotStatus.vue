<!-- src/views/SpotStatus.vue -->
<template>
  <div>
    <!-- 各区统计卡片 -->
    <el-row :gutter="16" style="margin-bottom: 16px">
      <el-col :span="6" v-for="s in stats" :key="s.areaId">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="area-name">{{ s.areaName }}</div>
            <div class="nums">
              <span>共 {{ s.total }}</span>
              <el-text type="success">空闲 {{ s.free }}</el-text>
              <el-text type="danger">占用 {{ s.occupied }}</el-text>
              <el-text type="warning">预约 {{ s.reserved }}</el-text>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图例 -->
    <div class="legend">
      <span class="dot free"></span>空闲
      <span class="dot occupied"></span>占用
      <span class="dot reserved"></span>已预约
      <span class="dot disabled"></span>停用
    </div>

    <!-- 车位格子，按区分组 -->
    <el-card v-for="(group, areaName) in groupedSpots" :key="areaName" class="area-block">
      <template #header>{{ areaName }}</template>
      <div class="spot-grid">
        <div
          v-for="spot in group"
          :key="spot.id"
          class="spot"
          :class="statusClass(spot.status)"
          :title="statusText(spot.status)"
        >
          {{ spot.spotCode }}
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { spotAllApi, spotStatsApi } from '@/api/spot'

const spots = ref([])
const stats = ref([])
let timer = null

// 按区域名分组，保持插入顺序
const groupedSpots = computed(() => {
  const map = {}
  spots.value.forEach(s => {
    (map[s.areaName] = map[s.areaName] || []).push(s)
  })
  return map
})

const statusClass = (status) => ['free', 'occupied', 'reserved', 'disabled'][status]
const statusText = (status) => ['空闲', '占用', '已预约', '停用'][status]

async function loadData() {
  spots.value = await spotAllApi()
  stats.value = await spotStatsApi()
}

onMounted(() => {
  loadData()
  timer = setInterval(loadData, 5000)   // 每5秒轮询刷新
})
onUnmounted(() => clearInterval(timer)) // 离开页面必须清除，防止内存泄漏
</script>

<style scoped>
.legend { margin-bottom: 12px; display: flex; align-items: center; gap: 6px; }
.dot { width: 14px; height: 14px; border-radius: 3px; display: inline-block; margin-left: 12px; }
.dot:first-child { margin-left: 0; }
.dot.free { background: #67c23a; }
.dot.occupied { background: #f56c6c; }
.dot.reserved { background: #e6a23c; }
.dot.disabled { background: #c0c4cc; }

.area-block { margin-bottom: 16px; }
.spot-grid { display: flex; flex-wrap: wrap; gap: 12px; }
.spot {
  width: 90px;
  height: 60px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-weight: bold;
  font-size: 14px;
  cursor: default;
}
.spot.free { background: #67c23a; }
.spot.occupied { background: #f56c6c; }
.spot.reserved { background: #e6a23c; }
.spot.disabled { background: #c0c4cc; }

.stat-card .area-name { font-size: 16px; font-weight: bold; margin-bottom: 8px; }
.stat-card .nums { display: flex; gap: 10px; font-size: 13px; }
</style>
