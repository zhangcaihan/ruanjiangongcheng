<template>
  <!-- Grid：sidebar 展开=260px；收起=0px（主区吃满） -->
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
        <div class="sidebar-item active" @click="navigateTo('/home')">
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
        <div class="sidebar-item" @click="navigateTo('/profile')">
          <span class="icon">👤</span>
          <span class="label">个人中心</span>
        </div>
      </nav>
    </aside>

    <!-- Main -->
    <main class="main-content">
      <!-- ✅ 永远贴着 sidebar/main 的分界线显示（收起时也在最左边） -->
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
        <div class="slogan-wrap" aria-label="标语">
          <span class="slogan-dot"></span>
          <span class="slogan-text">希望你喜欢你的每一次旅途</span>
          <span class="slogan-dot"></span>
        </div>
      </header>

      <!-- 内容区域 -->
      <div class="content-area">
        <div class="chat-card">
          <!-- 聊天头部 -->
          <div class="chat-header">
            <div class="chat-title">
              <div class="bot-avatar">🧭</div>
              <div>
                <div class="font-bold text-lg">TravelMate 智能规划助手</div>
                <div class="text-sm text-gray-600">
                  用一句话描述你的旅行需求，我来生成行程
                </div>
              </div>
            </div>
            <button class="btn btn-secondary btn-sm" type="button" @click="resetChat">
              清空对话
            </button>
          </div>

          <!-- 聊天消息区 -->
          <div class="chat-body" ref="chatBodyRef">
            <div v-if="messages.length === 0" class="welcome-message">
              <div class="msg-row assistant">
                <div class="msg-avatar">🤖</div>
                <div class="msg-bubble assistant">
                  <div class="msg-text">
                    你好！我是TravelMate，你的智能旅行规划助手。告诉我你的旅行想法，我来帮你规划完美行程！
                  </div>
                  <div class="msg-meta">试试点击下方的快捷模板开始</div>
                </div>
              </div>
            </div>

            <div v-for="m in messages" :key="m.id" class="msg-row" :class="m.role">
              <div v-if="m.role === 'assistant'" class="msg-avatar">🤖</div>

              <!-- 修改消息气泡部分 -->
              <div class="msg-bubble" :class="m.role">
                <!-- 删除原有的markdown渲染 -->
                <!-- <div class="msg-markdown" v-html="renderMarkdown(m.content, !isStreamingAssistant(m))"></div> -->

                <!-- 新增：JSON数据解析展示 -->
                <div v-if="m.itineraryData" class="itinerary-content">
                  <div class="itinerary-summary">
                    <h3>📅 行程概览</h3>
                    <p>共 {{ m.itineraryData.days.length }} 天行程</p>
                  </div>

                  <div v-for="day in m.itineraryData.days" :key="day.dayIndex" class="day-section">
                    <div class="day-header">
                      <span class="day-label">{{ day.label }}</span>
                      <span class="day-date">{{ day.date }}</span>
                    </div>

                    <div v-for="item in day.items" :key="item.time" class="activity-item">
                      <div class="activity-time">{{ item.time }}</div>
                      <div class="activity-content">
                        <div class="activity-title">{{ item.title }}</div>
                        <div class="activity-description" v-html="formatDescription(item.description)"></div>
                        <div class="activity-details">
                          <span class="detail-tag">📍 {{ item.attractions }}</span>
                          <span class="detail-tag">💰 {{ item.cost }}</span>
                          <span class="detail-tag">⏱ {{ item.durationHours }}小时</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 保留纯文本回退 -->
                <div v-else class="msg-text">{{ m.content }}</div>

                <div v-if="m.meta" class="msg-meta">{{ m.meta }}</div>

                <!-- 流式加载指示器 -->
                <div v-if="m.isStreaming" class="typing-indicator">
                  <div class="typing-dots">
                    <span></span><span></span><span></span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 快捷模板 -->
          <div class="chat-quick">
            <div class="quick-title">快捷模板</div>
            <div class="flex gap-3 flex-wrap">
              <button
                  type="button"
                  class="btn btn-secondary"
                  @click="selectTemplate('北京三日游 1500元')"
              >
                北京三日游 1500元
              </button>
              <button
                  type="button"
                  class="btn btn-secondary"
                  @click="selectTemplate('杭州两日游 美食之旅')"
              >
                杭州两日游 美食之旅
              </button>
              <button
                  type="button"
                  class="btn btn-secondary"
                  @click="selectTemplate('成都四日游 文化体验')"
              >
                成都四日游 文化体验
              </button>
              <button
                  type="button"
                  class="btn btn-secondary"
                  @click="selectTemplate('西安三日游 历史文化')"
              >
                西安三日游 历史文化
              </button>
              <button
                  type="button"
                  class="btn btn-secondary"
                  @click="selectTemplate('厦门两日游 海滨风光')"
              >
                厦门两日游 海滨风光
              </button>
            </div>
          </div>

          <!-- 输入区 -->
          <div class="chat-input-new">
            <div class="input-container">
              <textarea
                  class="chat-textarea-new"
                  placeholder="输入你的旅行需求，例如：上海三日游，2000预算，喜欢美食和艺术"
                  v-model="travelRequest"
                  @keydown.enter.exact.prevent="startPlanning"
                  :disabled="loading"
                  rows="1"
                  ref="textareaRef"
              ></textarea>

              <button
                  type="button"
                  class="send-button"
                  :disabled="loading || !travelRequest.trim()"
                  @click="startPlanning"
                  :class="{ sending: loading }"
                  aria-label="发送"
              >
                <svg v-if="!loading" class="send-icon" viewBox="0 0 24 24" fill="none">
                  <path
                      d="M5 12L19 12M12 5L19 12L12 19"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                  />
                </svg>
                <svg v-else class="loading-icon" viewBox="0 0 24 24" fill="none">
                  <circle
                      class="loading-circle"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      stroke-width="2"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>


