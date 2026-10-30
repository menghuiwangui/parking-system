<!-- src/views/Layout.vue -->
<template>
  <el-container class="layout">
    <!-- 侧边栏 -->
    <el-aside width="200px">
      <div class="logo">🚗 校园停车</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/status">
          <el-icon><Odometer /></el-icon>车位状态
        </el-menu-item>
        <template v-if="store.isAdmin">
          <el-menu-item index="/area">
            <el-icon><Location /></el-icon>停车区管理
          </el-menu-item>
          <el-menu-item index="/spots">
            <el-icon><Grid /></el-icon>车位管理
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶栏 -->
      <el-header class="header">
        <span>{{ $route.meta.title || '' }}</span>
        <div class="right">
          <el-tag :type="store.isAdmin ? 'danger' : 'info'" size="small">
            {{ store.isAdmin ? '管理员' : '用户' }}
          </el-tag>
          <span>{{ store.username }}</span>
          <el-button link type="danger" @click="onLogout">退出</el-button>
        </div>
      </el-header>
      <!-- 内容区 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Odometer, Location, Grid } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const router = useRouter()
const store = useUserStore()

function onLogout() {
  store.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; }
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-weight: bold;
  background: #263445;
}
.el-aside { background: #304156; }
.el-menu { border-right: none; }
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #e6e6e6;
}
.right { display: flex; align-items: center; gap: 12px; }
</style>
