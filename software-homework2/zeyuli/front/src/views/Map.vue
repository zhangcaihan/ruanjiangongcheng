<template>
  <div class="app-layout" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
    <!-- 侧边栏 -->
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
        <div class="sidebar-item active" @click="navigateTo('/map')">
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

    <!-- 主要内容区 -->
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

      <div class="map-wrapper" ref="containerRef">
    <!-- 地图视口 -->
    <div 
      class="map-viewport"
      @mousedown="startDrag"
      @mousemove="onDrag"
      @mouseup="stopDrag"
      @mouseleave="stopDrag"
      @wheel.prevent="onWheel"
      :style="{ cursor: isDragging ? 'grabbing' : 'grab' }"
    >
      <!-- 瓦片式地图容器 -->
      <div class="tile-container">
        <img
          v-for="tile in visibleTiles"
          :key="tile.key"
          :src="tile.url"
          class="map-tile"
          :style="{
            transform: `translate(${tile.x}px, ${tile.y}px)`,
            width: `${TILE_SIZE}px`,
            height: `${TILE_SIZE}px`
          }"
          draggable="false"
          alt=""
        />
      </div>
      
      <!-- 路线覆盖层 -->
      <svg class="route-overlay" v-if="routePath">
        <path :d="routePath" fill="none" stroke="#409EFF" stroke-width="6" stroke-linecap="round" stroke-linejoin="round" opacity="0.8"/>
        <path :d="routePath" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>

      <!-- 覆盖物插槽 -->
      <div class="overlay-container">
        <slot name="overlay"></slot>
      </div>
    </div>

    <!-- UI 控件层 -->
    <div class="ui-controls">
      <!-- 缩放控制 -->
      <div class="control-group zoom-group">
        <button class="ctrl-btn" @click="adjustZoom(1)" :disabled="zoom >= MAX_ZOOM">+</button>
        <span class="zoom-indicator">{{ zoom }}</span>
        <button class="ctrl-btn" @click="adjustZoom(-1)" :disabled="zoom <= MIN_ZOOM">−</button>
      </div>

      <!-- 导航按钮 -->
      <button class="nav-btn" @click="handleNavigation" :class="{ active: isNavigating }">
        <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
          <path d="M12 2L4.5 20.29l.71.71L12 18l6.79 3 .71-.71z"/>
        </svg>
        <span>{{ isNavigating ? '关闭导航' : '开始导航' }}</span>
      </button>
    </div>

    <!-- 导航面板 -->
    <div class="nav-panel" v-if="isNavigating">
      <div class="nav-header">
        <h3>路线规划</h3>
        <button class="close-btn" @click="closeNavigation">×</button>
      </div>
      <div class="nav-body">
        <div class="input-group">
          <span class="dot start"></span>
          <input v-model="navStart" placeholder="输入起点" @keyup.enter="searchRoute" />
        </div>
        <div class="input-group">
          <span class="dot end"></span>
          <input v-model="navEnd" placeholder="输入终点" @keyup.enter="searchRoute" />
        </div>
        <button class="search-btn" @click="searchRoute" :disabled="isRouting">
          {{ isRouting ? '规划中...' : '开始导航' }}
        </button>
      </div>
    </div>
    
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getRoute } from '../api/map'

const router = useRouter()

// 侧边栏状态
const sidebarCollapsed = ref(false)
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
  // 侧边栏动画结束后更新地图尺寸，防止变形
  setTimeout(() => {
    updateSize()
  }, 300)
}
const navigateTo = (path) => {
  router.push(path)
}

// 常量定义
const TILE_SIZE = 256
const MIN_ZOOM = 1
const MAX_ZOOM = 18
const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://106.15.90.163:8080'

// 状态管理
const containerRef = ref(null)
const viewportSize = reactive({ width: 800, height: 600 })
const isDragging = ref(false)
const lastMousePos = reactive({ x: 0, y: 0 })

// 经纬度和缩放级别
const center = reactive({ 
  lng: 118.796786, // 默认南京
  lat: 31.970968 
})
const zoom = ref(10)

// --- 导航状态 ---
const isNavigating = ref(false)
const navStart = ref('南京南站')
const navEnd = ref('南京大学')
const routePolyline = ref([]) // Array of {lng, lat}
const isRouting = ref(false)

// 范围: [0, TILE_SIZE * 2^zoom]
const project = (lng, lat, z) => {
  const n = Math.pow(2, z)
  const x = (lng + 180) / 360 * n * TILE_SIZE
  
  const latRad = lat * Math.PI / 180
  const y = (1 - Math.log(Math.tan(latRad) + 1 / Math.cos(latRad)) / Math.PI) / 2 * n * TILE_SIZE
  return { x, y }
}

