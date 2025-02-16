<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
import {
  logPage,
  logSelectOne,
} from "@/api/log";
import CodeEditor from "@/components/code/CodeEditor.vue";

const props = defineProps({
  taskId: {
      type: String
  }
})
// 初始化
onMounted(() => {
  load();
});

let loading = ref(false);
// 列表数据
let dataList = ref<any>([]);
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
  logPage({ ...page,taskId: props.taskId})
    .then((res) => {
      dataList.value = res.data.data["records"];
      page.total = res.data.data["total"];
    })
    .finally(() => {
      loading.value = false;
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
<!-- 列表区域 -->
<div
    class="data-list"
    :style="[{ height: 'calc(50vh)' }]"
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
    <el-table-column label="操作" width="100" align="center" fixed="right">
        <template #default="scope">
        <el-button
            v-permission="'menu_view'"
            link
            type="primary"
            icon="Tickets"
            :disabled="scope.row.executeStatus !== 'fail'"
            @click="handleViewLog(scope.row)"
            >日志</el-button>
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
</template>

<style lang="scss" scoped>
  .page-box {
    padding: 10px 10px 0 0;
    background-color: white;
    .page {
      float: right;
    }
  }
</style>