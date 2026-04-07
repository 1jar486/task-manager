import { createApp } from 'vue'
import App from './App.vue'
// 2. 核心修复：添加 assets/ 路径，确保能找到你那个 600 多行的样式文件
import './assets/style.css'

createApp(App).mount('#app')