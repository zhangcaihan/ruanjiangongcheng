<template>
  <div class="app-layout" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
    <!-- Sidebar -->
    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-brand">
        <div class="brand-row" @click="navigateTo('/home')" title="回到首页">
          <div class="brand-logo">🧳</div>
          <div class="brand-text">TravelMate</div>
        </div>
      </div>

      <nav class="sidebar-nav">
        <div class="sidebar-item" @click="navigateTo('/home')">
          <span class="icon">🏠</span>
          <span class="label">首页</span>
        </div>
        <div class="sidebar-item" @click="navigateTo('/result')">
          <span class="icon">📅</span>
          <span class="label">我的行程</span>
        </div>
        <div class="sidebar-item" @click="navigateTo('/map')">
          <span class="icon">🗺️</span>
          <span class="label">地图视图</span>
        </div>
<!--        <div class="sidebar-item" @click="navigateTo('/budget')">-->
<!--          <span class="icon">💰</span>-->
<!--          <span class="label">预算管理</span>-->
<!--        </div>-->
        <div class="sidebar-item active" @click="navigateTo('/profile')">
          <span class="icon">👤</span>
          <span class="label">个人中心</span>
        </div>
      </nav>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      <button
          class="edge-toggle"
          type="button"
          @click="toggleSidebar"
          :aria-label="sidebarCollapsed ? '打开侧边栏' : '收起侧边栏'"
          :title="sidebarCollapsed ? '打开侧边栏' : '收起侧边栏'"
      >
        <span class="chev" :class="{ right: sidebarCollapsed }"></span>
      </button>

      <div class="content-area">
        <div class="profile-container">
          <!-- 左侧：个人信息卡片 -->
          <div class="left-column">
            <div class="glass-card user-card">
              <div class="user-header">
                <div class="avatar-wrapper">
                  <span class="avatar-icon">👤</span>
                </div>
                <div class="user-info">
                  <h2 class="username">张三</h2>
                  <span class="user-badge">VIP 会员</span>
                </div>
              </div>

              <div class="stats-row">
                <div class="stat-item">
                  <div class="stat-val">12</div>
                  <div class="stat-label">行程</div>
                </div>
                <div class="stat-item">
                  <div class="stat-val">35</div>
                  <div class="stat-label">天数</div>
                </div>
                <div class="stat-item">
                  <div class="stat-val">850</div>
                  <div class="stat-label">积分</div>
                </div>
              </div>

              <div class="menu-list">
                <button class="menu-item">
                  <span class="menu-icon">❤️</span> 我的收藏
                </button>
                <button class="menu-item">
                  <span class="menu-icon">📜</span> 旅行历史
                </button>
                <button class="menu-item">
                  <span class="menu-icon">⚙️</span> 偏好设置
                </button>
              </div>
            </div>

            <!-- 人格测试入口卡片 -->
            <div class="glass-card test-entry-card">
              <div class="card-header">
                <h3>旅行人格测试</h3>
                <span class="icon-bg">🧬</span>
              </div>
              <p class="card-desc">测测你的旅行性格，获取专属推荐行程</p>
              <button class="btn-primary" @click="startTest" v-if="!testResult">开始测试</button>
              <div v-else class="result-preview">
                <div class="result-tag">{{ testResult.dominantPersonalityType }}</div>
                <button class="btn-text" @click="showResultModal = true">查看详情</button>
                <button class="btn-text" @click="startTest">重新测试</button>
              </div>
            </div>
          </div>

          <!-- 右侧：详细内容 -->
          <div class="right-column">
            <!-- 最近活动 -->
            <div class="glass-card activity-card">
              <h3>最近活动</h3>
              <div class="activity-list">
                <div class="activity-item">
                  <div class="activity-icon blue">📅</div>
                  <div class="activity-content">
                    <div class="activity-title">生成了北京文化三日游</div>
                    <div class="activity-time">2025年11月20日 14:30</div>
                  </div>
                </div>
                <div class="activity-item">
                  <div class="activity-icon green">❤️</div>
                  <div class="activity-content">
                    <div class="activity-title">收藏了故宫博物院</div>
                    <div class="activity-time">2025年11月20日 10:15</div>
                  </div>
                </div>
                <div class="activity-item">
                  <div class="activity-icon orange">✅</div>
                  <div class="activity-content">
                    <div class="activity-title">完成了杭州西湖两日游</div>
                    <div class="activity-time">2025年11月18日 18:45</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 测试模态框 -->
    <div class="modal-overlay" v-if="showTestModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>{{ testData?.testName || '旅行人格测试' }}</h3>
          <button class="close-btn" @click="closeTest">×</button>
        </div>
        <div class="modal-body" v-if="testData">
          <div class="progress-bar">
            <div class="progress"
                 :style="{ width: `${(currentQuestionIndex + 1) / testData.questions.length * 100}%` }"></div>
          </div>

          <div class="question-container">
            <h4 class="question-text">{{ currentQuestion.content }}</h4>
            <div class="options-list">
              <button
                  v-for="option in currentQuestion.options"
                  :key="option.optionId"
                  class="option-btn"
                  @click="selectOption(option)"
              >
                {{ option.content }}
              </button>
            </div>
          </div>
        </div>
        <div class="modal-body loading" v-else>
          加载中...
        </div>
      </div>
    </div>

    <!-- 结果模态框 -->
    <div class="modal-overlay" v-if="showResultModal && testResult">
      <div class="modal-content result-modal">
        <div class="modal-header">
          <h3>测试结果</h3>
          <button class="close-btn" @click="showResultModal = false">×</button>
        </div>
        <div class="modal-body result-body">
          <div class="result-header">
            <div class="result-icon">🎉</div>
            <h2>{{ testResult.dominantPersonalityType }}</h2>
            <p class="result-desc">{{ testResult.dominantPersonalityDescription }}</p>
          </div>

          <div class="result-tags">
            <span v-for="tag in testResult.travelStyleTags" :key="tag" class="tag">{{ tag }}</span>
          </div>

          <div class="result-section">
            <h4>💡 个性化建议</h4>
            <ul>
              <li v-for="(tip, index) in testResult.personalizedTips" :key="index">{{ tip }}</li>
            </ul>
          </div>

          <div class="result-actions">
            <button class="btn-primary" @click="generateItinerary">基于此人格生成行程</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import {ref, computed, reactive} from 'vue'
