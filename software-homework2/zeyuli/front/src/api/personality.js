import request from '../utils/request'

// 获取人格测试问卷
export const getPersonalityTest = () => {
  return request({
    url: '/personality-test/questionnaire',
    method: 'GET'
  })
}

// 计算测试结果
export const calculateTestResult = (data) => {
  return request({
    url: '/personality-test/calculate-result',
    method: 'POST',
    data
  })
}

// 生成人格行程
export const generatePersonalityItinerary = (params) => {
  return request({
    url: '/personality-test/generate-itinerary',
    method: 'GET',
    params
  })
}

// 获取所有人格类型
export const getAllPersonalityTypes = () => {
  return request({
    url: '/personality-test/all-personality-types',
    method: 'GET'
  })
}
