<template>
  <div class="page-container">
    <div class="toolbar">
      <h2>公告管理</h2>
      <div>
        <el-select v-model="searchType" placeholder="类型" clearable style="width: 120px; margin-right: 10px">
          <el-option label="通知" value="NOTICE" />
          <el-option label="活动" value="ACTIVITY" />
          <el-option label="系统公告" value="SYSTEM" />
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
        <el-button type="primary" @click="openDialog('create')">
          <el-icon><Plus /></el-icon> 发布公告
        </el-button>
      </div>
    </div>

    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getTypeTag(row.type)">{{ getTypeName(row.type) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="优先级" width="100" />
      <el-table-column prop="publisherName" label="发布人" width="100" />
      <el-table-column prop="publishTime" label="发布时间" width="160" />
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
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" style="width: 100%">
            <el-option label="通知" value="NOTICE" />
            <el-option label="活动" value="ACTIVITY" />
            <el-option label="系统公告" value="SYSTEM" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="form.priority" :min="0" :max="100" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="6" />
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
import { noticeApi } from '@/api'
import type { Notice } from '@/types'

const tableData = ref<Notice[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const dialogType = ref<'create' | 'edit'>('create')
const formRef = ref<FormInstance>()

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchType = ref('')

const form = reactive<any>({
  title: '',
  content: '',
  type: 'NOTICE',
  priority: 0
})

const rules: FormRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
}

const dialogTitle = computed(() => dialogType.value === 'create' ? '发布公告' : '编辑公告')

const loadData = async () => {
  loading.value = true
  try {
    const res = await noticeApi.getPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      type: searchType.value
    })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const openDialog = (type: 'create' | 'edit', row?: Notice) => {
  dialogType.value = type
  if (type === 'edit' && row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      title: '',
      content: '',
      type: 'NOTICE',
      priority: 0
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
          await noticeApi.create(form)
          ElMessage.success('发布成功')
        } else {
          await noticeApi.update(form.id, form)
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

const handleDelete = (row: Notice) => {
  ElMessageBox.confirm('确定要删除该公告吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await noticeApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

const getTypeName = (type: string) => {
  const names: Record<string, string> = {
    NOTICE: '通知',
    ACTIVITY: '活动',
    SYSTEM: '系统公告'
  }
  return names[type] || type
}

const getTypeTag = (type: string) => {
  const tags: Record<string, string> = {
    NOTICE: '',
    ACTIVITY: 'success',
    SYSTEM: 'warning'
  }
  return tags[type] || 'info'
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
