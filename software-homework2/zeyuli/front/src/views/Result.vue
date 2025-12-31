<template>
  <!-- 使用与Home相同的布局系统 -->
  <div class="app-layout" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
    <!-- Sidebar（与Home完全一致） -->
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
        <div class="sidebar-item active" @click="navigateTo('/result')">
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
        <div class="sidebar-item" @click="navigateTo('/profile')">
          <span class="icon">👤</span>
          <span class="label">个人中心</span>
        </div>
      </nav>
    </aside>
    <!-- 移动端遮罩层 -->
    <div
        class="sidebar-overlay"
        v-if="!sidebarCollapsed"
        @click="toggleSidebar"
    ></div>
    <!-- Main -->
    <main class="main-content">
      <!-- 折叠按钮 -->
      <button
          class="edge-toggle"
          type="button"
          @click="toggleSidebar"
          :aria-label="sidebarCollapsed ? '打开侧边栏' : '收起侧边栏'"
          :title="sidebarCollapsed ? '打开侧边栏' : '收起侧边栏'"
      >
        <span class="chev" :class="{ right: sidebarCollapsed }"></span>
      </button>

      <!-- Header -->
      <header class="header header-slogan">
        <!-- 1. 中间的“我的详细行程” -->
        <div class="slogan-wrap" aria-label="标语">
          <span class="slogan-dot"></span>
          <span class="slogan-text">我的详细行程</span>
          <span class="slogan-dot"></span>
        </div>

        <!-- 右侧用户信息（固定在右边） -->
        <div class="user-info">
          <div class="user-avatar">
            <span class="avatar-icon">👤</span>
          </div>
          <div class="user-name">张三</div>
        </div>
      </header>

      <!-- 内容区域 -->
      <div class="content-area">
        <div class="result-container">
          <!-- 主行程卡片 -->
          <div class="main-card glass-card">
            <!-- 行程头部 -->
            <div class="result-header">
              <div class="header-left">
                <h1 class="plan-title">{{ '我的旅行行程' }}</h1>
                <div class="plan-meta">
                  <span class="meta-tag">
                    <span class="meta-icon">📅</span>
                    {{ totalDays || 3 }}天
                  </span>
                  <span class="meta-tag">
                    <span class="meta-icon">💰</span>
                    ¥{{ 2500 }}
                  </span>
