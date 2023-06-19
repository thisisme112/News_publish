const app = createApp(App)
import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import * as ElementPlusIcons from '@element-plus/icons-vue'
for (const i in ElementPlusIcons) {
  app.component(i, ElementPlusIcons[i])
}
app.use(router).use(ElementPlus).mount('#app')
