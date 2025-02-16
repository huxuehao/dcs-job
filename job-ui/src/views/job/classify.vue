<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  classifyAdd,
  classifyUpdate,
  classifyDelete,
  classifyTree,
  classifySelectOne,
} from "@/api/classify";

const prop = {
  value: "id",
  label: "name",
  children: "children",
};

// 初始化
onMounted(() => {
  load();
  tree();
  monitorSearchResize();
});

let tHeight = ref(0);
let searchBox = ref();
const monitorSearchResize = () => {
  const resizeObserver = new ResizeObserver((entries) => {
    for (const entry of entries) {
      const { height } = entry.contentRect;
      tHeight.value = height;
    }
  });
  resizeObserver.observe(searchBox.value);
};

let loading = ref(false);
// 列表数据
let dataList = ref<any>([]);
let dataTree = ref<any>([]);

// 表单数据
let formData = ref<any>({});
const rules = {
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  code: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  sort: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
};
// 查询参数
let queryParams = reactive<any>({});
// 当前所选行
let selectedRows = ref<any>([]);

// 加载列表数据
const load = () => {
  loading.value = true;
  classifyTree(queryParams)
    .then((res) => {
      dataList.value = res.data.data;
    })
    .finally(() => {
      loading.value = false;
    });
};
// 加载列表数据
const tree = () => {
  classifyTree({}).then((res) => {
    dataTree.value = res.data.data;
  });
};
// 点击搜索
const handleSearch = () => {
  load();
};
// 点击重置
const handleReset = () => {
  for (let key in queryParams) {
    if (queryParams.hasOwnProperty(key)) {
      queryParams[key] = null;
    }
  }
  load();
};

let mode = ref("add");
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "20vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: false,
});
// 点击新增
const handleAdd = () => {
  mode.value = "add";
  stopBtn.value = false;
  formData.value = {
    type: 1,
    valid: 1,
  };
  dialogProp.title = "新增";
  dialogProp.visible = true;
};
let form = ref();
const handleAddDo = () => {
  form.value.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        classifyAdd(formData.value)
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
            tree();
          })
          .finally(() => {
            stopBtn.value = false;
          });
      } else if (mode.value === "edit") {
        classifyUpdate(formData.value)
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
            tree();
          })
          .finally(() => {
            stopBtn.value = false;
          });
      } else {
        loading.value = false;
        ElMessage({
          message: "模式不匹配",
          type: "error",
          plain: true,
        });
      }
    }
  });
};

const handleDelete = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  ElMessageBox.confirm("此操作将删除所选择的数据 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    classifyDelete(rows.map((item: any) => item.id))
      .then(() => {
        ElMessage({
          message: "删除成功",
          type: "success",
          plain: true,
        });
        load();
      })
      .catch(() => {
        loading.value = false;
      })
      .finally(() => {
        selectedRows.value = [];
      });
  });
};

const handleView = (row: any) => {
  mode.value = "view";
  stopBtn.value = false;
  formData.value = row;
  dialogProp.title = "查看";
  dialogProp.visible = true;
  classifySelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    dialogProp.visible = true;
  });
};

const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  formData.value = row;
  dialogProp.title = "编辑";
  dialogProp.visible = true;
  classifySelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    dialogProp.visible = true;
  });
};

const handleAddChild = (row: any) => {
  mode.value = "add";
  stopBtn.value = false;
  formData.value = {};
  formData.value.parentId = row.id;
  dialogProp.title = "新增子项";
  dialogProp.visible = true;
};
</script>

