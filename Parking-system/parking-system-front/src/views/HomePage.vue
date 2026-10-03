<!-- src/views/Home.vue -->
<template>
  <div style="padding: 20px">
    <h2>欢迎，{{ store.username }}</h2>
    <p>角色：{{ store.isAdmin ? '管理员' : '普通用户' }}</p>
    <el-button type="danger" @click="onLogout">退出登录</el-button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const store = useUserStore()

function onLogout() {
  store.logout()
  router.push('/login')
}
</script>