<!--                  <span class="meta-tag">-->
<!--                    <span class="meta-icon">📍</span>-->
<!--                    {{ '北京' }}-->
<!--                  </span>-->
                </div>
              </div>

              <!-- 2. 删除“保存”模块：这里原来是保存按钮，已经移除 -->
            </div>


            <!-- 天数导航 -->
            <div class="day-navigation">
              <div class="day-scroll">
                <button
                    v-for="day in itinerary.days"
                    :key="day.dayIndex"
                    type="button"
                    class="day-tab"
                    :class="{ active: currentDay === day.dayIndex }"
                    @click="currentDay = day.dayIndex"
                >
                  <span class="day-number">{{ day.label }}</span>
                  <span class="day-date">{{ formatDate(day.date) }}</span>
                </button>
              </div>
            </div>

            <!-- 当天行程内容 -->
            <div class="day-content">
              <div class="day-timeline">
                <div
                    v-for="(item, index) in currentDayItinerary.items"
                    :key="index"
                    class="timeline-item"
                >
                  <div class="timeline-marker">
                    <div class="marker-dot"></div>
                    <div
                        class="marker-line"
                        v-if="index < currentDayItinerary.items.length - 1"
                    ></div>
                  </div>

                  <div class="timeline-content glass-card">
                    <div class="timeline-time">{{ item.time || '09:00' }}</div>
                    <div class="timeline-main">
                      <h3 class="attraction-name">{{ item.title || '景点名称' }}</h3>
                      <p class="attraction-desc" v-html="item.description"></p>
                      <div class="attraction-meta">
            <span class="cost-badge">
              <span class="cost-icon">💰</span>
              {{ item.cost || '免费' }}
            </span>
                        <span class="duration-badge">
              <span class="duration-icon">⏱️</span>
              {{ item.durationHours || '2小时' }}
            </span>
                        <button
                            type="button"
                            class="btn btn-text btn-detail"
                            @click="showDetail(item)"
                        >
                          查看详情 →
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>



          </div>

          <!-- 右侧信息面板 -->
          <div class="side-panel">
            <!-- 预算分解卡片 -->
            <div class="info-card glass-card">
              <h3 class="card-title">
                <span class="card-icon">💰</span>
                预算分解
              </h3>

              <!-- 3. 减少图表占位高度，让文字整体上移，看起来更紧凑 -->
              <div class="budget-chart">
                <div class="chart-container" ref="chartRef"></div>
              </div>

              <div class="budget-list">
                <div
                    v-for="(item, index) in budgetItems"
                    :key="index"
                    class="budget-item"
                >
                  <div class="budget-header">
                    <span class="budget-category">{{ item.category }}</span>
                    <span class="budget-amount">¥{{ item.amount }}</span>
                  </div>
                  <div class="budget-progress">
                    <div
                        class="progress-bar"
                        :style="{
                        width: item.percentage + '%',
                        backgroundColor: item.color
                      }"
                    ></div>
                  </div>
                  <div class="budget-percentage">{{ item.percentage }}%</div>
                </div>
              </div>
            </div>

            <!-- 行程概览卡片 -->
            <div class="info-card glass-card">
              <h3 class="card-title">
                <span class="card-icon">📊</span>
                行程概览
              </h3>

              <div class="overview-stats">
                <div class="stat-item">
                  <div class="stat-icon">📅</div>
                  <div class="stat-content">
                    <div class="stat-value">{{  totalDays || 3 }}天</div>
                    <div class="stat-label">总天数</div>
                  </div>
                </div>

                <div class="stat-item">
                  <div class="stat-icon">📍</div>
                  <div class="stat-content">
                    <div class="stat-value">{{ totalAttractions }}个</div>
                    <div class="stat-label">总景点数</div>
                  </div>
                </div>

                <div class="stat-item">
                  <div class="stat-icon">🍜</div>
                  <div class="stat-content">
                    <div class="stat-value">{{ totalRestaurants }}个</div>
                    <div class="stat-label">餐饮推荐</div>
                  </div>
                </div>

                <div class="stat-item">
                  <div class="stat-icon">🚗</div>
                  <div class="stat-content">
                    <div class="stat-value">{{ randomDistance }}km</div>
                    <div class="stat-label">总交通距离</div>
                  </div>
                </div>
              </div>

              <div class="overview-summary">
                <h4 class="summary-title">行程特点</h4>
                <div class="summary-tags">
                  <span class="summary-tag">🏛️ 历史文化</span>
                  <span class="summary-tag">🍜 美食探索</span>
                  <span class="summary-tag">📸 拍照圣地</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import {getTravelInfo} from "../api/result.js";
import {getHot} from "../api/studentRoute.js";

const router = useRouter();

// 侧边栏状态
const sidebarCollapsed = ref(false);
const toggleSidebar = () => (sidebarCollapsed.value = !sidebarCollapsed.value);

// 当前选择的天数
const currentDay = ref(1);