<script setup>
import {
  ref,
  nextTick,
  onMounted,
  watch,
  onBeforeUnmount,
  computed,
} from "vue";
import { useRouter } from "vue-router";
import { planItinerary, parseItineraryFromMarkdown } from "../api/itinerary";
import DOMPurify from 'dompurify';
const router = useRouter();

const sidebarCollapsed = ref(false);
const toggleSidebar = () => (sidebarCollapsed.value = !sidebarCollapsed.value);

// 输入
const travelRequest = ref("");
const loading = ref(false);
const textareaRef = ref(null);

// 聊天消息
const messages = ref([]);
const chatBodyRef = ref(null);

// 存储第一个接口返回的原始数据，用于后续确认
const pendingItineraryData = ref({
  markdown: "",
  startCity: "",
  endCity: "",
  startDate: "",
  endDate: "",
});

// JSON解析和自然语言格式化函数
const parseAndFormatItinerary = (jsonData) => {
  try {
    // 如果是字符串，先尝试解析为JSON
    let data = jsonData;
    if (typeof jsonData === 'string') {
      // 清理可能的无效字符
      const cleaned = jsonData.replace(/[\x00-\x1F\x7F]/g, '').trim();
      if (!cleaned) return { days: [] };

      data = JSON.parse(cleaned);
    }

    // 验证数据结构
    if (!data || typeof data !== 'object') {
      throw new Error('无效的数据格式');
    }

    // 确保days是数组
    if (!Array.isArray(data.days)) {
      data.days = [];
    }

    // 验证每个day的结构
    data.days.forEach((day, index) => {
      if (!day.dayIndex) day.dayIndex = index + 1;
      if (!day.label) day.label = `第${day.dayIndex}天`;
      if (!day.items) day.items = [];
      if (!Array.isArray(day.items)) day.items = [];

      // 验证每个item的结构
      day.items.forEach(item => {
        if (!item.time) item.time = '待定';
        if (!item.title) item.title = '未命名活动';
        if (!item.description) item.description = '';
        if (!item.attractions) item.attractions = '';
        if (!item.cost) item.cost = '待定';
        if (!item.durationHours) item.durationHours = 0;
      });
    });

    return data;
  } catch (error) {
    // 返回一个安全的空结构
    return {
      days: [],
      error: error.message
    };
  }
};

// 修改formatDescription函数，确保返回安全的HTML
const formatDescription = (description) => {
  if (!description) return '';
  // 使用DOMPurify进行安全过滤
  return DOMPurify.sanitize(
      description
          .replace(/\\n/g, '<br>')
          .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
          .replace(/> (.*?)(<br>|$)/g, '<blockquote>$1</blockquote>')
  );
};

// 将JSON数据转换为自然语言摘要
const generateNaturalLanguageSummary = (itineraryData) => {
  if (!itineraryData.days || itineraryData.days.length === 0) {
    return '暂无行程信息';
  }

  const days = itineraryData.days;
  const totalDays = days.length;
  const firstDay = days[0];
  const lastDay = days[days.length - 1];

  let summary = `为您规划了${totalDays}天行程，从${firstDay.date}到${lastDay.date}。`;

  // 添加每天的主要活动
  days.forEach(day => {
    if (day.items && day.items.length > 0) {
      const mainActivities = day.items.map(item => item.title).join('、');
      summary += ` ${day.label}主要安排：${mainActivities}。`;
    }
  });

  return summary;
};