// 世界坐标 -> 经纬度
const unproject = (px, py, z) => {
  const n = Math.pow(2, z)
  const lng = px / (n * TILE_SIZE) * 360 - 180
  
  const n2 = Math.PI - 2 * Math.PI * py / (n * TILE_SIZE)
  const latRad = Math.atan(0.5 * (Math.exp(n2) - Math.exp(-n2)))
  const lat = latRad * 180 / Math.PI
  return { lng, lat }
}

// 瓦片索引 -> 瓦片中心经纬度
const getTileCenterLatLng = (tx, ty, z) => {
  // 瓦片中心的世界像素坐标
  const cx = (tx + 0.5) * TILE_SIZE
  const cy = (ty + 0.5) * TILE_SIZE
  return unproject(cx, cy, z)
}
// 计算可见瓦片

const visibleTiles = computed(() => {
  if (!viewportSize.width) return []

  const z = zoom.value
  // 1. 计算当前中心点的世界像素坐标
  const centerPx = project(center.lng, center.lat, z)
  
  // 2. 计算视口左上角的世界像素坐标
  const topLeftPx = {
    x: centerPx.x - viewportSize.width / 2,
    y: centerPx.y - viewportSize.height / 2
  }

  // 3. 计算覆盖视口的瓦片索引范围
  const startCol = Math.floor(topLeftPx.x / TILE_SIZE)
  const endCol = Math.floor((topLeftPx.x + viewportSize.width) / TILE_SIZE) + 1
  const startRow = Math.floor(topLeftPx.y / TILE_SIZE)
  const endRow = Math.floor((topLeftPx.y + viewportSize.height) / TILE_SIZE) + 1

  const tiles = []
  const maxIndex = Math.pow(2, z) - 1

  for (let col = startCol; col <= endCol; col++) {
    for (let row = startRow; row <= endRow; row++) {
      // 处理水平循环
      const normalizedCol = ((col % (maxIndex + 1)) + (maxIndex + 1)) % (maxIndex + 1)
      
      // 垂直方向不循环，超出范围不渲染
      if (row < 0 || row > maxIndex) continue

      // 计算该瓦片在屏幕上的 CSS 位置 
      // screenX = tileWorldX - viewportWorldLeft
      const screenX = col * TILE_SIZE - topLeftPx.x
      const screenY = row * TILE_SIZE - topLeftPx.y

      tiles.push({
        key: `${z}-${col}-${row}`,
        x: screenX,
        y: screenY,
        // 使用高德地图官方开源 XYZ 瓦片服务 (标准路网)
        // style=7: 矢量路网, style=6: 卫星图 => 卫星图不可用
        url: `https://webrd01.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7&x=${normalizedCol}&y=${row}&z=${z}`
      })
    }
  }
  console.log('>>> tiles', tiles.length, tiles.slice(0,3))
  return tiles
})

// 计算路径的 SVG d 属性
const routePath = computed(() => {
  if (!routePolyline.value.length) return ''
  
  const z = zoom.value
  const centerPx = project(center.lng, center.lat, z)
  const halfW = viewportSize.width / 2
  const halfH = viewportSize.height / 2
  
  return routePolyline.value.map((pt, index) => {
    const px = project(pt.lng, pt.lat, z)
    const screenX = px.x - centerPx.x + halfW
    const screenY = px.y - centerPx.y + halfH
    return `${index === 0 ? 'M' : 'L'} ${screenX} ${screenY}`
  }).join(' ')
})

// 交互处理

const startDrag = (e) => {
  isDragging.value = true
  lastMousePos.x = e.clientX
  lastMousePos.y = e.clientY
}

const onDrag = (e) => {
  if (!isDragging.value) return
  
  const dx = e.clientX - lastMousePos.x
  const dy = e.clientY - lastMousePos.y
  
  lastMousePos.x = e.clientX
  lastMousePos.y = e.clientY

  // 拖动逻辑：将像素差值转换为经纬度差值
  // 1. 当前中心转像素
  const currentPx = project(center.lng, center.lat, zoom.value)
  // 2. 减去偏移 (鼠标向右拖，地图向右动，意味着中心点向左移 -> x 减小)
  const newPx = {
    x: currentPx.x - dx,
    y: currentPx.y - dy
  }
  // 3. 转回经纬度
  const newCenter = unproject(newPx.x, newPx.y, zoom.value)
  
  // 更新状态
  center.lng = newCenter.lng
  // 限制纬度防止跑出世界范围
  center.lat = Math.max(-85, Math.min(85, newCenter.lat))
}

const stopDrag = () => {
  isDragging.value = false
}

const onWheel = (e) => {
  const delta = e.deltaY > 0 ? -1 : 1
  adjustZoom(delta)
}

const adjustZoom = (delta) => {
  const nextZoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, zoom.value + delta))
  if (nextZoom !== zoom.value) {
    zoom.value = nextZoom
  }
}

const handleNavigation = () => {
  isNavigating.value = !isNavigating.value
}

