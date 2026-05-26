<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>缴费管理</h2>
      <div>
        <el-select v-model="searchStatus" placeholder="状态" clearable style="width: 120px; margin-right: 10px">
          <el-option label="未支付" value="UNPAID" />
          <el-option label="已支付" value="PAID" />
          <el-option label="逾期" value="OVERDUE" />
        </el-select>
        <el-input v-model="searchPeriod" placeholder="缴费周期 如:2024-01" style="width: 150px; margin-right: 10px" clearable />
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="primary" @click="openDialog('create')">
          <el-icon><Plus /></el-icon> 新增账单
        </el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column label="业主信息" min-width="150">
        <template #default="{ row }">
          {{ row.ownerName }}<br/>
          <span style="font-size: 12px; color: #999">{{ row.ownerBuilding }}-{{ row.ownerUnit }}-{{ row.ownerRoomNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="paymentType" label="类型" width="120">
        <template #default="{ row }">
          <el-tag>{{ getTypeName(row.paymentType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="period" label="周期" width="100" />
      <el-table-column prop="amount" label="金额" width="120">
        <template #default="{ row }">
          <span style="color: #f56c6c; font-weight: bold">¥{{ row.amount }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="dueDate" label="到期日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusName(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="paidTime" label="支付时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openDialog('edit', row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="业主" prop="ownerId">
          <el-select v-model="form.ownerId" placeholder="选择业主" style="width: 100%">
            <el-option v-for="o in owners" :key="o.id" :label="`${o.realName} - ${o.building}-${o.unit}-${o.roomNumber}`" :value="o.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="缴费类型" prop="paymentType">
          <el-select v-model="form.paymentType" style="width: 100%">
            <el-option label="物业费" value="PROPERTY_FEE" />
            <el-option label="水费" value="WATER" />
            <el-option label="电费" value="ELECTRIC" />
            <el-option label="燃气费" value="GAS" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="缴费周期" prop="period">
          <el-input v-model="form.period" placeholder="如: 2024-01" />
        </el-form-item>
        <el-form-item label="到期日期" prop="dueDate">
          <el-date-picker v-model="form.dueDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { paymentApi, ownerApi } from '@/api'
import type { Payment, Owner } from '@/types'

const tableData = ref<Payment[]>([])
const owners = ref<Owner[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref<'create' | 'edit'>('create')
const formRef = ref<FormInstance>()

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchStatus = ref('')
const searchPeriod = ref('')

const form = reactive<any>({
  ownerId: null,
  paymentType: 'PROPERTY_FEE',
  amount: 0,
  period: '',
  dueDate: ''
})

const rules: FormRules = {
  ownerId: [{ required: true, message: '请选择业主', trigger: 'change' }],
  paymentType: [{ required: true, message: '请选择缴费类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  period: [{ required: true, message: '请输入缴费周期', trigger: 'blur' }]
}

const dialogTitle = computed(() => dialogType.value === 'create' ? '新增账单' : '编辑账单')

const loadData = async () => {
  loading.value = true
  try {
    const res = await paymentApi.getPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      status: searchStatus.value,
      period: searchPeriod.value
    })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadOwners = async () => {
  try {
    const res = await ownerApi.getAll()
    owners.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const openDialog = (type: 'create' | 'edit', row?: Payment) => {
  dialogType.value = type
  if (type === 'edit' && row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      ownerId: null,
      paymentType: 'PROPERTY_FEE',
      amount: 0,
      period: '',
      dueDate: ''
    })
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'create') {
          await paymentApi.create(form)
          ElMessage.success('创建成功')
        } else {
          await paymentApi.update(form.id, form)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleDelete = (row: Payment) => {
  ElMessageBox.confirm('确定要删除该账单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await paymentApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

const getTypeName = (type: string) => {
  const names: Record<string, string> = {
    PROPERTY_FEE: '物业费',
    WATER: '水费',
    ELECTRIC: '电费',
    GAS: '燃气费'
  }
  return names[type] || type
}

const getStatusName = (status: string) => {
  const names: Record<string, string> = {
    UNPAID: '未支付',
    PAID: '已支付',
    OVERDUE: '逾期'
  }
  return names[status] || status
}

const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    UNPAID: 'warning',
    PAID: 'success',
    OVERDUE: 'danger'
  }
  return types[status] || 'info'
}

onMounted(() => {
  loadData()
  loadOwners()
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