<template>
  <div style="height: 100%">
    <!-- 查询区域-->
    <div class="search-box" ref="searchBox">
      <el-form
        ref="queryForm"
        class="query-form"
        :model="queryParams"
        :inline="true"
        label-width="70px"
      >
        <el-form-item label="上级分类" prop="parentId" class="form-item">
          <el-tree-select
            v-model="queryParams.parentId"
            :data="dataTree"
            :render-after-expand="true"
            :default-expand-all="false"
            :props="prop"
            class="input-search"
            placeholder="上级分类"
            filterable
            check-strictly
            clearable
          ></el-tree-select>
        </el-form-item>
        <el-form-item label="分类名称" prop="name" class="form-item">
          <el-input
            v-model="queryParams.name"
            placeholder="分类名称"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="分类编码" prop="code" class="form-item">
          <el-input
            v-model="queryParams.code"
            placeholder="分类编码"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item class="form-item">
          <el-button type="primary" icon="Search" @click="handleSearch"
            >查询</el-button
          >
        </el-form-item>
        <el-form-item class="form-item">
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮区域-->
    <el-row class="button-box">
      <el-col :span="16">
        <el-button
          v-permission="'task_classify_add'"
          type="primary"
          icon="CirclePlus"
          @click="handleAdd()"
          >新增</el-button
        >
        <el-button
          v-permission="'task_classify_delete'"
          type="danger"
          icon="Delete"
          @click="handleDelete(selectedRows)"
        >
          删除
        </el-button>
      </el-col>
      <el-col :span="8" style="text-align: right">
        <el-button icon="RefreshRight" @click="handleSearch" title="刷新"
          >刷新</el-button
        >
      </el-col>
    </el-row>
    <!-- 列表区域 -->
    <div
      class="data-list"
      :style="[{ height: 'calc(100vh - 150px - ' + tHeight + 'px)' }]"
    >
      <el-table
        :stripe="false"
        :data="dataList"
        :header-cell-style="{
          backgroundColor: '#F5F7FA',
          color: '#666666',
        }"
        height="100%"
        v-loading="loading"
        @selection-change="(val: any) => { selectedRows = val }"
        row-key="id"
        border
      >
        <el-table-column
          type="selection"
          width="40"
          align="center"
        ></el-table-column>
        <el-table-column
          label="#"
          type="index"
          width="40"
          align="center"
        ></el-table-column>
        <el-table-column
          label="分类名称"
          prop="name"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="分类编码"
          prop="code"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="分类排序"
          prop="sort"
          width="100"
          align="center"
        ></el-table-column>
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="'task_classify_view'"
              link
              type=""
              icon="View"
              @click="handleView(scope.row)"
              >查看</el-button
            >
            <el-button
              v-permission="'task_classify_edit'"
              link
              type=""
              icon="Edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-permission="'task_classify_delete'"
              link
              type=""
              icon="Delete"
              style="color: #f63434"
              @click="handleDelete([scope.row])"
            >
              删除
            </el-button>
            <el-button
              v-permission="'task_classify_add_child'"
              link
              type=""
              icon="CirclePlus"
              @click="handleAddChild(scope.row)"
            >
              新增子项
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <el-form
      v-if="dialogProp.visible"
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="80px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="上级分类" prop="parentId">
            <el-tree-select
              v-model="formData.parentId"
              :data="dataTree"
              :render-after-expand="true"
              :default-expand-all="false"
              :props="prop"
              :disabled="mode === 'view'"
              placeholder="请选择上级分类"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分类名称" prop="name">
            <el-input
              placeholder="请填写分类名称"
              maxlength="40"
              :disabled="mode === 'view'"
              v-model="formData.name"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="分类编号" prop="code">
            <el-input
              placeholder="请填写分类编号"
              maxlength="40"
              :disabled="mode === 'view'"
              v-model="formData.code"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分类排序" prop="sort">
            <el-input
              placeholder="请填写分类排序"
              v-model="formData.sort"
              :disabled="mode === 'view'"
              type="number"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="备注说明" prop="remark">
            <el-input
              type="textarea"
              placeholder="请填写备注说明"
              maxlength="100"
              :disabled="mode === 'view'"
              v-model="formData.remark"
              show-word-limit
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button
        v-if="mode !== 'view'"
        type="primary"
        @click="handleAddDo"
        icon="CircleCheck"
        :loading="stopBtn"
        >保 存</el-button
      >
      <el-button @click="dialogProp.visible = false" icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;

.search-box,
.button-box,
.data-list {
  margin-left: 10px;
}
</style>