const escapeHtml = (unsafe = "") =>
    unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");

// 自动调整文本域高度
const adjustTextareaHeight = () => {
  const textarea = textareaRef.value;
  if (!textarea) return;
  textarea.style.height = "auto";
  textarea.style.height = Math.min(textarea.scrollHeight, 120) + "px";
};
const onInput = () => adjustTextareaHeight();

onMounted(() => {
  if (textareaRef.value) {
    textareaRef.value.addEventListener("input", onInput);
    adjustTextareaHeight();
  }
});

onBeforeUnmount(() => {
  if (textareaRef.value)
    textareaRef.value.removeEventListener("input", onInput);
});
watch(travelRequest, async () => {
  await nextTick();
  adjustTextareaHeight();
});

const scrollToBottom = () => {
  const el = chatBodyRef.value;
  if (!el) return;
  el.scrollTop = el.scrollHeight;
};

const pushMsg = async (role, content, meta = "") => {
  messages.value.push({
    id: `${Date.now()}-${Math.random()}`,
    role,
    content,
    meta,
  });
  await nextTick();
  scrollToBottom();
};

// 导航
const navigateTo = (path) => router.push(path);

// 选择模板
const selectTemplate = async (template) => {
  travelRequest.value = template;
  await nextTick();
  textareaRef.value?.focus();
};

// 清空对话
const resetChat = () => {
  messages.value = [];
  travelRequest.value = "";
  pendingItineraryData.value = {
    markdown: "",
    startCity: "",
    endCity: "",
    startDate: "",
    endDate: "",
  };
  nextTick(adjustTextareaHeight);
};

// 输入中状态
const isTypingMessage = (message) => {
  return (
      message.id.startsWith("temp-") &&
      message.content === "" &&
      message.meta === "正在生成行程..."
  );
};

/** 流式渲染节流 */
let rafPending = false;
let latestContent = "";

