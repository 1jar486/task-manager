let tasks = []; // 声明本地任务数组缓存

// A. 获取任务（保持不变）
async function fetchTasks() {
    try {
        const response = await fetch('http://localhost:8080/api/tasks');
        if (response.ok) {
            tasks = await response.json();
            renderTasks();
        }
    } catch (error) {
        console.error("连接失败", error);
    }
}

// B. 添加任务（【修改点】：组装包含 title, priority, tag 的 JSON 对象发给后端）
async function addTask(title, priority, tag) {
    if (title.trim() === "") return; // 如果标题为空则终止执行

    // 构建要发送的数据对象，字段名必须与后端 Task.java 的属性名完全一致
    const newTask = {
        title: title,
        priority: priority,
        tag: tag
    };

    try {
        const response = await fetch('http://localhost:8080/api/tasks', {
            method: 'POST', // 指定为新增请求
            // 【恢复】因为发送的是对象，必须改回 application/json
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newTask) // 将 JS 对象转换为 JSON 字符串发送
        });

        if (response.ok) {
            fetchTasks(); // 发送成功后重新加载列表
        }
    } catch (error) {
        alert("提交失败");
    }
}

// C. 切换状态（保持不变）
async function toggleTask(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/tasks/${id}`, { method: 'PUT' });
        if (response.ok) fetchTasks();
    } catch (error) {
        console.error("更新失败", error);
    }
}

// D. 删除任务（保持不变）
async function deleteTask(id) {
    if (!confirm("确定要删除这个任务吗？")) return;
    try {
        const response = await fetch(`http://localhost:8080/api/tasks/${id}`, { method: 'DELETE' });
        if (response.ok) fetchTasks();
    } catch (error) {
        console.error("删除失败", error);
    }
}

// UI 渲染引擎
function renderTasks() {
    // 1. 获取前端的三个分类容器
    const listStudy = document.getElementById('list-study');
    const listLife = document.getElementById('list-life');
    const listJob = document.getElementById('list-job');
    const taskCount = document.getElementById('taskCount');
    const completedCount = document.getElementById('completedCount');

    // 2. 清空当前视图，准备重新渲染
    listStudy.innerHTML = '';
    listLife.innerHTML = '';
    listJob.innerHTML = '';

    // 3. 定义优先级权重（数字越大，优先级越高）
    const priorityWeight = {
        "高": 3,
        "中": 2,
        "低": 1
    };

    // 4. 核心排序逻辑
    const sortedTasks = [...tasks].sort((a, b) => {
        // 第一层判断：比较优先级权重
        if (priorityWeight[a.priority] !== priorityWeight[b.priority]) {
            return priorityWeight[b.priority] - priorityWeight[a.priority]; // 降序：权重大的排前面
        }
        // 第二层判断：如果优先级相同，比较时间（使用数据库自增 ID 作为时间依据）
        // ID 越大，代表添加时间越晚。按照最新添加排在前面的原则，依然使用降序
        return b.id - a.id;
    });

    // 5. 遍历排序后的数据，生成卡片并分发
    sortedTasks.forEach(task => {
        const isDone = task.completed;
        const card = document.createElement('div');

        // 分配 CSS 类名：包含基础卡片样式、完成状态样式、优先级侧边条样式
        card.className = `task-card ${isDone ? 'completed' : ''} border-${task.priority}`;

        // 拼接卡片内部的 HTML 结构
        card.innerHTML = `
            <div class="card-header">
                <span class="badge badge-priority-${task.priority}">${task.priority}</span>
                <input type="checkbox" class="custom-checkbox" ${isDone ? 'checked' : ''} onclick="toggleTask(${task.id})">
            </div>
            <div class="card-body">
                <span class="task-title">${task.title}</span>
            </div>
            <div class="card-footer">
                <span class="task-id">#${task.id}</span>
                <button class="icon-delete-btn" onclick="deleteTask(${task.id})">删除</button>
            </div>
        `;

        // 6. 路由分发：根据标签将卡片放入对应的列
        if (task.tag === '学习') {
            listStudy.appendChild(card);
        } else if (task.tag === '生活') {
            listLife.appendChild(card);
        } else if (task.tag === '兼职') {
            listJob.appendChild(card);
        } else {
            // 容错处理：如果没有标签，默认放入生活类
            listLife.appendChild(card);
        }
    });

    // 7. 更新顶部统计数据
    taskCount.innerText = `全部 ${tasks.length}`;
    completedCount.innerText = `已完成 ${tasks.filter(t => t.completed).length}`;
}

// F. 事件绑定（【修改点】：点击按钮时，同时读取下拉框的值）
document.getElementById('addBtn').addEventListener('click', () => {
    const titleInput = document.getElementById('taskInput');
    const priorityInput = document.getElementById('priorityInput');
    const tagInput = document.getElementById('tagInput');

    // 调用 addTask，并传入三个参数
    addTask(titleInput.value, priorityInput.value, tagInput.value);

    // 提交后仅清空文字输入框
    titleInput.value = '';
});

// 回车键绑定
document.getElementById('taskInput').addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
        document.getElementById('addBtn').click();
    }
});

// 初始化日期
document.getElementById('current-date').innerText = new Date().toLocaleDateString('zh-CN', {
    month: 'long', day: 'numeric', weekday: 'long'
});

// 加载初始数据
fetchTasks();