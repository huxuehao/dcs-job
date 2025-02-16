<script lang="ts" setup>
import { ElMessage } from 'element-plus';
import { h, nextTick, onMounted, ref } from 'vue';
import Cron from '../cron/Cron.vue';

const props = defineProps({
  data: {
    type: Object,
    default: () => {}
  },
  mode: {
    type: String,
    defalut: () => "add"
  },
  treeList: {
    type: Array,
    defalut: () => []
  },
})

const prop = {
  value: "id",
  label: "name",
  children: "children",
};

const rules = {
  taskClassify: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  name: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  type: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  path: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  cron: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  enable: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
  openLog: [
    {
      required: true,
      message: "必填项",
      trigger: "blur",
    },
  ],
};

const addReadStar = (data: { column: any; $index: number }) => {
  return [
    h(
      "span",
      {
        style: "color: #f67a7a",
      },
      "*"
    ),
    h("span", " " + data.column.label),
  ];
};

let formData = ref<any>({
  type: 'TEMPLATE',
  enable: 0,
  openLog: 1
})
onMounted(() => {
  nextTick(() => {
    if(props.mode !== 'add') {
      formData.value = props.data
    }
  })
})

const clickAddParams = () => {
  if (!formData.value.config) {
    formData.value.config = [];
  }
  formData.value.config.push({
    key: null,
    value: null,
  });
};

const clickRemoveParams = (index: number) => {
  formData.value.config.splice(index, 1);
};

let showCronBox = ref(false)
let showEasyCronBox = ref(false)
const handleCronBox = (tag: number) => {
  if (tag === 0) {
    showEasyCronBox.value = !showEasyCronBox.value
  } else {
    showCronBox.value = !showCronBox.value
  }
};
let minuteCycle = ref(15)
const doEasyCron = () => {
  if (minuteCycle.value > 0 && minuteCycle.value < 60) {
    formData.value.cron = `${new Date().getSeconds()} ${getRandomInt(minuteCycle.value -1)}/${minuteCycle.value} * * * ?`;
    showEasyCronBox.value = false;
  } else {
    ElMessage({
      message: "调度周期需大于0分钟，且小于60分钟",
      type: "warning",
      plain: true
    })
  }
}
const getRandomInt = (max: number) => {
  return Math.floor(Math.random() * (max - 0 + 1)); // 包括min和max
};

let form = ref()
const getData = () => {
  const form_ = JSON.parse(JSON.stringify(formData.value))
  if(form_.type === "TEMPLATE") {
    form_.config = JSON.stringify(form_.config)
  } else if(form_.type === "GLUE" && !(form_.config instanceof String)) {
    form_.config = ""
  } else {
    form_.config = null
  }
  return form_
};
defineExpose({
  getData,
  form,
})
</script>

