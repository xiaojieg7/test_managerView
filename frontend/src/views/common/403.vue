<template>
  <div class="error-container">
    <div class="error-content">
      <h1>403</h1>
      <p>抱歉，您没有访问权限</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.error-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}

.error-content {
  text-align: center;
}

.error-content h1 {
  font-size: 100px;
  color: #409eff;
  margin-bottom: 20px;
}

.error-content p {
  font-size: 20px;
  color: #666;
  margin-bottom: 30px;
}
</style>
