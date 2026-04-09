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
    // 【关键修改】直接传对象，不要用 URLSearchParams
    // 这会匹配后端的 @RequestBody
    const token = await request.post('/auth/login', {
      username: form.value.username,
      password: form.value.password
    });

    if (token && token.includes("SUCCESS_TOKEN_FOR_")) {
      localStorage.setItem('token', token);
      router.push('/tasks');
    } else {
      alert(token || "登录失败");
    }
  } catch (error) {
    console.error(error);
    alert("网络请求失败");
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