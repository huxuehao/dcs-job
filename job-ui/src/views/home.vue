<script lang="ts" setup>
import { useUserStore } from "@/stores/user";
import { computed } from "vue";

const userStore = useUserStore();
let userInfo = computed(() => {
  return userStore.userInfo
})

const getTimePeriod = () => {
  const now = new Date();
  const hour = now.getHours();

  if (hour >= 5 && hour < 8) {
    return '早上';
  } else if (hour >= 8 && hour < 12) {
    return '上午';
  } else if (hour >= 12 && hour < 18) {
    return '下午';
  } else {
    return '晚上';
  }
}

</script>

<template>
  <div class="container">
    <div class="left-section">
      <h1>{{ getTimePeriod() }}好, {{ userInfo.nickname || userInfo.name || userInfo.account }}</h1>
      <h2>欢迎使用分布式调度框架-DCS JOB</h2>
      <div style="margin-top: 40px">
        <div style="margin-top: 20px">
          <span style="padding: 8px; background-color: #FCE4EC; border-radius: 6px;min-width: 0">任务管理</span>
          <br/><br/>
          <span>轻松创建、编辑和删除定时任务，支持多种任务类型和调度策略。</span>
        </div>
        <div style="margin-top: 20px">
          <span style="padding: 8px; background-color: #d5f5d8; border-radius: 6px;min-width: 0">分布式支持</span>
          <br/><br/>
          <span>基于分布式架构，支持多节点任务调度，确保系统高可用性和扩展性。</span>
        </div>
        <div style="margin-top: 20px">
          <span style="padding: 8px; background-color: #cceaf9; border-radius: 6px;min-width: 0">多种模式支持</span>
          <br/><br/>
          <span>支持3中任务定义模式：Bean模式、模版模式、Glue-Java模式。</span>
        </div>
      </div>
    </div>
    <div class="right-section">
      <img src="/src/assets/lf_icon2.png" alt="插图" />
    </div>
  </div>
</template>

<style lang="scss" scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f0f4f8;
  color: #333;
}

.container {
  position: relative;
  display: flex;
  padding: 20px;
}

.left-section {
  flex: 0 0 30%;
  padding-left: 15%;
  padding-top: 10%;
}

.right-section {
  flex: 0 0 45%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.right-section img {
  max-width: 100%;
  height: auto;
}

h1, h2 {
  margin-bottom: 20px;
  color: #2c3e50;
}
</style>