// 行程数据
const itinerary = ref({
  days: []
});
// 测试用 itinerary 数据对象
const testItinerary = {
  days: [
    {
      dayIndex: 1,
      date: "2025-12-12",
      label: "成都文化初体验",
      items: [
        {
          time: "09:00-12:00",
          title: "参观武侯祠",
          description: "探索三国历史遗迹，了解诸葛亮与刘备的故事。",
          attractions: "武侯祠",
          cost: "门票约50元",
          durationHours: 3
        },
        {
          time: "13:00-16:00",
          title: "游览锦里古街",
          description: "体验传统川西建筑风格，品尝当地小吃如龙抄手、担担面。",
          attractions: "锦里古街",
          cost: "免费进入，小吃费用约30元",
          durationHours: 3
        },
        {
          time: "19:00-21:00",
          title: "观看川剧变脸表演",
          description: "欣赏四川传统戏曲艺术，包括变脸、吐火等绝技。",
          attractions: "蜀风雅韵剧院",
          cost: "门票约100元",
          durationHours: 2
        }
      ]
    },
    {
      dayIndex: 2,
      date: "2025-12-13",
      label: "历史与艺术之旅",
      items: [
        {
          time: "10:00-13:00",
          title: "参观杜甫草堂",
          description: "探访唐代诗人杜甫的故居，感受古典文学氛围。",
          attractions: "杜甫草堂",
          cost: "门票约60元",
          durationHours: 3
        },
        {
          time: "14:00-17:00",
          title: "游览宽窄巷子",
          description: "漫步于清代古街区，体验老成都的悠闲生活。",
          attractions: "宽窄巷子",
          cost: "免费进入，购物费用自理",
          durationHours: 3
        },
        {
          time: "18:00-20:00",
          title: "品尝正宗川菜",
          description: "在本地餐厅享用麻辣火锅或宫保鸡丁等经典菜肴。",
          attractions: "陈麻婆豆腐餐厅",
          cost: "人均约80元",
          durationHours: 2
        }
      ]
    },
    {
      dayIndex: 3,
      date: "2025-12-14",
      label: "自然与民俗探索",
      items: [
        {
          time: "08:00-12:00",
          title: "参观都江堰水利工程",
          description: "了解古代水利智慧，欣赏岷江风光。",
          attractions: "都江堰景区",
          cost: "门票约90元",
          durationHours: 4
        },
        {
          time: "13:00-16:00",
          title: "游览青城山",
          description: "探访道教发源地，享受山林清幽。",
          attractions: "青城山",
          cost: "门票约80元",
          durationHours: 3
        },
        {
          time: "19:00-21:00",
          title: "体验茶馆文化",
          description: "在传统茶馆品茶，听评书或打麻将。",
          attractions: "鹤鸣茶社",
          cost: "茶费约30元",
          durationHours: 2
        }
      ]
    },
    {
      dayIndex: 4,
      date: "2025-12-15",
      label: "现代与传统融合",
      items: [
        {
          time: "09:00-12:00",
          title: "参观成都博物馆",
          description: "了解成都从古至今的历史文化变迁。",
          attractions: "成都博物馆",
          cost: "免费",
          durationHours: 3
        },
        {
          time: "13:00-15:00",
          title: "购物与休闲",
          description: "在春熙路商圈逛街，购买特产如蜀绣或茶叶。",
          attractions: "春熙路",
          cost: "购物费用自理",
          durationHours: 2
        },
        {
          time: "16:00-18:00",
          title: "结束行程",
          description: "整理行李，准备返程。",
          attractions: "",
          cost: "待定",
          durationHours: 2
        }
      ]
    }
  ],
  status: "draft",
  isDraft: true
};


//模拟获取
onMounted(async () => {
  try {
    const response = await getTravelInfo(localStorage.token);
    // 适配新的 JSON 结构
    const data = response.data || response;

    // itinerary.value = testItinerary;

    // 修改点 1: 新格式中核心数据在 itinerary 字段下
    if (data.itinerary) {
      itinerary.value = data.itinerary;
    } else {
      // 容错处理
      itinerary.value = { days: [] };
    }
  } catch (error) {
    console.error("请求数据失败:", error);
  }
});



// 预算分解数据
//公里数
const randomDistance = computed(() => {
  // 生成30-100公里之间的随机数
  return (30 + Math.random() * 70).toFixed(1);
});