const scheduleUiUpdate = (tempMessageId) => {
  if (rafPending) return;
  rafPending = true;
  requestAnimationFrame(() => {
    rafPending = false;
    const idx = messages.value.findIndex((m) => m.id === tempMessageId);
    if (idx !== -1) {
      messages.value[idx].content = latestContent;
    }
    scrollToBottom();
  });
};
const saveItinerary = async (itineraryData, token) => {
  try {
    const response = await fetch(`http://106.15.90.163:8080/user/cacheTravelInfo?token=${token}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        itinerary: itineraryData,
        timestamp: new Date().toISOString()
      })
    });

    if (!response.ok) {
      throw new Error(`保存失败: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('保存行程失败:', error);
    throw error;
  }
};

// 修改后的startPlanning函数
const startPlanning = async () => {
  const text = travelRequest.value.trim();
  if (!text || loading.value) return;

  loading.value = true;
  await pushMsg("user", text);
  travelRequest.value = "";
  adjustTextareaHeight();

  let tempMessageId;
  let rawJsonData = '';

  try {
    // 创建流式临时消息
    tempMessageId = `temp-${Date.now()}`;
    messages.value.push({
      id: tempMessageId,
      role: "assistant",
      content: "正在为您规划行程...",
      itineraryData: null, // 新增：存储解析后的JSON数据
      meta: "正在生成...",
      isStreaming: true
    });

    await nextTick();
    scrollToBottom();

    // 解析用户输入
    const parsedInfo = parseUserInput(text);

    const params = {
      startCity: parsedInfo.startCity,
      endCity: parsedInfo.endCity,
      startDate: parsedInfo.startDate,
      endDate: parsedInfo.endDate,
      token: localStorage.getItem("token") || "",
      userInput: text,
    };

    // 修改startPlanning函数中的流式处理部分
    let parseAttempts = 0;
    const MAX_PARSE_ATTEMPTS = 3;

    // 流式接收JSON数据
    // 流式接收JSON数据
    await planItinerary(params, (chunk) => {
      // 累积原始JSON数据
      rawJsonData += chunk;

      // 限制解析频率，避免过于频繁的解析尝试
      if (parseAttempts < MAX_PARSE_ATTEMPTS) {
        try {
          // 尝试解析累积的数据
          const parsedData = parseAndFormatItinerary(rawJsonData);
          parseAttempts = 0; // 重置尝试次数

          // 更新临时消息
          const tempIdx = messages.value.findIndex((m) => m.id === tempMessageId);
          if (tempIdx !== -1) {
            messages.value[tempIdx].itineraryData = parsedData;
            messages.value[tempIdx].content = generateNaturalLanguageSummary(parsedData);

            // 触发响应式更新
            messages.value = [...messages.value];
            scheduleUiUpdate(tempMessageId);
          }
        } catch (error) {
          parseAttempts++;
          // JSON还不完整，继续累积
          //console.log('接收JSON数据中...', chunk.length, 'bytes');
        }
      }
    });

    // 流式结束后，确认数据完整性
    let finalData;
    try {
      finalData = parseAndFormatItinerary(rawJsonData);
    } catch (error) {
      //console.error('最终JSON解析失败:', error);
      throw new Error('行程数据格式错误');
    }

    // 将临时消息转为正式消息
    const tempIdx = messages.value.findIndex((m) => m.id === tempMessageId);
    if (tempIdx !== -1) {
      messages.value[tempIdx] = {
        id: `${Date.now()}-${Math.random()}`,
        role: "assistant",
        content: generateNaturalLanguageSummary(finalData),
        itineraryData: finalData,
        meta: "行程生成完成",
        isStreaming: false
      };
    }

    // 保存行程数据供确认使用
    pendingItineraryData.value = {
      rawData: rawJsonData,
      parsedData: finalData,
      startCity: parsedInfo.startCity,
      endCity: parsedInfo.endCity,
      startDate: parsedInfo.startDate,
      endDate: parsedInfo.endDate,
    };

    // 可选：自动保存为草稿
    try {
      const userToken = localStorage.getItem("token");
      if (userToken && finalData) {
        // 标记为草稿
        const draftData = {
          ...finalData,
          status: 'draft',
          isDraft: true
        };

        await saveItinerary(draftData, userToken);
        //console.log('行程草稿已自动保存');
      }
    } catch (draftError) {
      console.warn('自动保存草稿失败:', draftError);
      // 不中断主流程
    }

    // 添加确认提示
    await pushMsg(
        "assistant",
        "行程规划完成！请检查内容，若满意可以点击\"确认该行程\"。",
        "系统提示"
    );

  } catch (error) {
    console.error("规划行程失败:", error);

    // 清理临时消息
    if (tempMessageId) {
      const tempIdx = messages.value.findIndex((m) => m.id === tempMessageId);
      if (tempIdx !== -1) {
        messages.value.splice(tempIdx, 1);
      }
    }

    await pushMsg(
        "assistant",
        `抱歉，规划过程中出现了问题：${error.message}`,
        "系统提示"
    );
  } finally {
    loading.value = false;
  }
};


// 解析用户输入，提取城市和日期信息
const parseUserInput = (input) => {
  // 这里实现简单的解析逻辑
  // 实际项目中可能需要更复杂的自然语言处理
  // 示例：匹配"X日游"或"X天"
  const dayMatch = input.match(/(\d+)(?:日|天)/);
  const days = dayMatch ? parseInt(dayMatch[1]) : 3;

  // 示例：匹配城市（简化版）
  const cities = ["北京", "上海", "广州", "深圳", "杭州", "成都", "西安", "厦门"];
  const foundCities = cities.filter(city => input.includes(city));

  // 设置默认值
  const today = new Date();
  const endDate = new Date(today);
  endDate.setDate(today.getDate() + days);

  return {
    startCity: foundCities[0] || "北京",
    endCity: foundCities[1] || foundCities[0] || "上海",
    startDate: formatDate(today),
    endDate: formatDate(endDate),
  };
};
// 格式化日期为 YYYY-MM-DD
const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};
/** 只对"最后一条正式 assistant 消息"展示确认按钮 */
const lastAssistantIndex = computed(() => {
  for (let i = messages.value.length - 1; i >= 0; i--) {
    const m = messages.value[i];
    if (m.role === "assistant" && !m.id.startsWith("temp-")) {
      return i;
    }
  }
  return -1;
});

const showConfirmFor = (m) => {
  const idx = messages.value.findIndex((x) => x.id === m.id);
  return (
      !loading.value &&
      idx === lastAssistantIndex.value &&
      m.role === "assistant" &&
      (m.content || "").trim().length > 0 &&
      pendingItineraryData.value.markdown // 确保有数据可以确认
  );
};

