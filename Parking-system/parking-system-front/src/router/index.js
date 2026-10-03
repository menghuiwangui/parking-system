import { createRouter, createWebHistory } from 'vue-router'
// 路由 + 路由守卫
import { useUserStore } from '@/store/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', name: 'Login', component: () => import('@/views/LoginPage.vue') },
    { path: '/register', name: 'Register', component: () => import('@/views/RegisterPage.vue') },
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/HomePage.vue'),
      meta: { requiresAuth: true }, // 路由自定义附加信息，用来给这条信息打标记
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