// 将budgetItems改为计算属性
const budgetItems = computed(() => {
  // 生成符合要求的随机百分比分配
  const housingPercent = 35 + Math.random() * 5; // 35%-40%
  const foodPercent = 75 - housingPercent; // 保证前两项之和为75%
  const transportPercent = 10 + Math.random() * 5; // 10%-15%
  const ticketPercent = 25 - transportPercent; // 保证后两项之和为25%

  // 总预算假设为2500元（根据模板中的数据）
  const totalBudget = 2500;

  // 根据百分比计算各项金额
  return [
    {
      category: "门票",
      amount: Math.round(totalBudget * ticketPercent / 100),
      percentage: parseFloat(ticketPercent.toFixed(2)),
      color: "#667eea"
    },
    {
      category: "餐饮",
      amount: Math.round(totalBudget * foodPercent / 100),
      percentage: parseFloat(foodPercent.toFixed(2)),
      color: "#764ba2"
    },
    {
      category: "住宿",
      amount: Math.round(totalBudget * housingPercent / 100),
      percentage: parseFloat(housingPercent.toFixed(2)),
      color: "#4f46e5"
    },
    {
      category: "交通",
      amount: Math.round(totalBudget * transportPercent / 100),
      percentage: parseFloat(transportPercent.toFixed(2)),
      color: "#8b5cf6"
    }
  ];
});


// 计算属性//修改1
const currentDayItinerary = computed(() => {
  return (
      itinerary.value.days.find(day => day.dayIndex === currentDay.value) || {}
  );
});
const totalDays = computed(() => {
  return itinerary.value.days ? itinerary.value.days.length : 0;
});

const totalAttractions = computed(() => {
  if (!itinerary.value.days) return 0;
  return itinerary.value.days.reduce((sum, day) => {
    // 统计每一天 items 数组的长度
    return sum + (day.items ? day.items.length : 0);
  }, 0);
});

const totalRestaurants = computed(() => {
  // return itinerary.value.dailyItineraries.reduce(
  //     (sum, day) => sum + (day.restaurants?.length || 0),
  //     0
  // );
  return 0;//临时修改测试
});

// 图表引用
const chartRef = ref(null);

// 方法
const navigateTo = (path) => router.push(path);

const formatDate = (date) => {
  const targetDate = new Date(date);
  return targetDate.toLocaleDateString("zh-CN", {
    month: "short",
    day: "numeric"
  });
};

const showDetail = (item) => {
  console.log("查看详情:", item);
  // 这里可以添加显示详情的逻辑
};

// 图表初始化
onMounted(() => {
  // 这里可以初始化图表
  // 例如使用 Chart.js 或 ECharts
});

onUnmounted(() => {
  // 清理图表
});
</script>

<style scoped>
/* 复用Home的变量和基础样式 */
.app-layout {
  --purple-1: #667eea;
  --purple-2: #764ba2;
  --ink: #0f172a;
  --stroke: rgba(15, 23, 42, 0.08);
  --glass: rgba(255, 255, 255, 0.78);
  --glass-2: rgba(255, 255, 255, 0.62);
  --sidebar-w: 239px;

  display: grid;
  grid-template-columns: var(--sidebar-w) 1fr;
  min-height: 100vh;
  gap: 0;
  column-gap: 0;
  background: radial-gradient(
      circle at 15% 10%,
      rgba(255, 255, 255, 0.1),
      transparent 45%
  ),
  radial-gradient(circle at 85% 30%, rgba(255, 255, 255, 0.08), transparent 40%),
  linear-gradient(135deg, var(--purple-1) 0%, var(--purple-2) 100%);
  isolation: isolate;
  position: relative;
}

.app-layout::before {
  content: "";
  position: absolute;
  inset: -120px;
  background: radial-gradient(
      circle at 30% 20%,
      rgba(255, 255, 255, 0.12),
      transparent 45%
  ),
  radial-gradient(circle at 70% 60%, rgba(255, 255, 255, 0.08), transparent 48%);
  filter: blur(18px);
  pointer-events: none;
  z-index: -1;
}

.app-layout.sidebar-collapsed {
  --sidebar-w: 0px;
}

