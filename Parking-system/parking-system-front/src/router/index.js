import { createRouter, createWebHistory } from 'vue-router'
// 路由 + 路由守卫
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', name: 'Login', component: () => import('@/views/LoginPage.vue') },
    { path: '/register', name: 'Register', component: () => import('@/views/RegisterPage.vue') },
    {
      path: '/',
      // name: 'Home',
      component: () => import('@/views/Layout.vue'),
      redirect: '/status',
      // meta: { requiresAuth: true }, // 路由自定义附加信息，用来给这条信息打标记
      children: [
        { path: 'status', name: 'SpotStatus', component: () => import('@/views/SpotStatus.vue'), meta: { title: '车位状态' }},
        { path: 'area', name: 'AreaManage', component: () => import('@/views/admin/AreaManage.vue'), meta: { title: '停车区管理', requiresAdmin: true } },
        { path: 'spots', name: 'SpotManage', component: () => import('@/views/admin/SpotManage.vue'), meta: { title: '车位管理', requiresAdmin: true } }
      ]
    },
  ],
})

// 路由守卫
router.beforeEach((to, from) => {
  const store = useUserStore()
  // 需要登录但未登录 -> 跳登录页
  if (to.meta.requiresAuth && !store.isLoggedIn) {
    return { name: 'Login', query: { redirect: to.fullPath } } // query 记录原本想去的路径
  }
  // 已登录去登录页 -> 跳首页
  if (to.name === 'Login' && store.isLoggedIn) {
    return { name: 'Home' }
  }
})

export default router
