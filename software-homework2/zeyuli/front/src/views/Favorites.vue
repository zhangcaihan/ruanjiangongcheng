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
            <h2 class="text-2xl font-bold">我的收藏</h2>
            <div class="flex gap-2">
              <button type="button" class="btn btn-text">
                <span class="icon">↕️</span>排序
              </button>
              <button type="button" class="btn btn-secondary">
                <span class="icon">🗑️</span>批量删除
              </button>
            </div>
          </div>

          <!-- 分类标签 -->
          <div class="flex gap-3 mb-4 overflow-x-auto hide-scrollbar">
            <button type="button" class="btn btn-primary">全部</button>
            <button type="button" class="btn btn-secondary">行程</button>
            <button type="button" class="btn btn-secondary">景点</button>
            <button type="button" class="btn btn-secondary">酒店</button>
            <button type="button" class="btn btn-secondary">美食</button>
          </div>

          <!-- 收藏列表 -->
          <div class="grid grid-cols-3 gap-4">
            <div class="p-4 border border-gray-200 rounded-lg">
              <div class="flex justify-between items-start">
                <div class="flex-1">
                  <h4 class="font-medium">故宫博物院</h4>
                  <p class="text-gray-600 mt-1">北京必去景点之一</p>
                  <div class="flex items-center gap-2 mt-2 text-sm">
                    <span class="text-blue-500">北京</span>
                    <span class="text-yellow-500">⭐4.8</span>
                    <span class="text-gray-500">参观</span>
                  </div>
                </div>
                <button type="button" class="btn btn-text">
                  <span class="icon">🗑️</span>
                </button>
              </div>
            </div>
            <div class="p-4 border border-gray-200 rounded-lg">
              <div class="flex justify-between items-start">
                <div class="flex-1">
                  <h4 class="font-medium">北京喜来登酒店</h4>
                  <p class="text-gray-600 mt-1">五星级商务酒店</p>
                  <div class="flex items-center gap-2 mt-2 text-sm">
                    <span class="text-blue-500">北京</span>
                    <span class="text-yellow-500">⭐4.9</span>
                    <span class="text-gray-500">住宿</span>
                  </div>
                </div>
                <button type="button" class="btn btn-text">
                  <span class="icon">🗑️</span>
                </button>
              </div>
            </div>
            <div class="p-4 border border-gray-200 rounded-lg">
              <div class="flex justify-between items-start">
                <div class="flex-1">
                  <h4 class="font-medium">四季民福烤鸭</h4>
                  <p class="text-gray-600 mt-1">地道北京烤鸭</p>
                  <div class="flex items-center gap-2 mt-2 text-sm">
                    <span class="text-blue-500">北京</span>
                    <span class="text-yellow-500">⭐4.7</span>
                    <span class="text-gray-500">美食</span>
                  </div>
                </div>
                <button type="button" class="btn btn-text">
                  <span class="icon">🗑️</span>
                </button>
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
  
  .grid-cols-3 {
    grid-template-columns: 1fr;
  }
}
</style>