import {useRouter} from 'vue-router'
import {getPersonalityTest, calculateTestResult} from '../api/personality'

const router = useRouter()

// --- 侧边栏逻辑 ---
const sidebarCollapsed = ref(false)
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}
const navigateTo = (path) => {
  router.push(path)
}

// --- 人格测试逻辑 ---
const showTestModal = ref(false)
const showResultModal = ref(false)
const testData = ref(null)
const currentQuestionIndex = ref(0)
const userAnswers = reactive({}) // { questionId: optionId }
const testResult = ref(null)

const currentQuestion = computed(() => {
  if (!testData.value || !testData.value.questions) return null
  return testData.value.questions[currentQuestionIndex.value]
})

const startTest = async () => {
  try {
    const res = await getPersonalityTest()
    testData.value = res
    currentQuestionIndex.value = 0
    // 清空之前的答案
    for (const key in userAnswers) delete userAnswers[key]
    showTestModal.value = true
  } catch (e) {
    console.error('获取问卷失败', e)
    alert('获取问卷失败，请稍后重试')
  }
}

const closeTest = () => {
  showTestModal.value = false
}

const selectOption = async (option) => {
  if (!currentQuestion.value) return

  // 记录答案
  userAnswers[currentQuestion.value.questionId] = option.optionId

  // 下一题或提交
  if (currentQuestionIndex.value < testData.value.questions.length - 1) {
    currentQuestionIndex.value++
  } else {
    await submitTest()
  }
}

const submitTest = async () => {
  try {
    // 再userAnswers里添加token
    userAnswers.token = localStorage.token;
    const res = await calculateTestResult(userAnswers)
    testResult.value = res
    showTestModal.value = false
    showResultModal.value = true
  } catch (e) {
    console.error('提交测试失败', e)
    alert('提交失败，请重试')
  }
}

const generateItinerary = () => {
  // 跳转到首页并带上人格参数，或者直接调用生成接口
  // 这里简单演示跳转到首页
  router.push({path: '/home', query: {personality: testResult.value.dominantPersonalityType}})
}
</script>

<style scoped>
/* 复用 Home.vue 的布局样式 */
.app-layout {
  --purple-1: #667eea;
  --purple-2: #764ba2;
  --sidebar-w: 239px;

  display: grid;
  grid-template-columns: var(--sidebar-w) 1fr;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  background: radial-gradient(circle at 15% 10%, rgba(255, 255, 255, 0.10), transparent 45%),
  radial-gradient(circle at 85% 30%, rgba(255, 255, 255, 0.08), transparent 40%),
  linear-gradient(135deg, var(--purple-1) 0%, var(--purple-2) 100%);
}

.app-layout.sidebar-collapsed {
  --sidebar-w: 0px;
}

/* Sidebar Styles (Copied from Home.vue) */
.sidebar {
  grid-column: 1;
  min-width: 0;
  height: 100vh;
  background: #ffffff;
  box-shadow: 0 14px 40px rgba(15, 23, 42, 0.12);
  border-right: 1px solid rgba(15, 23, 42, 0.06);
  z-index: 10;
  transition: opacity 0.2s ease, transform 0.25s ease;
}

.sidebar.collapsed {
  opacity: 0;
  pointer-events: none;
  transform: translateX(-12px);
}

