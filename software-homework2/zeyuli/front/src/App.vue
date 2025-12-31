<template>
  <div class="app-container">
    <router-view v-slot="{ Component }">
      <transition name="fade" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </div>
</template>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app, .app-container {
  height: 100%;
  width: 100%;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333;
}

/* 页面过渡动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from, .fade-leave-to {
  opacity: 0;
}

/* 通用按钮样式 */
button {
  font-family: inherit;
}

/* 通用输入框样式 */
input, textarea, select {
  font-family: inherit;
}

/* 链接样式 */
a {
  text-decoration: none;
  color: inherit;
}

/* Font Awesome 图标 */
/* 由于我们没有安装 Font Awesome，这里使用Unicode字符作为替代 */
.fas, .far {
  font-family: 'Material Icons';
  font-weight: normal;
  font-style: normal;
  font-size: 24px;
  line-height: 1;
  letter-spacing: normal;
  text-transform: none;
  display: inline-block;
  white-space: nowrap;
  word-wrap: normal;
  direction: ltr;
  -webkit-font-feature-settings: 'liga';
  -webkit-font-smoothing: antialiased;
}

/* 图标替代 */
.fas.fa-check-circle::before { content: '✓'; }
.fas.fa-map-marked-alt::before { content: '🗺️'; }
.fas.fa-star::before { content: '★'; }
.fas.fa-check::before { content: '✓'; }
.fas.fa-tags::before { content: '🏷️'; }
.far.fa-calendar-alt::before { content: '📅'; }
.fas.fa-exclamation-circle::before { content: '⚠️'; }
.fas.fa-check-circle::before { content: '✓'; }
.far.fa-heart::before { content: '♥'; }
</style>

<script>
export default {
  name: 'App'
}
</script>
