<template>
  <div class="dashboard">
    <h2>系统概览</h2>
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon users"><el-icon><User /></el-icon></div>
          <div class="stat-info">
            <p class="stat-label">总用户数</p>
            <p class="stat-value">{{ stats.totalUsers }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon owners"><el-icon><House /></el-icon></div>
          <div class="stat-info">
            <p class="stat-label">业主数量</p>
            <p class="stat-value">{{ stats.totalOwners }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon repairers"><el-icon><Tools /></el-icon></div>
          <div class="stat-info">
            <p class="stat-label">维修人员</p>
            <p class="stat-value">{{ stats.totalRepairers }}</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon repairs"><el-icon><Warning /></el-icon></div>
          <div class="stat-info">
            <p class="stat-label">待处理报修</p>
            <p class="stat-value">{{ stats.pendingRepairs }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { reactive, onMounted } from 'vue'
import { dashboardApi } from '@/api'
import type { DashboardStats } from '@/types'

const stats = reactive<DashboardStats>({
  totalUsers: 0,
  totalOwners: 0,
  totalRepairers: 0,
  pendingRepairs: 0,
  totalRepairs: 0
})

const loadStats = async () => {
  try {
    const res = await dashboardApi.getStats()
    Object.assign(stats, res.data)
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
  color: #333;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 28px;
  color: #fff;
}

.stat-icon.users { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-icon.owners { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-icon.repairers { background: linear-gradient(135deg, #fc4a1a 0%, #f7b733 100%); }
.stat-icon.repairs { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin: 0 0 8px 0;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin: 0;
}
</style>
