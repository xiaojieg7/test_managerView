<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>我的报修</h2>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon> 提交报修
      </el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="150" />
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
      <el-table-column prop="createTime" label="提交时间" width="160" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openDetailDialog(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="createDialogVisible" title="提交报修" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="报修标题" prop="title">
          <el-input v-model="form.title" placeholder="请简要描述报修内容" />
        </el-form-item>
        <el-form-item label="报修类别" prop="category">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="水电" value="水电" />
            <el-option label="门窗" value="门窗" />
            <el-option label="家电" value="家电" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="form.priority" style="width: 100%">
            <el-option label="低" value="LOW" />
            <el-option label="普通" value="NORMAL" />
            <el-option label="高" value="HIGH" />
            <el-option label="紧急" value="URGENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="详细描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="请详细描述报修情况" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="报修详情" width="500px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="标题">{{ currentRepair?.title }}</el-descriptions-item>
        <el-descriptions-item label="类别">{{ currentRepair?.category }}</el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag :type="getPriorityType(currentRepair?.priority || '')">{{ currentRepair?.priority }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRepair?.status || '')">{{ getStatusName(currentRepair?.status || '') }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ currentRepair?.description }}</el-descriptions-item>
        <el-descriptions-item label="维修备注" :span="2">{{ currentRepair?.repairRemark || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentRepair?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ currentRepair?.completeTime || '处理中' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { repairApi } from '@/api'
import type { Repair } from '@/types'

const tableData = ref<Repair[]>([])
const loading = ref(false)
const createDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentRepair = ref<Repair | null>(null)
const formRef = ref<FormInstance>()

const form = reactive({
  title: '',
  category: '其他',
  priority: 'NORMAL',
  description: ''
})

const rules: FormRules = {
  title: [{ required: true, message: '请输入报修标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择报修类别', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await repairApi.getMyRepairs()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const openDialog = () => {
  Object.assign(form, {
    title: '',
    category: '其他',
    priority: 'NORMAL',
    description: ''
  })
  createDialogVisible.value = true
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await repairApi.create(form)
        ElMessage.success('提交成功')
        createDialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      }
    }
  })
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
