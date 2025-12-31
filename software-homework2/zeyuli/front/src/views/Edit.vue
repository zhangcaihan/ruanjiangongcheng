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
        <div class="card mb-6">
          <div class="flex justify-between items-center mb-4">
            <div>
              <h2 class="text-2xl font-bold">编辑行程</h2>
              <div class="text-gray-600">北京文化三日游 - 第1天</div>
            </div>
            <div class="flex gap-2">
              <button type="button" class="btn btn-text">
                <span class="icon">↩️</span>撤销
              </button>
              <button type="button" class="btn btn-text">
                <span class="icon">↪️</span>重做
              </button>
            </div>
          </div>

          <div class="grid grid-cols-3 gap-6">
            <!-- 行程安排 -->
            <div class="col-span-2">
              <h3 class="font-bold text-lg mb-4">行程安排</h3>
              <div class="space-y-3">
                <div class="p-4 bg-gray-100 rounded-lg cursor-move hover:bg-gray-200 transition">
                  <div class="flex justify-between items-center">
                    <div>
                      <h4 class="font-medium">故宫博物院</h4>
                      <div class="text-gray-600 mt-1">09:00 - 12:00</div>
                      <div class="text-orange-500 mt-1 font-medium">¥60</div>
                    </div>
                    <div class="flex gap-2">
                      <button type="button" class="btn btn-text">
                        <span class="icon">☰</span>
                      </button>
                      <button type="button" class="btn btn-text">
                        <span class="icon">🗑️</span>
                      </button>
                    </div>
                  </div>
                </div>
                <div class="p-4 bg-gray-100 rounded-lg cursor-move hover:bg-gray-200 transition">
                  <div class="flex justify-between items-center">
                    <div>
                      <h4 class="font-medium">四季民福烤鸭</h4>
                      <div class="text-gray-600 mt-1">12:30 - 14:00</div>
                      <div class="text-orange-500 mt-1 font-medium">¥120</div>
                    </div>
                    <div class="flex gap-2">
                      <button type="button" class="btn btn-text">
                        <span class="icon">☰</span>
                      </button>
                      <button type="button" class="btn btn-text">
                        <span class="icon">🗑️</span>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 替换推荐 -->
            <div>
              <h3 class="font-bold text-lg mb-4">替换推荐</h3>
              <div class="space-y-3 mb-6">
                <div class="p-3 border border-gray-200 rounded-lg cursor-pointer hover:border-blue-500 transition">
                  <h4 class="font-medium">全聚德烤鸭</h4>
                  <p class="text-xs text-gray-600 mt-1">百年老字号烤鸭店</p>
                  <div class="text-orange-500 mt-2">¥110</div>
                </div>
                <div class="p-3 border border-gray-200 rounded-lg cursor-pointer hover:border-blue-500 transition">
                  <h4 class="font-medium">大董烤鸭</h4>
                  <p class="text-xs text-gray-600 mt-1">创新风味烤鸭</p>
                  <div class="text-orange-500 mt-2">¥150</div>
                </div>
              </div>
              <div class="p-4 bg-orange-50 rounded-lg">
                <h4 class="font-medium mb-2">预算变化</h4>
                <div class="text-lg font-bold">
                  <span class="text-orange-500">¥1560</span> → <span class="text-green-500">¥1550</span>
                </div>
              </div>
            </div>
          </div>

          <div class="flex justify-end gap-4 mt-6">
            <button type="button" class="btn btn-secondary px-8" @click="navigateTo('/result')">取消</button>
            <button type="button" class="btn btn-primary px-8" @click="navigateTo('/result')">确认修改</button>
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
