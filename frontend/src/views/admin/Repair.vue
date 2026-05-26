<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>报修管理</h2>
      <div>
        <el-select v-model="searchStatus" placeholder="状态" clearable style="width: 120px; margin-right: 10px">
          <el-option label="待分配" value="PENDING" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="已完成" value="COMPLETED" />
        </el-select>
        <el-input v-model="searchKeyword" placeholder="搜索标题/描述" style="width: 200px; margin-right: 10px" clearable />
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="category" label="类别" width="100" />
      <el-table-column label="报修人" width="120">
        <template #default="{ row }">
          {{ row.ownerName }}<br/>
          <span style="font-size: 12px; color: #999">{{ row.ownerBuilding }}-{{ row.ownerUnit }}-{{ row.ownerRoomNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="repairerName" label="维修人员" width="100">
        <template #default="{ row }">
          {{ row.repairerName || '-' }}
        </template>
      </el-table-column>
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
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openAssignDialog(row)" v-if="row.status === 'PENDING'">分配</el-button>
          <el-button type="info" size="small" @click="openDetailDialog(row)">详情</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next"
      @size-change="loadData"
      @current-change="loadData"
      style="margin-top: 20px"
    />

    <el-dialog v-model="assignDialogVisible" title="分配维修人员" width="400px">
      <el-form>
        <el-form-item label="维修人员">
          <el-select v-model="assignForm.repairerId" placeholder="选择维修人员" style="width: 100%">
            <el-option v-for="r in repairers" :key="r.id" :label="r.realName" :value="r.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign">确定分配</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="报修详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="标题">{{ currentRepair?.title }}</el-descriptions-item>
        <el-descriptions-item label="类别">{{ currentRepair?.category }}</el-descriptions-item>
        <el-descriptions-item label="报修人">{{ currentRepair?.ownerName }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ currentRepair?.ownerBuilding }}-{{ currentRepair?.ownerUnit }}-{{ currentRepair?.ownerRoomNumber }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ currentRepair?.description }}</el-descriptions-item>
        <el-descriptions-item label="维修人员">{{ currentRepair?.repairerName || '未分配' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getStatusName(currentRepair?.status || '') }}</el-descriptions-item>
        <el-descriptions-item label="维修备注" :span="2">{{ currentRepair?.repairRemark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentRepair?.createTime }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ currentRepair?.completeTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { repairApi, userApi } from '@/api'
import type { Repair, User } from '@/types'

const tableData = ref<Repair[]>([])
const repairers = ref<User[]>([])
const loading = ref(false)

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchStatus = ref('')
const searchKeyword = ref('')

const assignDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentRepair = ref<Repair | null>(null)

const assignForm = reactive({
  repairId: null as number | null,
  repairerId: null as number | null
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await repairApi.getPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      status: searchStatus.value,
      keyword: searchKeyword.value
    })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadRepairers = async () => {
  try {
    const res = await userApi.getList()
    repairers.value = (res.data || []).filter((u: User) => u.role === 'REPAIRER')
  } catch (error) {
    console.error(error)
  }
}

const openAssignDialog = (row: Repair) => {
  currentRepair.value = row
  assignForm.repairId = row.id
  assignForm.repairerId = null
  assignDialogVisible.value = true
}

const submitAssign = async () => {
  if (!assignForm.repairerId) {
    ElMessage.warning('请选择维修人员')
    return
  }
  try {
    await repairApi.assign(assignForm.repairId!, assignForm.repairerId!)
    ElMessage.success('分配成功')
    assignDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const openDetailDialog = (row: Repair) => {
  currentRepair.value = row
  detailDialogVisible.value = true
}

const handleDelete = (row: Repair) => {
  ElMessageBox.confirm('确定要删除该报修记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await repairApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
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
  loadRepairers()
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
