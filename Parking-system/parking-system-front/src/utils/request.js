import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useUserStore } from '@/store/user'

const request = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端地址
  timeout: 10000,
})

// 请求拦截器：自动带 token
request.interceptors.request.use((config) => {
  const store = useUserStore
  if (store.token) {
    config.headers.Authorization = 'Bearer ' + store.token
  }
  return config
})

// 响应拦截器：统一解包和错误处理
request.interceptors.response.use(
  (response) => {
    const { code, msg, data } = response.data
    if (code === 200) {
      return data
    }
    ElMessage.error(msg || '请求失败') // 这里会用 Element Plus 的 ElMessage 组件自动弹出一个红色的错误提示
    return Promise.reject(new Error(msg))
  },
  (error) => {
    // 401 → 清token跳登录；403 → 提示无权限
    // 可选链 如果 error.response 存在，就取它的 status；如果不存在，就直接返回 undefined，不会报错。
    if (error.response?.status === 400) {
      const store = useUserStore()
      store.logout()
      ElMessage.error('登录已过期，请重新登录')
      router.push('/login')
    } else if (error.response?.status === 403) {
      ElMessage.error('无权限访问')
    } else {
      // 后端 RuntimeException 的 msg 会到 response.data.msg
      ElMessage.error(error.response?.data?.msg || '网络异常')
    }
    return Promise.reject(error)
  },
)

export default request
