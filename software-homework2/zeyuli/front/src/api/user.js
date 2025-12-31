import request from '../utils/request'

// 用户登录
export const login = (data) => {
  return request({
    url: '/user/login',
    method: 'POST',
    data
  })
}

// 用户注册
export const register = (data) => {
  return request({
    url: '/user/register',
    method: 'POST',
    data
  })
}
