<template>
    <div class="container">
      <!-- 标题 -->
      <h1>欢迎使用分布式定时任务管理系统</h1>
      <h2>高效、可靠、易用的任务调度平台</h2>
  
      <!-- 欢迎语句 -->
      <div class="welcome-text">
        <p>欢迎来到分布式定时任务管理系统！我们为您提供了一个强大的任务调度平台，支持高并发、高可用的分布式任务管理。无论是简单的定时任务，还是复杂的分布式调度，我们都能轻松应对。</p>
        <p>立即开始，体验高效的任务管理！</p>
      </div>
  
      <!-- 功能卡片 -->
      <div class="card-container">
        <FeatureCard
          v-for="(feature, index) in features"
          :key="index"
          :title="feature.title"
          :description="feature.description"
        />
      </div>
    </div>
  </template>
  
  <script>
  import FeatureCard from './FeatureCard.vue';
  
  export default {
    components: {
      FeatureCard,
    },
    data() {
      return {
        features: [
          {
            title: '任务管理',
            description: '轻松创建、编辑和删除定时任务，支持多种任务类型和调度策略。',
          },
          {
            title: '分布式支持',
            description: '基于分布式架构，支持多节点任务调度，确保系统高可用性和扩展性。',
          },
          {
            title: '多种模式支持',
            description: '支持3中任务定义模式：Bean模式、模版模式、Glue-Java模式。',
          },
        ],
      };
    },
    methods: {
      startUsing() {
        alert('开始使用系统！');
      },
    },
  };
  </script>
  
  <style scoped>
  /* 全局样式 */
  body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f4f9;
    color: #333;
  }
  
  /* 容器样式 */
  .container {
    max-width: 100%;
    margin: 0 auto;
    padding: 20px;
    text-align: center;
  }
  
  /* 标题样式 */
  h1 {
    font-size: 2.5rem;
    color: #2c3e50;
    margin-bottom: 20px;
  }
  
  /* 副标题样式 */
  h2 {
    font-size: 1.8rem;
    color: #34495e;
    margin-bottom: 30px;
  }
  
  /* 欢迎语句样式 */
  .welcome-text {
    font-size: 1.2rem;
    line-height: 1.8;
    color: #555;
    margin-bottom: 40px;
  }
  
  /* 卡片容器样式 */
  .card-container {
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
    gap: 20px;
  }
  
  /* 按钮样式 */
  .btn {
    display: inline-block;
    padding: 10px 20px;
    font-size: 1rem;
    color: #fff;
    background-color: #3498db;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .btn:hover {
    background-color: #2980b9;
  }
  
  /* 页脚样式 */
  footer {
    margin-top: 50px;
    padding: 20px;
    background-color: #2c3e50;
    color: #fff;
    text-align: center;
  }
  
  footer a {
    color: #3498db;
    text-decoration: none;
  }
  
  footer a:hover {
    text-decoration: underline;
  }
  </style>