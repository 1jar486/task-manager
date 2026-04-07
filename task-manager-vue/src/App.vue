<template>
  <div class="app-container">
    <header class="app-header">
      <div class="header-left">
        <h1>今日清单</h1>
        <p id="current-date">{{ currentDate }}</p >
      </div>
      <div class="header-right">
        <button id="themeToggle" @click="toggleTheme">{{ isDark ? '☀️' : '🌙' }}</button>
      </div>
    </header>

    <div class="input-section card">
      <input type="text" v-model="newTask.title" placeholder="写下你的下一个目标..." autofocus @keyup.enter="handleAddTask">

      <select v-model="newTask.priority">
        <option value="高">🔥 高优先级</option>
        <option value="中">⚡ 中优先级</option>
        <option value="低">🍃 低优先级</option>
      </select>

      <select v-model="newTask.tag">
        <option value="学习">📖 学习</option>
        <option value="生活">🏠 生活</option>
        <option value="兼职">💼 副业</option>
      </select>

      <button id="addBtn" @click="handleAddTask">添加</button>
    </div>

    <div class="stats-bar">
      <span>全部 {{ totalCount }}</span>
      <span>已完成 {{ completedCount }}</span>
    </div>

    <div class="task-board">
      <div class="board-column" v-for="col in boardColumns" :key="col.name">
        <h2 class="column-title">
          <span class="material-icons">{{ col.icon }}</span> {{ col.name }}
        </h2>

        <div class="task-container">
          <div
              v-for="task in col.tasks"
              :key="task.id"
              :class="['task-card', { 'completed': task.completed }, `border-${task.priority}`]"
              style="animation: fadeUp 0.4s ease;"
          >
            <div class="card-header">
              <div class="badge-group">
                <span :class="`badge badge-priority-${task.priority}`">
                  <span class="material-icons">{{ priorityIcons[task.priority] }}</span>
                  {{ task.priority }}
                </span>
                <span class="badge badge-tag">
                  <span class="material-icons">{{ tagIcons[task.tag] }}</span>
                  {{ task.tag }}
                </span>
              </div>
              <input type="checkbox" class="custom-checkbox" :checked="task.completed" @change="toggleTask(task.id)">
            </div>
            <div class="card-body">
              <span class="task-title">{{ task.title }}</span>
            </div>
            <div class="card-footer">
              <span class="task-id">#{{ task.id }}</span>
              <button class="icon-delete-btn" @click="deleteTask(task.id)">删除</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import './assets/style.css'; // 引入你的原生 CSS 样式

// =========================
// 1. 响应式状态管理 (替代原生全局变量)
// =========================
const tasks = ref([]);
const isDark = ref(false);

const newTask = ref({
  title: '',
  priority: '中',
  tag: '学习'
});

// 图标映射表
const priorityIcons = { "高": "error", "中": "warning", "低": "check_circle" };
const tagIcons = { "学习": "school", "生活": "home", "兼职": "work" };
const priorityWeight = { "高": 3, "中": 2, "低": 1 };

// 日期计算
const currentDate = new Date().toLocaleDateString('zh-CN', { month: 'long', day: 'numeric', weekday: 'long' });

// =========================
// 2. 派生状态 (Computed)
// 替代了原生代码中的 filter 和手动计算
// =========================
const totalCount = computed(() => tasks.value.length);
const completedCount = computed(() => tasks.value.filter(t => t.completed).length);

// 自动对任务进行排序，然后按 Tag 分组
const sortedTasks = computed(() => {
  return [...tasks.value].sort((a, b) => {
    if (priorityWeight[a.priority] !== priorityWeight[b.priority]) {
      return priorityWeight[b.priority] - priorityWeight[a.priority];
    }
    return b.id - a.id;
  });
});

// 动态构建看板的三列数据
const boardColumns = computed(() => [
  { name: '学习', icon: 'school', tasks: sortedTasks.value.filter(t => t.tag === '学习') },
  { name: '生活', icon: 'home', tasks: sortedTasks.value.filter(t => t.tag === '生活') },
  { name: '兼职', icon: 'work', tasks: sortedTasks.value.filter(t => t.tag === '兼职') }
]);

// =========================
// 3. API 请求与交互动作
// =========================
const API_BASE = 'http://localhost:8080/api/tasks'; // 确保你的 JavaBoot 允许 CORS 跨域！

const fetchTasks = async () => {
  document.body.style.cursor = "progress"; // 简易 Loading
  try {
    const res = await axios.get(API_BASE);
    tasks.value = res.data; // 数据赋值后，Vue 会自动触发整个页面的重新渲染！
  } catch (error) {
    console.error("获取任务失败", error);
  } finally {
    document.body.style.cursor = "default";
  }
};

const handleAddTask = async () => {
  if (!newTask.value.title.trim()) return;
  try {
    await axios.post(API_BASE, newTask.value);
    newTask.value.title = ''; // 清空输入框
// 微交互：你可以保留原有的 DOM 操作处理动画，或者在 Vue 里通过绑定 class 实现
    const input = document.querySelector('input[type="text"]');
    if(input) {
      input.style.transform = "scale(1.05)";
      setTimeout(() => input.style.transform = "scale(1)", 150);
    }

    fetchTasks();
  } catch (error) {
    alert("提交失败");
  }
};

const toggleTask = async (id) => {
  try {
    await axios.put(`${API_BASE}/${id}`);
    fetchTasks();
  } catch (error) {
    console.error("更新失败", error);
  }
};

const deleteTask = async (id) => {
  if (!confirm("确定删除该任务？")) return;
  try {
    await axios.delete(`${API_BASE}/${id}`);
    fetchTasks();
  } catch (error) {
    console.error("删除失败", error);
  }
};

// =========================
// 4. 主题切换逻辑
// =========================
const toggleTheme = () => {
  isDark.value = !isDark.value;
  if (isDark.value) {
    document.documentElement.setAttribute('data-theme', 'dark');
    localStorage.setItem('theme', 'dark');
  } else {
    document.documentElement.removeAttribute('data-theme');
    localStorage.setItem('theme', 'light');
  }
};

// 初始化生命周期
onMounted(() => {
  // 恢复主题
  if (localStorage.getItem('theme') === 'dark') {
    isDark.value = true;
    document.documentElement.setAttribute('data-theme', 'dark');
  }
  // 加载数据
  fetchTasks();
});
</script>