// 修改确认行程函数
const confirmItinerary = async (m) => {
  try {
    loading.value = true;

    if (!pendingItineraryData.value.parsedData) {
      throw new Error('没有可确认的行程数据');
    }

    // 调用第二个接口格式化数据
    const formattedData = await parseItineraryFromMarkdown({
      // 这里可能需要调整参数，因为现在不是markdown了
      itineraryData: pendingItineraryData.value.parsedData,
      startCity: pendingItineraryData.value.startCity,
      endCity: pendingItineraryData.value.endCity,
      startDate: pendingItineraryData.value.startDate,
      endDate: pendingItineraryData.value.endDate,
      token: localStorage.getItem("token") || ""
    });

    // 缓存到浏览器
    localStorage.setItem("confirmedItinerary", JSON.stringify({
      ...formattedData,
      timestamp: new Date().toISOString(),
    }));

    // 通知其他组件
    window.dispatchEvent(new CustomEvent('itineraryConfirmed', {
      detail: formattedData
    }));

    await pushMsg(
        "assistant",
        "✅ 行程已确认！你可以在\"我的行程\"页面查看详细安排。",
        "系统提示"
    );

    // 清空待处理数据
    pendingItineraryData.value = {
      rawData: "",
      parsedData: null,
      startCity: "",
      endCity: "",
      startDate: "",
      endDate: "",
    };

  } catch (error) {
    console.error("确认行程失败：", error);
    await pushMsg(
        "assistant",
        `确认行程时出错：${error.message}`,
        "系统提示"
    );
  } finally {
    loading.value = false;
  }
};

// 复制 Markdown，方便用户粘贴
const copyMarkdown = async (text) => {
  try {
    await navigator.clipboard.writeText(text || "");
    await pushMsg("assistant", "已复制到剪贴板 📋", "系统提示");
  } catch {
    await pushMsg(
        "assistant",
        "复制失败，请手动选择文本复制。",
        "系统提示"
    );
  }
};

// 导出一些方法供其他组件使用
defineExpose({
  getConfirmedItinerary: () => {
    const stored = localStorage.getItem("confirmedItinerary");
    return stored ? JSON.parse(stored) : null;
  },
  clearItineraryCache: () => {
    localStorage.removeItem("confirmedItinerary");
    sessionStorage.removeItem("currentItinerary");
  },
});
</script>

<style scoped>
/* ====== 布局：Grid，sidebar 收起时列宽=0，主区吃满视口 ====== */
.app-layout {
  /* 主题变量（紫色基调） */
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

  background:
      radial-gradient(circle at 15% 10%, rgba(255, 255, 255, 0.1), transparent 45%),
      radial-gradient(circle at 85% 30%, rgba(255, 255, 255, 0.08), transparent 40%),
      linear-gradient(135deg, var(--purple-1) 0%, var(--purple-2) 100%);
  position: relative;
  overflow: hidden;

  /* ✅ 独立堆叠上下文：避免伪元素压到内容上造成“紫色竖条” */
  isolation: isolate;
}

/* 背景光晕层：强制压到最底下 */
.app-layout::before {
  content: "";
  position: absolute;
  inset: -120px;
  background:
      radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.12), transparent 45%),
      radial-gradient(circle at 70% 60%, rgba(255, 255, 255, 0.08), transparent 48%);
  filter: blur(18px);
  pointer-events: none;
  z-index: -1;
}

.app-layout.sidebar-collapsed {
  --sidebar-w: 0px;
}

/* ====== Sidebar ====== */
.sidebar {
  grid-column: 1;
  min-width: 0;
  height: 100vh;

  /* ✅ 不透明：防止透出紫色底造成“竖条” */
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

/* ====== Main：紫色基调背景 ====== */
.main-content {
  grid-column: 2;
  min-width: 0;
  z-index: 1;
  display: flex;
  flex-direction: column;
  position: relative;

  background:
      radial-gradient(circle at 18% 10%, rgba(102, 126, 234, 0.28), transparent 46%),
      radial-gradient(circle at 82% 30%, rgba(118, 75, 162, 0.22), transparent 48%),
      radial-gradient(circle at 60% 90%, rgba(102, 126, 234, 0.16), transparent 52%),
      linear-gradient(135deg, rgba(102, 126, 234, 0.22), rgba(118, 75, 162, 0.18));
}

/* 分界线 */
.main-content::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background: rgba(255, 255, 255, 0.85);
  pointer-events: none;
}

