<template>
  <div class="app-container" style="display: flex; justify-content: center; align-items: center; min-height: 100vh;">
    <div class="input-section card" style="width: 100%; max-width: 400px; display: flex; flex-direction: column; gap: 15px;">
      <h2 style="text-align: center; margin-bottom: 20px;">任务管理系统</h2>

      <input type="text" v-model="form.username" placeholder="请输入用户名" autofocus>
      <input type="password" v-model="form.password" placeholder="请输入密码">

      <button id="addBtn" @click="handleLogin" style="width: 100%;">登 录</button>
      <button @click="handleRegister" style="width: 100%; background-color: var(--card-bg); color: var(--text-main); border: 1px solid var(--border-color);">注 册</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import request from '../utils/request';

const router = useRouter();
const form = ref({ username: '', password: '' });

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    alert("请填写完整！");
    return;
  }
  try {
    // 1. 发起登录请求，res 接收到的是后端返回的 JSON 对象 {"token": "SUCCESS..."}
    const res = await request.post('/auth/login', {
      username: form.value.username,
      password: form.value.password
    });

    // 2. 正规做法：先检查 res 是否存在，再从 res.token 中取值
    // 因为后端现在返回的是 Map，数据在 .token 字段里
    if (res && res.token && res.token.includes("SUCCESS_TOKEN_FOR_")) {
      // 3. 把真正的字符串存入本地缓存
      localStorage.setItem('token', res.token);

      // 4. 跳转到任务看板
      alert("登录成功！");
      router.push('/tasks');
    } else {
      // 如果后端返回了错误信息，通常在 res 中或者直接弹出
      alert(res || "用户名或密码错误");
    }
  } catch (error) {
    console.error("登录报错详情:", error);
    alert("登录失败：请检查用户名密码或后端服务");
  }
};

const handleRegister = async () => {
  if (!form.value.username || !form.value.password) {
    alert("请填写完整！");
    return;
  }
  try {
    // 配合后端 @RequestBody 的写法，直接发送 JSON 对象
    const res = await request.post('/auth/register', {
      username: form.value.username,
      password: form.value.password
    });

    if (res === "注册成功") {
      alert("注册成功，请登录！");
    } else {
      alert(res || "注册失败");
    }
  } catch (error) {
    console.error(error);
    alert("注册请求失败，请检查后端设置");
  }
};
</script>