<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>楼栋管理</h2>
      <el-button type="primary" @click="openDialog('create')">
        <el-icon><Plus /></el-icon> 新增楼栋
      </el-button>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="buildingName" label="楼栋名称" />
      <el-table-column prop="buildingCode" label="编码" width="100" />
      <el-table-column prop="unitCount" label="单元数" width="100" />
      <el-table-column prop="floorCount" label="楼层数" width="100" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '停用' }}
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="楼栋名称" prop="buildingName">
          <el-input v-model="form.buildingName" />
        </el-form-item>
        <el-form-item label="楼栋编码" prop="buildingCode">
          <el-input v-model="form.buildingCode" />
        </el-form-item>
        <el-form-item label="单元数" prop="unitCount">
          <el-input-number v-model="form.unitCount" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="楼层数" prop="floorCount">
          <el-input-number v-model="form.floorCount" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { buildingApi } from '@/api'
import type { Building } from '@/types'

const tableData = ref<Building[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref<'create' | 'edit'>('create')
const formRef = ref<FormInstance>()

const form = reactive<any>({
  buildingName: '',
  buildingCode: '',
  unitCount: 1,
  floorCount: 1,
  description: ''
})

const rules: FormRules = {
  buildingName: [{ required: true, message: '请输入楼栋名称', trigger: 'blur' }],
  buildingCode: [{ required: true, message: '请输入楼栋编码', trigger: 'blur' }]
}

const dialogTitle = computed(() => dialogType.value === 'create' ? '新增楼栋' : '编辑楼栋')

const loadData = async () => {
  loading.value = true
  try {
    const res = await buildingApi.getAll()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const openDialog = (type: 'create' | 'edit', row?: Building) => {
  dialogType.value = type
  if (type === 'edit' && row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      buildingName: '',
      buildingCode: '',
      unitCount: 1,
      floorCount: 1,
      description: ''
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
          await buildingApi.create(form)
          ElMessage.success('创建成功')
        } else {
          await buildingApi.update(form.id, form)
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

const handleDelete = (row: Building) => {
  ElMessageBox.confirm('确定要删除该楼栋吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await buildingApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
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