/* Sidebar（与Home完全一致） */
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
  user-select: none;
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
  letter-spacing: 0.02em;
  color: #1d4ed8;
  white-space: nowrap;
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
  user-select: none;
}

.sidebar-item:hover {
  background: rgba(15, 23, 42, 0.04);
  transform: translateY(-1px);
}

.sidebar-item.active {
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: #fff;
  box-shadow: 0 16px 34px rgba(102, 126, 234, 0.26);
  border: 1px solid rgba(255, 255, 255, 0.18);
}

.icon {
  font-size: 18px;
}

.label {
  white-space: nowrap;
  font-size: 16px;
}

/* Main Content */
.main-content {
  grid-column: 2;
  min-width: 0;
  z-index: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  background: radial-gradient(
      circle at 18% 10%,
      rgba(102, 126, 234, 0.28),
      transparent 46%
  ),
  radial-gradient(circle at 82% 30%, rgba(118, 75, 162, 0.22), transparent 48%),
  radial-gradient(circle at 60% 90%, rgba(102, 126, 234, 0.16), transparent 52%),
  linear-gradient(
      135deg,
      rgba(102, 126, 234, 0.22),
      rgba(118, 75, 162, 0.18)
  );
  /* 关键修改：添加整体滚动 */
  height: 100vh;
  overflow-y: auto;
  margin-top: 72px; /* 添加margin-top，避免内容被固定header遮挡 */
}

.main-content::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background: rgba(255, 255, 255, 0.85);
  pointer-events: none;
  z-index: 1;
}

/* Edge Toggle Button */
.edge-toggle {
  position: fixed;
  left: calc(239px + 30px);
  top: 18px;
  transform: translateX(-50%);
  z-index: 1100; /* 比header更高的z-index */
  width: 42px;
  height: 42px;
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.55);
  background: rgba(255, 255, 255, 0.55);
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.14),
  inset 0 1px 0 rgba(255, 255, 255, 0.8);
  cursor: pointer;
  display: grid;
  place-items: center;
  transition: transform 0.2s ease, background 0.2s ease, box-shadow 0.2s ease;
}

.edge-toggle:hover {
  background: rgba(255, 255, 255, 0.75);
  box-shadow: 0 22px 52px rgba(15, 23, 42, 0.16),
  inset 0 1px 0 rgba(255, 255, 255, 0.9);
}

.chev {
  width: 10px;
  height: 10px;
  border-right: 3px solid rgba(76, 29, 149, 0.75);
  border-bottom: 3px solid rgba(76, 29, 149, 0.75);
  transform: rotate(135deg);
  transition: transform 0.2s ease;
}

.chev.right {
  transform: rotate(-45deg);
}

.app-layout.sidebar-collapsed .edge-toggle {
  left: 15px;
  transform: translateX(0);
}

/* Header */
.header.header-slogan {
  position: fixed;
  top: 0;
  left: 239px;
  right: 0;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center; /* 让中间的标语真正居中 */
  padding: 0 70px;
  background: radial-gradient(
      circle at 20% 0%,
      rgba(102, 126, 234, 0.22),
      transparent 55%
  ),
  radial-gradient(
      circle at 80% 100%,
      rgba(118, 75, 162, 0.18),
      transparent 60%
  ),
  rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.08),
  inset 0 1px 0 rgba(255, 255, 255, 0.65);
  z-index: 1000; /* 提高z-index，确保在最顶层 */
  transition: left 0.3s ease; /* 添加过渡效果，与侧边栏动画同步 */
}
/* 当侧边栏折叠时，调整header的left值 */
.app-layout.sidebar-collapsed .header.header-slogan {
  left: 0; /* 侧边栏折叠时，header左对齐 */
}



.slogan-wrap {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.1),
  inset 0 1px 0 rgba(255, 255, 255, 0.7);
  margin: 0 auto;
}

.slogan-text {
  font-size: 18px;
  font-weight: 900;
  letter-spacing: 0.1em;
  line-height: 1;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 12px 30px rgba(102, 126, 234, 0.16);
  user-select: none;
  white-space: nowrap;
}

