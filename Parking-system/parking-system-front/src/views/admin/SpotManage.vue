<!-- src/views/admin/SpotManage.vue -->
<template>
  <div>
    <!-- 筛选栏 -->
    <div class="filter">
      <el-select v-model="query.areaId" placeholder="全部区域" clearable style="width: 160px">
        <el-option v-for="a in areas" :key="a.id" :label="a.areaName" :value="a.id" />
      </el-select>
      <el-select v-model="query.status" placeholder="全部状态" clearable style="width: 140px">
        <el-option label="空闲" :value="0" />
        <el-option label="占用" :value="1" />
        <el-option label="已预约" :value="2" />
        <el-option label="停用" :value="3" />
      </el-select>
      <el-input v-model="query.spotCode" placeholder="车位编号" clearable style="width: 160px" />
      <el-button type="primary" @click="search">查询</el-button>
      <el-button type="success" @click="openDialog()">新增车位</el-button>
    </div>

    <el-table :data="list" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="spotCode" label="车位编号" width="120" />
      <el-table-column prop="areaName" label="所属区域" />
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag :type="['success', 'danger', 'warning', 'info'][row.status]">
            {{ ['空闲', '占用', '已预约', '停用'][row.status] }}
          </el-tag>
        </template>
      </el-table-column>
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
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑车位' : '新增车位'" width="420px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="所属区域" prop="areaId">
          <el-select v-model="form.areaId" placeholder="请选择区域" style="width: 100%">
            <el-option v-for="a in areas" :key="a.id" :label="a.areaName" :value="a.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="车位编号" prop="spotCode">
          <el-input v-model="form.spotCode" placeholder="如：A-006" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="0">空闲</el-radio>
            <el-radio :value="1">占用</el-radio>
            <el-radio :value="2">已预约</el-radio>
            <el-radio :value="3">停用</el-radio>
          </el-radio-group>
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
import { areaListApi } from '@/api/area'
import { spotPageApi, spotAddApi, spotUpdateApi, spotDeleteApi } from '@/api/spot'

const list = ref([])
const total = ref(0)
const areas = ref([])
const loading = ref(false)
const query = reactive({ pageNum: 1, pageSize: 10, areaId: null, status: null, spotCode: '' })
const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, areaId: null, spotCode: '', status: 0 })

const rules = {
  areaId: [{ required: true, message: '请选择区域', trigger: 'change' }],
  spotCode: [{ required: true, message: '请输入车位编号', trigger: 'blur' }]
}

async function loadData() {
  loading.value = true
  try {
    const page = await spotPageApi(query)
    list.value = page.records
    total.value = page.total
  } finally {
    loading.value = false
  }
}

function search() {
  query.pageNum = 1
  loadData()
}

function openDialog(row) {
  Object.assign(form, row || { id: null, areaId: null, spotCode: '', status: 0 })
  dialogVisible.value = true
}

async function onSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  form.id ? await spotUpdateApi(form) : await spotAddApi(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  loadData()
}

async function onDelete(row) {
  await ElMessageBox.confirm(`确定删除车位「${row.spotCode}」吗？`, '提示', { type: 'warning' })
  await spotDeleteApi(row.id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(async () => {
  areas.value = await areaListApi()
  loadData()
})
</script>

<style scoped>
.filter { display: flex; gap: 10px; margin-bottom: 12px; }
.pager { margin-top: 12px; justify-content: flex-end; }
</style>
