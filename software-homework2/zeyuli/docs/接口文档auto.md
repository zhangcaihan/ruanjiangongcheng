---
title: 默认模块
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
code_clipboard: true
highlight_theme: darkula
headingLevel: 2
generator: "@tarslib/widdershins v4.0.30"

---

# 默认模块

Base URLs:

# Authentication

# 地图服务控制器

## GET 获取地图中心点

GET /map/center

根据位置获取地图中心点坐标

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|location|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "lat": 0,
  "lng": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[Point](#schemapoint)|

## GET 搜索位置

GET /map/search-locations

根据查询关键词搜索位置

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|query|query|string| 是 |none|
|region|query|string| 是 |none|

> 返回示例

> 200 Response

```json
[
  [
    {
      "name": "",
      "lat": 0,
      "lng": 0,
      "address": "",
      "type": "",
      "cityCode": ""
    },
    {
      "name": "",
      "lat": 0,
      "lng": 0,
      "address": "",
      "type": "",
      "cityCode": ""
    }]
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[Location](#schemalocation)]|false|none||none|
|» name|string|false|none||none|
|» lat|number|false|none||none|
|» lng|number|false|none||none|
|» address|string|false|none||none|
|» type|string|false|none||none|
|» cityCode|string|false|none||none|

## GET 获取路线

GET /map/route

获取两地之间的路线规划

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|origin|query|string| 是 |none|
|destination|query|string| 是 |none|
|mode|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "duration": 0,
  "distance": 0,
  "mode": "",
  "estimatedCost": 0,
  "steps": [
    {
      "instruction": "",
      "distance": 0,
      "duration": 0,
      "mode": "",
      "cost": 0
    }
  ],
  "polyline": "",
  "startName": "",
  "endName": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[Route](#schemaroute)|

## GET 获取周边POI

GET /map/surrounding-pois

获取指定位置周边的兴趣点

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|location|query|string| 是 |none|
|radius|query|string| 是 |none|
|types|query|string| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "name": "",
    "lat": 0,
    "lng": 0
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[POI](#schemapoi)]|false|none||none|
|» name|string|false|none||none|
|» lat|number|false|none||none|
|» lng|number|false|none||none|

## GET 获取地图截图

GET /map/screenshot

获取指定区域的地图截图

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|center|query|string| 是 |none|
|zoom|query|string| 是 |none|
|width|query|string| 是 |none|
|height|query|string| 是 |none|

> 返回示例

> 200 Response

```json
"string"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|string|

## GET 获取天气信息

GET /map/weather

获取指定城市的天气信息

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|cityCode|query|string| 是 |none|

> 返回示例

> 200 Response

```json
"string"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|string|

## GET 根据坐标获取地址

GET /map/address-by-location

根据经纬度坐标获取详细地址信息

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|lat|query|number| 是 |none|
|lng|query|number| 是 |none|

> 返回示例

> 200 Response

```json
"string"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|string|

## GET 获取两点距离

GET /map/distance

计算两个地点之间的距离

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|origin|query|string| 是 |none|
|destination|query|string| 是 |none|

> 返回示例

> 200 Response

```json
0
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|number|

## GET 获取景点详情

GET /map/poi-details/{poiId}

获取指定景点的详细信息

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|poiId|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## GET 获取城市景点

GET /map/attractions-by-city

获取指定城市的景点列表

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|page|query|integer| 是 |none|
|pageSize|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "name": "",
    "lat": 0,
    "lng": 0
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[POI](#schemapoi)]|false|none||none|
|» name|string|false|none||none|
|» lat|number|false|none||none|
|» lng|number|false|none||none|

# 用户控制层

## POST 用户登录

POST /user/login

用户登录接口

> Body 请求参数

```json
{
  "userName": "string",
  "password": "string",
  "loginType": "ACCOUNT_SECRET_LOGIN"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[UserVo](#schemauservo)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## POST 用户注册

POST /user/register

用户注册接口

> Body 请求参数

```json
{
  "userName": "string",
  "password": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[RegisterVo](#schemaregistervo)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "statusCode": 200,
  "message": "成功"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

# DeekSeekController

## GET 根据用户输入的出发地、目的地、出发日期、返回日期，生成旅行计划

GET /deekseek/planItinerary

根据用户输入的出发地、目的地、出发日期、返回日期，生成旅行计划

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|startCity|query|string| 是 |none|
|endCity|query|string| 是 |none|
|startDate|query|string| 是 |none|
|endDate|query|string| 是 |none|
|token|query|string| 是 |none|
|userInput|query|string| 是 |none|

> 返回示例

> 200 Response

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 行程规划控制器

## POST 按预算规划行程

POST /itinerary/plan-by-budget

根据预算金额生成优化的行程方案

> Body 请求参数

```json
{
  "key": {}
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|days|query|integer| 是 |none|
|budget|query|number| 是 |none|
|body|body|[MapObject](#schemamapobject)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "planName": "",
  "city": "",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "",
          "estimatedCost": 0,
          "steps": [
            {
              "instruction": "",
              "distance": 0,
              "duration": 0,
              "mode": "",
              "cost": 0
            }
          ],
          "polyline": "",
          "startName": "",
          "endName": ""
        }
      ],
      "dailyCost": 0,
      "weather": "",
      "suggestions": [
        ""
      ]
    }
  ],
  "additionalInfo": {
    "": {}
  },
  "planType": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ItineraryPlanVO](#schemaitineraryplanvo)|

## POST 根据条件调整行程

POST /itinerary/adjust-by-condition

根据天气、交通等情况重新规划行程

> Body 请求参数

```json
{
  "planName": "string",
  "city": "string",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "string",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "string",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "string",
          "estimatedCost": 0,
          "steps": [
            {}
          ],
          "polyline": "string",
          "startName": "string",
          "endName": "string"
        }
      ],
      "dailyCost": 0,
      "weather": "string",
      "suggestions": [
        "string"
      ]
    }
  ],
  "additionalInfo": {
    "key": {}
  },
  "planType": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|weatherCondition|query|string| 是 |none|
|trafficCondition|query|string| 是 |none|
|body|body|[ItineraryPlanVO](#schemaitineraryplanvo)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "planName": "",
  "city": "",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "",
          "estimatedCost": 0,
          "steps": [
            {
              "instruction": "",
              "distance": 0,
              "duration": 0,
              "mode": "",
              "cost": 0
            }
          ],
          "polyline": "",
          "startName": "",
          "endName": ""
        }
      ],
      "dailyCost": 0,
      "weather": "",
      "suggestions": [
        ""
      ]
    }
  ],
  "additionalInfo": {
    "": {}
  },
  "planType": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ItineraryPlanVO](#schemaitineraryplanvo)|

## GET 生成人格化行程

GET /itinerary/generate-personality

根据用户人格类型生成专属行程

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|days|query|integer| 是 |none|
|personalityType|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "planName": "",
  "city": "",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "",
          "estimatedCost": 0,
          "steps": [
            {
              "instruction": "",
              "distance": 0,
              "duration": 0,
              "mode": "",
              "cost": 0
            }
          ],
          "polyline": "",
          "startName": "",
          "endName": ""
        }
      ],
      "dailyCost": 0,
      "weather": "",
      "suggestions": [
        ""
      ]
    }
  ],
  "additionalInfo": {
    "": {}
  },
  "planType": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ItineraryPlanVO](#schemaitineraryplanvo)|

## GET 获取学生专属行程

GET /itinerary/student-itinerary

获取针对高校学生的专属行程

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|university|query|string| 是 |none|
|days|query|integer| 是 |none|
|maxBudget|query|number| 是 |none|

> 返回示例

> 200 Response

```json
{
  "planName": "",
  "city": "",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "",
          "estimatedCost": 0,
          "steps": [
            {
              "instruction": "",
              "distance": 0,
              "duration": 0,
              "mode": "",
              "cost": 0
            }
          ],
          "polyline": "",
          "startName": "",
          "endName": ""
        }
      ],
      "dailyCost": 0,
      "weather": "",
      "suggestions": [
        ""
      ]
    }
  ],
  "additionalInfo": {
    "": {}
  },
  "planType": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ItineraryPlanVO](#schemaitineraryplanvo)|

## GET 生成搭子行程

GET /itinerary/generate-companion

根据旅行搭子人格生成个性化行程

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|days|query|integer| 是 |none|
|companionType|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "planName": "",
  "city": "",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "",
          "estimatedCost": 0,
          "steps": [
            {
              "instruction": "",
              "distance": 0,
              "duration": 0,
              "mode": "",
              "cost": 0
            }
          ],
          "polyline": "",
          "startName": "",
          "endName": ""
        }
      ],
      "dailyCost": 0,
      "weather": "",
      "suggestions": [
        ""
      ]
    }
  ],
  "additionalInfo": {
    "": {}
  },
  "planType": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ItineraryPlanVO](#schemaitineraryplanvo)|

## POST 计算行程费用

POST /itinerary/calculate-cost

计算指定景点和路线的总费用

> Body 请求参数

```json
[
  {
    "name": "string",
    "lat": 0,
    "lng": 0
  }
]
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|accommodationCost|query|number| 是 |none|
|body|body|[POI](#schemapoi)| 否 |none|

> 返回示例

> 200 Response

```json
0
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|number|

## GET 优化景点选择

GET /itinerary/optimize-attractions

根据预算优化景点选择

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|budget|query|number| 是 |none|
|days|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "name": "",
    "lat": 0,
    "lng": 0
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[POI](#schemapoi)]|false|none||none|
|» name|string|false|none||none|
|» lat|number|false|none||none|
|» lng|number|false|none||none|

## GET 优化交通方式

GET /itinerary/optimize-transportation

优化交通方式以降低成本

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|origin|query|string| 是 |none|
|destination|query|string| 是 |none|
|currentMode|query|string| 是 |none|
|costLimit|query|number| 是 |none|

> 返回示例

> 200 Response

```json
{
  "duration": 0,
  "distance": 0,
  "mode": "",
  "estimatedCost": 0,
  "steps": [
    {
      "instruction": "",
      "distance": 0,
      "duration": 0,
      "mode": "",
      "cost": 0
    }
  ],
  "polyline": "",
  "startName": "",
  "endName": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[Route](#schemaroute)|

# 学生专属线路控制器

## GET 获取推荐的学生专属线路

GET /student-route/recommended

根据城市、学生类型和行程天数获取推荐的学生专属线路

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|studentType|query|string| 是 |none|
|days|query|integer| 是 |none|
|limit|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "routeId": "",
    "routeName": "",
    "description": "",
    "city": "",
    "days": 0,
    "studentType": "",
    "minBudget": 0,
    "maxBudget": 0,
    "averageCost": 0,
    "attractions": [
      {
        "name": "",
        "lat": 0,
        "lng": 0
      }
    ],
    "routes": [
      {
        "duration": 0,
        "distance": 0,
        "mode": "",
        "estimatedCost": 0,
        "steps": [
          {
            "instruction": "",
            "distance": 0,
            "duration": 0,
            "mode": "",
            "cost": 0
          }
        ],
        "polyline": "",
        "startName": "",
        "endName": ""
      }
    ],
    "recommendedReasons": [
      ""
    ],
    "studentDiscounts": [
      {
        "discountName": "",
        "description": "",
        "discountValue": 0,
        "applicableTo": "",
        "requiredDocuments": "",
        "validFrom": 0,
        "validTo": 0,
        "isValid": false
      }
    ],
    "tags": [
      ""
    ],
    "recommendedSeasons": [
      ""
    ],
    "routeType": "",
    "groupSize": "",
    "viewCount": 0,
    "favoriteCount": 0,
    "rating": 0,
    "ratingCount": 0,
    "isHot": false,
    "isRecommended": false,
    "dailySchedules": [
      {
        "day": 0,
        "morning": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "afternoon": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "evening": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "recommendedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "transportationSuggestion": "",
        "dailyBudget": 0,
        "notices": [
          ""
        ]
      }
    ],
    "accommodationInfo": {
      "accommodationType": "",
      "locationSuggestion": "",
      "minPrice": 0,
      "maxPrice": 0,
      "studentFriendlyHotels": [
        {
          "hotelName": "",
          "address": "",
          "studentPrice": 0,
          "originalPrice": 0,
          "discount": 0,
          "distanceToCityCenter": 0,
          "facilities": [
            ""
          ],
          "bookingLink": ""
        }
      ],
      "notices": [
        ""
      ]
    },
    "createTime": 0,
    "updateTime": 0,
    "additionalInfo": {
      "": {}
    }
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[StudentRoute](#schemastudentroute)]|false|none||none|
|» routeId|string|false|none||线路唯一标识|
|» routeName|string|false|none||线路名称|
|» description|string|false|none||线路描述|
|» city|string|false|none||所在城市|
|» days|integer|false|none||行程天数|
|» studentType|string|false|none||适合学生类型（大学生、中学生等）|
|» minBudget|number|false|none||预算范围（最低价）|
|» maxBudget|number|false|none||预算范围（最高价）|
|» averageCost|number|false|none||平均费用|
|» attractions|[[POI](#schemapoi)]|false|none||景点列表|
|»» name|string|false|none||none|
|»» lat|number|false|none||none|
|»» lng|number|false|none||none|
|» routes|[[Route](#schemaroute)]|false|none||交通信息|
|»» duration|number|false|none||总时长(秒)|
|»» distance|number|false|none||总距离(米)|
|»» mode|string|false|none||交通方式|
|»» estimatedCost|number|false|none||预估费用|
|»» steps|[[RouteStep](#schemaroutestep)]|false|none||路线步骤|
|»»» instruction|string|false|none||导航指示|
|»»» distance|number|false|none||该段距离|
|»»» duration|number|false|none||该段时长|
|»»» mode|string|false|none||该段交通方式|
|»»» cost|number|false|none||该段费用|
|»» polyline|string|false|none||路线轨迹点集|
|»» startName|string|false|none||起点名称|
|»» endName|string|false|none||终点名称|
|» recommendedReasons|[string]|false|none||推荐理由|
|» studentDiscounts|[[DiscountInfo](#schemadiscountinfo)]|false|none||学生专属优惠信息|
|»» discountName|string|false|none||优惠名称|
|»» description|string|false|none||优惠描述|
|»» discountValue|number|false|none||折扣力度（如 8.5折）|
|»» applicableTo|string|false|none||适用景点/服务|
|»» requiredDocuments|string|false|none||所需凭证|
|»» validFrom|integer(int64)|false|none||有效期开始|
|»» validTo|integer(int64)|false|none||有效期结束|
|»» isValid|boolean|false|none||是否可用|
|» tags|[string]|false|none||线路特点标签|
|» recommendedSeasons|[string]|false|none||推荐季节|
|» routeType|string|false|none||线路类型（文化、自然、美食等）|
|» groupSize|string|false|none||适合群体大小（个人、小团体、班级等）|
|» viewCount|integer|false|none||累计浏览次数|
|» favoriteCount|integer|false|none||收藏次数|
|» rating|number|false|none||评分|
|» ratingCount|integer|false|none||评分人数|
|» isHot|boolean|false|none||是否热门|
|» isRecommended|boolean|false|none||是否推荐|
|» dailySchedules|[[DailySchedule](#schemadailyschedule)]|false|none||每日行程安排|
|»» day|integer|false|none||第几天|
|»» morning|[SchedulePart](#schemaschedulepart)|false|none||上午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»»» name|string|false|none||none|
|»»»» lat|number|false|none||none|
|»»»» lng|number|false|none||none|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» afternoon|[SchedulePart](#schemaschedulepart)|false|none||下午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» evening|[SchedulePart](#schemaschedulepart)|false|none||晚上行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» recommendedAttractions|[[POI](#schemapoi)]|false|none||全天推荐景点|
|»»» name|string|false|none||none|
|»»» lat|number|false|none||none|
|»»» lng|number|false|none||none|
|»» transportationSuggestion|string|false|none||当日交通建议|
|»» dailyBudget|number|false|none||当日预算|
|»» notices|[string]|false|none||注意事项|
|» accommodationInfo|[AccommodationInfo](#schemaaccommodationinfo)|false|none||住宿建议|
|»» accommodationType|string|false|none||推荐住宿类型|
|»» locationSuggestion|string|false|none||住宿位置建议|
|»» minPrice|number|false|none||价格区间（最低）|
|»» maxPrice|number|false|none||价格区间（最高）|
|»» studentFriendlyHotels|[[StudentFriendlyHotel](#schemastudentfriendlyhotel)]|false|none||学生优惠住宿推荐|
|»»» hotelName|string|false|none||酒店名称|
|»»» address|string|false|none||酒店地址|
|»»» studentPrice|number|false|none||学生价格|
|»»» originalPrice|number|false|none||原价|
|»»» discount|number|false|none||折扣力度|
|»»» distanceToCityCenter|number|false|none||距离市中心距离|
|»»» facilities|[string]|false|none||设施描述|
|»»» bookingLink|string|false|none||预订链接|
|»» notices|[string]|false|none||住宿注意事项|
|» createTime|integer(int64)|false|none||创建时间|
|» updateTime|integer(int64)|false|none||更新时间|
|» additionalInfo|[MapObject](#schemamapobject)|false|none||额外信息|
|»» key|[key](#schemakey)|false|none||none|

## GET 搜索学生专属线路

GET /student-route/search

根据条件查询学生专属线路，支持分页

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 否 |none|
|minBudget|query|number| 否 |none|
|maxBudget|query|number| 否 |none|
|days|query|integer| 否 |none|
|tags|query|array[string]| 否 |none|
|studentType|query|string| 否 |none|
|page|query|integer| 是 |none|
|pageSize|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## GET 获取线路详情

GET /student-route/detail/{routeId}

根据ID获取学生专属线路的详细信息

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|routeId|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "routeId": "",
  "routeName": "",
  "description": "",
  "city": "",
  "days": 0,
  "studentType": "",
  "minBudget": 0,
  "maxBudget": 0,
  "averageCost": 0,
  "attractions": [
    {
      "name": "",
      "lat": 0,
      "lng": 0
    }
  ],
  "routes": [
    {
      "duration": 0,
      "distance": 0,
      "mode": "",
      "estimatedCost": 0,
      "steps": [
        {
          "instruction": "",
          "distance": 0,
          "duration": 0,
          "mode": "",
          "cost": 0
        }
      ],
      "polyline": "",
      "startName": "",
      "endName": ""
    }
  ],
  "recommendedReasons": [
    ""
  ],
  "studentDiscounts": [
    {
      "discountName": "",
      "description": "",
      "discountValue": 0,
      "applicableTo": "",
      "requiredDocuments": "",
      "validFrom": 0,
      "validTo": 0,
      "isValid": false
    }
  ],
  "tags": [
    ""
  ],
  "recommendedSeasons": [
    ""
  ],
  "routeType": "",
  "groupSize": "",
  "viewCount": 0,
  "favoriteCount": 0,
  "rating": 0,
  "ratingCount": 0,
  "isHot": false,
  "isRecommended": false,
  "dailySchedules": [
    {
      "day": 0,
      "morning": {
        "timeRange": "",
        "activity": "",
        "relatedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "estimatedDuration": 0
      },
      "afternoon": {
        "timeRange": "",
        "activity": "",
        "relatedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "estimatedDuration": 0
      },
      "evening": {
        "timeRange": "",
        "activity": "",
        "relatedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "estimatedDuration": 0
      },
      "recommendedAttractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "transportationSuggestion": "",
      "dailyBudget": 0,
      "notices": [
        ""
      ]
    }
  ],
  "accommodationInfo": {
    "accommodationType": "",
    "locationSuggestion": "",
    "minPrice": 0,
    "maxPrice": 0,
    "studentFriendlyHotels": [
      {
        "hotelName": "",
        "address": "",
        "studentPrice": 0,
        "originalPrice": 0,
        "discount": 0,
        "distanceToCityCenter": 0,
        "facilities": [
          ""
        ],
        "bookingLink": ""
      }
    ],
    "notices": [
      ""
    ]
  },
  "createTime": 0,
  "updateTime": 0,
  "additionalInfo": {
    "": {}
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[StudentRoute](#schemastudentroute)|

## GET 获取热门线路

GET /student-route/hot

获取热门的学生专属线路列表

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|limit|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "routeId": "",
    "routeName": "",
    "description": "",
    "city": "",
    "days": 0,
    "studentType": "",
    "minBudget": 0,
    "maxBudget": 0,
    "averageCost": 0,
    "attractions": [
      {
        "name": "",
        "lat": 0,
        "lng": 0
      }
    ],
    "routes": [
      {
        "duration": 0,
        "distance": 0,
        "mode": "",
        "estimatedCost": 0,
        "steps": [
          {
            "instruction": "",
            "distance": 0,
            "duration": 0,
            "mode": "",
            "cost": 0
          }
        ],
        "polyline": "",
        "startName": "",
        "endName": ""
      }
    ],
    "recommendedReasons": [
      ""
    ],
    "studentDiscounts": [
      {
        "discountName": "",
        "description": "",
        "discountValue": 0,
        "applicableTo": "",
        "requiredDocuments": "",
        "validFrom": 0,
        "validTo": 0,
        "isValid": false
      }
    ],
    "tags": [
      ""
    ],
    "recommendedSeasons": [
      ""
    ],
    "routeType": "",
    "groupSize": "",
    "viewCount": 0,
    "favoriteCount": 0,
    "rating": 0,
    "ratingCount": 0,
    "isHot": false,
    "isRecommended": false,
    "dailySchedules": [
      {
        "day": 0,
        "morning": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "afternoon": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "evening": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "recommendedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "transportationSuggestion": "",
        "dailyBudget": 0,
        "notices": [
          ""
        ]
      }
    ],
    "accommodationInfo": {
      "accommodationType": "",
      "locationSuggestion": "",
      "minPrice": 0,
      "maxPrice": 0,
      "studentFriendlyHotels": [
        {
          "hotelName": "",
          "address": "",
          "studentPrice": 0,
          "originalPrice": 0,
          "discount": 0,
          "distanceToCityCenter": 0,
          "facilities": [
            ""
          ],
          "bookingLink": ""
        }
      ],
      "notices": [
        ""
      ]
    },
    "createTime": 0,
    "updateTime": 0,
    "additionalInfo": {
      "": {}
    }
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[StudentRoute](#schemastudentroute)]|false|none||none|
|» routeId|string|false|none||线路唯一标识|
|» routeName|string|false|none||线路名称|
|» description|string|false|none||线路描述|
|» city|string|false|none||所在城市|
|» days|integer|false|none||行程天数|
|» studentType|string|false|none||适合学生类型（大学生、中学生等）|
|» minBudget|number|false|none||预算范围（最低价）|
|» maxBudget|number|false|none||预算范围（最高价）|
|» averageCost|number|false|none||平均费用|
|» attractions|[[POI](#schemapoi)]|false|none||景点列表|
|»» name|string|false|none||none|
|»» lat|number|false|none||none|
|»» lng|number|false|none||none|
|» routes|[[Route](#schemaroute)]|false|none||交通信息|
|»» duration|number|false|none||总时长(秒)|
|»» distance|number|false|none||总距离(米)|
|»» mode|string|false|none||交通方式|
|»» estimatedCost|number|false|none||预估费用|
|»» steps|[[RouteStep](#schemaroutestep)]|false|none||路线步骤|
|»»» instruction|string|false|none||导航指示|
|»»» distance|number|false|none||该段距离|
|»»» duration|number|false|none||该段时长|
|»»» mode|string|false|none||该段交通方式|
|»»» cost|number|false|none||该段费用|
|»» polyline|string|false|none||路线轨迹点集|
|»» startName|string|false|none||起点名称|
|»» endName|string|false|none||终点名称|
|» recommendedReasons|[string]|false|none||推荐理由|
|» studentDiscounts|[[DiscountInfo](#schemadiscountinfo)]|false|none||学生专属优惠信息|
|»» discountName|string|false|none||优惠名称|
|»» description|string|false|none||优惠描述|
|»» discountValue|number|false|none||折扣力度（如 8.5折）|
|»» applicableTo|string|false|none||适用景点/服务|
|»» requiredDocuments|string|false|none||所需凭证|
|»» validFrom|integer(int64)|false|none||有效期开始|
|»» validTo|integer(int64)|false|none||有效期结束|
|»» isValid|boolean|false|none||是否可用|
|» tags|[string]|false|none||线路特点标签|
|» recommendedSeasons|[string]|false|none||推荐季节|
|» routeType|string|false|none||线路类型（文化、自然、美食等）|
|» groupSize|string|false|none||适合群体大小（个人、小团体、班级等）|
|» viewCount|integer|false|none||累计浏览次数|
|» favoriteCount|integer|false|none||收藏次数|
|» rating|number|false|none||评分|
|» ratingCount|integer|false|none||评分人数|
|» isHot|boolean|false|none||是否热门|
|» isRecommended|boolean|false|none||是否推荐|
|» dailySchedules|[[DailySchedule](#schemadailyschedule)]|false|none||每日行程安排|
|»» day|integer|false|none||第几天|
|»» morning|[SchedulePart](#schemaschedulepart)|false|none||上午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»»» name|string|false|none||none|
|»»»» lat|number|false|none||none|
|»»»» lng|number|false|none||none|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» afternoon|[SchedulePart](#schemaschedulepart)|false|none||下午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» evening|[SchedulePart](#schemaschedulepart)|false|none||晚上行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» recommendedAttractions|[[POI](#schemapoi)]|false|none||全天推荐景点|
|»»» name|string|false|none||none|
|»»» lat|number|false|none||none|
|»»» lng|number|false|none||none|
|»» transportationSuggestion|string|false|none||当日交通建议|
|»» dailyBudget|number|false|none||当日预算|
|»» notices|[string]|false|none||注意事项|
|» accommodationInfo|[AccommodationInfo](#schemaaccommodationinfo)|false|none||住宿建议|
|»» accommodationType|string|false|none||推荐住宿类型|
|»» locationSuggestion|string|false|none||住宿位置建议|
|»» minPrice|number|false|none||价格区间（最低）|
|»» maxPrice|number|false|none||价格区间（最高）|
|»» studentFriendlyHotels|[[StudentFriendlyHotel](#schemastudentfriendlyhotel)]|false|none||学生优惠住宿推荐|
|»»» hotelName|string|false|none||酒店名称|
|»»» address|string|false|none||酒店地址|
|»»» studentPrice|number|false|none||学生价格|
|»»» originalPrice|number|false|none||原价|
|»»» discount|number|false|none||折扣力度|
|»»» distanceToCityCenter|number|false|none||距离市中心距离|
|»»» facilities|[string]|false|none||设施描述|
|»»» bookingLink|string|false|none||预订链接|
|»» notices|[string]|false|none||住宿注意事项|
|» createTime|integer(int64)|false|none||创建时间|
|» updateTime|integer(int64)|false|none||更新时间|
|» additionalInfo|[MapObject](#schemamapobject)|false|none||额外信息|
|»» key|[key](#schemakey)|false|none||none|

## GET 获取季节性推荐线路

GET /student-route/seasonal

根据季节获取推荐的学生专属线路

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|season|query|string| 是 |none|
|limit|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "routeId": "",
    "routeName": "",
    "description": "",
    "city": "",
    "days": 0,
    "studentType": "",
    "minBudget": 0,
    "maxBudget": 0,
    "averageCost": 0,
    "attractions": [
      {
        "name": "",
        "lat": 0,
        "lng": 0
      }
    ],
    "routes": [
      {
        "duration": 0,
        "distance": 0,
        "mode": "",
        "estimatedCost": 0,
        "steps": [
          {
            "instruction": "",
            "distance": 0,
            "duration": 0,
            "mode": "",
            "cost": 0
          }
        ],
        "polyline": "",
        "startName": "",
        "endName": ""
      }
    ],
    "recommendedReasons": [
      ""
    ],
    "studentDiscounts": [
      {
        "discountName": "",
        "description": "",
        "discountValue": 0,
        "applicableTo": "",
        "requiredDocuments": "",
        "validFrom": 0,
        "validTo": 0,
        "isValid": false
      }
    ],
    "tags": [
      ""
    ],
    "recommendedSeasons": [
      ""
    ],
    "routeType": "",
    "groupSize": "",
    "viewCount": 0,
    "favoriteCount": 0,
    "rating": 0,
    "ratingCount": 0,
    "isHot": false,
    "isRecommended": false,
    "dailySchedules": [
      {
        "day": 0,
        "morning": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "afternoon": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "evening": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "recommendedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "transportationSuggestion": "",
        "dailyBudget": 0,
        "notices": [
          ""
        ]
      }
    ],
    "accommodationInfo": {
      "accommodationType": "",
      "locationSuggestion": "",
      "minPrice": 0,
      "maxPrice": 0,
      "studentFriendlyHotels": [
        {
          "hotelName": "",
          "address": "",
          "studentPrice": 0,
          "originalPrice": 0,
          "discount": 0,
          "distanceToCityCenter": 0,
          "facilities": [
            ""
          ],
          "bookingLink": ""
        }
      ],
      "notices": [
        ""
      ]
    },
    "createTime": 0,
    "updateTime": 0,
    "additionalInfo": {
      "": {}
    }
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[StudentRoute](#schemastudentroute)]|false|none||none|
|» routeId|string|false|none||线路唯一标识|
|» routeName|string|false|none||线路名称|
|» description|string|false|none||线路描述|
|» city|string|false|none||所在城市|
|» days|integer|false|none||行程天数|
|» studentType|string|false|none||适合学生类型（大学生、中学生等）|
|» minBudget|number|false|none||预算范围（最低价）|
|» maxBudget|number|false|none||预算范围（最高价）|
|» averageCost|number|false|none||平均费用|
|» attractions|[[POI](#schemapoi)]|false|none||景点列表|
|»» name|string|false|none||none|
|»» lat|number|false|none||none|
|»» lng|number|false|none||none|
|» routes|[[Route](#schemaroute)]|false|none||交通信息|
|»» duration|number|false|none||总时长(秒)|
|»» distance|number|false|none||总距离(米)|
|»» mode|string|false|none||交通方式|
|»» estimatedCost|number|false|none||预估费用|
|»» steps|[[RouteStep](#schemaroutestep)]|false|none||路线步骤|
|»»» instruction|string|false|none||导航指示|
|»»» distance|number|false|none||该段距离|
|»»» duration|number|false|none||该段时长|
|»»» mode|string|false|none||该段交通方式|
|»»» cost|number|false|none||该段费用|
|»» polyline|string|false|none||路线轨迹点集|
|»» startName|string|false|none||起点名称|
|»» endName|string|false|none||终点名称|
|» recommendedReasons|[string]|false|none||推荐理由|
|» studentDiscounts|[[DiscountInfo](#schemadiscountinfo)]|false|none||学生专属优惠信息|
|»» discountName|string|false|none||优惠名称|
|»» description|string|false|none||优惠描述|
|»» discountValue|number|false|none||折扣力度（如 8.5折）|
|»» applicableTo|string|false|none||适用景点/服务|
|»» requiredDocuments|string|false|none||所需凭证|
|»» validFrom|integer(int64)|false|none||有效期开始|
|»» validTo|integer(int64)|false|none||有效期结束|
|»» isValid|boolean|false|none||是否可用|
|» tags|[string]|false|none||线路特点标签|
|» recommendedSeasons|[string]|false|none||推荐季节|
|» routeType|string|false|none||线路类型（文化、自然、美食等）|
|» groupSize|string|false|none||适合群体大小（个人、小团体、班级等）|
|» viewCount|integer|false|none||累计浏览次数|
|» favoriteCount|integer|false|none||收藏次数|
|» rating|number|false|none||评分|
|» ratingCount|integer|false|none||评分人数|
|» isHot|boolean|false|none||是否热门|
|» isRecommended|boolean|false|none||是否推荐|
|» dailySchedules|[[DailySchedule](#schemadailyschedule)]|false|none||每日行程安排|
|»» day|integer|false|none||第几天|
|»» morning|[SchedulePart](#schemaschedulepart)|false|none||上午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»»» name|string|false|none||none|
|»»»» lat|number|false|none||none|
|»»»» lng|number|false|none||none|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» afternoon|[SchedulePart](#schemaschedulepart)|false|none||下午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» evening|[SchedulePart](#schemaschedulepart)|false|none||晚上行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» recommendedAttractions|[[POI](#schemapoi)]|false|none||全天推荐景点|
|»»» name|string|false|none||none|
|»»» lat|number|false|none||none|
|»»» lng|number|false|none||none|
|»» transportationSuggestion|string|false|none||当日交通建议|
|»» dailyBudget|number|false|none||当日预算|
|»» notices|[string]|false|none||注意事项|
|» accommodationInfo|[AccommodationInfo](#schemaaccommodationinfo)|false|none||住宿建议|
|»» accommodationType|string|false|none||推荐住宿类型|
|»» locationSuggestion|string|false|none||住宿位置建议|
|»» minPrice|number|false|none||价格区间（最低）|
|»» maxPrice|number|false|none||价格区间（最高）|
|»» studentFriendlyHotels|[[StudentFriendlyHotel](#schemastudentfriendlyhotel)]|false|none||学生优惠住宿推荐|
|»»» hotelName|string|false|none||酒店名称|
|»»» address|string|false|none||酒店地址|
|»»» studentPrice|number|false|none||学生价格|
|»»» originalPrice|number|false|none||原价|
|»»» discount|number|false|none||折扣力度|
|»»» distanceToCityCenter|number|false|none||距离市中心距离|
|»»» facilities|[string]|false|none||设施描述|
|»»» bookingLink|string|false|none||预订链接|
|»» notices|[string]|false|none||住宿注意事项|
|» createTime|integer(int64)|false|none||创建时间|
|» updateTime|integer(int64)|false|none||更新时间|
|» additionalInfo|[MapObject](#schemamapobject)|false|none||额外信息|
|»» key|[key](#schemakey)|false|none||none|

## GET 获取学生专属优惠

GET /student-route/discounts

获取指定城市的学生专属优惠信息

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|attractionType|query|string| 否 |none|

> 返回示例

> 200 Response

```json
[
  {
    "discountName": "",
    "description": "",
    "discountValue": 0,
    "applicableTo": "",
    "requiredDocuments": "",
    "validFrom": 0,
    "validTo": 0,
    "isValid": false
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[DiscountInfo](#schemadiscountinfo)]|false|none||none|
|» discountName|string|false|none||优惠名称|
|» description|string|false|none||优惠描述|
|» discountValue|number|false|none||折扣力度（如 8.5折）|
|» applicableTo|string|false|none||适用景点/服务|
|» requiredDocuments|string|false|none||所需凭证|
|» validFrom|integer(int64)|false|none||有效期开始|
|» validTo|integer(int64)|false|none||有效期结束|
|» isValid|boolean|false|none||是否可用|

## POST 增加浏览次数

POST /student-route/view-count/{routeId}

增加指定线路的浏览次数

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|routeId|path|string| 是 |none|

> 返回示例

> 200 Response

```json
0
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|integer|

## POST 收藏/取消收藏线路

POST /student-route/favorite

收藏或取消收藏指定的学生专属线路

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|routeId|query|string| 是 |none|
|userId|query|string| 是 |none|
|isFavorite|query|boolean| 是 |none|

> 返回示例

> 200 Response

```json
true
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|boolean|

## POST 线路评分

POST /student-route/rate

为指定的学生专属线路评分

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|routeId|query|string| 是 |none|
|userId|query|string| 是 |none|
|rating|query|number| 是 |none|

> 返回示例

> 200 Response

```json
0
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|number|

## GET 个性化推荐

GET /student-route/personalized/{userId}

根据用户偏好推荐学生线路

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|path|string| 是 |none|
|limit|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "routeId": "",
    "routeName": "",
    "description": "",
    "city": "",
    "days": 0,
    "studentType": "",
    "minBudget": 0,
    "maxBudget": 0,
    "averageCost": 0,
    "attractions": [
      {
        "name": "",
        "lat": 0,
        "lng": 0
      }
    ],
    "routes": [
      {
        "duration": 0,
        "distance": 0,
        "mode": "",
        "estimatedCost": 0,
        "steps": [
          {
            "instruction": "",
            "distance": 0,
            "duration": 0,
            "mode": "",
            "cost": 0
          }
        ],
        "polyline": "",
        "startName": "",
        "endName": ""
      }
    ],
    "recommendedReasons": [
      ""
    ],
    "studentDiscounts": [
      {
        "discountName": "",
        "description": "",
        "discountValue": 0,
        "applicableTo": "",
        "requiredDocuments": "",
        "validFrom": 0,
        "validTo": 0,
        "isValid": false
      }
    ],
    "tags": [
      ""
    ],
    "recommendedSeasons": [
      ""
    ],
    "routeType": "",
    "groupSize": "",
    "viewCount": 0,
    "favoriteCount": 0,
    "rating": 0,
    "ratingCount": 0,
    "isHot": false,
    "isRecommended": false,
    "dailySchedules": [
      {
        "day": 0,
        "morning": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "afternoon": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "evening": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "recommendedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "transportationSuggestion": "",
        "dailyBudget": 0,
        "notices": [
          ""
        ]
      }
    ],
    "accommodationInfo": {
      "accommodationType": "",
      "locationSuggestion": "",
      "minPrice": 0,
      "maxPrice": 0,
      "studentFriendlyHotels": [
        {
          "hotelName": "",
          "address": "",
          "studentPrice": 0,
          "originalPrice": 0,
          "discount": 0,
          "distanceToCityCenter": 0,
          "facilities": [
            ""
          ],
          "bookingLink": ""
        }
      ],
      "notices": [
        ""
      ]
    },
    "createTime": 0,
    "updateTime": 0,
    "additionalInfo": {
      "": {}
    }
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[StudentRoute](#schemastudentroute)]|false|none||none|
|» routeId|string|false|none||线路唯一标识|
|» routeName|string|false|none||线路名称|
|» description|string|false|none||线路描述|
|» city|string|false|none||所在城市|
|» days|integer|false|none||行程天数|
|» studentType|string|false|none||适合学生类型（大学生、中学生等）|
|» minBudget|number|false|none||预算范围（最低价）|
|» maxBudget|number|false|none||预算范围（最高价）|
|» averageCost|number|false|none||平均费用|
|» attractions|[[POI](#schemapoi)]|false|none||景点列表|
|»» name|string|false|none||none|
|»» lat|number|false|none||none|
|»» lng|number|false|none||none|
|» routes|[[Route](#schemaroute)]|false|none||交通信息|
|»» duration|number|false|none||总时长(秒)|
|»» distance|number|false|none||总距离(米)|
|»» mode|string|false|none||交通方式|
|»» estimatedCost|number|false|none||预估费用|
|»» steps|[[RouteStep](#schemaroutestep)]|false|none||路线步骤|
|»»» instruction|string|false|none||导航指示|
|»»» distance|number|false|none||该段距离|
|»»» duration|number|false|none||该段时长|
|»»» mode|string|false|none||该段交通方式|
|»»» cost|number|false|none||该段费用|
|»» polyline|string|false|none||路线轨迹点集|
|»» startName|string|false|none||起点名称|
|»» endName|string|false|none||终点名称|
|» recommendedReasons|[string]|false|none||推荐理由|
|» studentDiscounts|[[DiscountInfo](#schemadiscountinfo)]|false|none||学生专属优惠信息|
|»» discountName|string|false|none||优惠名称|
|»» description|string|false|none||优惠描述|
|»» discountValue|number|false|none||折扣力度（如 8.5折）|
|»» applicableTo|string|false|none||适用景点/服务|
|»» requiredDocuments|string|false|none||所需凭证|
|»» validFrom|integer(int64)|false|none||有效期开始|
|»» validTo|integer(int64)|false|none||有效期结束|
|»» isValid|boolean|false|none||是否可用|
|» tags|[string]|false|none||线路特点标签|
|» recommendedSeasons|[string]|false|none||推荐季节|
|» routeType|string|false|none||线路类型（文化、自然、美食等）|
|» groupSize|string|false|none||适合群体大小（个人、小团体、班级等）|
|» viewCount|integer|false|none||累计浏览次数|
|» favoriteCount|integer|false|none||收藏次数|
|» rating|number|false|none||评分|
|» ratingCount|integer|false|none||评分人数|
|» isHot|boolean|false|none||是否热门|
|» isRecommended|boolean|false|none||是否推荐|
|» dailySchedules|[[DailySchedule](#schemadailyschedule)]|false|none||每日行程安排|
|»» day|integer|false|none||第几天|
|»» morning|[SchedulePart](#schemaschedulepart)|false|none||上午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»»» name|string|false|none||none|
|»»»» lat|number|false|none||none|
|»»»» lng|number|false|none||none|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» afternoon|[SchedulePart](#schemaschedulepart)|false|none||下午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» evening|[SchedulePart](#schemaschedulepart)|false|none||晚上行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» recommendedAttractions|[[POI](#schemapoi)]|false|none||全天推荐景点|
|»»» name|string|false|none||none|
|»»» lat|number|false|none||none|
|»»» lng|number|false|none||none|
|»» transportationSuggestion|string|false|none||当日交通建议|
|»» dailyBudget|number|false|none||当日预算|
|»» notices|[string]|false|none||注意事项|
|» accommodationInfo|[AccommodationInfo](#schemaaccommodationinfo)|false|none||住宿建议|
|»» accommodationType|string|false|none||推荐住宿类型|
|»» locationSuggestion|string|false|none||住宿位置建议|
|»» minPrice|number|false|none||价格区间（最低）|
|»» maxPrice|number|false|none||价格区间（最高）|
|»» studentFriendlyHotels|[[StudentFriendlyHotel](#schemastudentfriendlyhotel)]|false|none||学生优惠住宿推荐|
|»»» hotelName|string|false|none||酒店名称|
|»»» address|string|false|none||酒店地址|
|»»» studentPrice|number|false|none||学生价格|
|»»» originalPrice|number|false|none||原价|
|»»» discount|number|false|none||折扣力度|
|»»» distanceToCityCenter|number|false|none||距离市中心距离|
|»»» facilities|[string]|false|none||设施描述|
|»»» bookingLink|string|false|none||预订链接|
|»» notices|[string]|false|none||住宿注意事项|
|» createTime|integer(int64)|false|none||创建时间|
|» updateTime|integer(int64)|false|none||更新时间|
|» additionalInfo|[MapObject](#schemamapobject)|false|none||额外信息|
|»» key|[key](#schemakey)|false|none||none|

## GET 获取学生友好型住宿

GET /student-route/hotels

获取指定城市的学生友好型住宿推荐

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|key|query|any| 否 |none|
|page|query|integer| 是 |none|
|pageSize|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "hotelName": "",
    "address": "",
    "studentPrice": 0,
    "originalPrice": 0,
    "discount": 0,
    "distanceToCityCenter": 0,
    "facilities": [
      ""
    ],
    "bookingLink": ""
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[StudentFriendlyHotel](#schemastudentfriendlyhotel)]|false|none||none|
|» hotelName|string|false|none||酒店名称|
|» address|string|false|none||酒店地址|
|» studentPrice|number|false|none||学生价格|
|» originalPrice|number|false|none||原价|
|» discount|number|false|none||折扣力度|
|» distanceToCityCenter|number|false|none||距离市中心距离|
|» facilities|[string]|false|none||设施描述|
|» bookingLink|string|false|none||预订链接|

## GET 获取线路可用优惠

GET /student-route/{routeId}/discounts

获取指定线路可用的学生专属优惠

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|routeId|path|string| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "discountName": "",
    "description": "",
    "discountValue": 0,
    "applicableTo": "",
    "requiredDocuments": "",
    "validFrom": 0,
    "validTo": 0,
    "isValid": false
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[DiscountInfo](#schemadiscountinfo)]|false|none||none|
|» discountName|string|false|none||优惠名称|
|» description|string|false|none||优惠描述|
|» discountValue|number|false|none||折扣力度（如 8.5折）|
|» applicableTo|string|false|none||适用景点/服务|
|» requiredDocuments|string|false|none||所需凭证|
|» validFrom|integer(int64)|false|none||有效期开始|
|» validTo|integer(int64)|false|none||有效期结束|
|» isValid|boolean|false|none||是否可用|

## POST 对比线路

POST /student-route/compare

对比多条学生专属线路

> Body 请求参数

```json
[
  "string"
]
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|array[string]| 否 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## GET 根据人格测试结果推荐

GET /student-route/personality-based

根据人格测试结果推荐学生线路

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|personalityType|query|string| 是 |none|
|city|query|string| 否 |none|
|limit|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "routeId": "",
    "routeName": "",
    "description": "",
    "city": "",
    "days": 0,
    "studentType": "",
    "minBudget": 0,
    "maxBudget": 0,
    "averageCost": 0,
    "attractions": [
      {
        "name": "",
        "lat": 0,
        "lng": 0
      }
    ],
    "routes": [
      {
        "duration": 0,
        "distance": 0,
        "mode": "",
        "estimatedCost": 0,
        "steps": [
          {
            "instruction": "",
            "distance": 0,
            "duration": 0,
            "mode": "",
            "cost": 0
          }
        ],
        "polyline": "",
        "startName": "",
        "endName": ""
      }
    ],
    "recommendedReasons": [
      ""
    ],
    "studentDiscounts": [
      {
        "discountName": "",
        "description": "",
        "discountValue": 0,
        "applicableTo": "",
        "requiredDocuments": "",
        "validFrom": 0,
        "validTo": 0,
        "isValid": false
      }
    ],
    "tags": [
      ""
    ],
    "recommendedSeasons": [
      ""
    ],
    "routeType": "",
    "groupSize": "",
    "viewCount": 0,
    "favoriteCount": 0,
    "rating": 0,
    "ratingCount": 0,
    "isHot": false,
    "isRecommended": false,
    "dailySchedules": [
      {
        "day": 0,
        "morning": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "afternoon": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "evening": {
          "timeRange": "",
          "activity": "",
          "relatedAttractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "estimatedDuration": 0
        },
        "recommendedAttractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "transportationSuggestion": "",
        "dailyBudget": 0,
        "notices": [
          ""
        ]
      }
    ],
    "accommodationInfo": {
      "accommodationType": "",
      "locationSuggestion": "",
      "minPrice": 0,
      "maxPrice": 0,
      "studentFriendlyHotels": [
        {
          "hotelName": "",
          "address": "",
          "studentPrice": 0,
          "originalPrice": 0,
          "discount": 0,
          "distanceToCityCenter": 0,
          "facilities": [
            ""
          ],
          "bookingLink": ""
        }
      ],
      "notices": [
        ""
      ]
    },
    "createTime": 0,
    "updateTime": 0,
    "additionalInfo": {
      "": {}
    }
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[StudentRoute](#schemastudentroute)]|false|none||none|
|» routeId|string|false|none||线路唯一标识|
|» routeName|string|false|none||线路名称|
|» description|string|false|none||线路描述|
|» city|string|false|none||所在城市|
|» days|integer|false|none||行程天数|
|» studentType|string|false|none||适合学生类型（大学生、中学生等）|
|» minBudget|number|false|none||预算范围（最低价）|
|» maxBudget|number|false|none||预算范围（最高价）|
|» averageCost|number|false|none||平均费用|
|» attractions|[[POI](#schemapoi)]|false|none||景点列表|
|»» name|string|false|none||none|
|»» lat|number|false|none||none|
|»» lng|number|false|none||none|
|» routes|[[Route](#schemaroute)]|false|none||交通信息|
|»» duration|number|false|none||总时长(秒)|
|»» distance|number|false|none||总距离(米)|
|»» mode|string|false|none||交通方式|
|»» estimatedCost|number|false|none||预估费用|
|»» steps|[[RouteStep](#schemaroutestep)]|false|none||路线步骤|
|»»» instruction|string|false|none||导航指示|
|»»» distance|number|false|none||该段距离|
|»»» duration|number|false|none||该段时长|
|»»» mode|string|false|none||该段交通方式|
|»»» cost|number|false|none||该段费用|
|»» polyline|string|false|none||路线轨迹点集|
|»» startName|string|false|none||起点名称|
|»» endName|string|false|none||终点名称|
|» recommendedReasons|[string]|false|none||推荐理由|
|» studentDiscounts|[[DiscountInfo](#schemadiscountinfo)]|false|none||学生专属优惠信息|
|»» discountName|string|false|none||优惠名称|
|»» description|string|false|none||优惠描述|
|»» discountValue|number|false|none||折扣力度（如 8.5折）|
|»» applicableTo|string|false|none||适用景点/服务|
|»» requiredDocuments|string|false|none||所需凭证|
|»» validFrom|integer(int64)|false|none||有效期开始|
|»» validTo|integer(int64)|false|none||有效期结束|
|»» isValid|boolean|false|none||是否可用|
|» tags|[string]|false|none||线路特点标签|
|» recommendedSeasons|[string]|false|none||推荐季节|
|» routeType|string|false|none||线路类型（文化、自然、美食等）|
|» groupSize|string|false|none||适合群体大小（个人、小团体、班级等）|
|» viewCount|integer|false|none||累计浏览次数|
|» favoriteCount|integer|false|none||收藏次数|
|» rating|number|false|none||评分|
|» ratingCount|integer|false|none||评分人数|
|» isHot|boolean|false|none||是否热门|
|» isRecommended|boolean|false|none||是否推荐|
|» dailySchedules|[[DailySchedule](#schemadailyschedule)]|false|none||每日行程安排|
|»» day|integer|false|none||第几天|
|»» morning|[SchedulePart](#schemaschedulepart)|false|none||上午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»»» name|string|false|none||none|
|»»»» lat|number|false|none||none|
|»»»» lng|number|false|none||none|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» afternoon|[SchedulePart](#schemaschedulepart)|false|none||下午行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» evening|[SchedulePart](#schemaschedulepart)|false|none||晚上行程|
|»»» timeRange|string|false|none||时间段描述|
|»»» activity|string|false|none||活动内容|
|»»» relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|»»» estimatedDuration|integer|false|none||预计时长（分钟）|
|»» recommendedAttractions|[[POI](#schemapoi)]|false|none||全天推荐景点|
|»»» name|string|false|none||none|
|»»» lat|number|false|none||none|
|»»» lng|number|false|none||none|
|»» transportationSuggestion|string|false|none||当日交通建议|
|»» dailyBudget|number|false|none||当日预算|
|»» notices|[string]|false|none||注意事项|
|» accommodationInfo|[AccommodationInfo](#schemaaccommodationinfo)|false|none||住宿建议|
|»» accommodationType|string|false|none||推荐住宿类型|
|»» locationSuggestion|string|false|none||住宿位置建议|
|»» minPrice|number|false|none||价格区间（最低）|
|»» maxPrice|number|false|none||价格区间（最高）|
|»» studentFriendlyHotels|[[StudentFriendlyHotel](#schemastudentfriendlyhotel)]|false|none||学生优惠住宿推荐|
|»»» hotelName|string|false|none||酒店名称|
|»»» address|string|false|none||酒店地址|
|»»» studentPrice|number|false|none||学生价格|
|»»» originalPrice|number|false|none||原价|
|»»» discount|number|false|none||折扣力度|
|»»» distanceToCityCenter|number|false|none||距离市中心距离|
|»»» facilities|[string]|false|none||设施描述|
|»»» bookingLink|string|false|none||预订链接|
|»» notices|[string]|false|none||住宿注意事项|
|» createTime|integer(int64)|false|none||创建时间|
|» updateTime|integer(int64)|false|none||更新时间|
|» additionalInfo|[MapObject](#schemamapobject)|false|none||额外信息|
|»» key|[key](#schemakey)|false|none||none|

# AI旅行搭子控制器

## POST 处理用户消息

POST /ai-travel-buddy/process-message

接收用户消息并生成AI回复

> Body 请求参数

```json
{
  "messageId": "string",
  "sessionId": "string",
  "senderType": "string",
  "content": "string",
  "messageType": "string",
  "sendTime": "string",
  "status": "string",
  "relatedItineraryId": "string",
  "relatedAttractionId": "string",
  "additionalInfo": {
    "key": {}
  },
  "replyToMessageId": "string",
  "sentiment": "string",
  "processingTime": 0,
  "quickReplyRequired": true,
  "quickReplyOptions": {
    "key": "string"
  },
  "contextType": "string",
  "location": {
    "key": 0
  },
  "tags": {
    "key": "string"
  },
  "errorCode": "string",
  "errorMessage": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[ChatMessage](#schemachatmessage)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "messageId": "",
  "sessionId": "",
  "senderType": "",
  "content": "",
  "messageType": "",
  "sendTime": "",
  "status": "",
  "relatedItineraryId": "",
  "relatedAttractionId": "",
  "additionalInfo": {
    "": {}
  },
  "replyToMessageId": "",
  "sentiment": "",
  "processingTime": 0,
  "quickReplyRequired": false,
  "quickReplyOptions": {
    "": ""
  },
  "contextType": "",
  "location": {
    "": 0
  },
  "tags": {
    "": ""
  },
  "errorCode": "",
  "errorMessage": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ChatMessage](#schemachatmessage)|

## POST 创建新会话

POST /ai-travel-buddy/create-session

创建新的聊天会话

> Body 请求参数

```json
{
  "key": {}
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 是 |none|
|initialMessage|query|string| 是 |none|
|body|body|[MapObject](#schemamapobject)| 否 |none|

> 返回示例

> 200 Response

```json
"string"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|string|

## GET 获取会话历史

GET /ai-travel-buddy/session-history

获取指定会话的消息历史

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|sessionId|query|string| 是 |none|
|limit|query|integer| 是 |none|
|offset|query|integer| 是 |none|

> 返回示例

> 200 Response

```json
[
  {
    "messageId": "",
    "sessionId": "",
    "senderType": "",
    "content": "",
    "messageType": "",
    "sendTime": "",
    "status": "",
    "relatedItineraryId": "",
    "relatedAttractionId": "",
    "additionalInfo": {
      "": {}
    },
    "replyToMessageId": "",
    "sentiment": "",
    "processingTime": 0,
    "quickReplyRequired": false,
    "quickReplyOptions": {
      "": ""
    },
    "contextType": "",
    "location": {
      "": 0
    },
    "tags": {
      "": ""
    },
    "errorCode": "",
    "errorMessage": ""
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[ChatMessage](#schemachatmessage)]|false|none||none|
|» messageId|string|false|none||消息ID|
|» sessionId|string|false|none||会话ID|
|» senderType|string|false|none||发送者类型<br />user - 用户<br />ai - AI旅行搭子|
|» content|string|false|none||消息内容|
|» messageType|string|false|none||消息类型<br />text - 文本<br />image - 图片<br />audio - 音频<br />system - 系统消息<br />recommendation - 推荐消息|
|» sendTime|string|false|none||发送时间|
|» status|string|false|none||消息状态<br />sent - 已发送<br />read - 已读<br />processing - 处理中<br />error - 发送失败|
|» relatedItineraryId|string|false|none||相关行程ID|
|» relatedAttractionId|string|false|none||相关景点ID|
|» additionalInfo|[MapObject](#schemamapobject)|false|none||附加信息<br />可以包含推荐详情、图片URL等|
|»» key|[key](#schemakey)|false|none||none|
|» replyToMessageId|string|false|none||回复消息的ID<br />用于消息引用|
|» sentiment|string|false|none||消息情感分析结果<br />positive - 积极<br />neutral - 中性<br />negative - 消极|
|» processingTime|integer(int64)|false|none||消息处理时间（毫秒）|
|» quickReplyRequired|boolean|false|none||是否需要快速回复|
|» quickReplyOptions|[MapString](#schemamapstring)|false|none||快速回复选项|
|»» key|string|false|none||none|
|» contextType|string|false|none||消息所属上下文类型<br />planning - 行程规划阶段<br />during_trip - 旅行进行中<br />after_trip - 旅行结束后|
|» location|[MapDouble](#schemamapdouble)|false|none||地理位置信息<br />用于基于位置的推荐|
|»» key|number|false|none||none|
|» tags|[MapString](#schemamapstring)|false|none||消息标签|
|»» key|string|false|none||none|
|» errorCode|string|false|none||错误码|
|» errorMessage|string|false|none||错误信息|

## POST 获取智能建议

POST /ai-travel-buddy/intelligent-suggestion

根据用户当前位置和时间提供智能建议

> Body 请求参数

```json
{
  "key": 0
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 是 |none|
|currentTime|query|string| 是 |none|
|body|body|[MapDouble](#schemamapdouble)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "messageId": "",
  "sessionId": "",
  "senderType": "",
  "content": "",
  "messageType": "",
  "sendTime": "",
  "status": "",
  "relatedItineraryId": "",
  "relatedAttractionId": "",
  "additionalInfo": {
    "": {}
  },
  "replyToMessageId": "",
  "sentiment": "",
  "processingTime": 0,
  "quickReplyRequired": false,
  "quickReplyOptions": {
    "": ""
  },
  "contextType": "",
  "location": {
    "": 0
  },
  "tags": {
    "": ""
  },
  "errorCode": "",
  "errorMessage": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ChatMessage](#schemachatmessage)|

## POST 推荐景点

POST /ai-travel-buddy/recommend-attractions

根据用户查询推荐相关景点

> Body 请求参数

```json
{
  "key": 0
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|query|query|string| 是 |none|
|numberOfRecommendations|query|integer| 是 |none|
|body|body|[MapDouble](#schemamapdouble)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "messageId": "",
  "sessionId": "",
  "senderType": "",
  "content": "",
  "messageType": "",
  "sendTime": "",
  "status": "",
  "relatedItineraryId": "",
  "relatedAttractionId": "",
  "additionalInfo": {
    "": {}
  },
  "replyToMessageId": "",
  "sentiment": "",
  "processingTime": 0,
  "quickReplyRequired": false,
  "quickReplyOptions": {
    "": ""
  },
  "contextType": "",
  "location": {
    "": 0
  },
  "tags": {
    "": ""
  },
  "errorCode": "",
  "errorMessage": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ChatMessage](#schemachatmessage)|

## POST 生成旅行总结

POST /ai-travel-buddy/trip-summary

生成用户旅行的总结和回顾

> Body 请求参数

```json
{
  "planName": "string",
  "city": "string",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "string",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "string",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "string",
          "estimatedCost": 0,
          "steps": [
            {}
          ],
          "polyline": "string",
          "startName": "string",
          "endName": "string"
        }
      ],
      "dailyCost": 0,
      "weather": "string",
      "suggestions": [
        "string"
      ]
    }
  ],
  "additionalInfo": {
    "key": {}
  },
  "planType": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 是 |none|
|includePhotos|query|boolean| 是 |none|
|body|body|[ItineraryPlanVO](#schemaitineraryplanvo)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "messageId": "",
  "sessionId": "",
  "senderType": "",
  "content": "",
  "messageType": "",
  "sendTime": "",
  "status": "",
  "relatedItineraryId": "",
  "relatedAttractionId": "",
  "additionalInfo": {
    "": {}
  },
  "replyToMessageId": "",
  "sentiment": "",
  "processingTime": 0,
  "quickReplyRequired": false,
  "quickReplyOptions": {
    "": ""
  },
  "contextType": "",
  "location": {
    "": 0
  },
  "tags": {
    "": ""
  },
  "errorCode": "",
  "errorMessage": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ChatMessage](#schemachatmessage)|

## GET 获取用户偏好

GET /ai-travel-buddy/user-preferences/{userId}

获取用户的旅行聊天偏好设置

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## PUT 更新用户偏好

PUT /ai-travel-buddy/user-preferences/{userId}

更新用户的聊天偏好设置

> Body 请求参数

```json
{
  "key": {}
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|path|string| 是 |none|
|body|body|[MapObject](#schemamapobject)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## POST 设置AI性格

POST /ai-travel-buddy/set-personality/{sessionId}

设置AI旅行搭子的性格特征

> Body 请求参数

```json
{
  "key": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|sessionId|path|string| 是 |none|
|body|body|[MapString](#schemamapstring)| 否 |none|

> 返回示例

> 200 Response

```json
true
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|boolean|

## GET 获取可用性格类型

GET /ai-travel-buddy/available-personalities

获取系统支持的AI旅行搭子性格类型

> 返回示例

> 200 Response

```json
[
  ""
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST 处理多模态输入

POST /ai-travel-buddy/process-multimodal

处理包含文本、图片、语音的多模态输入

> Body 请求参数

```json
{
  "messageId": "string",
  "sessionId": "string",
  "senderType": "string",
  "content": "string",
  "messageType": "string",
  "sendTime": "string",
  "status": "string",
  "relatedItineraryId": "string",
  "relatedAttractionId": "string",
  "additionalInfo": {
    "key": {}
  },
  "replyToMessageId": "string",
  "sentiment": "string",
  "processingTime": 0,
  "quickReplyRequired": true,
  "quickReplyOptions": {
    "key": "string"
  },
  "contextType": "string",
  "location": {
    "key": 0
  },
  "tags": {
    "key": "string"
  },
  "errorCode": "string",
  "errorMessage": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[ChatMessage](#schemachatmessage)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "messageId": "",
  "sessionId": "",
  "senderType": "",
  "content": "",
  "messageType": "",
  "sendTime": "",
  "status": "",
  "relatedItineraryId": "",
  "relatedAttractionId": "",
  "additionalInfo": {
    "": {}
  },
  "replyToMessageId": "",
  "sentiment": "",
  "processingTime": 0,
  "quickReplyRequired": false,
  "quickReplyOptions": {
    "": ""
  },
  "contextType": "",
  "location": {
    "": 0
  },
  "tags": {
    "": ""
  },
  "errorCode": "",
  "errorMessage": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ChatMessage](#schemachatmessage)|

## POST 生成天气提醒

POST /ai-travel-buddy/weather-alert

生成基于当前位置和旅行计划的天气提醒

> Body 请求参数

```json
{
  "key": 0
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[MapDouble](#schemamapdouble)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "messageId": "",
  "sessionId": "",
  "senderType": "",
  "content": "",
  "messageType": "",
  "sendTime": "",
  "status": "",
  "relatedItineraryId": "",
  "relatedAttractionId": "",
  "additionalInfo": {
    "": {}
  },
  "replyToMessageId": "",
  "sentiment": "",
  "processingTime": 0,
  "quickReplyRequired": false,
  "quickReplyOptions": {
    "": ""
  },
  "contextType": "",
  "location": {
    "": 0
  },
  "tags": {
    "": ""
  },
  "errorCode": "",
  "errorMessage": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ChatMessage](#schemachatmessage)|

## DELETE 关闭会话

DELETE /ai-travel-buddy/close-session/{sessionId}

关闭指定的聊天会话

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|sessionId|path|string| 是 |none|

> 返回示例

> 200 Response

```json
true
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|boolean|

# 旅行人格测试控制器

## GET 获取人格测试问卷

GET /personality-test/questionnaire

获取三分钟旅行人格测试问卷

> 返回示例

> 200 Response

```json
{
  "testId": "",
  "testName": "",
  "questions": [
    {
      "questionId": "",
      "content": "",
      "options": [
        {
          "optionId": "",
          "content": "",
          "traitScores": {
            "": 0
          }
        }
      ],
      "weight": 0
    }
  ],
  "personalityTypes": {
    "": {
      "typeCode": "",
      "typeName": "",
      "description": "",
      "travelPreference": "",
      "recommendedAttractionTypes": {
        "": 0
      },
      "recommendedTransportationTypes": {
        "": 0
      },
      "dailyAttractionCountRange": {
        "min": 0,
        "max": 0
      },
      "dailyPace": "",
      "recommendedAccommodationTypes": [
        ""
      ],
      "uniqueItineraryElements": [
        ""
      ]
    }
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[PersonalityTest](#schemapersonalitytest)|

## POST 计算测试结果

POST /personality-test/calculate-result

根据用户答案计算人格测试结果

> Body 请求参数

```json
{
  "token": "",
  "Q1": "Q1C",
  "Q2": "Q2D",
  "Q3": "Q3D",
  "Q4": "Q4B",
  "Q5": "Q5C"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[MapString](#schemamapstring)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "testId": "",
  "userAnswers": {
    "": ""
  },
  "traitScores": {
    "": 0
  },
  "dominantPersonalityType": "",
  "dominantPersonalityDescription": "",
  "travelPreference": "",
  "travelStyleTags": [
    ""
  ],
  "personalizedTips": [
    ""
  ],
  "attractionPreferences": {
    "": 0
  },
  "transportationPreferences": {
    "": 0
  },
  "idealDailyAttractionCount": {
    "min": 0,
    "max": 0
  },
  "idealPace": "",
  "recommendedAccommodationTypes": [
    ""
  ],
  "uniqueItineraryElements": [
    ""
  ],
  "similarPersonalityTypes": [
    ""
  ],
  "completedTimestamp": 0,
  "generatedItineraryId": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[PersonalityTestResultVO](#schemapersonalitytestresultvo)|

## GET 生成人格行程

GET /personality-test/generate-itinerary

根据人格类型生成推荐行程

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|city|query|string| 是 |none|
|days|query|integer| 是 |none|
|personalityType|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "planName": "",
  "city": "",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "",
          "estimatedCost": 0,
          "steps": [
            {
              "instruction": "",
              "distance": 0,
              "duration": 0,
              "mode": "",
              "cost": 0
            }
          ],
          "polyline": "",
          "startName": "",
          "endName": ""
        }
      ],
      "dailyCost": 0,
      "weather": "",
      "suggestions": [
        ""
      ]
    }
  ],
  "additionalInfo": {
    "": {}
  },
  "planType": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ItineraryPlanVO](#schemaitineraryplanvo)|

## GET 获取所有人格类型

GET /personality-test/all-personality-types

获取所有可用的人格类型信息

> 返回示例

> 200 Response

```json
{
  "": {
    "typeCode": "",
    "typeName": "",
    "description": "",
    "travelPreference": "",
    "recommendedAttractionTypes": {
      "": 0
    },
    "recommendedTransportationTypes": {
      "": 0
    },
    "dailyAttractionCountRange": {
      "min": 0,
      "max": 0
    },
    "dailyPace": "",
    "recommendedAccommodationTypes": [
      ""
    ],
    "uniqueItineraryElements": [
      ""
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapPersonalityType](#schemamappersonalitytype)|

## GET 验证人格类型

GET /personality-test/validate-personality-type

验证人格类型是否有效

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|personalityType|query|string| 是 |none|

> 返回示例

> 200 Response

```json
true
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|boolean|

## GET 预测人格类型

GET /personality-test/predict-personality/{userId}

根据用户历史行为预测可能的人格类型

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapDouble](#schemamapdouble)|

# 多模态规划控制器

## POST 多模态行程规划

POST /multimodal-planning/plan-by-multimodal

通过多模态输入（文字、图片等）进行行程规划

> Body 请求参数

```json
{
  "inputType": "string",
  "textInput": "string",
  "imageData": "string",
  "imageType": "string",
  "audioData": "string",
  "audioType": "string",
  "userPreferences": {
    "key": {}
  },
  "constraints": {
    "key": {}
  },
  "additionalNotes": "string",
  "language": "string",
  "needImageRecommendations": true,
  "needAudioGuide": true,
  "outputFormat": "string",
  "historicalTravelIds": [
    "string"
  ],
  "referenceImages": [
    "string"
  ],
  "textIntentType": "string",
  "geoContext": {
    "key": 0
  },
  "timeContext": {
    "key": {}
  },
  "confidenceScore": 0
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[MultimodalInput](#schemamultimodalinput)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "planName": "",
  "city": "",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "",
          "estimatedCost": 0,
          "steps": [
            {
              "instruction": "",
              "distance": 0,
              "duration": 0,
              "mode": "",
              "cost": 0
            }
          ],
          "polyline": "",
          "startName": "",
          "endName": ""
        }
      ],
      "dailyCost": 0,
      "weather": "",
      "suggestions": [
        ""
      ]
    }
  ],
  "additionalInfo": {
    "": {}
  },
  "planType": ""
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[ItineraryPlanVO](#schemaitineraryplanvo)|

## POST 从图片提取信息

POST /multimodal-planning/extract-from-image

从图片中提取旅行相关信息

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|imageData|query|string| 是 |none|
|imageType|query|string| 是 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## POST 从语音识别需求

POST /multimodal-planning/recognize-from-audio

从语音中识别旅行需求文本

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|audioData|query|string| 是 |none|
|audioType|query|string| 是 |none|

> 返回示例

> 200 Response

```json
"string"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|string|

## POST 理解自然语言需求

POST /multimodal-planning/understand-natural-language

理解自然语言描述的旅行需求

> Body 请求参数

```json
"string"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## POST 融合多模态信息

POST /multimodal-planning/fuse-information

融合文本、图片、音频等多种输入模态的信息

> Body 请求参数

```json
{
  "key": {}
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[MapObject](#schemamapobject)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "": {}
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapObject](#schemamapobject)|

## POST 生成多模态推荐

POST /multimodal-planning/generate-recommendations

生成多模态行程推荐结果

> Body 请求参数

```json
{
  "key": {}
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|limit|query|integer| 是 |none|
|body|body|[MapObject](#schemamapobject)| 否 |none|

> 返回示例

> 200 Response

```json
[
  {
    "resultId": "",
    "itinerary": {
      "planName": "",
      "city": "",
      "days": 0,
      "totalBudget": 0,
      "estimatedCost": 0,
      "accommodationSuggestion": "",
      "dailyItineraries": [
        {
          "day": 0,
          "attractions": [
            {
              "name": "",
              "lat": 0,
              "lng": 0
            }
          ],
          "routes": [
            {
              "duration": 0,
              "distance": 0,
              "mode": "",
              "estimatedCost": 0,
              "steps": [
                {
                  "instruction": "",
                  "distance": 0,
                  "duration": 0,
                  "mode": "",
                  "cost": 0
                }
              ],
              "polyline": "",
              "startName": "",
              "endName": ""
            }
          ],
          "dailyCost": 0,
          "weather": "",
          "suggestions": [
            ""
          ]
        }
      ],
      "additionalInfo": {
        "": {}
      },
      "planType": ""
    },
    "recommendedImages": [
      {
        "": ""
      }
    ],
    "audioGuides": [
      {
        "": ""
      }
    ],
    "matchScore": 0,
    "recommendationReasons": [
      ""
    ],
    "highlightTags": [
      ""
    ],
    "visualizationData": {
      "": {}
    },
    "presentationOrder": [
      ""
    ],
    "generatedTime": 0,
    "primaryModality": "",
    "supportedInteractions": [
      ""
    ],
    "additionalInformation": {
      "": {}
    },
    "resultSummary": "",
    "alternativeResults": [
      {
        "resultId": "",
        "itinerary": {
          "planName": "",
          "city": "",
          "days": 0,
          "totalBudget": 0,
          "estimatedCost": 0,
          "accommodationSuggestion": "",
          "dailyItineraries": [
            {
              "day": 0,
              "attractions": [
                {
                  "name": "",
                  "lat": 0,
                  "lng": 0
                }
              ],
              "routes": [
                {
                  "duration": 0,
                  "distance": 0,
                  "mode": "",
                  "estimatedCost": 0,
                  "steps": [
                    "[Object]"
                  ],
                  "polyline": "",
                  "startName": "",
                  "endName": ""
                }
              ],
              "dailyCost": 0,
              "weather": "",
              "suggestions": [
                ""
              ]
            }
          ],
          "additionalInfo": {
            "": {}
          },
          "planType": ""
        },
        "recommendedImages": [
          {
            "": ""
          }
        ],
        "audioGuides": [
          {
            "": ""
          }
        ],
        "matchScore": 0,
        "recommendationReasons": [
          ""
        ],
        "highlightTags": [
          ""
        ],
        "visualizationData": {
          "": {}
        },
        "presentationOrder": [
          ""
        ],
        "generatedTime": 0,
        "primaryModality": "",
        "supportedInteractions": [
          ""
        ],
        "additionalInformation": {
          "": {}
        },
        "resultSummary": "",
        "alternativeResults": [
          {
            "resultId": "",
            "itinerary": {
              "planName": "",
              "city": "",
              "days": 0,
              "totalBudget": 0,
              "estimatedCost": 0,
              "accommodationSuggestion": "",
              "dailyItineraries": [
                {
                  "day": 0,
                  "attractions": [
                    "[Object]"
                  ],
                  "routes": [
                    "[Object]"
                  ],
                  "dailyCost": 0,
                  "weather": "",
                  "suggestions": [
                    ""
                  ]
                }
              ],
              "additionalInfo": {
                "": {}
              },
              "planType": ""
            },
            "recommendedImages": [
              {
                "": ""
              }
            ],
            "audioGuides": [
              {
                "": ""
              }
            ],
            "matchScore": 0,
            "recommendationReasons": [
              ""
            ],
            "highlightTags": [
              ""
            ],
            "visualizationData": {
              "": {}
            },
            "presentationOrder": [
              ""
            ],
            "generatedTime": 0,
            "primaryModality": "",
            "supportedInteractions": [
              ""
            ],
            "additionalInformation": {
              "": {}
            },
            "resultSummary": "",
            "alternativeResults": [],
            "resultType": "",
            "userFeedbackScore": 0,
            "applicableScenarios": [
              ""
            ],
            "mediaResourceCount": {
              "": 0
            }
          }
        ],
        "resultType": "",
        "userFeedbackScore": 0,
        "applicableScenarios": [
          ""
        ],
        "mediaResourceCount": {
          "": 0
        }
      }
    ],
    "resultType": "",
    "userFeedbackScore": 0,
    "applicableScenarios": [
      ""
    ],
    "mediaResourceCount": {
      "": 0
    }
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|*anonymous*|[[MultimodalResult](#schemamultimodalresult)]|false|none||none|
|» resultId|string|false|none||结果ID|
|» itinerary|[ItineraryPlanVO](#schemaitineraryplanvo)|false|none||行程规划结果|
|»» planName|string|false|none||行程名称|
|»» city|string|false|none||目的地城市|
|»» days|integer|false|none||旅行天数|
|»» totalBudget|number|false|none||总预算|
|»» estimatedCost|number|false|none||预估总费用|
|»» accommodationSuggestion|string|false|none||住宿建议|
|»» dailyItineraries|[[DailyItinerary](#schemadailyitinerary)]|false|none||每日行程|
|»»» day|integer|false|none||第几天|
|»»» attractions|[[POI](#schemapoi)]|false|none||当天景点|
|»»»» name|string|false|none||none|
|»»»» lat|number|false|none||none|
|»»»» lng|number|false|none||none|
|»»» routes|[[Route](#schemaroute)]|false|none||路线|
|»»»» duration|number|false|none||总时长(秒)|
|»»»» distance|number|false|none||总距离(米)|
|»»»» mode|string|false|none||交通方式|
|»»»» estimatedCost|number|false|none||预估费用|
|»»»» steps|[[RouteStep](#schemaroutestep)]|false|none||路线步骤|
|»»»»» instruction|string|false|none||导航指示|
|»»»»» distance|number|false|none||该段距离|
|»»»»» duration|number|false|none||该段时长|
|»»»»» mode|string|false|none||该段交通方式|
|»»»»» cost|number|false|none||该段费用|
|»»»» polyline|string|false|none||路线轨迹点集|
|»»»» startName|string|false|none||起点名称|
|»»»» endName|string|false|none||终点名称|
|»»» dailyCost|number|false|none||当日费用|
|»»» weather|string|false|none||天气情况|
|»»» suggestions|[string]|false|none||当日建议|
|»» additionalInfo|[MapObject](#schemamapobject)|false|none||附加信息（如天气提醒、注意事项等）|
|»»» key|[key](#schemakey)|false|none||none|
|»» planType|string|false|none||行程类型（价格锁定、人格化等）|
|» recommendedImages|[[MapString](#schemamapstring)]|false|none||推荐图片列表<br />每个元素包含图片URL和描述|
|»» key|string|false|none||none|
|» audioGuides|[[MapString](#schemamapstring)]|false|none||语音解说列表<br />每个元素包含音频URL和描述|
|»» key|string|false|none||none|
|» matchScore|number|false|none||结果匹配度<br />表示该结果与用户需求的匹配程度，范围0-1|
|» recommendationReasons|[string]|false|none||推荐理由|
|» highlightTags|[string]|false|none||亮点标签|
|» visualizationData|[MapObject](#schemamapobject)|false|none||视觉化展示数据<br />用于生成图表或地图可视化|
|»» key|[key](#schemakey)|false|none||none|
|» presentationOrder|[string]|false|none||多媒体展示顺序<br />指导前端如何排序展示不同模态的内容|
|» generatedTime|integer(int64)|false|none||结果生成时间|
|» primaryModality|string|false|none||主要输入模态<br />表示主要基于哪种模态生成的结果|
|» supportedInteractions|[string]|false|none||支持的交互方式<br />如：edit（编辑）、share（分享）、save（保存）|
|» additionalInformation|[MapObject](#schemamapobject)|false|none||补充信息<br />其他可能有用的信息|
|»» key|[key](#schemakey)|false|none||none|
|» resultSummary|string|false|none||结果摘要<br />简短描述结果的核心内容|
|» alternativeResults|[[MultimodalResult](#schemamultimodalresult)]|false|none||可替代方案列表<br />其他可能的行程建议|
|»» resultId|string|false|none||结果ID|
|»» itinerary|[ItineraryPlanVO](#schemaitineraryplanvo)|false|none||行程规划结果|
|»» recommendedImages|[[MapString](#schemamapstring)]|false|none||推荐图片列表<br />每个元素包含图片URL和描述|
|»» audioGuides|[[MapString](#schemamapstring)]|false|none||语音解说列表<br />每个元素包含音频URL和描述|
|»» matchScore|number|false|none||结果匹配度<br />表示该结果与用户需求的匹配程度，范围0-1|
|»» recommendationReasons|[string]|false|none||推荐理由|
|»» highlightTags|[string]|false|none||亮点标签|
|»» visualizationData|[MapObject](#schemamapobject)|false|none||视觉化展示数据<br />用于生成图表或地图可视化|
|»» presentationOrder|[string]|false|none||多媒体展示顺序<br />指导前端如何排序展示不同模态的内容|
|»» generatedTime|integer(int64)|false|none||结果生成时间|
|»» primaryModality|string|false|none||主要输入模态<br />表示主要基于哪种模态生成的结果|
|»» supportedInteractions|[string]|false|none||支持的交互方式<br />如：edit（编辑）、share（分享）、save（保存）|
|»» additionalInformation|[MapObject](#schemamapobject)|false|none||补充信息<br />其他可能有用的信息|
|»» resultSummary|string|false|none||结果摘要<br />简短描述结果的核心内容|
|»» alternativeResults|[[MultimodalResult](#schemamultimodalresult)]|false|none||可替代方案列表<br />其他可能的行程建议|
|»» resultType|string|false|none||结果类型<br />如：complete（完整行程）、partial（部分行程）、suggestion（建议）|
|»» userFeedbackScore|number|false|none||用户反馈评分<br />用户对该结果的评分，范围1-5|
|»» applicableScenarios|[string]|false|none||适用场景<br />该结果适合的旅行场景描述|
|»» mediaResourceCount|[MapInteger](#schemamapinteger)|false|none||媒体资源统计<br />包含的图片、音频等资源数量|
|»»» key|integer|false|none||none|
|» resultType|string|false|none||结果类型<br />如：complete（完整行程）、partial（部分行程）、suggestion（建议）|
|» userFeedbackScore|number|false|none||用户反馈评分<br />用户对该结果的评分，范围1-5|
|» applicableScenarios|[string]|false|none||适用场景<br />该结果适合的旅行场景描述|
|» mediaResourceCount|[MapInteger](#schemamapinteger)|false|none||媒体资源统计<br />包含的图片、音频等资源数量|

## POST 分析用户旅行风格

POST /multimodal-planning/analyze-travel-style

从多模态数据中分析用户旅行风格

> Body 请求参数

```json
[
  {
    "key": {}
  }
]
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|[MapObject](#schemamapobject)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "": 0
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MapDouble](#schemamapdouble)|

## POST 转换为多模态展示

POST /multimodal-planning/convert-to-multimodal

将行程规划结果转换为多模态展示数据

> Body 请求参数

```json
{
  "planName": "string",
  "city": "string",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "string",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "string",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "string",
          "estimatedCost": 0,
          "steps": [
            {}
          ],
          "polyline": "string",
          "startName": "string",
          "endName": "string"
        }
      ],
      "dailyCost": 0,
      "weather": "string",
      "suggestions": [
        "string"
      ]
    }
  ],
  "additionalInfo": {
    "key": {}
  },
  "planType": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|includeImage|query|boolean| 是 |none|
|includeAudio|query|boolean| 是 |none|
|body|body|[ItineraryPlanVO](#schemaitineraryplanvo)| 否 |none|

> 返回示例

> 200 Response

```json
{
  "resultId": "",
  "itinerary": {
    "planName": "",
    "city": "",
    "days": 0,
    "totalBudget": 0,
    "estimatedCost": 0,
    "accommodationSuggestion": "",
    "dailyItineraries": [
      {
        "day": 0,
        "attractions": [
          {
            "name": "",
            "lat": 0,
            "lng": 0
          }
        ],
        "routes": [
          {
            "duration": 0,
            "distance": 0,
            "mode": "",
            "estimatedCost": 0,
            "steps": [
              {
                "instruction": "",
                "distance": 0,
                "duration": 0,
                "mode": "",
                "cost": 0
              }
            ],
            "polyline": "",
            "startName": "",
            "endName": ""
          }
        ],
        "dailyCost": 0,
        "weather": "",
        "suggestions": [
          ""
        ]
      }
    ],
    "additionalInfo": {
      "": {}
    },
    "planType": ""
  },
  "recommendedImages": [
    {
      "": ""
    }
  ],
  "audioGuides": [
    {
      "": ""
    }
  ],
  "matchScore": 0,
  "recommendationReasons": [
    ""
  ],
  "highlightTags": [
    ""
  ],
  "visualizationData": {
    "": {}
  },
  "presentationOrder": [
    ""
  ],
  "generatedTime": 0,
  "primaryModality": "",
  "supportedInteractions": [
    ""
  ],
  "additionalInformation": {
    "": {}
  },
  "resultSummary": "",
  "alternativeResults": [
    {
      "resultId": "",
      "itinerary": {
        "planName": "",
        "city": "",
        "days": 0,
        "totalBudget": 0,
        "estimatedCost": 0,
        "accommodationSuggestion": "",
        "dailyItineraries": [
          {
            "day": 0,
            "attractions": [
              {
                "name": "",
                "lat": 0,
                "lng": 0
              }
            ],
            "routes": [
              {
                "duration": 0,
                "distance": 0,
                "mode": "",
                "estimatedCost": 0,
                "steps": [
                  {
                    "instruction": "",
                    "distance": 0,
                    "duration": 0,
                    "mode": "",
                    "cost": 0
                  }
                ],
                "polyline": "",
                "startName": "",
                "endName": ""
              }
            ],
            "dailyCost": 0,
            "weather": "",
            "suggestions": [
              ""
            ]
          }
        ],
        "additionalInfo": {
          "": {}
        },
        "planType": ""
      },
      "recommendedImages": [
        {
          "": ""
        }
      ],
      "audioGuides": [
        {
          "": ""
        }
      ],
      "matchScore": 0,
      "recommendationReasons": [
        ""
      ],
      "highlightTags": [
        ""
      ],
      "visualizationData": {
        "": {}
      },
      "presentationOrder": [
        ""
      ],
      "generatedTime": 0,
      "primaryModality": "",
      "supportedInteractions": [
        ""
      ],
      "additionalInformation": {
        "": {}
      },
      "resultSummary": "",
      "alternativeResults": [
        {
          "resultId": "",
          "itinerary": {
            "planName": "",
            "city": "",
            "days": 0,
            "totalBudget": 0,
            "estimatedCost": 0,
            "accommodationSuggestion": "",
            "dailyItineraries": [
              {
                "day": 0,
                "attractions": [
                  {
                    "name": "",
                    "lat": 0,
                    "lng": 0
                  }
                ],
                "routes": [
                  {
                    "duration": 0,
                    "distance": 0,
                    "mode": "",
                    "estimatedCost": 0,
                    "steps": "[Object]",
                    "polyline": "",
                    "startName": "",
                    "endName": ""
                  }
                ],
                "dailyCost": 0,
                "weather": "",
                "suggestions": [
                  ""
                ]
              }
            ],
            "additionalInfo": {
              "": {}
            },
            "planType": ""
          },
          "recommendedImages": [
            {
              "": ""
            }
          ],
          "audioGuides": [
            {
              "": ""
            }
          ],
          "matchScore": 0,
          "recommendationReasons": [
            ""
          ],
          "highlightTags": [
            ""
          ],
          "visualizationData": {
            "": {}
          },
          "presentationOrder": [
            ""
          ],
          "generatedTime": 0,
          "primaryModality": "",
          "supportedInteractions": [
            ""
          ],
          "additionalInformation": {
            "": {}
          },
          "resultSummary": "",
          "alternativeResults": [],
          "resultType": "",
          "userFeedbackScore": 0,
          "applicableScenarios": [
            ""
          ],
          "mediaResourceCount": {
            "": 0
          }
        }
      ],
      "resultType": "",
      "userFeedbackScore": 0,
      "applicableScenarios": [
        ""
      ],
      "mediaResourceCount": {
        "": 0
      }
    }
  ],
  "resultType": "",
  "userFeedbackScore": 0,
  "applicableScenarios": [
    ""
  ],
  "mediaResourceCount": {
    "": 0
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|[MultimodalResult](#schemamultimodalresult)|

# 数据模型

<h2 id="tocS_Point">Point</h2>

<a id="schemapoint"></a>
<a id="schema_Point"></a>
<a id="tocSpoint"></a>
<a id="tocspoint"></a>

```json
{
  "lat": 0,
  "lng": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|lat|number|false|none||none|
|lng|number|false|none||none|

<h2 id="tocS_Location">Location</h2>

<a id="schemalocation"></a>
<a id="schema_Location"></a>
<a id="tocSlocation"></a>
<a id="tocslocation"></a>

```json
{
  "name": "string",
  "lat": 0,
  "lng": 0,
  "address": "string",
  "type": "string",
  "cityCode": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|name|string|false|none||none|
|lat|number|false|none||none|
|lng|number|false|none||none|
|address|string|false|none||none|
|type|string|false|none||none|
|cityCode|string|false|none||none|

<h2 id="tocS_RouteStep">RouteStep</h2>

<a id="schemaroutestep"></a>
<a id="schema_RouteStep"></a>
<a id="tocSroutestep"></a>
<a id="tocsroutestep"></a>

```json
{
  "instruction": "string",
  "distance": 0,
  "duration": 0,
  "mode": "string",
  "cost": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|instruction|string|false|none||导航指示|
|distance|number|false|none||该段距离|
|duration|number|false|none||该段时长|
|mode|string|false|none||该段交通方式|
|cost|number|false|none||该段费用|

<h2 id="tocS_Route">Route</h2>

<a id="schemaroute"></a>
<a id="schema_Route"></a>
<a id="tocSroute"></a>
<a id="tocsroute"></a>

```json
{
  "duration": 0,
  "distance": 0,
  "mode": "string",
  "estimatedCost": 0,
  "steps": [
    {
      "instruction": "string",
      "distance": 0,
      "duration": 0,
      "mode": "string",
      "cost": 0
    }
  ],
  "polyline": "string",
  "startName": "string",
  "endName": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|duration|number|false|none||总时长(秒)|
|distance|number|false|none||总距离(米)|
|mode|string|false|none||交通方式|
|estimatedCost|number|false|none||预估费用|
|steps|[[RouteStep](#schemaroutestep)]|false|none||路线步骤|
|polyline|string|false|none||路线轨迹点集|
|startName|string|false|none||起点名称|
|endName|string|false|none||终点名称|

<h2 id="tocS_POI">POI</h2>

<a id="schemapoi"></a>
<a id="schema_POI"></a>
<a id="tocSpoi"></a>
<a id="tocspoi"></a>

```json
{
  "name": "string",
  "lat": 0,
  "lng": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|name|string|false|none||none|
|lat|number|false|none||none|
|lng|number|false|none||none|

<h2 id="tocS_MapObject">MapObject</h2>

<a id="schemamapobject"></a>
<a id="schema_MapObject"></a>
<a id="tocSmapobject"></a>
<a id="tocsmapobject"></a>

```json
{
  "key": {}
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|key|[key](#schemakey)|false|none||none|

<h2 id="tocS_UserVo">UserVo</h2>

<a id="schemauservo"></a>
<a id="schema_UserVo"></a>
<a id="tocSuservo"></a>
<a id="tocsuservo"></a>

```json
{
  "userName": "string",
  "password": "string",
  "loginType": "ACCOUNT_SECRET_LOGIN"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|userName|string|false|none||none|
|password|string|false|none||none|
|loginType|string|false|none||none|

#### 枚举值

|属性|值|
|---|---|
|loginType|ACCOUNT_SECRET_LOGIN|
|loginType|QRCODE_LOGIN|

<h2 id="tocS_RegisterVo">RegisterVo</h2>

<a id="schemaregistervo"></a>
<a id="schema_RegisterVo"></a>
<a id="tocSregistervo"></a>
<a id="tocsregistervo"></a>

```json
{
  "userName": "string",
  "password": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|userName|string|false|none||none|
|password|string|false|none||none|

<h2 id="tocS_DailyItinerary">DailyItinerary</h2>

<a id="schemadailyitinerary"></a>
<a id="schema_DailyItinerary"></a>
<a id="tocSdailyitinerary"></a>
<a id="tocsdailyitinerary"></a>

```json
{
  "day": 0,
  "attractions": [
    {
      "name": "string",
      "lat": 0,
      "lng": 0
    }
  ],
  "routes": [
    {
      "duration": 0,
      "distance": 0,
      "mode": "string",
      "estimatedCost": 0,
      "steps": [
        {
          "instruction": "string",
          "distance": 0,
          "duration": 0,
          "mode": "string",
          "cost": 0
        }
      ],
      "polyline": "string",
      "startName": "string",
      "endName": "string"
    }
  ],
  "dailyCost": 0,
  "weather": "string",
  "suggestions": [
    "string"
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|day|integer|false|none||第几天|
|attractions|[[POI](#schemapoi)]|false|none||当天景点|
|routes|[[Route](#schemaroute)]|false|none||路线|
|dailyCost|number|false|none||当日费用|
|weather|string|false|none||天气情况|
|suggestions|[string]|false|none||当日建议|

<h2 id="tocS_key">key</h2>

<a id="schemakey"></a>
<a id="schema_key"></a>
<a id="tocSkey"></a>
<a id="tocskey"></a>

```json
{}

```

### 属性

*None*

<h2 id="tocS_ItineraryPlanVO">ItineraryPlanVO</h2>

<a id="schemaitineraryplanvo"></a>
<a id="schema_ItineraryPlanVO"></a>
<a id="tocSitineraryplanvo"></a>
<a id="tocsitineraryplanvo"></a>

```json
{
  "planName": "string",
  "city": "string",
  "days": 0,
  "totalBudget": 0,
  "estimatedCost": 0,
  "accommodationSuggestion": "string",
  "dailyItineraries": [
    {
      "day": 0,
      "attractions": [
        {
          "name": "string",
          "lat": 0,
          "lng": 0
        }
      ],
      "routes": [
        {
          "duration": 0,
          "distance": 0,
          "mode": "string",
          "estimatedCost": 0,
          "steps": [
            {}
          ],
          "polyline": "string",
          "startName": "string",
          "endName": "string"
        }
      ],
      "dailyCost": 0,
      "weather": "string",
      "suggestions": [
        "string"
      ]
    }
  ],
  "additionalInfo": {
    "key": {}
  },
  "planType": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|planName|string|false|none||行程名称|
|city|string|false|none||目的地城市|
|days|integer|false|none||旅行天数|
|totalBudget|number|false|none||总预算|
|estimatedCost|number|false|none||预估总费用|
|accommodationSuggestion|string|false|none||住宿建议|
|dailyItineraries|[[DailyItinerary](#schemadailyitinerary)]|false|none||每日行程|
|additionalInfo|[MapObject](#schemamapobject)|false|none||附加信息（如天气提醒、注意事项等）|
|planType|string|false|none||行程类型（价格锁定、人格化等）|

<h2 id="tocS_DiscountInfo">DiscountInfo</h2>

<a id="schemadiscountinfo"></a>
<a id="schema_DiscountInfo"></a>
<a id="tocSdiscountinfo"></a>
<a id="tocsdiscountinfo"></a>

```json
{
  "discountName": "string",
  "description": "string",
  "discountValue": 0,
  "applicableTo": "string",
  "requiredDocuments": "string",
  "validFrom": 0,
  "validTo": 0,
  "isValid": true
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|discountName|string|false|none||优惠名称|
|description|string|false|none||优惠描述|
|discountValue|number|false|none||折扣力度（如 8.5折）|
|applicableTo|string|false|none||适用景点/服务|
|requiredDocuments|string|false|none||所需凭证|
|validFrom|integer(int64)|false|none||有效期开始|
|validTo|integer(int64)|false|none||有效期结束|
|isValid|boolean|false|none||是否可用|

<h2 id="tocS_SchedulePart">SchedulePart</h2>

<a id="schemaschedulepart"></a>
<a id="schema_SchedulePart"></a>
<a id="tocSschedulepart"></a>
<a id="tocsschedulepart"></a>

```json
{
  "timeRange": "string",
  "activity": "string",
  "relatedAttractions": [
    {
      "name": "string",
      "lat": 0,
      "lng": 0
    }
  ],
  "estimatedDuration": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|timeRange|string|false|none||时间段描述|
|activity|string|false|none||活动内容|
|relatedAttractions|[[POI](#schemapoi)]|false|none||相关景点|
|estimatedDuration|integer|false|none||预计时长（分钟）|

<h2 id="tocS_DailySchedule">DailySchedule</h2>

<a id="schemadailyschedule"></a>
<a id="schema_DailySchedule"></a>
<a id="tocSdailyschedule"></a>
<a id="tocsdailyschedule"></a>

```json
{
  "day": 0,
  "morning": {
    "timeRange": "string",
    "activity": "string",
    "relatedAttractions": [
      {
        "name": "string",
        "lat": 0,
        "lng": 0
      }
    ],
    "estimatedDuration": 0
  },
  "afternoon": {
    "timeRange": "string",
    "activity": "string",
    "relatedAttractions": [
      {
        "name": "string",
        "lat": 0,
        "lng": 0
      }
    ],
    "estimatedDuration": 0
  },
  "evening": {
    "timeRange": "string",
    "activity": "string",
    "relatedAttractions": [
      {
        "name": "string",
        "lat": 0,
        "lng": 0
      }
    ],
    "estimatedDuration": 0
  },
  "recommendedAttractions": [
    {
      "name": "string",
      "lat": 0,
      "lng": 0
    }
  ],
  "transportationSuggestion": "string",
  "dailyBudget": 0,
  "notices": [
    "string"
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|day|integer|false|none||第几天|
|morning|[SchedulePart](#schemaschedulepart)|false|none||上午行程|
|afternoon|[SchedulePart](#schemaschedulepart)|false|none||下午行程|
|evening|[SchedulePart](#schemaschedulepart)|false|none||晚上行程|
|recommendedAttractions|[[POI](#schemapoi)]|false|none||全天推荐景点|
|transportationSuggestion|string|false|none||当日交通建议|
|dailyBudget|number|false|none||当日预算|
|notices|[string]|false|none||注意事项|

<h2 id="tocS_StudentFriendlyHotel">StudentFriendlyHotel</h2>

<a id="schemastudentfriendlyhotel"></a>
<a id="schema_StudentFriendlyHotel"></a>
<a id="tocSstudentfriendlyhotel"></a>
<a id="tocsstudentfriendlyhotel"></a>

```json
{
  "hotelName": "string",
  "address": "string",
  "studentPrice": 0,
  "originalPrice": 0,
  "discount": 0,
  "distanceToCityCenter": 0,
  "facilities": [
    "string"
  ],
  "bookingLink": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|hotelName|string|false|none||酒店名称|
|address|string|false|none||酒店地址|
|studentPrice|number|false|none||学生价格|
|originalPrice|number|false|none||原价|
|discount|number|false|none||折扣力度|
|distanceToCityCenter|number|false|none||距离市中心距离|
|facilities|[string]|false|none||设施描述|
|bookingLink|string|false|none||预订链接|

<h2 id="tocS_AccommodationInfo">AccommodationInfo</h2>

<a id="schemaaccommodationinfo"></a>
<a id="schema_AccommodationInfo"></a>
<a id="tocSaccommodationinfo"></a>
<a id="tocsaccommodationinfo"></a>

```json
{
  "accommodationType": "string",
  "locationSuggestion": "string",
  "minPrice": 0,
  "maxPrice": 0,
  "studentFriendlyHotels": [
    {
      "hotelName": "string",
      "address": "string",
      "studentPrice": 0,
      "originalPrice": 0,
      "discount": 0,
      "distanceToCityCenter": 0,
      "facilities": [
        "string"
      ],
      "bookingLink": "string"
    }
  ],
  "notices": [
    "string"
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|accommodationType|string|false|none||推荐住宿类型|
|locationSuggestion|string|false|none||住宿位置建议|
|minPrice|number|false|none||价格区间（最低）|
|maxPrice|number|false|none||价格区间（最高）|
|studentFriendlyHotels|[[StudentFriendlyHotel](#schemastudentfriendlyhotel)]|false|none||学生优惠住宿推荐|
|notices|[string]|false|none||住宿注意事项|

<h2 id="tocS_StudentRoute">StudentRoute</h2>

<a id="schemastudentroute"></a>
<a id="schema_StudentRoute"></a>
<a id="tocSstudentroute"></a>
<a id="tocsstudentroute"></a>

```json
{
  "routeId": "string",
  "routeName": "string",
  "description": "string",
  "city": "string",
  "days": 0,
  "studentType": "string",
  "minBudget": 0,
  "maxBudget": 0,
  "averageCost": 0,
  "attractions": [
    {
      "name": "string",
      "lat": 0,
      "lng": 0
    }
  ],
  "routes": [
    {
      "duration": 0,
      "distance": 0,
      "mode": "string",
      "estimatedCost": 0,
      "steps": [
        {
          "instruction": "string",
          "distance": 0,
          "duration": 0,
          "mode": "string",
          "cost": 0
        }
      ],
      "polyline": "string",
      "startName": "string",
      "endName": "string"
    }
  ],
  "recommendedReasons": [
    "string"
  ],
  "studentDiscounts": [
    {
      "discountName": "string",
      "description": "string",
      "discountValue": 0,
      "applicableTo": "string",
      "requiredDocuments": "string",
      "validFrom": 0,
      "validTo": 0,
      "isValid": true
    }
  ],
  "tags": [
    "string"
  ],
  "recommendedSeasons": [
    "string"
  ],
  "routeType": "string",
  "groupSize": "string",
  "viewCount": 0,
  "favoriteCount": 0,
  "rating": 0,
  "ratingCount": 0,
  "isHot": true,
  "isRecommended": true,
  "dailySchedules": [
    {
      "day": 0,
      "morning": {
        "timeRange": "string",
        "activity": "string",
        "relatedAttractions": [
          {
            "name": null,
            "lat": null,
            "lng": null
          }
        ],
        "estimatedDuration": 0
      },
      "afternoon": {
        "timeRange": "string",
        "activity": "string",
        "relatedAttractions": [
          {
            "name": null,
            "lat": null,
            "lng": null
          }
        ],
        "estimatedDuration": 0
      },
      "evening": {
        "timeRange": "string",
        "activity": "string",
        "relatedAttractions": [
          {
            "name": null,
            "lat": null,
            "lng": null
          }
        ],
        "estimatedDuration": 0
      },
      "recommendedAttractions": [
        {
          "name": "string",
          "lat": 0,
          "lng": 0
        }
      ],
      "transportationSuggestion": "string",
      "dailyBudget": 0,
      "notices": [
        "string"
      ]
    }
  ],
  "accommodationInfo": {
    "accommodationType": "string",
    "locationSuggestion": "string",
    "minPrice": 0,
    "maxPrice": 0,
    "studentFriendlyHotels": [
      {
        "hotelName": "string",
        "address": "string",
        "studentPrice": 0,
        "originalPrice": 0,
        "discount": 0,
        "distanceToCityCenter": 0,
        "facilities": [
          "string"
        ],
        "bookingLink": "string"
      }
    ],
    "notices": [
      "string"
    ]
  },
  "createTime": 0,
  "updateTime": 0,
  "additionalInfo": {
    "key": {}
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|routeId|string|false|none||线路唯一标识|
|routeName|string|false|none||线路名称|
|description|string|false|none||线路描述|
|city|string|false|none||所在城市|
|days|integer|false|none||行程天数|
|studentType|string|false|none||适合学生类型（大学生、中学生等）|
|minBudget|number|false|none||预算范围（最低价）|
|maxBudget|number|false|none||预算范围（最高价）|
|averageCost|number|false|none||平均费用|
|attractions|[[POI](#schemapoi)]|false|none||景点列表|
|routes|[[Route](#schemaroute)]|false|none||交通信息|
|recommendedReasons|[string]|false|none||推荐理由|
|studentDiscounts|[[DiscountInfo](#schemadiscountinfo)]|false|none||学生专属优惠信息|
|tags|[string]|false|none||线路特点标签|
|recommendedSeasons|[string]|false|none||推荐季节|
|routeType|string|false|none||线路类型（文化、自然、美食等）|
|groupSize|string|false|none||适合群体大小（个人、小团体、班级等）|
|viewCount|integer|false|none||累计浏览次数|
|favoriteCount|integer|false|none||收藏次数|
|rating|number|false|none||评分|
|ratingCount|integer|false|none||评分人数|
|isHot|boolean|false|none||是否热门|
|isRecommended|boolean|false|none||是否推荐|
|dailySchedules|[[DailySchedule](#schemadailyschedule)]|false|none||每日行程安排|
|accommodationInfo|[AccommodationInfo](#schemaaccommodationinfo)|false|none||住宿建议|
|createTime|integer(int64)|false|none||创建时间|
|updateTime|integer(int64)|false|none||更新时间|
|additionalInfo|[MapObject](#schemamapobject)|false|none||额外信息|

<h2 id="tocS_MapString">MapString</h2>

<a id="schemamapstring"></a>
<a id="schema_MapString"></a>
<a id="tocSmapstring"></a>
<a id="tocsmapstring"></a>

```json
{
  "key": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|key|string|false|none||none|

<h2 id="tocS_MapDouble">MapDouble</h2>

<a id="schemamapdouble"></a>
<a id="schema_MapDouble"></a>
<a id="tocSmapdouble"></a>
<a id="tocsmapdouble"></a>

```json
{
  "key": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|key|number|false|none||none|

<h2 id="tocS_ChatMessage">ChatMessage</h2>

<a id="schemachatmessage"></a>
<a id="schema_ChatMessage"></a>
<a id="tocSchatmessage"></a>
<a id="tocschatmessage"></a>

```json
{
  "messageId": "string",
  "sessionId": "string",
  "senderType": "string",
  "content": "string",
  "messageType": "string",
  "sendTime": "string",
  "status": "string",
  "relatedItineraryId": "string",
  "relatedAttractionId": "string",
  "additionalInfo": {
    "key": {}
  },
  "replyToMessageId": "string",
  "sentiment": "string",
  "processingTime": 0,
  "quickReplyRequired": true,
  "quickReplyOptions": {
    "key": "string"
  },
  "contextType": "string",
  "location": {
    "key": 0
  },
  "tags": {
    "key": "string"
  },
  "errorCode": "string",
  "errorMessage": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|messageId|string|false|none||消息ID|
|sessionId|string|false|none||会话ID|
|senderType|string|false|none||发送者类型<br />user - 用户<br />ai - AI旅行搭子|
|content|string|false|none||消息内容|
|messageType|string|false|none||消息类型<br />text - 文本<br />image - 图片<br />audio - 音频<br />system - 系统消息<br />recommendation - 推荐消息|
|sendTime|string|false|none||发送时间|
|status|string|false|none||消息状态<br />sent - 已发送<br />read - 已读<br />processing - 处理中<br />error - 发送失败|
|relatedItineraryId|string|false|none||相关行程ID|
|relatedAttractionId|string|false|none||相关景点ID|
|additionalInfo|[MapObject](#schemamapobject)|false|none||附加信息<br />可以包含推荐详情、图片URL等|
|replyToMessageId|string|false|none||回复消息的ID<br />用于消息引用|
|sentiment|string|false|none||消息情感分析结果<br />positive - 积极<br />neutral - 中性<br />negative - 消极|
|processingTime|integer(int64)|false|none||消息处理时间（毫秒）|
|quickReplyRequired|boolean|false|none||是否需要快速回复|
|quickReplyOptions|[MapString](#schemamapstring)|false|none||快速回复选项|
|contextType|string|false|none||消息所属上下文类型<br />planning - 行程规划阶段<br />during_trip - 旅行进行中<br />after_trip - 旅行结束后|
|location|[MapDouble](#schemamapdouble)|false|none||地理位置信息<br />用于基于位置的推荐|
|tags|[MapString](#schemamapstring)|false|none||消息标签|
|errorCode|string|false|none||错误码|
|errorMessage|string|false|none||错误信息|

<h2 id="tocS_MapInteger">MapInteger</h2>

<a id="schemamapinteger"></a>
<a id="schema_MapInteger"></a>
<a id="tocSmapinteger"></a>
<a id="tocsmapinteger"></a>

```json
{
  "key": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|key|integer|false|none||none|

<h2 id="tocS_TestOption">TestOption</h2>

<a id="schematestoption"></a>
<a id="schema_TestOption"></a>
<a id="tocStestoption"></a>
<a id="tocstestoption"></a>

```json
{
  "optionId": "string",
  "content": "string",
  "traitScores": {
    "key": 0
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|optionId|string|false|none||选项ID|
|content|string|false|none||选项内容|
|traitScores|[MapInteger](#schemamapinteger)|false|none||选项对应的人格特质得分映射<br />键为人格特质代码，值为得分|

<h2 id="tocS_TestQuestion">TestQuestion</h2>

<a id="schematestquestion"></a>
<a id="schema_TestQuestion"></a>
<a id="tocStestquestion"></a>
<a id="tocstestquestion"></a>

```json
{
  "questionId": "string",
  "content": "string",
  "options": [
    {
      "optionId": "string",
      "content": "string",
      "traitScores": {
        "key": 0
      }
    }
  ],
  "weight": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|questionId|string|false|none||问题ID|
|content|string|false|none||问题内容|
|options|[[TestOption](#schematestoption)]|false|none||选项列表|
|weight|integer|false|none||问题权重|

<h2 id="tocS_AttractionCountRange">AttractionCountRange</h2>

<a id="schemaattractioncountrange"></a>
<a id="schema_AttractionCountRange"></a>
<a id="tocSattractioncountrange"></a>
<a id="tocsattractioncountrange"></a>

```json
{
  "min": 0,
  "max": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|min|integer|false|none||最小数量|
|max|integer|false|none||最大数量|

<h2 id="tocS_MapPersonalityType">MapPersonalityType</h2>

<a id="schemamappersonalitytype"></a>
<a id="schema_MapPersonalityType"></a>
<a id="tocSmappersonalitytype"></a>
<a id="tocsmappersonalitytype"></a>

```json
{
  "key": {
    "typeCode": "string",
    "typeName": "string",
    "description": "string",
    "travelPreference": "string",
    "recommendedAttractionTypes": {
      "key": 0
    },
    "recommendedTransportationTypes": {
      "key": 0
    },
    "dailyAttractionCountRange": {
      "min": 0,
      "max": 0
    },
    "dailyPace": "string",
    "recommendedAccommodationTypes": [
      "string"
    ],
    "uniqueItineraryElements": [
      "string"
    ]
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|key|object|false|none||none|
|» typeCode|string|false|none||人格类型代码|
|» typeName|string|false|none||人格类型名称|
|» description|string|false|none||人格类型描述|
|» travelPreference|string|false|none||旅行偏好描述|
|» recommendedAttractionTypes|[MapInteger](#schemamapinteger)|false|none||推荐景点类型权重<br />键为景点类型，值为权重|
|» recommendedTransportationTypes|[MapInteger](#schemamapinteger)|false|none||推荐交通方式权重|
|» dailyAttractionCountRange|[AttractionCountRange](#schemaattractioncountrange)|false|none||每日推荐景点数量范围|
|» dailyPace|string|false|none||每日行程节奏描述|
|» recommendedAccommodationTypes|[string]|false|none||推荐住宿类型|
|» uniqueItineraryElements|[string]|false|none||特色行程元素|

<h2 id="tocS_PersonalityTest">PersonalityTest</h2>

<a id="schemapersonalitytest"></a>
<a id="schema_PersonalityTest"></a>
<a id="tocSpersonalitytest"></a>
<a id="tocspersonalitytest"></a>

```json
{
  "testId": "string",
  "testName": "string",
  "questions": [
    {
      "questionId": "string",
      "content": "string",
      "options": [
        {
          "optionId": "string",
          "content": "string",
          "traitScores": {
            "key": null
          }
        }
      ],
      "weight": 0
    }
  ],
  "personalityTypes": {
    "key": {
      "typeCode": "string",
      "typeName": "string",
      "description": "string",
      "travelPreference": "string",
      "recommendedAttractionTypes": {
        "key": 0
      },
      "recommendedTransportationTypes": {
        "key": 0
      },
      "dailyAttractionCountRange": {
        "min": 0,
        "max": 0
      },
      "dailyPace": "string",
      "recommendedAccommodationTypes": [
        "string"
      ],
      "uniqueItineraryElements": [
        "string"
      ]
    }
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|testId|string|false|none||测试ID|
|testName|string|false|none||测试名称|
|questions|[[TestQuestion](#schematestquestion)]|false|none||测试问题列表|
|personalityTypes|[MapPersonalityType](#schemamappersonalitytype)|false|none||人格类型映射表|

<h2 id="tocS_DailyAttractionCountRange">DailyAttractionCountRange</h2>

<a id="schemadailyattractioncountrange"></a>
<a id="schema_DailyAttractionCountRange"></a>
<a id="tocSdailyattractioncountrange"></a>
<a id="tocsdailyattractioncountrange"></a>

```json
{
  "min": 0,
  "max": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|min|integer|false|none||最小数量|
|max|integer|false|none||最大数量|

<h2 id="tocS_PersonalityTestResultVO">PersonalityTestResultVO</h2>

<a id="schemapersonalitytestresultvo"></a>
<a id="schema_PersonalityTestResultVO"></a>
<a id="tocSpersonalitytestresultvo"></a>
<a id="tocspersonalitytestresultvo"></a>

```json
{
  "testId": "string",
  "userAnswers": {
    "key": "string"
  },
  "traitScores": {
    "key": 0
  },
  "dominantPersonalityType": "string",
  "dominantPersonalityDescription": "string",
  "travelPreference": "string",
  "travelStyleTags": [
    "string"
  ],
  "personalizedTips": [
    "string"
  ],
  "attractionPreferences": {
    "key": 0
  },
  "transportationPreferences": {
    "key": 0
  },
  "idealDailyAttractionCount": {
    "min": 0,
    "max": 0
  },
  "idealPace": "string",
  "recommendedAccommodationTypes": [
    "string"
  ],
  "uniqueItineraryElements": [
    "string"
  ],
  "similarPersonalityTypes": [
    "string"
  ],
  "completedTimestamp": 0,
  "generatedItineraryId": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|testId|string|false|none||测试ID|
|userAnswers|[MapString](#schemamapstring)|false|none||用户答案映射<br />键为问题ID，值为选项ID|
|traitScores|[MapInteger](#schemamapinteger)|false|none||人格特质得分映射<br />键为人格特质代码，值为得分|
|dominantPersonalityType|string|false|none||主导人格类型|
|dominantPersonalityDescription|string|false|none||主导人格类型描述|
|travelPreference|string|false|none||旅行偏好描述|
|travelStyleTags|[string]|false|none||旅行风格标签列表|
|personalizedTips|[string]|false|none||个性化旅行建议|
|attractionPreferences|[MapInteger](#schemamapinteger)|false|none||景点偏好权重<br />键为景点类型，值为权重|
|transportationPreferences|[MapInteger](#schemamapinteger)|false|none||交通方式偏好权重|
|idealDailyAttractionCount|[DailyAttractionCountRange](#schemadailyattractioncountrange)|false|none||理想每日景点数量范围|
|idealPace|string|false|none||理想行程节奏|
|recommendedAccommodationTypes|[string]|false|none||推荐住宿类型|
|uniqueItineraryElements|[string]|false|none||特色行程元素|
|similarPersonalityTypes|[string]|false|none||推荐的相似人格类型|
|completedTimestamp|integer(int64)|false|none||人格测试完成时间戳|
|generatedItineraryId|string|false|none||生成的行程计划ID（如果直接生成行程）|

<h2 id="tocS_MultimodalInput">MultimodalInput</h2>

<a id="schemamultimodalinput"></a>
<a id="schema_MultimodalInput"></a>
<a id="tocSmultimodalinput"></a>
<a id="tocsmultimodalinput"></a>

```json
{
  "inputType": "string",
  "textInput": "string",
  "imageData": "string",
  "imageType": "string",
  "audioData": "string",
  "audioType": "string",
  "userPreferences": {
    "key": {}
  },
  "constraints": {
    "key": {}
  },
  "additionalNotes": "string",
  "language": "string",
  "needImageRecommendations": true,
  "needAudioGuide": true,
  "outputFormat": "string",
  "historicalTravelIds": [
    "string"
  ],
  "referenceImages": [
    "string"
  ],
  "textIntentType": "string",
  "geoContext": {
    "key": 0
  },
  "timeContext": {
    "key": {}
  },
  "confidenceScore": 0
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|inputType|string|false|none||输入类型<br />可选值：text（文本）、image（图片）、audio（语音）、mixed（混合）|
|textInput|string|false|none||文本输入内容<br />如："我想去成都玩3天，预算1500元，想看大熊猫和都江堰"|
|imageData|string|false|none||图片输入数据<br />Base64编码的图片数据|
|imageType|string|false|none||图片类型<br />如：jpg、png、bmp等|
|audioData|string|false|none||音频输入数据<br />Base64编码的音频数据|
|audioType|string|false|none||音频类型<br />如：mp3、wav等|
|userPreferences|[MapObject](#schemamapobject)|false|none||用户偏好信息<br />可包含：旅行风格、偏好景点类型、交通方式等|
|constraints|[MapObject](#schemamapobject)|false|none||约束条件<br />如：预算、天数、必去景点等|
|additionalNotes|string|false|none||补充说明信息<br />用户可以提供额外的说明|
|language|string|false|none||语言类型<br />如：zh-CN（简体中文）、en-US（美式英语）等|
|needImageRecommendations|boolean|false|none||是否需要图片推荐|
|needAudioGuide|boolean|false|none||是否需要语音解说|
|outputFormat|string|false|none||期望输出格式<br />可选值：detailed（详细）、brief（简洁）、visual（视觉化）|
|historicalTravelIds|[string]|false|none||历史旅行记录ID列表<br />用于参考用户的历史旅行偏好|
|referenceImages|[string]|false|none||用户上传的参考图片列表<br />多个参考图片的Base64编码数据|
|textIntentType|string|false|none||文本输入的意图类型<br />如：plan（规划）、query（查询）、adjust（调整）|
|geoContext|[MapDouble](#schemamapdouble)|false|none||地理上下文信息<br />如：当前位置、最近访问的地点等|
|timeContext|[MapObject](#schemamapobject)|false|none||时间上下文信息<br />如：当前时间、季节等|
|confidenceScore|number|false|none||输入的置信度<br />对于自动识别的输入内容的置信度评分|

<h2 id="tocS_MultimodalResult">MultimodalResult</h2>

<a id="schemamultimodalresult"></a>
<a id="schema_MultimodalResult"></a>
<a id="tocSmultimodalresult"></a>
<a id="tocsmultimodalresult"></a>

```json
{
  "resultId": "string",
  "itinerary": {
    "planName": "string",
    "city": "string",
    "days": 0,
    "totalBudget": 0,
    "estimatedCost": 0,
    "accommodationSuggestion": "string",
    "dailyItineraries": [
      {
        "day": 0,
        "attractions": [
          {
            "name": null,
            "lat": null,
            "lng": null
          }
        ],
        "routes": [
          {
            "duration": null,
            "distance": null,
            "mode": null,
            "estimatedCost": null,
            "steps": null,
            "polyline": null,
            "startName": null,
            "endName": null
          }
        ],
        "dailyCost": 0,
        "weather": "string",
        "suggestions": [
          "string"
        ]
      }
    ],
    "additionalInfo": {
      "key": {}
    },
    "planType": "string"
  },
  "recommendedImages": [
    {
      "key": "string"
    }
  ],
  "audioGuides": [
    {
      "key": "string"
    }
  ],
  "matchScore": 0,
  "recommendationReasons": [
    "string"
  ],
  "highlightTags": [
    "string"
  ],
  "visualizationData": {
    "key": {}
  },
  "presentationOrder": [
    "string"
  ],
  "generatedTime": 0,
  "primaryModality": "string",
  "supportedInteractions": [
    "string"
  ],
  "additionalInformation": {
    "key": {}
  },
  "resultSummary": "string",
  "alternativeResults": [
    {
      "resultId": "string",
      "itinerary": {
        "planName": "string",
        "city": "string",
        "days": 0,
        "totalBudget": 0,
        "estimatedCost": 0,
        "accommodationSuggestion": "string",
        "dailyItineraries": [
          {
            "day": null,
            "attractions": null,
            "routes": null,
            "dailyCost": null,
            "weather": null,
            "suggestions": null
          }
        ],
        "additionalInfo": {
          "key": {}
        },
        "planType": "string"
      },
      "recommendedImages": [
        {
          "key": "string"
        }
      ],
      "audioGuides": [
        {
          "key": "string"
        }
      ],
      "matchScore": 0,
      "recommendationReasons": [
        "string"
      ],
      "highlightTags": [
        "string"
      ],
      "visualizationData": {
        "key": {}
      },
      "presentationOrder": [
        "string"
      ],
      "generatedTime": 0,
      "primaryModality": "string",
      "supportedInteractions": [
        "string"
      ],
      "additionalInformation": {
        "key": {}
      },
      "resultSummary": "string",
      "alternativeResults": [
        {
          "resultId": "string",
          "itinerary": {
            "planName": null,
            "city": null,
            "days": null,
            "totalBudget": null,
            "estimatedCost": null,
            "accommodationSuggestion": null,
            "dailyItineraries": null,
            "additionalInfo": null,
            "planType": null
          },
          "recommendedImages": [
            {}
          ],
          "audioGuides": [
            {}
          ],
          "matchScore": 0,
          "recommendationReasons": [
            "string"
          ],
          "highlightTags": [
            "string"
          ],
          "visualizationData": {
            "key": null
          },
          "presentationOrder": [
            "string"
          ],
          "generatedTime": 0,
          "primaryModality": "string",
          "supportedInteractions": [
            "string"
          ],
          "additionalInformation": {
            "key": null
          },
          "resultSummary": "string",
          "alternativeResults": [
            {}
          ],
          "resultType": "string",
          "userFeedbackScore": 0,
          "applicableScenarios": [
            "string"
          ],
          "mediaResourceCount": {
            "key": null
          }
        }
      ],
      "resultType": "string",
      "userFeedbackScore": 0,
      "applicableScenarios": [
        "string"
      ],
      "mediaResourceCount": {
        "key": 0
      }
    }
  ],
  "resultType": "string",
  "userFeedbackScore": 0,
  "applicableScenarios": [
    "string"
  ],
  "mediaResourceCount": {
    "key": 0
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|resultId|string|false|none||结果ID|
|itinerary|[ItineraryPlanVO](#schemaitineraryplanvo)|false|none||行程规划结果|
|recommendedImages|[[MapString](#schemamapstring)]|false|none||推荐图片列表<br />每个元素包含图片URL和描述|
|audioGuides|[[MapString](#schemamapstring)]|false|none||语音解说列表<br />每个元素包含音频URL和描述|
|matchScore|number|false|none||结果匹配度<br />表示该结果与用户需求的匹配程度，范围0-1|
|recommendationReasons|[string]|false|none||推荐理由|
|highlightTags|[string]|false|none||亮点标签|
|visualizationData|[MapObject](#schemamapobject)|false|none||视觉化展示数据<br />用于生成图表或地图可视化|
|presentationOrder|[string]|false|none||多媒体展示顺序<br />指导前端如何排序展示不同模态的内容|
|generatedTime|integer(int64)|false|none||结果生成时间|
|primaryModality|string|false|none||主要输入模态<br />表示主要基于哪种模态生成的结果|
|supportedInteractions|[string]|false|none||支持的交互方式<br />如：edit（编辑）、share（分享）、save（保存）|
|additionalInformation|[MapObject](#schemamapobject)|false|none||补充信息<br />其他可能有用的信息|
|resultSummary|string|false|none||结果摘要<br />简短描述结果的核心内容|
|alternativeResults|[[MultimodalResult](#schemamultimodalresult)]|false|none||可替代方案列表<br />其他可能的行程建议|
|resultType|string|false|none||结果类型<br />如：complete（完整行程）、partial（部分行程）、suggestion（建议）|
|userFeedbackScore|number|false|none||用户反馈评分<br />用户对该结果的评分，范围1-5|
|applicableScenarios|[string]|false|none||适用场景<br />该结果适合的旅行场景描述|
|mediaResourceCount|[MapInteger](#schemamapinteger)|false|none||媒体资源统计<br />包含的图片、音频等资源数量|