.slogan-dot {
  width: 8px;
  height: 8px;
  border-radius: 999px;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  box-shadow: 0 10px 22px rgba(102, 126, 234, 0.22);
  opacity: 0.95;
}

/* User Info */
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  position: absolute; /* 固定在右边，不影响中间居中 */
  right: 70px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 1; /* 确保在header上方 */
}

.user-avatar {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: white;
  box-shadow: 0 10px 25px rgba(102, 126, 234, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.avatar-icon {
  font-size: 20px;
}

.user-name {
  font-weight: 600;
  color: #0f172a;
  font-size: 15px;
}

/* Content Area */
.content-area {
  flex: 1;
  min-height: calc(100vh - 72px);
  padding: clamp(20px, 2.5vw, 32px);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  background: transparent;
}

/* Result Container */
.result-container {
  width: min(1400px, 100%);
  display: grid;
  grid-template-columns: 1fr 360px;
  gap: 24px;
  height: calc(100vh - 72px - clamp(32px, 5vw, 64px));
}

/* Glass Card（通用卡片样式） */
.glass-card {
  background: radial-gradient(
      circle at 20% 0%,
      rgba(102, 126, 234, 0.12),
      transparent 42%
  ),
  radial-gradient(
      circle at 90% 30%,
      rgba(118, 75, 162, 0.1),
      transparent 45%
  ),
  var(--glass);
  border: 1px solid rgba(255, 255, 255, 0.55);
  box-shadow: 0 20px 60px rgba(17, 24, 39, 0.15),
  0 10px 30px rgba(102, 126, 234, 0.12);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-radius: 20px;
}

/* Main Card - 整个主行程卡片增加滚动条 */
.main-card {
  grid-column: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow-y: auto; /* 4. 主行程右侧滚动条 */
}

.main-card::-webkit-scrollbar {
  width: 8px;
}

.main-card::-webkit-scrollbar-track {
  background: rgba(15, 23, 42, 0.04);
  border-radius: 4px;
}

.main-card::-webkit-scrollbar-thumb {
  background: linear-gradient(to bottom, var(--purple-1), var(--purple-2));
  border-radius: 4px;
  border: 2px solid rgba(255, 255, 255, 0.8);
}

.main-card::-webkit-scrollbar-thumb:hover {
  opacity: 0.9;
}

.result-header {
  padding: 24px 28px;
  border-bottom: 1px solid rgba(15, 23, 42, 0.08);
  background: radial-gradient(
      circle at 15% 10%,
      rgba(102, 126, 234, 0.14),
      transparent 40%
  ),
  radial-gradient(
      circle at 85% 45%,
      rgba(118, 75, 162, 0.1),
      transparent 45%
  ),
  rgba(255, 255, 255, 0.52);
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-shrink: 0;
}

.header-left {
  flex: 1;
}

.plan-title {
  font-size: 28px;
  font-weight: 900;
  color: #0f172a;
  margin-bottom: 12px;
  line-height: 1.2;
}

.plan-meta {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.meta-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(15, 23, 42, 0.08);
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  color: #0f172a;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.meta-icon {
  font-size: 14px;
}

/* Day Navigation */
.day-navigation {
  padding: 16px 28px;
  border-bottom: 1px solid rgba(15, 23, 42, 0.06);
  background: rgba(248, 250, 252, 0.5);
  flex-shrink: 0;
}

.day-scroll {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.day-scroll::-webkit-scrollbar {
  height: 4px;
}

.day-scroll::-webkit-scrollbar-track {
  background: rgba(15, 23, 42, 0.04);
  border-radius: 4px;
}

.day-scroll::-webkit-scrollbar-thumb {
  background: linear-gradient(
      to right,
      rgba(102, 126, 234, 0.4),
      rgba(118, 75, 162, 0.4)
  );
  border-radius: 4px;
}

.day-tab {
  padding: 12px 20px;
  border-radius: 12px;
  background: white;
  border: 1px solid rgba(15, 23, 42, 0.08);
  cursor: pointer;
  transition: all 0.25s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  min-width: 100px;
  flex-shrink: 0;
}

.day-tab:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
}

.day-tab.active {
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: white;
  border-color: rgba(255, 255, 255, 0.2);
  box-shadow: 0 12px 28px rgba(102, 126, 234, 0.25);
}

.day-number {
  font-weight: 700;
  font-size: 16px;
}

.day-date {
  font-size: 12px;
  opacity: 0.8;
}

.day-tab.active .day-date {
  opacity: 0.9;
}

/* Day Content - 不再单独滚动，由 main-card 控制整体滚动 */
.day-content {
  flex: 1;
  padding: 28px;
}

.day-timeline {
  position: relative;
}

.timeline-item {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;
  position: relative;
}

.timeline-marker {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;
  width: 24px;
}

.marker-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  border: 3px solid white;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
  z-index: 2;
  position: relative;
}

.marker-line {
  position: absolute;
  top: 16px;
  bottom: -24px;
  width: 2px;
  background: linear-gradient(to bottom, var(--purple-1), var(--purple-2));
  opacity: 0.2;
  z-index: 1;
}

.timeline-content {
  flex: 1;
  display: grid;
  grid-template-columns: 80px 1fr;
  gap: 20px;
  padding: 20px;
  transition: transform 0.25s ease;
}

.timeline-content:hover {
  transform: translateY(-2px);
  box-shadow: 0 20px 50px rgba(17, 24, 39, 0.18),
  0 10px 30px rgba(102, 126, 234, 0.15);
}

.timeline-time {
  font-size: 16px;
  font-weight: 700;
  color: var(--purple-1);
  display: flex;
  align-items: flex-start;
  padding-top: 4px;
}

.timeline-main {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.attraction-name {
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
  line-height: 1.3;
}

.attraction-desc {
  color: #475569;
  line-height: 1.5;
  font-size: 14px;
}

.attraction-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-top: 8px;
}

.cost-badge,
.duration-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(255, 255, 255, 0.7);
  border: 1px solid rgba(15, 23, 42, 0.08);
  border-radius: 10px;
  font-size: 13px;
  font-weight: 500;
  color: #0f172a;
}

