import request from '../utils/request'

// 根据用户输入的出发地、目的地、出发日期、返回日期，生成旅行计划
export const planItinerary = (params) => {
  return request({
    url: '/deekseek/planItinerary',
    method: 'GET',
    params
  })
}
