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
          <h2 class="text-2xl font-bold mb-6">帮助与反馈</h2>

          <div class="grid grid-cols-2 gap-8">
            <div>
              <!-- 常见问题 -->
              <h3 class="font-bold text-lg mb-4">常见问题</h3>
              <div class="space-y-2 mb-6">
                <div class="p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100">
                  <div class="font-medium">如何创建行程规划？</div>
                </div>
                <div class="p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100">
                  <div class="font-medium">如何导出行程信息？</div>
                </div>
                <div class="p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100">
                  <div class="font-medium">如何修改预算？</div>
                </div>
                <div class="p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100">
                  <div class="font-medium">如何联系客服？</div>
                </div>
              </div>

              <!-- 联系我们 -->
              <h3 class="font-bold text-lg mb-4">联系我们</h3>
              <div class="space-y-2">
                <div class="flex items-center gap-2">
                  <span class="icon">📞</span>
                  <span>客服电话: 400-123-4567</span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="icon">✉️</span>
                  <span>邮箱: support@travelmate.com</span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="icon">💬</span>
                  <span>在线客服: 工作时间 09:00-20:00</span>
                </div>
              </div>
            </div>

            <div>
              <!-- 意见反馈 -->
              <h3 class="font-bold text-lg mb-4">意见反馈</h3>
              <form class="space-y-3">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">反馈类型</label>
                  <input 
                    type="text" 
                    v-model="feedback.type" 
                    placeholder="反馈类型" 
                    class="input"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">问题描述</label>
                  <textarea 
                    v-model="feedback.description" 
                    placeholder="请描述您的问题或建议..." 
                    class="input h-32 resize-none"
                  ></textarea>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">您的邮箱</label>
                  <input 
                    type="email" 
                    v-model="feedback.email" 
                    placeholder="您的邮箱" 
                    class="input"
                  />
                </div>
                <div class="flex justify-end">
                  <button type="button" class="btn btn-primary px-8" @click="submitFeedback">提交反馈</button>
                </div>
              </form>

              <!-- 版本信息 -->
              <div class="mt-6 text-xs text-gray-600">
                <div>版本: 1.0</div>
                <div>更新日期: 2025年11月29日</div>
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

// 反馈表单数据
const feedback = ref({
  type: '',
  description: '',
  email: ''
})

// 提交反馈
const submitFeedback = () => {
  // 这里可以添加提交反馈的逻辑
  console.log('提交反馈:', feedback.value)
  // 重置表单
  feedback.value = {
    type: '',
    description: '',
    email: ''
  }
  alert('反馈提交成功，感谢您的支持！')
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
