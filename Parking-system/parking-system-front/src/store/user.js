import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: localStorage.getItem('userId') || '',
    username: localStorage.getItem('username') || '',
    role: localStorage.getItem('role') || '0', // 0普通 1管理员
  }),
  actions: {
    setLogin(data) {
      this.token = data.token
      this.userId = data.userId
      this.username = data.username
      this.role = data.role
      localStorage.setItem('token', data.token)
      localStorage.setItem('userId', data.userId)
      localStorage.setItem('username', data.username)
      localStorage.setItem('role', data.role)
    },
    logout() {
      this.token = ''
      this.userId = ''
      this.username = ''
      this.role = '0'
      localStorage.clear()
    },
  },
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.role === '1',
  },
})