const searchRoute = async () => {
  if (!navStart.value || !navEnd.value) return
  
  isRouting.value = true
  try {
    const res = await getRoute({
      origin: navStart.value,
      destination: navEnd.value,
      mode: 'driving'
    })
    
    if (res && res.polyline) {
      // 解析 polyline: "lng,lat;lng,lat;..."
      const points = res.polyline.split(';').map(p => {
        const [lng, lat] = p.split(',')
        return { lng: parseFloat(lng), lat: parseFloat(lat) }
      })
      routePolyline.value = points
      
      // 自动调整视野到路径中心（简单实现：取第一个点）
      if (points.length > 0) {
        center.lng = points[0].lng
        center.lat = points[0].lat
      }
    } else {
      alert('未找到路线')
    }
  } catch (e) {
    console.error('Route failed', e)
    alert('路线规划失败，请重试')
  } finally {
    isRouting.value = false
  }
}

const closeNavigation = () => {
  isNavigating.value = false
  routePolyline.value = []
}

// 视口尺寸监听
const updateSize = () => {
  if (containerRef.value) {
    viewportSize.width = containerRef.value.clientWidth
    viewportSize.height = containerRef.value.clientHeight
  }
}

onMounted(() => {
  updateSize()
  window.addEventListener('resize', updateSize)
})

onUnmounted(() => {
  window.removeEventListener('resize', updateSize)
})
</script>

<style scoped>
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
  height: 100vh;
  width: 100vw;
  gap: 0;
  column-gap: 0;
  overflow: hidden;
  isolation: isolate;
}

.app-layout.sidebar-collapsed {
  --sidebar-w: 0px;
}

/* 滚动条 */
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

/* ====== 主界面 ====== */
.main-content {
  grid-column: 2;
  min-width: 0;
  z-index: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  height: 100vh;
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

.map-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  background-color: #f0f2f5;
  overflow: hidden;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
}

.map-viewport {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
  /* 禁用默认触摸操作，防止移动端滚动页面 */
  touch-action: none; 
}

.tile-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none; /* 让鼠标事件穿透到底层 div */
}

.map-tile {
  position: absolute;
  left: 0;
  top: 0;
  will-change: transform;
  /* 避免图片被选中 */
  user-select: none;
  -webkit-user-drag: none;
}

.ui-controls {
  position: absolute;
  right: 24px;
  bottom: 32px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  align-items: flex-end;
  z-index: 10;
}

.control-group {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.zoom-group {
  width: 40px;
  align-items: center;
}

.ctrl-btn {
  width: 100%;
  height: 40px;
  border: none;
  background: white;
  font-size: 20px;
  cursor: pointer;
  color: #333;
  transition: background 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ctrl-btn:hover:not(:disabled) {
  background: #f5f5f5;
}

.ctrl-btn:disabled {
  color: #ccc;
  cursor: not-allowed;
}

.zoom-indicator {
  font-size: 12px;
  font-weight: bold;
  color: #666;
  padding: 4px 0;
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
  width: 100%;
  text-align: center;
}

.nav-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: #1890ff;
  color: white;
  border: none;
  border-radius: 24px;
  font-size: 16px;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
  cursor: pointer;
  transition: all 0.3s ease;
}

.nav-btn:hover {
  background: #40a9ff;
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.4);
}

.nav-btn:active {
  transform: translateY(0);
}

.debug-info {
  position: absolute;
  top: 10px;
  left: 10px;
  background: rgba(0,0,0,0.5);
  color: white;
  padding: 4px 8px;
  font-size: 12px;
  pointer-events: none;
}

/* 路线覆盖层 */
.route-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 5;
}

/* 导航面板 */
.nav-panel {
  position: absolute;
  top: 24px;
  left: 24px;
  width: 320px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.15);
  z-index: 20;
  overflow: hidden;
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from { transform: translateX(-20px); opacity: 0; }
  to { transform: translateX(0); opacity: 1; }
}

.nav-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.nav-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #999;
  cursor: pointer;
}

.nav-body {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.input-group {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #f5f7fa;
  padding: 8px 12px;
  border-radius: 6px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.dot.start { background: #52c41a; }
.dot.end { background: #f5222d; }

.input-group input {
  border: none;
  background: transparent;
  width: 100%;
  outline: none;
  font-size: 14px;
}

.search-btn {
  margin-top: 8px;
  padding: 10px;
  background: #1890ff;
  color: white;
  border: none;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;
}

.search-btn:hover {
  background: #40a9ff;
}

.search-btn:disabled {
  background: #d9d9d9;
  cursor: not-allowed;
}

.nav-btn.active {
  background: #ff4d4f;
  box-shadow: 0 4px 12px rgba(255, 77, 79, 0.3);
}

.nav-btn.active:hover {
  background: #ff7875;
}
</style>
