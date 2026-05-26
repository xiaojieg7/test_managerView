<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>我的缴费</h2>
      <div>
        <span class="unpaid-tip">待缴费总额：</span>
        <span class="unpaid-amount">¥{{ unpaidAmount }}</span>
      </div>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
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
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 'UNPAID' || row.status === 'OVERDUE'"
            type="primary"
            size="small"
            @click="handlePay(row)"
          >
            去支付
          </el-button>
          <span v-else style="color: #999">-</span>
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
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { paymentApi } from '@/api'
import type { Payment } from '@/types'

const tableData = ref<Payment[]>([])
const loading = ref(false)
const unpaidAmount = ref(0)

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const res = await paymentApi.getMyPayments({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadUnpaidAmount = async () => {
  try {
    const res = await paymentApi.getMyUnpaid()
    unpaidAmount.value = res.data || 0
  } catch (error) {
    console.error(error)
  }
}

const handlePay = (row: Payment) => {
  ElMessageBox.confirm(`确定要支付 ${getTypeName(row.paymentType)} ¥${row.amount} 吗？`, '确认支付', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(async () => {
    try {
      await paymentApi.pay(row.id, 'ONLINE')
      ElMessage.success('支付成功')
      loadData()
      loadUnpaidAmount()
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
  loadUnpaidAmount()
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

.unpaid-tip {
  color: #666;
  font-size: 14px;
}

.unpaid-amount {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
}
</style>
