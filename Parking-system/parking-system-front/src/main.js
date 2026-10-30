import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import pinia from './store'

const app = createApp(App)

app.use(pinia)  // ← 必须在 router 之前
app.use(router)

app.use(ElementPlus, { locale: zhCn })
app.mount('#app')