.cost-icon,
.duration-icon {
  font-size: 12px;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  border-radius: 14px;
  font-weight: 600;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.25s ease;
  border: 1px solid transparent;
  white-space: nowrap;
}

.btn-text {
  background: transparent;
  color: var(--purple-1);
  padding: 8px 12px;
  border: none;
  box-shadow: none;
}

.btn-text:hover {
  background: rgba(102, 126, 234, 0.08);
  transform: translateY(-1px);
}

.btn-detail {
  margin-left: auto;
  font-weight: 600;
  color: var(--purple-1);
  padding: 0;
}

/* Side Panel */
.side-panel {
  grid-column: 2;
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: 100%;
}

.info-card {
  padding: 20px 24px 22px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.card-title {
  font-size: 18px;
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-icon {
  font-size: 20px;
}

/* Budget Chart - 调整预算分解模块，使文字更靠上 */
.budget-chart {
  margin-top: 4px;
  margin-bottom: 8px;
  height: 80px; /* 从 120 减小为 80，避免占位太空 */
}

.chart-container {
  height: 100%;
  width: 100%;
  position: relative;
}

/* Budget List - 调整预算列表样式 */
.budget-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.budget-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.budget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.budget-category {
  font-weight: 600;
  color: #0f172a;
  font-size: 14px;
}

.budget-amount {
  font-weight: 700;
  color: var(--purple-1);
  font-size: 15px;
}

.budget-progress {
  height: 6px;
  background: rgba(15, 23, 42, 0.08);
  border-radius: 3px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  border-radius: 3px;
  transition: width 0.5s ease;
}

.budget-percentage {
  font-size: 12px;
  color: #64748b;
  align-self: flex-end;
  font-weight: 600;
}

/* Overview Stats */
.overview-stats {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 18px;
}

.stat-item {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 14px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  border: 1px solid rgba(15, 23, 42, 0.06);
}

.stat-icon {
  font-size: 24px;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: white;
  border-radius: 12px;
  flex-shrink: 0;
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-value {
  font-size: 20px;
  font-weight: 800;
  color: #0f172a;
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: #64748b;
  font-weight: 500;
}

/* Overview Summary */
.overview-summary {
  padding-top: 16px;
  border-top: 1px solid rgba(15, 23, 42, 0.06);
}

.summary-title {
  font-size: 16px;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 10px;
}

.summary-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.summary-tag {
  padding: 6px 12px;
  background: rgba(255, 255, 255, 0.8);
  border: 1px solid rgba(15, 23, 42, 0.08);
  border-radius: 10px;
  font-size: 13px;
  color: #0f172a;
  display: flex;
  align-items: center;
  gap: 6px;
}

@media (max-width: 1200px) {
  .result-container {
    grid-template-columns: 1fr;
    gap: 20px;
    height: auto;
  }

  .side-panel {
    grid-column: 1;
    grid-template-columns: repeat(2, 1fr);
    display: grid;
    gap: 20px;
  }

  .main-card {
    max-height: 520px;
  }
}

@media (max-width: 768px) {
  .app-layout {
    display: flex;
    flex-direction: column;
    height: 100vh;
    overflow: hidden;
  }

  /* 修改侧边栏为移动端样式 */
  .sidebar {
    position: fixed;
    top: 0;
    left: 0;
    width: 280px;
    height: 100vh;
    background: #ffffff;
    box-shadow: 0 14px 40px rgba(15, 23, 42, 0.12);
    z-index: 1001;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
    margin-top: 0;
    opacity: 1;
    pointer-events: auto;
  }

  /* 修复：确保侧边栏在展开状态时正确显示 */
  .sidebar.collapsed {
    transform: translateX(-100%); /* 隐藏在左侧 */
  }

  /* 遮罩层样式 */
  .sidebar-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    z-index: 1000;
    backdrop-filter: blur(2px);
  }

  /* 当侧边栏展开时，显示出来 */
  .sidebar:not(.collapsed) {
    transform: translateX(0); /* 移动到可见位置 */
  }

  /* 修改header样式 */
  .header.header-slogan {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    padding: 0 16px;
    height: 60px;
    z-index:999;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
  }

  /* 修改main-content样式 */
  .main-content {
    height: 100vh;
    margin-top: 0;
    overflow-y: auto;
    padding-top: 60px;
    position: relative;
    z-index: 1;
  }

  /* 修改折叠按钮样式 */
  .edge-toggle {
    position: fixed;
    left: 16px;
    top: 18px;
    transform: none;
    z-index: 1100;
    background: rgba(255, 255, 255, 0.9);
    border: 1px solid rgba(15, 23, 42, 0.1);
  }

  .user-info {
    position: static;
    transform: none;
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .content-area {
    padding: 16px;
    padding-top: 0;
  }

  .result-container {
    height: auto;
  }

  .result-header {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .side-panel {
    grid-template-columns: 1fr;
  }

  .overview-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .day-content {
    padding: 20px;
  }

  .main-card {
    max-height: none;
  }
}

@media (max-width: 480px) {
  .main-content {
    height: 100vh;
    padding-top: 50px;
  }

  .header.header-slogan {
    height: 50px;
  }

  .plan-title {
    font-size: 22px;
  }

  .overview-stats {
    grid-template-columns: 1fr;
  }

  .timeline-content {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .timeline-time {
    font-size: 14px;
  }

  .day-navigation {
    padding: 12px 16px;
  }

  .day-tab {
    min-width: 80px;
    padding: 10px 16px;
  }

  .user-info {
    position: static;
    transform: none;
  }

  .edge-toggle {
    left: 12px;
    top: 15px;
  }
}
</style>
