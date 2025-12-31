<template>
  <div class="register-container">
    <div class="register-box">
      <div class="flex items-center mb-6">
        <button type="button" class="btn btn-text" @click="$router.push('/login')">
          <span class="icon">←</span>返回登录
        </button>
        <h2 class="text-2xl font-bold ml-4">注册账户</h2>
      </div>
      <form @submit.prevent="handleRegister">
        <div class="form-item grid grid-cols-2 gap-4">
          <div>
            <label class="form-label">用户名</label>
            <input 
              type="text"
              v-model="registerForm.username"
              placeholder="用户名"
              required 
              class="input"
            />
          </div>
          <div>
            <label class="form-label">验证码</label>
            <div class="flex gap-2">
              <input 
                type="text" 
                v-model="registerForm.verifyCode" 
                placeholder="验证码" 
                required 
                class="input flex-1"
              />
              <button type="button" class="btn btn-secondary" @click="sendVerifyCode">发送</button>
            </div>
          </div>
        </div>
        <div class="form-item grid grid-cols-2 gap-4">
          <div>
            <label class="form-label">设置密码</label>
            <input 
              type="password" 
              v-model="registerForm.password" 
              placeholder="设置密码" 
              required 
              class="input"
            />
          </div>
          <div>
            <label class="form-label">确认密码</label>
            <input 
              type="password" 
              v-model="registerForm.confirmPassword" 
              placeholder="确认密码" 
              required 
              class="input"
            />
          </div>
        </div>
        <div class="form-item">
          <label class="form-label">昵称</label>
          <input 
            type="text" 
            v-model="registerForm.nickname" 
            placeholder="昵称" 
            required 
            class="input"
          />
        </div>
        <div class="form-item">
          <label class="form-label">选择身份</label>
          <div class="flex gap-6 mt-2">
            <label class="flex items-center gap-2">
              <input type="radio" name="identity" value="student" v-model="registerForm.identity" class="mr-1"/>
              学生
            </label>
            <label class="flex items-center gap-2">
              <input type="radio" name="identity" value="worker" v-model="registerForm.identity" class="mr-1"/>
              上班族
            </label>
            <label class="flex items-center gap-2">
              <input type="radio" name="identity" value="other" v-model="registerForm.identity" class="mr-1"/>
              其他
            </label>
          </div>
        </div>
        <div class="form-item flex items-center gap-2 text-sm">
          <input type="checkbox" id="agree-terms" v-model="registerForm.agreeTerms" required/>
          <label for="agree-terms" class="text-gray-600">
            我同意
            <span class="text-blue-500 cursor-pointer">服务协议</span>
            和
            <span class="text-blue-500 cursor-pointer">隐私政策</span>
          </label>
        </div>
        <div class="form-item flex gap-4">
          <button type="button" class="btn btn-secondary flex-1" @click="$router.push('/login')">返回登录</button>
          <button type="submit" class="btn btn-primary flex-1">注册</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/user'
// todo 待完善
const router = useRouter()

// 注册表单数据
const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  identity: 'student',
  verifyCode: '',
  agreeTerms: false
})

// 发送验证码
const sendVerifyCode = () => {
  // 这里可以添加发送验证码的逻辑
  console.log('发送验证码')
}

// 处理注册
const handleRegister = async () => {
  try {
    // 验证密码是否一致
    if (registerForm.value.password !== registerForm.value.confirmPassword) {
      console.error('两次输入的密码不一致')
      return
    }
    
    // 调用注册接口
    const response = await register({
      username: registerForm.value.username,
      password: registerForm.value.password
    })
    
    // 注册成功后跳转到登录页
    router.push('/login')
  } catch (error) {
    console.error('注册失败:', error)
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(to bottom right, #e6f7ff, #f0f5ff);
}

.register-box {
  max-width: 560px;
  width: 100%;
  padding: 40px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

/* 图标样式 */
.icon {
  font-size: 16px;
}
</style>
