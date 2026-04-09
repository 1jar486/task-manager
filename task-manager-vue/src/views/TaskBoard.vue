<template>
  <div class="app-container">
    <header class="app-header">
      <div class="header-left">
        <h1>今日清单</h1>
        <p id="current-date">{{ currentDate }}</p >
      </div>
      <div class="header-right">
        <button @click="handleLogout" style="margin-right: 10px; background-color: var(--card-bg); color: var(--text-main); border: 1px solid var(--border-color); padding: 5px 15px; border-radius: 8px; cursor: pointer;">登出账号</button>
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
import { useRouter } from 'vue-router';
import request from '../utils/request';
// 确保样式文件被引入
import '../assets/style.css';

const router = useRouter();
const tasks = ref([]);
const isDark = ref(false);

const newTask = ref({
  title: '',
  priority: '中',
  tag: '学习'
});

const priorityIcons = { "高": "error", "中": "warning", "低": "check_circle" };
const tagIcons = { "学习": "school", "生活": "home", "兼职": "work" };
const priorityWeight = { "高": 3, "中": 2, "低": 1 };

const currentDate = new Date().toLocaleDateString('zh-CN', { month: 'long', day: 'numeric', weekday: 'long' });

const totalCount = computed(() => tasks.value.length);
const completedCount = computed(() => tasks.value.filter(t => t.completed).length);

const sortedTasks = computed(() => {
  return [...tasks.value].sort((a, b) => {
    if (priorityWeight[a.priority] !== priorityWeight[b.priority]) {
      return priorityWeight[b.priority] - priorityWeight[a.priority];
    }
    return b.id - a.id;
  });
});

const boardColumns = computed(() => [
  { name: '学习', icon: 'school', tasks: sortedTasks.value.filter(t => t.tag === '学习') },
  { name: '生活', icon: 'home', tasks: sortedTasks.value.filter(t => t.tag === '生活') },
  { name: '兼职', icon: 'work', tasks: sortedTasks.value.filter(t => t.tag === '兼职') }
]);

// 1. 获取任务
const fetchTasks = async () => {
  document.body.style.cursor = "progress";
  try {
    tasks.value = await request.get('/api/tasks');
  } catch (error) {
    console.error("获取任务失败", error);
  } finally {
    document.body.style.cursor = "default";
  }
};

// 2. 添加任务（含你原有的缩放微交互动画）
const handleAddTask = async () => {
  if (!newTask.value.title.trim()) return;
  try {
    await request.post('/api/tasks', newTask.value);

    // 保留你原有的 DOM 缩放动画逻辑
    const input = document.querySelector('input[type="text"]');
    if(input) {
      input.style.transform = "scale(1.05)";
      setTimeout(() => input.style.transform = "scale(1)", 150);
    }

    newTask.value.title = '';
    fetchTasks();
  } catch (error) {
    alert("提交失败");
  }
};

// 3. 切换状态
const toggleTask = async (id) => {
  try {
    await request.put(`/api/tasks/${id}`);
    fetchTasks();
  } catch (error) {
    console.error("更新失败", error);
  }
};

// 4. 删除任务
const deleteTask = async (id) => {
  if (!confirm("确定删除该任务？")) return;
  try {
    await request.delete(`/api/tasks/${id}`);
    fetchTasks();
  } catch (error) {
    console.error("删除失败", error);
  }
};

// 5. 登出逻辑
const handleLogout = () => {
  localStorage.removeItem('token');
  router.push('/login');
};

// 6. 主题切换
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

onMounted(() => {
  // 初始化主题
  if (localStorage.getItem('theme') === 'dark') {
    isDark.value = true;
    document.documentElement.setAttribute('data-theme', 'dark');
  }
  fetchTasks();
});
</script>