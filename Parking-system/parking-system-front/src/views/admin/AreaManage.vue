<!-- src/views/admin/AreaManage.vue -->
<template>
  <div>
    <el-button type="primary" @click="openDialog()" style="margin-bottom: 12px">新增停车区</el-button>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="areaName" label="区域名称" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="description" label="说明" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      class="pager"
      v-model:current-page="query.pageNum"
      :page-size="query.pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadData"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑区域' : '新增区域'" width="450px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="区域名称" prop="areaName">
          <el-input v-model="form.areaName" placeholder="如：教学楼A区" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="说明" prop="description">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { areaListApi, areaAddApi, areaUpdateApi, areaDeleteApi } from '@/api/area'

const list = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10 })
const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, areaName: '', location: '', description: '' })

const rules = {
  areaName: [{ required: true, message: '请输入区域名称', trigger: 'blur' }]
}

async function loadData() {
  loading.value = true
  try {
    const page = await areaPageApi(query)
    list.value = page.records
    total.value = page.total
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  // 编辑回填 / 新增清空
  Object.assign(form, row || { id: null, areaName: '', location: '', description: '' })
  dialogVisible.value = true
}

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  form.id ? await areaUpdateApi(form) : await areaAddApi(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  loadData()
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确定删除「${row.areaName}」吗？`, '提示', { type: 'warning' })
  await areaDeleteApi(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.pager { margin-top: 12px; justify-content: flex-end; }
</style>
