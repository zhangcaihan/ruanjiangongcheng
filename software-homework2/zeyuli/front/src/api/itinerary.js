import request from '../utils/request'

// 按预算规划行程
export const planByBudget = (params, data) => {
    return request({
        url: '/itinerary/plan-by-budget',
        method: 'POST',
        params,
        data
    })
}

// 生成旅行计划（流式）
export const planItinerary = (params, onChunk) => {
    return new Promise((resolve, reject) => {
        // 获取token
        const token = localStorage.getItem('token')

        // 构建请求头
        const headers = {
            'Content-Type': 'application/json'
        }

        // 如果token存在，添加Authorization头
        if (token) {
            headers.Authorization = `Bearer ${token}`
        }

        // 使用原生fetch API实现流式接收
        // TODO 注意要使用 baseUrl
        fetch(`deekseek/planItinerary?${new URLSearchParams(params)}`, {
            method: 'GET',
            headers
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`)
                }

                // 获取 ReadableStream
                const reader = response.body.getReader()
                const decoder = new TextDecoder('utf-8')
                let result = ''

                // 递归读取流数据
                const readStream = () => {
                    return reader.read().then(({ done, value }) => {
                        if (done) {
                            resolve(result)
                            return
                        }

                        // 解码并处理数据
                        const chunk = decoder.decode(value, { stream: true })
                        result += chunk

                        // 实时回调处理数据
                        if (onChunk && typeof onChunk === 'function') {
                            onChunk(chunk)
                        }

                        // 继续读取
                        return readStream()
                    })
                }

                return readStream()
            })
            .catch(error => {
                reject(error)
            })
    })
}

/**
 * ✅ 新增：确认行程时调用的接口
 * 前端把最终确认的 Markdown + 出发/目的地/日期 等发给后端，
 * 后端用 AI 解析成 { markdown, itinerary } 并返回。
 *
 * 使用方式（在组件里）：
 * const confirmed = await parseItineraryFromMarkdown({...})
 * // confirmed 预期为：{ markdown: string, itinerary: {...} }
 */
export const parseItineraryFromMarkdown = (data) => {
    return request({
        // ⚠️ 这里的地址要和你后端 Controller 对应，按后端实际改
        // 比如你后端写的是 @PostMapping("/deekseek/confirmItinerary")
        // 那就改成 '/deekseek/confirmItinerary'
        url: 'deekseek/formatUserInput',
        method: 'POST',
        timeout: 100000,
        data
    }).then(res => res.data)
}

// 根据条件调整行程
export const adjustByCondition = (params, data) => {
    return request({
        url: '/itinerary/adjust-by-condition',
        method: 'POST',
        params,
        data
    })
}

// 生成人格化行程
export const generatePersonality = (params) => {
    return request({
        url: '/itinerary/generate-personality',
        method: 'GET',
        params
    })
}

// 获取学生专属行程
export const studentItinerary = (params) => {
    return request({
        url: '/itinerary/student-itinerary',
        method: 'GET',
        params
    })
}

// 生成搭子行程
export const generateCompanion = (params) => {
    return request({
        url: '/itinerary/generate-companion',
        method: 'GET',
        params
    })
}

// 计算行程费用
export const calculateCost = (params, data) => {
    return request({
        url: '/itinerary/calculate-cost',
        method: 'POST',
        params,
        data
    })
}

// 优化景点选择
export const optimizeAttractions = (params) => {
    return request({
        url: '/itinerary/optimize-attractions',
        method: 'GET',
        params
    })
}

// 优化交通方式
export const optimizeTransportation = (params) => {
    return request({
        url: '/itinerary/optimize-transportation',
        method: 'GET',
        params
    })
}
