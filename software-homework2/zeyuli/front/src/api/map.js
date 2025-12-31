import request from '../utils/request'

// 获取地图中心点
export const getMapCenter = (params) => {
  return request({
    url: '/map/center',
    method: 'GET',
    params
  })
}

// 搜索位置
export const searchLocations = (params) => {
  return request({
    url: '/map/search-locations',
    method: 'GET',
    params
  })
}

// 获取路线
export const getRoute = (params) => {
  return request({
    url: '/map/route',
    method: 'GET',
    params
  })
}

// 获取周边POI
export const getSurroundingPOIs = (params) => {
  return request({
    url: '/map/surrounding-pois',
    method: 'GET',
    params
  })
}

// 获取地图截图
export const getMapScreenshot = (params) => {
  return request({
    url: '/map/screenshot',
    method: 'GET',
    params
  })
}

// 获取天气信息
export const getWeather = (params) => {
  return request({
    url: '/map/weather',
    method: 'GET',
    params
  })
}

// 根据坐标获取地址
export const getAddressByLocation = (params) => {
  return request({
    url: '/map/address-by-location',
    method: 'GET',
    params
  })
}

// 获取两点距离
export const getDistance = (params) => {
  return request({
    url: '/map/distance',
    method: 'GET',
    params
  })
}

// 获取景点详情
export const getPOIDetails = (params) => {
  return request({
    url: `/map/poi-details/${params.poiId}`,
    method: 'GET'
  })
}

// 获取城市景点
export const getAttractionsByCity = (params) => {
  return request({
    url: '/map/attractions-by-city',
    method: 'GET',
    params
  })
}
