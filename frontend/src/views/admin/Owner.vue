<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>业主管理</h2>
      <div>
        <el-select v-model="searchBuilding" placeholder="选择楼栋" clearable style="width: 150px; margin-right: 10px">
          <el-option v-for="b in buildings" :key="b.buildingCode" :label="b.buildingName" :value="b.buildingCode" />
        </el-select>
        <el-input v-model="searchKeyword" placeholder="搜索姓名/房号" style="width: 200px; margin-right: 10px" clearable />
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="primary" @click="openDialog('create')">
          <el-icon><Plus /></el-icon> 新增业主
        </el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column label="房间信息" width="180">
        <template #default="{ row }">
          {{ row.building }}-{{ row.unit }}-{{ row.roomNumber }}
        </template>
      </el-table-column>
      <el-table-column prop="checkInDate" label="入住日期" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '已入住' : '未入住' }}
          </el-tag>
        </template>
      </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="关联用户" prop="userId" v-if="dialogType === 'create'">
          <el-select v-model="form.userId" filterable placeholder="选择用户" style="width: 100%">
            <el-option v-for="u in users" :key="u.id" :label="`${u.username} - ${u.realName}`" :value="u.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋" prop="building">
          <el-input v-model="form.building" />
        </el-form-item>
        <el-form-item label="单元" prop="unit">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="房号" prop="roomNumber">
          <el-input v-model="form.roomNumber" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" />
        </el-form-item>
        <el-form-item label="入住日期" prop="checkInDate">
          <el-date-picker v-model="form.checkInDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
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
import { ownerApi, userApi, buildingApi } from '@/api'
import type { Owner, User, Building } from '@/types'

const tableData = ref<Owner[]>([])
const users = ref<User[]>([])
const buildings = ref<Building[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref<'create' | 'edit'>('create')
const formRef = ref<FormInstance>()

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchBuilding = ref('')
const searchKeyword = ref('')

const form = reactive<any>({
  userId: null,
  building: '',
  unit: '',
  roomNumber: '',
  idCard: '',
  checkInDate: ''
})

const rules: FormRules = {
  userId: [{ required: true, message: '请选择用户', trigger: 'change' }],
  building: [{ required: true, message: '请输入楼栋', trigger: 'blur' }],
  roomNumber: [{ required: true, message: '请输入房号', trigger: 'blur' }]
}

const dialogTitle = computed(() => dialogType.value === 'create' ? '新增业主' : '编辑业主')

const loadData = async () => {
  loading.value = true
  try {
    const res = await ownerApi.getPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      building: searchBuilding.value,
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

const loadUsers = async () => {
  try {
    const res = await userApi.getList()
    users.value = (res.data || []).filter((u: User) => u.role === 'OWNER')
  } catch (error) {
    console.error(error)
  }
}

const loadBuildings = async () => {
  try {
    const res = await buildingApi.getAll()
    buildings.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const openDialog = (type: 'create' | 'edit', row?: Owner) => {
  dialogType.value = type
  if (type === 'edit' && row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      userId: null,
      building: '',
      unit: '',
      roomNumber: '',
      idCard: '',
      checkInDate: ''
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
          await ownerApi.create(form)
          ElMessage.success('创建成功')
        } else {
          await ownerApi.update(form.id, form)
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

const handleDelete = (row: Owner) => {
  ElMessageBox.confirm('确定要删除该业主吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await ownerApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
  loadUsers()
  loadBuildings()
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
