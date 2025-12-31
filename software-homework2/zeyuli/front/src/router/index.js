import { createRouter, createWebHistory } from 'vue-router'

// 导入页面组件
const Login = () => import('../views/Login.vue')
const Register = () => import('../views/Register.vue')
const Home = () => import('../views/Home.vue')
const Result = () => import('../views/Result.vue')
const Map = () => import('../views/Map.vue')
const Edit = () => import('../views/Edit.vue')
// const Budget = () => import('../views/Budget.vue')
const Profile = () => import('../views/Profile.vue')
const Favorites = () => import('../views/Favorites.vue')
const History = () => import('../views/History.vue')
const Preferences = () => import('../views/Preferences.vue')
const Help = () => import('../views/Help.vue')

// 创建路由实例
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/result',
      name: 'result',
      component: Result
    },
    {
      path: '/map',
      name: 'map',
      component: Map
    },
    {
      path: '/edit',
      name: 'edit',
      component: Edit
    },
    // {
    //   path: '/budget',
    //   name: 'budget',
    //   component: Budget
    // },
    {
      path: '/profile',
      name: 'profile',
      component: Profile
    },
    {
      path: '/favorites',
      name: 'favorites',
      component: Favorites
    },
    {
      path: '/history',
      name: 'history',
      component: History
    },
    {
      path: '/preferences',
      name: 'preferences',
      component: Preferences
    },
    {
      path: '/help',
      name: 'help',
      component: Help
    }
  ]
})

// 路由守卫
// router.beforeEach((to, from, next) => {
//   // 获取token
//   const token = localStorage.getItem('token')
//
//   // 不需要登录的页面
//   const publicPages = ['/login', '/register']
//   const authRequired = !publicPages.includes(to.path)
//
//   if (authRequired && !token) {
//     // 需要登录但没有token，重定向到登录页
//     next('/login')
//   } else {
//     // 不需要登录或有token，继续访问
//     next()
//   }
// })

export default router
