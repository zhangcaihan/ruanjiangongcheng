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
          <h2 class="text-2xl font-bold mb-6">偏好设置</h2>

          <div class="grid grid-cols-2 gap-8">
            <div>
              <!-- 旅行偏好 -->
              <h3 class="font-bold text-lg mb-4">旅行偏好</h3>
              <div class="space-y-3">
                <label class="flex items-center gap-3">
                  <input type="checkbox" v-model="preferences.travel.backpacker" class="w-5 h-5"/>
                  <span>背包客自助游</span>
                </label>
                <label class="flex items-center gap-3">
                  <input type="checkbox" v-model="preferences.travel.foodTour" class="w-5 h-5"/>
                  <span>美食之旅</span>
                </label>
                <label class="flex items-center gap-3">
                  <input type="checkbox" v-model="preferences.travel.culturalExperience" class="w-5 h-5"/>
                  <span>文化体验</span>
                </label>
                <label class="flex items-center gap-3">
                  <input type="checkbox" v-model="preferences.travel.beachVacation" class="w-5 h-5"/>
                  <span>海滨度假</span>
                </label>
              </div>

              <!-- 预算偏好 -->
              <h3 class="font-bold text-lg mt-6 mb-4">预算偏好</h3>
              <div>
                <div class="text-sm mb-2">平均预算范围</div>
                <input 
                  type="range" 
                  v-model="preferences.budget.average" 
                  min="1000" 
                  max="10000" 
                  class="w-full"
                />
                <div class="text-sm text-gray-600 mt-1">¥{{ preferences.budget.average }}</div>
              </div>
            </div>

            <div>
              <!-- 目的地偏好 -->
              <h3 class="font-bold text-lg mb-4">目的地偏好</h3>
              <div class="space-y-2 mb-6">
                <div class="flex justify-between">
                  <span class="text-sm">喜欢的城市:</span>
                  <span class="text-sm font-medium">北京、上海、成都</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-sm">气候偏好:</span>
                  <span class="text-sm font-medium">温暖气候</span>
                </div>
                <div class="flex justify-between">
                  <span class="text-sm">风景类型:</span>
                  <span class="text-sm font-medium">人文景观、美食文化</span>
                </div>
              </div>

              <!-- 通知设置 -->
              <h3 class="font-bold text-lg mb-4">通知设置</h3>
              <div class="space-y-3">
                <label class="flex items-center justify-between">
                  <span>推荐通知</span>
                  <input type="checkbox" v-model="preferences.notification.recommendation" class="w-5 h-5"/>
                </label>
                <label class="flex items-center justify-between">
                  <span>行程提醒</span>
                  <input type="checkbox" v-model="preferences.notification.itinerary" class="w-5 h-5"/>
                </label>
                <label class="flex items-center justify-between">
                  <span>优惠通知</span>
                  <input type="checkbox" v-model="preferences.notification.discount" class="w-5 h-5"/>
                </label>
              </div>
            </div>
          </div>

          <div class="flex justify-end gap-4 mt-8">
            <button type="button" class="btn btn-secondary px-8" @click="resetPreferences">重置</button>
            <button type="button" class="btn btn-primary px-8" @click="savePreferences">保存设置</button>
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

// 偏好设置数据
const preferences = ref({
  travel: {
    backpacker: true,
    foodTour: true,
    culturalExperience: false,
    beachVacation: true
  },
  budget: {
    average: 3000
  },
  notification: {
    recommendation: true,
    itinerary: true,
    discount: false
  }
})

// 保存设置
const savePreferences = () => {
  // 这里可以添加保存偏好设置的逻辑
  console.log('保存偏好设置:', preferences.value)
  router.push('/profile')
}

// 重置设置
const resetPreferences = () => {
  preferences.value = {
    travel: {
      backpacker: true,
      foodTour: true,
      culturalExperience: false,
      beachVacation: true
    },
    budget: {
      average: 3000
    },
    notification: {
      recommendation: true,
      itinerary: true,
      discount: false
    }
  }
}

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
  
  .grid-cols-2 {
    grid-template-columns: 1fr;
  }
}
</style>