/* ====== 分界线按钮 ====== */
.edge-toggle {
  position: absolute;
  left: 30px;
  top: 18px;
  transform: translateX(-50%);
  z-index: 50;

  width: 42px;
  height: 42px;
  border-radius: 16px;

  border: 1px solid rgba(255, 255, 255, 0.55);
  background: rgba(255, 255, 255, 0.55);
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.14), inset 0 1px 0 rgba(255, 255, 255, 0.8);

  cursor: pointer;
  display: grid;
  place-items: center;
  transition: transform 0.2s ease, background 0.2s ease, box-shadow 0.2s ease;
}
.edge-toggle:hover {
  background: rgba(255, 255, 255, 0.75);
  box-shadow: 0 22px 52px rgba(15, 23, 42, 0.16), inset 0 1px 0 rgba(255, 255, 255, 0.9);
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
  transform: translateX(6px);
}

/* ====== Header ====== */
.header.header-slogan {
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 18px;

  background:
      radial-gradient(circle at 20% 0%, rgba(102, 126, 234, 0.22), transparent 55%),
      radial-gradient(circle at 80% 100%, rgba(118, 75, 162, 0.18), transparent 60%),
      rgba(255, 255, 255, 0.55);

  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);

  border-bottom: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06), inset 0 1px 0 rgba(255, 255, 255, 0.65);
}

.slogan-wrap {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  padding: 10px 16px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.1), inset 0 1px 0 rgba(255, 255, 255, 0.7);
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

/* ====== 内容区域 ====== */
.content-area {
  flex: 1;
  min-height: calc(100vh - 72px);
  padding: clamp(14px, 2.2vw, 28px);
  display: flex;
  justify-content: center;
  align-items: stretch;
  background: transparent;
}

/* ====== Chat Card：紫系玻璃态 ====== */
.chat-card {
  width: min(1320px, 100%);
  height: calc(100vh - 72px - clamp(28px, 4.4vw, 56px));
  display: flex;
  flex-direction: column;

  border-radius: 24px;
  overflow: hidden;

  background:
      radial-gradient(circle at 20% 0%, rgba(102, 126, 234, 0.18), transparent 42%),
      radial-gradient(circle at 90% 30%, rgba(118, 75, 162, 0.14), transparent 45%),
      var(--glass);

  border: 1px solid rgba(255, 255, 255, 0.55);
  box-shadow: 0 30px 90px rgba(17, 24, 39, 0.18), 0 10px 30px rgba(102, 126, 234, 0.14);

  backdrop-filter: blur(18px);
  -webkit-backdrop-filter: blur(18px);
}

/* ====== 聊天区域样式 ====== */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 22px 24px 16px;
  border-bottom: 1px solid rgba(15, 23, 42, 0.08);
  background:
      radial-gradient(circle at 15% 10%, rgba(102, 126, 234, 0.14), transparent 40%),
      radial-gradient(circle at 85% 45%, rgba(118, 75, 162, 0.1), transparent 45%),
      rgba(255, 255, 255, 0.52);
}
.chat-title {
  display: flex;
  align-items: center;
  gap: 16px;
}
.bot-avatar {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: white;
  font-size: 24px;
  box-shadow: 0 16px 36px rgba(102, 126, 234, 0.26);
  border: 1px solid rgba(255, 255, 255, 0.18);
}

.chat-body {
  flex: 1;
  min-height: 220px;
  padding: 20px;
  overflow-y: auto;

  background:
      radial-gradient(circle at 18% 10%, rgba(102, 126, 234, 0.1), transparent 40%),
      radial-gradient(circle at 82% 45%, rgba(118, 75, 162, 0.08), transparent 44%),
      rgba(255, 255, 255, 0.7);
}

.chat-body::-webkit-scrollbar {
  width: 6px;
}
.chat-body::-webkit-scrollbar-track {
  background: rgba(15, 23, 42, 0.06);
  border-radius: 6px;
}
.chat-body::-webkit-scrollbar-thumb {
  background: linear-gradient(
      to bottom,
      rgba(102, 126, 234, 0.95),
      rgba(118, 75, 162, 0.95)
  );
  border-radius: 6px;
}

.welcome-message {
  animation: fadeIn 0.5s ease-out;
}
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.msg-row {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  align-items: flex-end;
  animation: slideIn 0.26s ease-out;
}
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.msg-row.user {
  justify-content: flex-end;
}
.msg-row.assistant {
  justify-content: flex-start;
}

.msg-avatar {
  width: 36px;
  height: 36px;
  border-radius: 12px;
  display: grid;
  place-content: center;
  background: rgba(15, 23, 42, 0.06);
  flex-shrink: 0;
  font-size: 18px;
}
.msg-avatar.user {
  background: linear-gradient(135deg, var(--purple-1), var(--purple-2));
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.16);
}

