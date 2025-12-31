<template>
  <div class="app-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="p-6 border-b">
        <div class="font-bold text-blue-600 text-xl">TravelMate</div>
      </div>
      <nav>
        <div class="sidebar-item" @click="navigateTo('/home')">
          <span class="icon">🏠</span>
          <span>首页</span>
        </div>
        <div class="sidebar-item" @click="navigateTo('/result')">
          <span class="icon">📅</span>
          <span>我的行程</span>
        </div>
        <div class="sidebar-item" @click="navigateTo('/map')">
          <span class="icon">🗺️</span>
          <span>地图视图</span>
        </div>
        <div class="sidebar-item" @click="navigateTo('/budget')">
          <span class="icon">💰</span>
          <span>预算管理</span>
        </div>
        <div class="sidebar-item active" @click="navigateTo('/profile')">
          <span class="icon">👤</span>
          <span>个人中心</span>
        </div>
      </nav>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部导航 -->
      <header class="header">
        <div class="flex items-center gap-4">
          <div class="relative">
            <input 
              type="text" 
              placeholder="搜索行程、景点..." 
              class="input pl-10 w-64"
            />
            <span class="icon absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500">🔍</span>
          </div>
        </div>
        <div class="flex items-center gap-4">
          <button type="button" class="btn btn-text">
            <span class="icon">🔔</span>
          </button>
          <div class="flex items-center gap-2">
            <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
              <span class="icon">👤</span>
            </div>
            <span class="text-sm font-medium">张三</span>
          </div>
        </div>
      </header>

      <!-- 内容区域 -->
      <div class="content-area">
        <div class="card">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-2xl font-bold">旅行历史</h2>
            <div class="flex gap-2">
              <button type="button" class="btn btn-secondary">
                <span class="icon">📅</span>筛选日期
              </button>
              <button type="button" class="btn btn-primary">
                <span class="icon">📝</span>生成游记
              </button>
            </div>
          </div>

          <!-- 历史列表 -->
          <div class="space-y-3">
            <div class="p-4 border-l-4 border-blue-500 bg-gray-50 rounded cursor-pointer hover:bg-gray-100">
              <div class="font-medium">北京文化三日游</div>
              <div class="text-gray-600 mt-1">2025年9月15-17日</div>
              <div class="flex gap-4 mt-1">
                <span class="text-sm text-gray-500">参与人数: 2</span>
                <span class="text-sm text-gray-500">评分: ⭐⭐⭐⭐⭐</span>
                <button type="button" class="btn btn-text ml-auto">导出行程</button>
              </div>
            </div>
            <div class="p-4 border-l-4 border-green-500 bg-gray-50 rounded cursor-pointer hover:bg-gray-100">
              <div class="font-medium">杭州西湖两日游</div>
              <div class="text-gray-600 mt-1">2025年8月10-11日</div>
              <div class="flex gap-4 mt-1">
                <span class="text-sm text-gray-500">参与人数: 3</span>
                <span class="text-sm text-gray-500">评分: ⭐⭐⭐⭐</span>
                <button type="button" class="btn btn-text ml-auto">导出行程</button>
              </div>
            </div>
            <div class="p-4 border-l-4 border-orange-500 bg-gray-50 rounded cursor-pointer hover:bg-gray-100">
              <div class="font-medium">成都四日游 文化体验</div>
              <div class="text-gray-600 mt-1">2025年7月5-8日</div>
              <div class="flex gap-4 mt-1">
                <span class="text-sm text-gray-500">参与人数: 1</span>
                <span class="text-sm text-gray-500">评分: ⭐⭐⭐⭐⭐</span>
                <button type="button" class="btn btn-text ml-auto">导出行程</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 导航到指定页面
const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
}

/* 图标样式 */
.icon {
  font-size: 18px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .app-layout {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    height: auto;
    position: relative;
  }
  
  .main-content {
    margin-left: 0;
  }
}
</style>
