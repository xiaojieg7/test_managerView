<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>维修记录</h2>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column label="报修人信息" width="180">
        <template #default="{ row }">
          {{ row.ownerName }}<br/>
          <span style="font-size: 12px; color: #999">{{ row.ownerBuilding }}-{{ row.ownerUnit }}-{{ row.ownerRoomNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="类别" width="100" />
      <el-table-column prop="priority" label="优先级" width="100">
        <template #default="{ row }">
          <el-tag :type="getPriorityType(row.priority)">{{ row.priority }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusName(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="completeTime" label="完成时间" width="160" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="info" size="small" @click="openDetailDialog(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="detailDialogVisible" title="报修详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="标题">{{ currentRepair?.title }}</el-descriptions-item>
        <el-descriptions-item label="类别">{{ currentRepair?.category }}</el-descriptions-item>
        <el-descriptions-item label="报修人">{{ currentRepair?.ownerName }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">
          {{ currentRepair?.ownerBuilding }}-{{ currentRepair?.ownerUnit }}-{{ currentRepair?.ownerRoomNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ currentRepair?.description }}</el-descriptions-item>
        <el-descriptions-item label="维修备注" :span="2">{{ currentRepair?.repairRemark || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentRepair?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ currentRepair?.completeTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { repairApi } from '@/api'
import type { Repair } from '@/types'

const tableData = ref<Repair[]>([])
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentRepair = ref<Repair | null>(null)

const loadData = async () => {
  loading.value = true
  try {
    const res = await repairApi.getAssignedRepairs()
    tableData.value = (res.data || []).filter((r: Repair) => r.status === 'COMPLETED')
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const openDetailDialog = (row: Repair) => {
  currentRepair.value = row
  detailDialogVisible.value = true
}

const getStatusName = (status: string) => {
  const names: Record<string, string> = {
    PENDING: '待分配',
    PROCESSING: '处理中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return names[status] || status
}

const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    PENDING: 'warning',
    PROCESSING: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'info'
  }
  return types[status] || 'info'
}

const getPriorityType = (priority: string) => {
  const types: Record<string, string> = {
    LOW: 'info',
    NORMAL: '',
    HIGH: 'warning',
    URGENT: 'danger'
  }
  return types[priority] || ''
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.toolbar h2 {
  margin: 0;
  font-size: 18px;
}
</style>
