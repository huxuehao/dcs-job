<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  logDelete,
  logPage,
  logSelectOne,
} from "@/api/log";
import {
  taskList
} from "@/api/task";
import CodeEditor from "@/components/code/CodeEditor.vue";
// 初始化
onMounted(() => {
  load();
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
// 查询参数
let queryParams = reactive<any>({});
// 当前所选行
let selectedRows = ref<any>([]);
// 分页配置
let page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
// 加载列表数据
const load = () => {
  loading.value = true;
  taskList()
    .then((taskRes) => {
      const taskList = taskRes.data.data;
      logPage({ ...page,...queryParams})
        .then((res) => {
          dataList.value = res.data.data["records"];
          page.total = res.data.data["total"];
          dataList.value.forEach((bItem: any) => {
            const aItem = taskList.find(
              (aItem: any) => aItem.id === bItem.taskId
            );
            if (aItem) {
              bItem.name = aItem.name;
            } else {
              bItem.name = "--";
            }
          })
        })
        .finally(() => {
          loading.value = false;
        });
    })
    .catch(() => {
      loading.value = false;
    })
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
    logDelete(rows.map((item: any) => item.id))
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

const changePageNo = (pageNo: number) => {
  page.current = pageNo;
  load();
};
const changePageSize = (pageSize: number) => {
  page.size = pageSize;
  load();
};

const dialogProp = reactive({
  visible: false,
  withHeader: false,
  showClose: true,
  appendToBody: true, 
  closeOnPressEscape: false,
  title: "错误日志",
  size:"100%",
  direction: "btt"
});
let logLoading = ref(false)
let logText = ref("")
const handleViewLog = (row: any) => {
  logLoading.value = true
  dialogProp.visible = true;
  logSelectOne(row.id).then((res) => {
    logText.value = res.data.data.content;
  }).catch(() => {
    dialogProp.visible = false;
  }).finally(() => {
    logLoading.value = false
  });
};

const closeGlue = () => {
  dialogProp.visible = false;
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
        <el-form-item label="任务名称" prop="taskName" class="form-item">
          <el-input
            v-model="queryParams.taskName"
            placeholder="任务名称"
            class="input-search"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="执行结果" prop="executeStatus" class="form-item">
          <el-select
            v-model="queryParams.executeStatus"
            placeholder="执行结果"
            class="input-search"
            clearable
          >
            <el-option label="成功" value="success"></el-option>
            <el-option label="失败" value="fail"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行时间" prop="executeTime" class="form-item">
          <el-date-picker
            v-model="queryParams.executeTime"
            type="datetimerange"
            style="width: 320px;"
            start-placeholder="起始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            date-format="yyyy-MM-dd HH:mm:ss"
          />
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
          v-permission="'menu_delete'"
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
      :style="[{ height: 'calc(100% - var(--table-hg-150) - ' + tHeight + 'px)' }]"
    >
      <el-table
        :stripe="true"
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
        <el-table-column label="任务名称" prop="name" show-overflow-tooltip>
        </el-table-column>
        <el-table-column
          label="执行时间"
          prop="executeTime"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          label="执行结果"
          prop="executeStatus"
          align="center"
        >
          <template #default="scope">
            <el-tag
              v-if="scope.row.executeStatus === 'success'"
              disable-transitions
              type="success"
              >成功</el-tag
            >
            <el-tag
              v-else-if="scope.row.executeStatus === 'fail'"
              disable-transitions
              type="danger"
              >失败</el-tag
            >
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="scope">
            <el-button
              v-permission="'menu_view'"
              link
              type="primary"
              icon="Tickets"
              :disabled="scope.row.executeStatus !== 'fail'"
              @click="handleViewLog(scope.row)"
              >日志</el-button
            >
            <el-button
              v-permission="'menu_delete'"
              link
              type=""
              icon="Delete"
              style="color: #f63434"
              @click="handleDelete([scope.row])"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 分页区域 -->
    <div class="page-box">
      <el-pagination
        class="page"
        background
        layout="total,sizes,prev,pager,next,jumper"
        :total="page.total"
        :current-page.sync="page.current"
        :page-sizes="[20, 30, 40, 50, 60]"
        :page-size="page.size"
        @current-change="changePageNo"
        @size-change="changePageSize"
      />
    </div>
    <!-- 日志预览 -->
    <el-drawer
      v-bind="dialogProp"
      v-model="dialogProp.visible"
      class="drawer-none-padding">
      <CodeEditor 
        v-if="dialogProp.visible"
        v-model="logText" 
        @close="closeGlue()"
        title="错误日志"
        readOnly
        v-loading="logLoading" 
        element-loading-background="rgba(0, 0, 0, 0)">
      </CodeEditor>
  </el-drawer>
  </div>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;
</style>