.sidebar-brand {
  padding: 18px 16px;
  border-bottom: 1px solid rgba(15, 23, 42, 0.06);
}

.brand-row {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.brand-logo {
  width: 40px;
  height: 40px;
  border-radius: 14px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  box-shadow: 0 16px 36px rgba(102, 126, 234, 0.26);
  border: 1px solid rgba(255, 255, 255, 0.18);
}

.brand-text {
  font-weight: 900;
  color: #1d4ed8;
  font-size: 20px;
}

.sidebar-nav {
  padding: 8px 0;
}

.sidebar-item {
  padding: 14px 18px;
  margin: 8px 14px;
  border-radius: 16px;
  color: #334155;
  cursor: pointer;
  transition: all 0.22s ease;
  display: flex;
  align-items: center;
  gap: 14px;
}

.sidebar-item:hover {
  background: rgba(15, 23, 42, 0.04);
}

.sidebar-item.active {
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: #fff;
  box-shadow: 0 16px 34px rgba(102, 126, 234, 0.26);
}

.icon {
  font-size: 18px;
}

/* Main Content */
.main-content {
  grid-column: 2;
  min-width: 0;
  position: relative;
  height: 100vh;
  overflow-y: auto;
  padding: 20px;
}

.edge-toggle {
  position: absolute;
  left: 4%;
  top: 18px;
  transform: translateX(-50%);
  z-index: 50;
  width: 42px;
  height: 42px;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.55);
  background: rgba(255, 255, 255, 0.55);
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.14);
  cursor: pointer;
  display: grid;
  place-items: center;
}

.chev {
  width: 10px;
  height: 10px;
  border-right: 3px solid rgba(76, 29, 149, 0.75);
  border-bottom: 3px solid rgba(76, 29, 149, 0.75);
  transform: rotate(135deg);
}

.chev.right {
  transform: rotate(-45deg);
}

/* Profile Specific Styles */
.content-area {
  max-width: 1200px;
  margin: 60px auto 0;
  width: 100%;
}

.profile-container {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
}

.glass-card {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(12px);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.05);
  padding: 24px;
  margin-bottom: 24px;
}

.user-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.avatar-wrapper {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
}

.username {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin: 0 0 4px 0;
}

.user-badge {
  background: linear-gradient(90deg, #f6d365 0%, #fda085 100%);
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
}

.stats-row {
  display: flex;
  justify-content: space-around;
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.stat-item {
  text-align: center;
}

.stat-val {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

.menu-item {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border: none;
  background: transparent;
  cursor: pointer;
  border-radius: 12px;
  transition: background 0.2s;
  color: #444;
  font-size: 15px;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.5);
}

/* Test Entry Card */
.test-entry-card {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.8), rgba(255, 255, 255, 0.6));
  position: relative;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
}

.icon-bg {
  font-size: 48px;
  opacity: 0.2;
  position: absolute;
  right: -10px;
  top: -10px;
}

.card-desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
}

.btn-primary {
  width: 40%;
  padding: 10px;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-weight: bold;
  transition: transform 0.1s;
  margin-left: 28%;
}

.btn-primary:active {
  transform: scale(0.98);
}

/* Activity List */
.activity-item {
  display: flex;
  gap: 16px;
  padding: 16px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.activity-icon.blue {
  background: #e6f7ff;
  color: #1890ff;
}

.activity-icon.green {
  background: #f6ffed;
  color: #52c41a;
}

.activity-icon.orange {
  background: #fff7e6;
  color: #fa8c16;
}

.activity-title {
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.activity-time {
  font-size: 12px;
  color: #999;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: white;
  width: 90%;
  max-width: 500px;
  border-radius: 24px;
  padding: 24px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  animation: slideUp 0.3s ease;
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.progress-bar {
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  margin-bottom: 24px;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: var(--purple-1);
  transition: width 0.3s ease;
}

.question-text {
  font-size: 18px;
  margin-bottom: 24px;
  color: #333;
}

.option-btn {
  width: 100%;
  padding: 16px;
  margin-bottom: 12px;
  border: 1px solid #eee;
  border-radius: 12px;
  background: white;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s;
}

.option-btn:hover {
  border-color: var(--purple-1);
  background: #f6f8ff;
}

/* Result Modal */
.result-header {
  text-align: center;
  margin-bottom: 24px;
}

.result-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.result-desc {
  color: #666;
  line-height: 1.6;
}

.result-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
  margin-bottom: 24px;
}

.tag {
  background: #f0f2f5;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  color: #666;
}

.result-section h4 {
  margin-bottom: 12px;
}

.result-section ul {
  padding-left: 20px;
  color: #555;
}

.result-section li {
  margin-bottom: 8px;
}

.result-actions {
  margin-top: 24px;
}
</style>