.msg-bubble {
  max-width: min(760px, 75%);
  padding: 14px 18px;
  border-radius: 18px;
  line-height: 1.6;
  font-size: 15px;
  white-space: pre-wrap;
  word-break: break-word;
  position: relative;
}
.msg-bubble.assistant {
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
  border-top-left-radius: 10px;
}
.msg-bubble.user {
  background: linear-gradient(
      135deg,
      rgba(102, 126, 234, 1),
      rgba(118, 75, 162, 1)
  );
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.14);
  box-shadow: 0 14px 34px rgba(102, 126, 234, 0.18);
  border-top-right-radius: 10px;
}
.msg-meta {
  margin-top: 8px;
  font-size: 12px;
  opacity: 0.72;
  border-top: 1px solid rgba(15, 23, 42, 0.08);
  padding-top: 8px;
}
.msg-bubble.user .msg-meta {
  border-top-color: rgba(255, 255, 255, 0.18);
}
/* JSON行程内容样式 */
.itinerary-content {
  font-size: 14px;
  line-height: 1.6;
}

.itinerary-summary {
  background: linear-gradient(135deg, #667eea15, #764ba215);
  padding: 12px 16px;
  border-radius: 12px;
  margin-bottom: 16px;
  border-left: 4px solid #667eea;
}

.itinerary-summary h3 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
}

.day-section {
  margin-bottom: 20px;
  padding: 16px;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.day-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 2px solid #667eea40;
}

.day-label {
  font-weight: 600;
  color: #1e293b;
  font-size: 15px;
}

.day-date {
  color: #64748b;
  font-size: 13px;
}

.activity-item {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  padding: 12px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.activity-item:last-child {
  margin-bottom: 0;
}

.activity-time {
  min-width: 60px;
  font-weight: 600;
  color: #667eea;
  font-size: 13px;
  padding-top: 2px;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 6px;
  font-size: 14px;
}

.activity-description {
  color: #475569;
  font-size: 13px;
  line-height: 1.5;
  margin-bottom: 8px;
}

.activity-description strong {
  color: #334155;
  font-weight: 600;
}

.activity-description blockquote {
  margin: 4px 0;
  padding-left: 8px;
  border-left: 3px solid #cbd5e1;
  color: #64748b;
  font-style: italic;
}

.activity-details {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.detail-tag {
  background: #f1f5f9;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 11px;
  color: #475569;
  border: 1px solid #e2e8f0;
}

/* Markdown 展示 */
.msg-markdown {
  font-size: 15px;
  line-height: 1.7;
}
.msg-markdown :deep(p) {
  margin: 8px 0;
}
.msg-markdown :deep(h1),
.msg-markdown :deep(h2),
.msg-markdown :deep(h3) {
  margin: 12px 0 8px;
  font-weight: 900;
  color: #0f172a;
}
.msg-markdown :deep(h1) {
  font-size: 20px;
}
.msg-markdown :deep(h2) {
  font-size: 17px;
}
.msg-markdown :deep(h3) {
  font-size: 15px;
}
.msg-markdown :deep(ul),
.msg-markdown :deep(ol) {
  padding-left: 20px;
  margin: 8px 0;
}
.msg-markdown :deep(li) {
  margin: 4px 0;
}
.msg-markdown :deep(code) {
  padding: 2px 6px;
  border-radius: 8px;
  background: rgba(102, 126, 234, 0.08);
  border: 1px solid rgba(102, 126, 234, 0.18);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 0.92em;
}
.msg-markdown :deep(pre) {
  padding: 12px 14px;
  border-radius: 14px;
  background: rgba(15, 23, 42, 0.96);
  color: #fff;
  overflow: auto;
  margin: 10px 0;
}
.msg-markdown :deep(blockquote) {
  margin: 10px 0;
  padding: 8px 12px;
  border-left: 4px solid rgba(102, 126, 234, 0.8);
  background: rgba(102, 126, 234, 0.08);
  border-radius: 12px;
}
.md-stream-tail {
  opacity: 0.75;
}

/* 打字中动画 */
.typing-dots {
  display: flex;
  gap: 6px;
  padding: 4px 0;
}
.typing-dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(102, 126, 234, 1);
  animation: typing 1.4s infinite ease-in-out;
}
.typing-dots span:nth-child(1) {
  animation-delay: -0.32s;
}
.typing-dots span:nth-child(2) {
  animation-delay: -0.16s;
}
@keyframes typing {
  0%,
  80%,
  100% {
    opacity: 0.3;
    transform: translateY(0);
  }
  40% {
    opacity: 1;
    transform: translateY(-4px);
  }
}

/* 确认行程按钮区域 */
.msg-actions {
  display: flex;
  gap: 10px;
  margin-top: 12px;
  padding-top: 10px;
  border-top: 1px dashed rgba(15, 23, 42, 0.12);
}
.btn-primary {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.22);
  padding: 6px 12px;
  box-shadow: 0 10px 24px rgba(102, 126, 234, 0.4);
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}
.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 14px 34px rgba(102, 126, 234, 0.6);
}
.btn-sm {
  font-size: 13px;
  font-weight: 700;
}

