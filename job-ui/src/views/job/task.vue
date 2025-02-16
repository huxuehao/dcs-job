<script lang="ts" setup>
import { onMounted, reactive, ref } from "vue";
// 拖动组件
import ResizeBox from "@/components/resize/ResizeBox.vue";
// 左侧分类树
import SearchTree from "@/components/tree/SearchTree.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { classifyTree } from "@/api/classify";
import SparkMd5 from "spark-md5";
import {
  taskAdd,
  taskUpdate,
  taskDelete,
  taskPage,
  taskSelectOne,
  taskEnable,
  taskDisable,
  taskFutureExecutionPlan,
  taskExecuteOnece
} from "@/api/task";
import {
  glueSave,
  glueSavePublish,
  glueListByTaskId,
  glueSelectOne
} from "@/api/glue";
import TaskForm from "@/components/task/TaskForm.vue";
import CodeEditor from "@/components/code/CodeEditor.vue";
import { formatJavaCode } from "@/utils/tools";
import TaskLog from "@/components/task/TaskLog.vue";

const prop = {
  value: "id",
  label: "name",
  children: "children",
};

let currentTaskClassify = ref(null);
let classifyTreeList = ref<any>([]);
// 初始化
onMounted(() => {
  classifyTree({}).then((res: any) => {
    classifyTreeList.value = res.data.data;
  });
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
// 分页配置
let page = reactive({
  total: 0,
  current: 1,
  size: 20,
});
// 当前所选行
let selectedRows = ref<any>([]);

//  点击分类树
const handleNodeClick = (node: any) => {
  currentTaskClassify.value = node.id;
  load();
};
// 加载列表数据
const load = () => {
  loading.value = true;
  taskPage({ ...page, ...queryParams, taskClassify: currentTaskClassify.value })
    .then((res) => {
      dataList.value = res.data.data["records"];
      page.total = res.data.data["total"];
    })
    .finally(() => {
      loading.value = false;
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
  currentTaskClassify.value = null;
  queryParams.share = 1;
  load();
};


let mode = ref("add");
let addVisible = ref(false);
let formData = ref<any>({
  valid: 1,
});
let showShare = ref(true);
let stopBtn = ref(false);
const dialogProp = reactive({
  visible: false,
  title: "",
  top: "5vh",
  width: "60%",
  modal: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: false,
});
// 点击新增
const handleAdd = () => {
  mode.value = "add";
  addVisible.value = false;
  formData.value = {
    valid: 1,
  };
  showShare.value = true;
  stopBtn.value = false;
  dialogProp.title = "新增";
  dialogProp.visible = true;
};

let form = ref();
// 新增
const handleAddDo = () => {
  form.value.form.validate((valid: any) => {
    if (valid) {
      stopBtn.value = true;
      if (mode.value === "add") {
        taskAdd(form.value.getData())
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
          })
          .finally(() => {
            stopBtn.value = false;
          });
      } else if (mode.value === "edit") {
        taskUpdate(form.value.getData())
          .then(() => {
            dialogProp.visible = false;
            ElMessage({
              message: "保存成功",
              type: "success",
              plain: true,
            });
            load();
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

// 查看
const handleView = (row: any) => {
  mode.value = "view";
  stopBtn.value = false;
  dialogProp.title = "查看";
  taskSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    if(formData.value.type === "TEMPLATE") {
      formData.value.config = JSON.parse(res.data.data.config || "[]");
    }
    dialogProp.visible = true;
  });
};

// 删除
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
    taskDelete(rows.map((item: any) => item.id))
      .then((res) => {
        if (res.data.code === 200) {
          ElMessage({
            message: "删除成功",
            type: "success",
            plain: true,
          });
        }
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

const handleEdit = (row: any) => {
  mode.value = "edit";
  stopBtn.value = false;
  dialogProp.title = "编辑";
  taskSelectOne(row.id).then((res) => {
    formData.value = res.data.data;
    if(formData.value.type === "TEMPLATE") {
      formData.value.config = JSON.parse(res.data.data.config || "[]");
    }
    dialogProp.visible = true;
  });
};

// 启用
const handleEnable = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  ElMessageBox.confirm("此操作将启动所选择的任务 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    taskEnable(rows.map((item: any) => item.id))
      .then((res) => {
        if (res.data.code === 200) {
          ElMessage({
            message: "删除成功",
            type: "success",
            plain: true,
          });
        }
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

// 停用
const handleUnEnable = (rows: any) => {
  if (!rows || rows.length === 0) {
    ElMessage({
      message: "至少选择一条数据",
      type: "warning",
      plain: true,
    });
    return;
  }

  ElMessageBox.confirm("此操作将停止所选择的任务 , 是否确定?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    loading.value = true;
    taskDisable(rows.map((item: any) => item.id))
      .then((res) => {
        if (res.data.code === 200) {
          ElMessage({
            message: "删除成功",
            type: "success",
            plain: true,
          });
        }
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

// 执行一次
const handleExecuteOnece = (row: any) => {
  ElMessageBox.confirm(`此操作将触发 [ ${row.name} ] 任务执行一次 , 是否确定?`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    taskExecuteOnece(row.id).then(() => {
      ElMessage({
        message: "触发成功",
        type: "success",
        plain: true
      })
    })
  });
};

// 执行计划
let planList = ref<any[]>([])
const planListShow = reactive({
  visible: false,
  title: "未来10次执行计划",
  top: "5vh",
  width: "300px",
  modal: true,
  center: true,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: false,
});
const handleFutureExecutionPlan = async (cron: string) => {
  const plans = await taskFutureExecutionPlan(cron)
  planList.value = plans.data.data
  planListShow.visible = true
};

const changePageNo = (pageNo: number) => {
  page.current = pageNo;
  load();
};
const changePageSize = (pageSize: number) => {
  page.size = pageSize;
  load();
};

// 编辑Glue
const codeDialogProp = reactive({
  taskId: "",
  visible: false,
  withHeader: false,
  showClose: true,
  appendToBody: true, 
  closeOnPressEscape: false,
  title: "脚本编辑器-JAVA",
  size:"100%",
  direction: "btt"
});
let code = ref("")
let nextCode = ref("")
let codeLoading = ref(false)
// 打开
const openGlue = (row: any) => {
  code.value = ""
  nextCode.value = ""
  codeLoading.value = true
  codeDialogProp.visible = true
  codeDialogProp.taskId = row.id
  taskSelectOne(row.id).then((res) => {
    code.value = res.data.data.config || ""
    nextCode.value = SparkMd5.hash(code.value)
  }).catch(() => {
    codeDialogProp.visible = false
  }).finally(() => {
    codeLoading.value = false
  })
};
// 保存并发布
const saveAndPublishGlue = () => {
  ElMessageBox.prompt('请填写版本说明', '保存并发布', {
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(({ value }) => {
    codeLoading.value = true
    glueSavePublish({
      taskId: codeDialogProp.taskId,
      remark: value,
      config: code.value
    }).then(() => {
      nextCode.value = SparkMd5.hash(code.value)
      codeDialogProp.visible = false
      ElMessage({
        type: 'success',
        message: "保存并发布成功",
        plain: true
      })
    }).finally(() => {
      codeLoading.value = false
    })
  })
};
// 保存
const saveGlue = () => {
  ElMessageBox.prompt('请填写版本说明', '保存', {
    confirmButtonText: '确定',
    cancelButtonText: '取消'
  }).then(({ value }) => {
    codeLoading.value = true
    glueSave({
      taskId: codeDialogProp.taskId,
      remark: value,
      config: code.value
    }).then(() => {
      nextCode.value = SparkMd5.hash(code.value)
      ElMessage({
        type: 'success',
        message: "保存成功",
        plain: true
      })
    }).finally(() => {
      codeLoading.value = false
    })
  })
};
// 关闭
const closeGlue = () => {
  if(nextCode.value !== SparkMd5.hash(code.value)) {
    ElMessageBox.confirm(`当前内容未保存，是否确认关闭?`, "提示", {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    }).then(() => {
      codeDialogProp.visible = false
    });
  } else {
    codeDialogProp.visible = false
  }
};

// 版本控制
const glueVersion = reactive({
  visible: false,
  title: "历史版本",
  top: "10vh",
  width: "60vw",
  modal: true,
  center: false,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: true,
})
let glueList = ref<any[]>([])
const handleGlueList = () => {
  glueList.value = []
  glueListByTaskId(codeDialogProp.taskId).then((res) => {
    glueList.value = res.data.data
    glueVersion.visible = true
  })
};
// 载入
const loadGlueVersion = (row: any) => {
  ElMessageBox.confirm(`确认载入[ ${row.remark}] 版本?`, "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    glueVersion.visible = false
    codeLoading.value = true
    glueSelectOne(row.taskId, row.createTime).then((res) => {
      code.value = res.data.data.config
    }).finally(() => {
      codeLoading.value = false
    })
  });
};

const handleFormatJavaCode = () => {
  code.value = formatJavaCode(code.value)
};

const initJavaCode = () => {
code.value =
`import com.tiger.job.common.JobHandler;

public class Demo extends JobHandler {

    @Override
    public void execute() throws Exception {
        System.out.println("do something...");
    }
}`
}

// 执行日志
const logDialog = reactive({
  taskId: "",
  visible: false,
  title: "执行日志",
  top: "10vh",
  width: "50vw",
  modal: true,
  center: false,
  appendToBody: true,
  showClose: true,
  closeOnClickModal: true,
})

const showExecLog = (row: any) => {
  logDialog.taskId = row.id
  logDialog.visible = true
};

</script>

<template>
  <ResizeBox :initialWidth="250" :minWidth="250" :maxWidth="450">
    <template #left>
      <div class="resize-left-box">
        <SearchTree
          :data="classifyTreeList"
          title="分类树"
          @node-click="handleNodeClick"
          height="calc(100vh - 170px)"
        ></SearchTree>
      </div>
    </template>
    <template #right>
      <div class="resize-right-box">
        <!-- 查询区域-->
        <div class="search-box" ref="searchBox">
          <el-form
            ref="queryForm"
            class="query-form"
            :model="queryParams"
            :inline="true"
            label-width="70px"
          >
            <el-form-item label="任务名称" prop="name" class="form-item">
              <el-input
                v-model="queryParams.name"
                placeholder="任务名称"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="任务策略" prop="cron" class="form-item">
              <el-input
                v-model="queryParams.cron"
                placeholder="任务策略"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="执行路径" prop="path" class="form-item">
              <el-input
                v-model="queryParams.path"
                placeholder="执行路径"
                class="input-search"
                clearable
              ></el-input>
            </el-form-item>
            <el-form-item label="运行模式" prop="type" class="form-item">
              <el-select
                v-model="queryParams.type"
                placeholder="运行模式"
                class="input-search"
                clearable
              >
                <el-option label="注解" value="ANNOTATION"></el-option>
                <el-option label="模版" value="TEMPLATE"></el-option>
                <el-option label="GLUE" value="GLUE"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否启用" prop="enable" class="form-item">
              <el-select
                v-model="queryParams.enable"
                placeholder="是否启用"
                class="input-search"
                clearable
              >
                <el-option label="是" :value="1"></el-option>
                <el-option label="否" :value="0"></el-option>
              </el-select>
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
              v-permission="'task_add'"
              type="primary"
              icon="CirclePlus"
              @click="handleAdd()"
              >新增</el-button
            >
            <el-button
              v-permission="'task_delete'"
              type="danger"
              icon="Delete"
              @click="handleDelete(selectedRows)"
            >
              删除
            </el-button>
            <el-button
              v-permission="'task_enable'"
              type="success"
              icon="CircleCheck"
              @click="handleEnable(selectedRows)"
            >
              任务启用
            </el-button>
            <el-button
              v-permission="'task_un_enable'"
              type="danger"
              icon="CircleClose"
              @click="handleUnEnable(selectedRows)"
            >
              任务停止
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button icon="RefreshRight" @click="handleSearch" title="刷新"
              >刷新
            </el-button>
          </el-col>
        </el-row>
        <!-- 列表区域 -->
        <div
          class="data-list"
          :style="[{ height: 'calc(100vh - 180px - ' + tHeight + 'px)' }]"
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
              label="任务名称"
              prop="name"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="策略配置"
              prop="cron"
              align="center"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              label="运行模式"
              prop="type"
              align="center"
              width="100"
            >
              <template #default="scope">
                <span v-if="scope.row.type === 'ANNOTATION'">注解</span>
                <span v-else-if="scope.row.type === 'TEMPLATE'">模版</span>
                <span v-else-if="scope.row.type === 'GLUE'">GLUE</span>
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              label="是否启用"
              prop="enable"
              align="center"
              width="100"
            >
              <template #default="scope">
                <el-tag
                  disable-transitions
                  type="success"
                  v-if="scope.row.enable === 1"
                  >是</el-tag
                >
                <el-tag
                  disable-transitions
                  type="danger"
                  v-else-if="scope.row.enable === 0"
                  >否</el-tag
                >
                <span v-else>--</span>
              </template>
            </el-table-column>
            <el-table-column
              label="操作"
              width="280"
              align="center"
              fixed="right"
            >
              <template #default="scope">
                <el-button
                  v-permission="'task_view'"
                  link
                  type=""
                  icon="View"
                  @click="handleView(scope.row)"
                  >查看</el-button
                >
                <el-button
                  v-permission="'task_edit'"
                  link
                  type=""
                  icon="Edit"
                  @click="handleEdit(scope.row)"
                  >编辑</el-button
                >
                <el-button
                  v-permission="'task_delete'"
                  link
                  type=""
                  icon="Delete"
                  style="color: #f63434"
                  @click="handleDelete([scope.row])"
                >
                  删除
                </el-button>
                <el-popover placement="bottom" width="150" trigger="click">
                  <template #reference>
                    <el-button link type="">
                      <span style="margin-right: 5px;">更多操作</span><el-icon><ArrowDown /></el-icon>
                    </el-button>
                  </template>
                  <el-row v-permission="'task_exec_onece'" :gutter="0" class="my-popover">
                    <el-col :span="24" v-permission="'task_edit'">
                      <el-button
                        link
                        type="primary"
                        icon="Pointer"
                        @click="handleExecuteOnece(scope.row)"
                        >执行一次</el-button>
                    </el-col>
                  </el-row>
                  <el-row v-permission="'task_exec_log'" :gutter="0" class="my-popover">
                    <el-col :span="24" v-permission="'task_log'">
                      <el-button
                        link
                        type=""
                        icon="Tickets"
                        @click="showExecLog(scope.row)"
                      >
                        执行日志
                      </el-button>
                    </el-col>
                  </el-row>
                  <div style="border-bottom: 1px solid #EBEEF5; width: 100%; margin: 5px 0;"></div>
                  <el-row :gutter="0" class="my-popover">
                    <el-col :span="24" v-permission="'task_log'">
                      <el-button
                        link
                        type=""
                        icon="Timer"
                        @click="handleFutureExecutionPlan(scope.row.cron)"
                        :disabled="scope.row.enable === 0"
                      >
                        未来执行计划
                      </el-button>
                    </el-col>
                  </el-row>
                  <div style="border-bottom: 1px solid #EBEEF5; width: 100%; margin: 5px 0;"></div>
                  <el-row v-permission="'task_start'" :gutter="0" class="my-popover" v-if="scope.row.enable === 0">
                    <el-col :span="24" v-permission="'task_log'">
                      <el-button
                        link
                        type="success"
                        icon="SwitchButton"
                        @click="handleEnable([scope.row])"
                      >
                        启动任务
                      </el-button>
                    </el-col>
                  </el-row>
                  <el-row v-permission="'task_stop'" :gutter="0" class="my-popover" v-else>
                    <el-col :span="24" v-permission="'task_log'">
                      <el-button
                        link
                        type="danger"
                        icon="SwitchButton"
                        @click="handleUnEnable([scope.row])"
                      >
                        停止任务
                      </el-button>
                    </el-col>
                  </el-row>
                  <div v-permission="'task_glue'" v-if="scope.row.type === 'GLUE'" style="border-bottom: 1px solid #EBEEF5; width: 100%; margin: 5px 0;"></div>
                  <el-row v-permission="'task_glue'" v-if="scope.row.type === 'GLUE'" :gutter="0" class="my-popover">
                    <el-col :span="24" v-permission="'task_log'">
                      <el-button
                        link
                        type="primary"
                        icon="Edit"
                        @click="openGlue(scope.row)"
                      >
                        GLUE编辑器
                      </el-button>
                    </el-col>
                  </el-row>
                </el-popover>
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
      </div>
    </template>
  </ResizeBox>
  <el-dialog v-bind="dialogProp" v-model="dialogProp.visible">
    <TaskForm 
      ref="form" 
      v-if="dialogProp.visible" 
      :data="formData" 
      :tree-list="classifyTreeList" 
      :mode="mode">
    </TaskForm>
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
  <el-dialog v-bind="planListShow" v-model="planListShow.visible">
    <div v-for="(item, index) in planList" :key="index" class="plan-list"> {{ item }}</div>
    <template #footer>
      <el-button @click="planListShow.visible = false" icon="CircleClose"
        >关 闭</el-button
      >
    </template>
  </el-dialog>
  <el-drawer
    v-bind="codeDialogProp"
    v-model="codeDialogProp.visible"
    class="drawer-none-padding">
      <CodeEditor 
        v-if="codeDialogProp.visible"
        v-model="code" 
        @close="closeGlue()"
        title="脚本编辑器-JAVA"
        v-loading="codeLoading" 
        element-loading-background="rgba(0, 0, 0, 0)">
        <template #button>
          <el-button icon="RefreshRight" @click="initJavaCode()">初始化代码</el-button>
          <el-button icon="Switch" @click="handleFormatJavaCode()">格式化代码</el-button>
          <el-button :disabled="codeLoading" icon="CircleCheck" type="primary" @click="saveAndPublishGlue()">保存并发布</el-button>
          <el-button :disabled="codeLoading" icon="CirclePlus" type="success" @click="saveGlue()">保存</el-button>
          <el-button :disabled="codeLoading" icon="Clock" type="warning" @click="handleGlueList()">版本控制</el-button>
        </template>
      </CodeEditor>
  </el-drawer>
  <el-dialog 
    v-bind="glueVersion" 
    v-model="glueVersion.visible"
    @close="() => {glueList = []}">
    <el-table v-if="glueVersion.visible" :data="glueList" height="50vh" border>
      <el-table-column label="说明" prop="remark" show-overflow-tooltip></el-table-column>
      <el-table-column label="时间" prop="createTime" align="center" width="180px"></el-table-column>
      <el-table-column label="操作" width="80" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="loadGlueVersion(scope.row)">载入</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
  <el-dialog v-bind="logDialog" v-model="logDialog.visible">
    <TaskLog v-if="logDialog.visible" :taskId="logDialog.taskId"></TaskLog>
    <template #footer>
      <div></div>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
@use "/src/style/views/index.scss" as *;

.my-popover {
  text-align: left;
  padding: 2px 10px;
  margin: 2px 0;
  
  &:hover {
    background-color: #F5F7FA;
  }
}
.plan-list {
  width: 100%;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background-color: #F5F7FA;
  margin: 10px 0;
}
</style>
