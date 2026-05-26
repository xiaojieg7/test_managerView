<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>我的报修</h2>
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
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 'PENDING'"
            type="primary"
            size="small"
            @click="openStatusDialog(row, 'PROCESSING')"
          >
            开始处理
          </el-button>
          <el-button
            v-if="row.status === 'PROCESSING'"
            type="success"
            size="small"
            @click="openStatusDialog(row, 'COMPLETED')"
          >
            完成
          </el-button>
          <el-button type="info" size="small" @click="openDetailDialog(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="statusDialogVisible" title="更新状态" width="400px">
      <el-form>
        <el-form-item label="状态">
          <el-tag :type="getStatusType(targetStatus)">{{ getStatusName(targetStatus) }}</el-tag>
        </el-form-item>
        <el-form-item label="维修备注">
          <el-input v-model="remark" type="textarea" :rows="3" placeholder="请填写维修备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStatus">确定</el-button>
      </template>
    </el-dialog>

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
import { ElMessage } from 'element-plus'
import { repairApi } from '@/api'
import type { Repair } from '@/types'

const tableData = ref<Repair[]>([])
const loading = ref(false)
const statusDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentRepair = ref<Repair | null>(null)
const currentRepairId = ref<number | null>(null)
const targetStatus = ref('')
const remark = ref('')

const loadData = async () => {
  loading.value = true
  try {
    const res = await repairApi.getAssignedRepairs()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const openStatusDialog = (row: Repair, status: string) => {
  currentRepairId.value = row.id
  targetStatus.value = status
  remark.value = ''
  statusDialogVisible.value = true
}

const submitStatus = async () => {
  if (!currentRepairId.value) return
  try {
    await repairApi.updateStatus(currentRepairId.value, targetStatus.value, remark.value)
    ElMessage.success('状态更新成功')
    statusDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
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