/* 快捷模板 & 输入区 */
.chat-quick {
  padding: 18px 24px;
  border-top: 1px solid rgba(15, 23, 42, 0.06);
  background: linear-gradient(
      to right,
      rgba(248, 250, 252, 0.74),
      rgba(241, 245, 249, 0.62)
  );
}
.quick-title {
  font-weight: 800;
  font-size: 13px;
  color: #0f172a;
  margin-bottom: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.quick-title::before {
  content: "🚀";
}

.chat-input-new {
  padding: 14px 24px 18px;
  background: rgba(255, 255, 255, 0.66);
  position: relative;
}
.chat-input-new::before,
.chat-input-new::after {
  content: "";
  position: absolute;
  left: 24px;
  right: 24px;
  height: 1px;
  background: linear-gradient(
      90deg,
      transparent,
      rgba(15, 23, 42, 0.12),
      transparent
  );
}
.chat-input-new::before {
  top: 0;
}
.chat-input-new::after {
  bottom: 0;
}

.input-container {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 12px 10px 16px;
  border-radius: 18px;
  background: rgba(248, 250, 252, 0.76);
  border: 1px solid rgba(15, 23, 42, 0.08);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.65),
  0 10px 30px rgba(15, 23, 42, 0.1);
  transition: all 0.25s ease;
}
.input-container:focus-within {
  background: rgba(255, 255, 255, 0.92);
  border-color: rgba(102, 126, 234, 0.45);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.7),
  0 16px 45px rgba(102, 126, 234, 0.16);
  transform: translateY(-1px);
}

.chat-textarea-new {
  flex: 1;
  min-height: 26px;
  max-height: 120px;
  padding: 6px 0;
  border: none;
  background: transparent;
  font-size: 15px;
  line-height: 1.6;
  color: #0f172a;
  resize: none;
  outline: none;
  font-family: inherit;
}
.chat-textarea-new::placeholder {
  color: rgba(71, 85, 105, 0.6);
}
.chat-textarea-new:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.send-button {
  width: 44px;
  height: 40px;
  min-width: 44px;
  border-radius: 14px;
  background: linear-gradient(
      135deg,
      rgba(102, 126, 234, 1),
      rgba(118, 75, 162, 1)
  );
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.18);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10px 24px rgba(102, 126, 234, 0.24),
  inset 0 1px 0 rgba(255, 255, 255, 0.25);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.send-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 14px 34px rgba(102, 126, 234, 0.3),
  inset 0 1px 0 rgba(255, 255, 255, 0.25);
}
.send-button:disabled {
  opacity: 0.55;
  cursor: not-allowed;
  background: rgba(148, 163, 184, 0.9);
  box-shadow: none;
}
.send-icon {
  width: 22px;
  height: 22px;
  stroke-width: 2.5;
  stroke: currentColor;
}
.loading-icon {
  width: 22px;
  height: 22px;
}
.loading-circle {
  stroke-dasharray: 60;
  stroke-dashoffset: 60;
  animation: loading 1.5s linear infinite;
  transform-origin: center;
  stroke: currentColor;
}
@keyframes loading {
  0% {
    stroke-dashoffset: 60;
    transform: rotate(0deg);
  }
  50% {
    stroke-dashoffset: 15;
  }
  100% {
    stroke-dashoffset: 60;
    transform: rotate(360deg);
  }
}

/* 移动端 */
@media (max-width: 768px) {
  .activity-item {
    flex-direction: column;
    gap: 8px;
  }

  .activity-time {
    min-width: auto;
  }

  .activity-details {
    gap: 6px;
  }

  .app-layout {
    display: flex;
    flex-direction: column;
  }
  .sidebar,
  .sidebar.collapsed {
    height: auto;
    opacity: 1;
    pointer-events: auto;
    transform: none;
  }
  .edge-toggle {
    position: fixed;
    left: 14px;
    top: 14px;
    transform: none;
  }
  .content-area {
    padding: 16px;
    min-height: calc(100vh - 72px);
  }
  .chat-card {
    width: 100%;
    height: calc(100vh - 72px - 32px);
  }
  .slogan-text {
    font-size: 15px;
    letter-spacing: 0.06em;
  }
}
</style>
