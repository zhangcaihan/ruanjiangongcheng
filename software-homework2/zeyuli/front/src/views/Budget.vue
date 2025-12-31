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
        <div class="sidebar-item active" @click="navigateTo('/budget')">
          <span class="icon">💰</span>
          <span>预算管理</span>
        </div>
        <div class="sidebar-item" @click="navigateTo('/profile')">
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
        <!-- 预算概览 -->
        <div class="grid grid-cols-3 gap-6 mb-6">
          <div class="card">
            <div class="text-sm text-gray-600 mb-1">总预算</div>
            <div class="text-4xl font-bold text-blue-500">¥1560</div>
          </div>
          <div class="card">
            <div class="text-sm text-gray-600 mb-1">已支出</div>
            <div class="text-4xl font-bold text-green-500">¥850</div>
          </div>
          <div class="card">
            <div class="text-sm text-gray-600 mb-1">剩余预算</div>
            <div class="text-4xl font-bold text-orange-500">¥710</div>
          </div>
        </div>

        <!-- 分类明细和支出明细 -->
        <div class="grid grid-cols-2 gap-6">
          <!-- 分类明细 -->
          <div class="card">
            <div class="flex justify-between items-center mb-4">
              <h3 class="font-bold text-lg">分类明细</h3>
              <button type="button" class="btn btn-text">
                <span class="icon">🔍</span>筛选
              </button>
            </div>
            <div class="space-y-3">
              <div class="flex justify-between items-center p-3 bg-gray-50 rounded">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
                    <span class="icon">🚗</span>
                  </div>
                  <span>交通</span>
                </div>
                <span class="text-orange-500 font-medium">¥200</span>
              </div>
              <div class="flex justify-between items-center p-3 bg-gray-50 rounded">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center">
                    <span class="icon">🏨</span>
                  </div>
                  <span>住宿</span>
                </div>
                <span class="text-orange-500 font-medium">¥500</span>
              </div>
              <div class="flex justify-between items-center p-3 bg-gray-50 rounded">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 rounded-full bg-yellow-100 flex items-center justify-center">
                    <span class="icon">🍽️</span>
                  </div>
                  <span>餐饮</span>
                </div>
                <span class="text-orange-500 font-medium">¥640</span>
              </div>
              <div class="flex justify-between items-center p-3 bg-gray-50 rounded">
                <div class="flex items-center gap-3">
                  <div class="w-8 h-8 rounded-full bg-purple-100 flex items-center justify-center">
                    <span class="icon">🎫</span>
                  </div>
                  <span>景点门票</span>
                </div>
                <span class="text-orange-500 font-medium">¥320</span>
              </div>
            </div>
          </div>

          <!-- 支出明细 -->
          <div class="card">
            <div class="flex justify-between items-center mb-4">
              <h3 class="font-bold text-lg">支出明细</h3>
              <button type="button" class="btn btn-primary">
                <span class="icon">➕</span>添加费用
              </button>
            </div>
            <div class="space-y-3">
              <div class="flex justify-between items-center p-3">
                <div>
                  <div class="font-medium">北京地铁卡</div>
                  <div class="text-sm text-gray-500">11月20日</div>
                </div>
                <span class="text-orange-500">¥100</span>
              </div>
              <div class="flex justify-between items-center p-3">
                <div>
                  <div class="font-medium">故宫门票</div>
                  <div class="text-sm text-gray-500">11月20日</div>
                </div>
                <span class="text-orange-500">¥60</span>
              </div>
              <div class="flex justify-between items-center p-3">
                <div>
                  <div class="font-medium">四季民福烤鸭</div>
                  <div class="text-sm text-gray-500">11月20日</div>
                </div>
                <span class="text-orange-500">¥120</span>
              </div>
            </div>
            <div class="pt-4 border-t mt-4">
              <button type="button" class="btn btn-text">
                <span class="icon">📤</span>导出报表
              </button>
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
  
  .grid-cols-3,
  .grid-cols-2 {
    grid-template-columns: 1fr;
  }
}
</style>
