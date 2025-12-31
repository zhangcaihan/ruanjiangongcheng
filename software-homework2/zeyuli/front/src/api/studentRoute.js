import request from '../utils/request'

// 获取推荐的学生专属线路
export const getRecommended = (params) => {
  return request({
    url: '/student-route/recommended',
    method: 'GET',
    params
  })
}

// 搜索学生专属线路
export const search = (params) => {
  return request({
    url: '/student-route/search',
    method: 'GET',
    params
  })
}

// 获取线路详情
export const getDetail = (params) => {
  return request({
    url: `/student-route/detail/${params.routeId}`,
    method: 'GET'
  })
}

// 获取热门线路
export const getHot = (params) => {
  return request({
    url: '/student-route/hot',
    method: 'GET',
    params
  })
}

// 获取季节性推荐线路
export const getSeasonal = (params) => {
  return request({
    url: '/student-route/seasonal',
    method: 'GET',
    params
  })
}