<template>
  <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="80px"
    >
    <!-- <el-divider content-position="left">基础信息</el-divider> -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="所属分类" prop="taskClassify">
            <el-tree-select
              v-model="formData.taskClassify"
              :data="treeList"
              :render-after-expand="true"
              :default-expand-all="false"
              :props="prop"
              :disabled="mode === 'view'"
              class="input-search"
              placeholder="所属分类"
              filterable
              check-strictly
              clearable
            ></el-tree-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务名称" prop="name">
            <el-input
              placeholder="请填写任务名称"
              v-model="formData.name"
              maxlength="40"
              :disabled="mode === 'view'"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form-item label="任务描述" prop="remark">
            <el-input
              type="textarea"
              placeholder="请填写任务描述"
              v-model="formData.remark"
              maxlength="100"
              show-word-limit
              :disabled="mode === 'view'"
              clearable
            >
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="right"><el-icon><Setting /></el-icon> 模式配置</el-divider>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="运行模式" prop="type">
            <el-select
                v-model="formData.type"
                :disabled="mode === 'view' || formData.type === 'ANNOTATION'"
                placeholder="请选择运行模式"
                class="input-search"
                clearable
              >
                <el-option label="注解" value="ANNOTATION" disabled></el-option>
                <el-option label="模版" value="TEMPLATE"></el-option>
                <el-option label="GLUE" value="GLUE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="['ANNOTATION','TEMPLATE'].includes(formData.type)">
        <el-col :span="24">
          <el-form-item :label="`${formData.type === 'ANNOTATION' ? '执行' : '包名'}路径`" prop="path">
            <el-input
              placeholder="请填写执行路径"
              v-model="formData.path"
              :disabled="mode === 'view'"
              maxlength="200"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="0" v-if="['TEMPLATE'].includes(formData.type)">
        <el-col :span="24">
          <el-form-item label="执行参数">
            <el-table
              :data="formData.config"
              :maxHeight="180"
              empty-text="暂无请求参数"
              ref="subTable"
              class="subTable"
              :header-cell-style="{
                backgroundColor: '#F5F7FA',
                color: '#666666',
              }"
              row-key="rowKey"
              border
            >
              <el-table-column
                type="index"
                label="#"
                width="40px"
                align="center"
              ></el-table-column>
              <el-table-column
                prop="title"
                label="参数名"
                :render-header="addReadStar"
              >
                <template #default="scope">
                  <el-form-item
                    label-width="0"
                    :style="{ marginBottom: 0 }"
                    :prop="'config[' + scope.$index + '].key'"
                    :rules="[
                      { required: true, message: '必填项', trigger: 'blur' },
                    ]"
                  >
                    <el-input
                      v-model="scope.row.key"
                      :disabled="mode === 'view'"
                      placeholder="参数名"
                      clearable
                    ></el-input>
                  </el-form-item>
                </template>
              </el-table-column>
              <el-table-column
                prop="value"
                label="参数值"
                :render-header="addReadStar"
              >
                <template #default="scope">
                  <el-form-item
                    label-width="0"
                    :style="{ marginBottom: 0 }"
                    :prop="'config[' + scope.$index + '].value'"
                    :rules="[
                      { required: true, message: '必填项', trigger: 'blur' },
                    ]"
                  >
                    <el-input
                      v-model="scope.row.value"
                      :disabled="mode === 'view'"
                      placeholder="参数值"
                      clearable
                    ></el-input>
                  </el-form-item>
                </template>
              </el-table-column>

              <el-table-column
                align="center"
                width="50px"
                v-if="mode !== 'view'"
              >
                <template #header>
                  <el-button
                    title="添加"
                    @click="clickAddParams()"
                    icon="Plus"
                    circle
                    type="primary"
                  ></el-button>
                </template>
                <template #default="scope">
                  <el-button
                    title="删除"
                    @click="clickRemoveParams(scope.$index)"
                    icon="Delete"
                    circle
                    type="danger"
                  ></el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="right"><el-icon><Setting /></el-icon> 调度配置</el-divider>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="调度周期" prop="cron">
            <el-input
              v-model="formData.cron"
              :disabled="mode === 'view'"
              auto-complete="off"
              placeholder="请配置调度周期"
              clearable>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col span="12" v-if="mode !== 'view'">
          <el-dropdown>
              <el-button type="primary" style="margin-left: 10px;height: 28px;">配置Cron</el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleCronBox(0)">简单生成器</el-dropdown-item>
                  <el-dropdown-item @click="handleCronBox(1)">复杂生成器</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
        </el-col>
      </el-row>
      <el-row :gutter="0">
        <el-col :span="12">
          <el-form-item label="是否启用" prop="enable">
            <el-select
              placeholder="请选择是否启用"
              v-model="formData.enable"
              :disabled="mode !== 'edit'"
              clearable
            >
              <el-option label="是" :value="1"></el-option>
              <el-option label="否" :value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用日志" prop="openLog">
            <el-select
              placeholder="请选择启用日志"
              v-model="formData.openLog"
              :disabled="mode === 'view'"
              clearable
            >
              <el-option label="是" :value="1"></el-option>
              <el-option label="否" :value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-dialog 
      title="简单Cron生成器" 
      v-model="showEasyCronBox" 
      width="300px" 
      :show-close="false"
      append-to-body>
      <el-form>
        <el-row :gutter="0">
          <el-col :span="24">
            <el-form-item label="调度周期" prop="cron">
              <el-input-number  
                v-model="minuteCycle" 
                step-strictly 
                :step="1"
                :min="1" 
                :max="59"
                style="width: calc(100% - 60px);"
                controls-position="right">
              </el-input-number>
              <span style="margin-left: 20px">分钟</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="doEasyCron">确 定</el-button>
        <el-button @click="showEasyCronBox = false;">关 闭</el-button>
      </template>
    </el-dialog>
    <el-dialog title="复杂cron生成器" v-model="showCronBox" width="60%" append-to-body>
      <Cron v-if="showCronBox" v-model:value="formData.cron"></Cron>
      <template #footer>
        <el-button type="primary" @click="showCronBox = false">确 定</el-button>
        <el-button @click="showCronBox = false;">关 闭</el-button>
      </template>
    </el-dialog>
</template>

<style lang="scss" scoped>
</style>