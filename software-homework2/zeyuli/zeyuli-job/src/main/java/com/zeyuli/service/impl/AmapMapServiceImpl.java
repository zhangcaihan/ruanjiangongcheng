package com.zeyuli.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeyuli.enm.POIEnum;
import com.zeyuli.pojo.bo.*;
import com.zeyuli.pojo.bo.Route;
import com.zeyuli.pojo.vo.*;
import com.zeyuli.service.MapService;
import com.zeyuli.util.JsonUtil;
import com.zeyuli.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 高德地图服务实现
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service("amapMapService")
@Slf4j
public class AmapMapServiceImpl implements MapService {

    private static final String KEY = "0f874a8f530e4c8b18ed3b197e682be6"; // 需要替换为实际的高德地图API密钥
    private static final String BASE_URL = "https://restapi.amap.com/v3";
    private static final String DIRECTION_URL = "https://restapi.amap.com/v4/direction";
    private static final String DIRECTION_URL_DRIVING = "https://restapi.amap.com/v3/direction";
    private static final String WEATHER_URL = "https://restapi.amap.com/v3/weather/weatherInfo";
    private static final String POI_DETAIL_URL = "https://restapi.amap.com/v3/place/detail";

    // 获取API密钥的方法，便于未来实现密钥管理
    private String getKey() {
        return KEY;
    }

    /**
     * 高德返回结果：
     * <pre>
     *     {@code
     *     {
     *   "status" : "1",
     *   "info" : "OK",
     *   "infocode" : "10000",
     *   "count" : "1",
     *   "geocodes" : [ {
     *     "formatted_address" : "江苏省南京市",
     *     "country" : "中国",
     *     "province" : "江苏省",
     *     "citycode" : "025",
     *     "city" : "南京市",
     *     "district" : [ ],
     *     "township" : [ ],
     *     "neighborhood" : {
     *       "name" : [ ],
     *       "type" : [ ]
     *     },
     *     "building" : {
     *       "name" : [ ],
     *       "type" : [ ]
     *     },
     *     "adcode" : "320100",
     *     "street" : [ ],
     *     "number" : [ ],
     *     "location" : "118.796624,32.059344",
     *     "level" : "市"
     *   } ]
     * }}
     * </pre>
     *
     * @param location 地址
     * @return : com.zeyuli.pojo.bo.Point
     * @author : 李泽聿
     * @since : 2025-12-08 08:20
     */
    @Override
    public Point getMapCenter(String location) {
        try {
            // 地理编码API：将地址转换为坐标
            String url = BASE_URL + "/geocode/geo?address=" + URLEncoder.encode(location, StandardCharsets.UTF_8) + "&key=" + getKey();
            String result = httpGet(url);
            if (result.contains("location")) {
                ObjectMapper mapper = new ObjectMapper();
                GaodeAddress gaodeAddress = mapper.readValue(result, GaodeAddress.class);
                // 获取location
                Geocodes geocodes = gaodeAddress.getGeocodes().get(0);
                // 解析location
                return geocodes.analyzeLatAndLon();
            }
        } catch (Exception e) {
            log.error("获取地图中心点失败: {}", e.getMessage());
        }
        return null;
    }

    public String getCityCode(String cityName) {
        try {
            // 城市编码API：将城市名称转换为城市编码
            String url = BASE_URL + "/geocode/geo?address=" + URLEncoder.encode(cityName, StandardCharsets.UTF_8) + "&key=" + getKey();
            String result = httpGet(url);
            if (result.contains("location")) {
                ObjectMapper mapper = new ObjectMapper();
                GaodeAddress gaodeAddress = mapper.readValue(result, GaodeAddress.class);
                // 获取location
                Geocodes geocodes = gaodeAddress.getGeocodes().get(0);
                // 解析location
                return JsonUtil.text(geocodes.getAdcode());
            }
        } catch (Exception e) {
            log.error("获取城市编码失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 在区域内根据关键字搜素
     *
     * @param query  搜索关键词
     * @param region 区域
     * @return : java.util.List<com.zeyuli.pojo.bo.Location>
     * @author : 李泽聿
     * @since : 2025-12-08 08:56
     */
    @Override
    public List<Location> searchLocations(String query, String region) {
        List<Location> locations = new ArrayList<>();
        try {
            // 搜索POI
            String url = BASE_URL + "/place/text?keywords=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&region=" +
                    URLEncoder.encode(region, StandardCharsets.UTF_8) + "&key=" + getKey();
            String result = httpGet(url);
            log.info("搜索位置结果: {}", url);
            ObjectMapper mapper = new ObjectMapper();
            POIAddress poiAddress = mapper.readValue(result, POIAddress.class);

            String cityCode = getCityCode(region);
            List<Pois> pois = poiAddress.getPois();
            for (Pois poi : pois) {
                Location location = new Location();
                // 解析名称
                location.setName(poi.getName());
                // 分别设置经纬度
                String[] latAndLon = poi.getLocation().split(",");
                location.setLat(Double.parseDouble(latAndLon[0]));
                location.setLng(Double.parseDouble(latAndLon[1]));
                // 设置地址
                location.setAddress(poi.getAddress());
                // 设置类型
                location.setType(poi.getType());
                // 设置城市编码
                location.setCityCode(cityCode);
                locations.add(location);
            }
        } catch (Exception e) {
            log.error("搜索位置失败: {}", e.getMessage());
        }
        return locations;
    }

    /**
     * 根据名称搜索位置，并限制数量
     *
     * @param query  搜索关键词
     * @param region 区域
     * @param coount 搜索数量
     * @return : java.util.List<com.zeyuli.pojo.bo.Location>
     * @author : 李泽聿
     * @since : 2025-12-09 19:24
     */
    @Override
    public List<Location> searchLocations(String query, String region, int coount) {
        List<Location> locations = searchLocations(query, region);
        if (locations.size() > coount) {
            return locations.subList(0, coount);
        }
        System.out.println(query+locations);
        return locations;
    }

    /**
     *
     * @param origin      起始地点（经度，纬度）
     * @param destination 结束地点（经度，纬度）
     * @param mode        交通方式（仅walking,transit,bicycling,driving）可选，默认{@code driving}
     * @return : com.zeyuli.pojo.bo.Route
     * @author : 李泽聿
     * @since : 2025-12-10 19:52
     */
    @Override
    public Route getRoute(String origin, String destination, String mode) {
        Route route = new Route();
        log.info("获取路径规划: {} -> {}", origin, destination);

        try {
            // 1. 构造 URL
            String type = switch (mode.toLowerCase()) {
                case "walking" -> "walking";
                case "transit" -> "transit/integrated";
                case "bicycling" -> "bicycling";
                default -> "driving";
            };

            // 将城市解析成经纬度
            Point originPoint = getMapCenter(origin);
            Point destinationPoint = getMapCenter(destination);



            if (originPoint == null || destinationPoint == null) {
                log.warn("路径规划失败：无法解析起点或终点");
                return route;
            }

            String url;
            if (type.equals("driving")) {
                // 驾车路径规划API
                url = DIRECTION_URL_DRIVING + "/" + type +
                        "?origin=" + originPoint.formatLatAndLng() +
                        "&destination=" + destinationPoint.formatLatAndLng() +
                        "&key=" + getKey();

            } else {
                url = DIRECTION_URL + "/" + type +
                        "?origin=" + originPoint.formatLatAndLng() +
                        "&destination=" + destinationPoint.formatLatAndLng() +
                        "&key=" + getKey();
            }


            String json = httpGet(url);
            log.info("路径规划URL: {}", url);

            // 2. 解析JSON响应
            ObjectMapper mapper = new ObjectMapper();
            GetRouteVo jsonResponse = mapper.readValue(json, GetRouteVo.class);

            // 检查错误码
            if (jsonResponse.getErrcode() != 0) {
                log.error("路径规划API返回错误: {}, {}", jsonResponse.getErrcode(), jsonResponse.getErrmsg());
                return route;
            }

            // 3. 提取路径数据
            GetRouteData data = jsonResponse.getData();

            if (type.equals("driving")) {
                Paths paths = jsonResponse.getRouteVo().getPaths().get(0);
                route.setMode(mode);
                route.setDistance(paths.getDistance());
                route.setDuration(paths.getDuration());

                // 设置起点和终点名称
                route.setStartName(origin);
                route.setEndName(destination);

                // 转换Steps为RouteStep并构建完整polyline
                List<RouteStep> routeSteps = new ArrayList<>();
                StringBuilder fullPolyline = new StringBuilder();

                List<Steps> steps = paths.getSteps();
                for (Steps step : steps) {
                    // 创建RouteStep
                    setRouteStep(mode, routeSteps, step);

                    // 构建完整polyline
                    if (step.getPolyline() != null && !step.getPolyline().isEmpty()) {
                        if (!fullPolyline.isEmpty()) {
                            fullPolyline.append(";");
                        }
                        fullPolyline.append(step.getPolyline());
                    }
                }

                // ：将转换后的步骤和折线设置到返回对象中
                route.setSteps(routeSteps);
                route.setPolyline(fullPolyline.toString());

                // 计算预估费用
                double estimatedCost = calculateEstimatedCost(mode, paths.getDistance());
                route.setEstimatedCost(estimatedCost);

                log.info("【驾车】路径规划成功，距离: {}米，时长: {}秒", route.getDistance(), route.getDuration());

            } else {
                Paths firstPath = data.getPaths().get(0);

                // 4. 填充Route对象
                route.setMode(mode);
                route.setDistance(firstPath.getDistance());
                route.setDuration(firstPath.getDuration());

                // 设置起点和终点名称（这里需要根据实际情况获取，暂时设置为坐标）
                route.setStartName(origin);
                route.setEndName(destination);

                // 5. 转换Steps为RouteStep并构建完整polyline
                List<RouteStep> routeSteps = new ArrayList<>();
                StringBuilder fullPolyline = new StringBuilder();

                List<Steps> steps = firstPath.getSteps();
                for (Steps step : steps) {
                    // 创建RouteStep
                    setRouteStep(mode, routeSteps, step);

                    // 构建完整polyline
                    if (step.getPolyline() != null && !step.getPolyline().isEmpty()) {
                        if (!fullPolyline.isEmpty()) {
                            fullPolyline.append(";");
                        }
                        fullPolyline.append(step.getPolyline());
                    }
                }

                route.setSteps(routeSteps);
                route.setPolyline(fullPolyline.toString());

                // 6. 计算预估费用（根据不同的交通方式）
                double estimatedCost = calculateEstimatedCost(mode, firstPath.getDistance());
                route.setEstimatedCost(estimatedCost);

                log.info("路径规划成功，距离: {}米，时长: {}秒", route.getDistance(), route.getDuration());

            }

        } catch (Exception e) {
            log.error("获取路线失败", e);
        }

        return route;
    }

    private void setRouteStep(String mode, List<RouteStep> routeSteps, Steps step) {
        RouteStep routeStep = new RouteStep();
        routeStep.setInstruction(step.getInstruction());
        routeStep.setDistance(step.getDistance());
        routeStep.setDuration(step.getDuration());
        routeStep.setMode(mode);
        routeStep.setCost(0.0); // 驾车路径规划无费用

        routeSteps.add(routeStep);
    }

    /**
     * 根据交通方式和距离计算预估费用
     */
    private double calculateEstimatedCost(String mode, double distance) {
        return switch (mode.toLowerCase()) {
            case "driving" -> distance * 0.002; // 假设每公里2元
            case "transit" -> Math.min(10.0, distance * 0.003); // 公交/地铁，上限10元
            default -> 0.0; // 步行和骑行免费
        };
    }

    /**
     * 获取周边的相关POI情况
     *
     * @param location 当前位置(必须提供经纬度)
     * @param radius   半径(单位：米)
     * @param types    POI类型{@link com.zeyuli.enm.POIEnum}(前端返回格式：190203|190204)
     * @return : java.util.List<com.zeyuli.pojo.bo.POI>
     * @author : 李泽聿
     * @since : 2025-12-08 11:12
     */
    @Override
    // todo 待实现
    public List<POI> getSurroundingPOIs(String location, String radius, String types) {
        List<POI> pois = new ArrayList<>();
        try {
            // 周边搜索API
            String url = BASE_URL + "/place/around?location=" + location + "&radius=" + radius +
                    "&types=" + types + "&key=" + getKey();
            String result = httpGet(url);
            // 解析JSON响应，简化处理
            log.info("周边POI搜索结果: {}", result);
            // 实际实现需要解析JSON并构建POI对象
        } catch (Exception e) {
            log.error("获取周边POI失败: {}", e.getMessage());
        }
        return pois;
    }

    /**
     * 获取高德地图静态截图
     *
     * @param center 地图中心点坐标，格式：经度,纬度（例如：116.473168,39.993015）
     * @param zoom   缩放级别，范围：1-17（1为世界，17为街道）
     * @param width  图片宽度，范围：1-1024（像素）
     * @param height 图片高度，范围：1-1024（像素）
     * @return 静态地图图片的URL
     * @author : 李泽聿
     * @since : 2025-12-09 19:35
     */
    @Override
    public String getMapScreenshot(String center, String zoom, String width, String height) {
        try {
            // 参数验证
            if (StringUtils.isEmpty(center) || StringUtils.isEmpty(zoom)
                    || StringUtils.isEmpty(width) || StringUtils.isEmpty(height)) {
                log.error("静态地图参数不能为空");
                return "";
            }

            // 验证坐标格式
            if (!center.matches("^\\d+(\\.\\d+)?,\\d+(\\.\\d+)?$")) {
                log.error("中心点坐标格式错误: {}", center);
                return "";
            }

            // 验证缩放级别（1-17）
            int zoomInt = Integer.parseInt(zoom);
            if (zoomInt < 1 || zoomInt > 17) {
                log.error("缩放级别超出范围（1-17）: {}", zoom);
                return "";
            }

            // 验证图片尺寸（1-1024）
            int widthInt = Integer.parseInt(width);
            int heightInt = Integer.parseInt(height);
            if (widthInt < 1 || widthInt > 1024 || heightInt < 1 || heightInt > 1024) {
                log.error("图片尺寸超出范围（1-1024）: {}x{}", width, height);
                return "";
            }

            // 构建静态地图API URL
            String url = String.format("%s/staticmap?location=%s&zoom=%s&size=%s*%s&key=%s",
                    BASE_URL,
                    URLEncoder.encode(center, StandardCharsets.UTF_8),
                    URLEncoder.encode(zoom, StandardCharsets.UTF_8),
                    URLEncoder.encode(width, StandardCharsets.UTF_8),
                    URLEncoder.encode(height, StandardCharsets.UTF_8),
                    getKey()
            );

            log.info("静态地图URL: {}", url);

            // 可选：验证URL是否有效（可以发送HEAD请求检查）
            if (isUrlValid(url)) {
                return url;
            } else {
                log.error("静态地图URL无效");
                return "";
            }

        } catch (NumberFormatException e) {
            log.error("参数格式转换失败: {}", e.getMessage());
            return "";
        } catch (Exception e) {
            log.error("获取地图截图失败: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * 验证URL是否有效
     */
    private boolean isUrlValid(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            return responseCode == 200; // HTTP 200 OK
        } catch (Exception e) {
            log.warn("URL验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取城市天气信息
     *
     * @param cityCode 城市码（可以是城市编码或城市名称）
     * @return 天气信息JSON字符串，或null（失败时）
     * @author : 李泽聿
     * @since : 2025-12-09 19:50
     */
    @Override
    public String getWeatherInfo(String cityCode) {
        try {
            // 参数验证
            if (StringUtils.isBlank(cityCode)) {
                log.error("城市码不能为空");
                return "城市码不能为空";
            }

            // 清理参数
            cityCode = cityCode.trim();

            // 判断cityCode是编码还是城市名，并进行相应处理
            String encodedCity = URLEncoder.encode(cityCode, StandardCharsets.UTF_8);

            // 构建URL - 实时天气API
            String url = WEATHER_URL + "?city=" + encodedCity + "&key=" + getKey() + "&extensions=base";
            log.info("天气API请求URL: {}", url);

            // 发送HTTP请求
            String response = httpGet(url);

            if (StringUtils.isBlank(response)) {
                log.error("天气API返回空响应");
                return "网络错误";
            }

            log.info("天气API返回: {}", response);

            // 解析响应，验证状态
            ObjectMapper mapper = new ObjectMapper();
            WeatherInfoVo jsonResponse = mapper.readValue(response, WeatherInfoVo.class);
            String status = jsonResponse.getStatus();

            if (!"1".equals(status)) {
                String info = jsonResponse.getInfo();
                String infocode = jsonResponse.getInfocode();
                log.error("天气API调用失败: {}, 错误码: {}", info, infocode);
            }

            // 返回完整的响应（可以是JSON字符串，也可以处理后返回）
            List<LivesVo> lives = jsonResponse.getLives();
            return lives.get(0).getWeather();

        } catch (Exception e) {
            log.error("获取天气信息失败: {}", e.getMessage(), e);
        }
        return "获取天气信息失败";
    }


    /**
     * 计算交通费用
     *
     * @param mode     交通方式
     * @param distance 距离（米）
     * @param duration 时长（秒）
     * @return 预估费用
     */
    private double calculateCost(String mode, double distance, double duration) {
        return switch (mode.toLowerCase()) {
            case "driving" ->
                // 驾车费用估算：0.8元/公里
                    (distance / 1000) * 0.8;
            case "transit" ->
                // 公共交通费用估算：固定费用+距离费用
                    3 + (distance / 10000) * 2;
            case "walking", "bicycling" -> 0;
            default -> 0;
        };
    }

    /**
     * 发送HTTP GET请求
     *
     * @param urlString URL地址
     * @return 响应内容
     */
    private String httpGet(String urlString) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }

    /**
     *
     * @param lat 维度
     * @param lng 经度
     * @return : java.lang.String
     * @author : 李泽聿
     * @since : 2025-12-09 21:01
     */
    @Override
    public String getAddressByLocation(double lat, double lng) {
        try {
            // 格式化为高德API要求的精度（通常6位小数足够）
            String location = String.format("%.6f,%.6f", lat, lng);

            // 构建URL - 逆地理编码API
            String url = String.format("%s/geocode/regeo?location=%s&key=%s&extensions=%s&poitype=%s&radius=%s&roadlevel=%s",
                    BASE_URL,
                    URLEncoder.encode(location, StandardCharsets.UTF_8),
                    getKey(),
                    "base",       // 默认返回基本地址信息
                    "all",        // 返回所有类型的POI
                    "1000",       // 搜索半径（米）
                    "0"           // 道路等级：0-全部，1-高速公路，2-国道，3-省道，4-县道
            );

            log.info("逆地理编码API请求URL: {}", url.replace(getKey(), "***")); // 安全日志

            // 发送HTTP请求
            String response = httpGet(url);

            if (StringUtils.isBlank(response)) {
                log.error("逆地理编码API返回空响应");
                return "网络错误";
            }

            log.debug("逆地理编码API返回: {}", response);

            // 解析响应，验证状态
            ObjectMapper mapper = new ObjectMapper();
            ReverseCodingVo res = mapper.readValue(response, ReverseCodingVo.class);

            if (!"1".equals(res.getStatus())) {
                String info = res.getInfo();
                String infocode = res.getInfocode();
                log.error("逆地理编码API调用失败: {}, 错误码: {}", info, infocode);
                return "网络错误";
            }

            // 完整地址
            return res.getRegeocode().getFormatted_address();

        } catch (Exception e) {
            log.error("获取地址信息失败: {}", e.getMessage(), e);
            return "网络错误";
        }
    }

    /**
     *
     * @param origin      经度,纬度（例如：116.473168,39.993015）
     * @param destination 经度,纬度（例如：116.473168,39.993015）
     * @return : double 计算的距离，单位：米，-1表示失败
     * @author : 李泽聿
     * @since : 2025-12-09 21:20
     */
    @Override
    public double getDistance(String origin, String destination) {
        try {
            // 参数验证
            if (StringUtils.isBlank(origin) || StringUtils.isBlank(destination)) {
                log.error("起点或终点不能为空");
                return -1;
            }
            System.out.println(origin+","+destination);

            // 这里使用type=1（驾车路径距离），因为直线距离太简单，驾车距离更实用
            String url = String.format("%s/distance?origins=%s&destination=%s&type=%d&key=%s",
                    BASE_URL,
                    URLEncoder.encode(origin, StandardCharsets.UTF_8),
                    URLEncoder.encode(destination, StandardCharsets.UTF_8),
                    1,  // 驾车距离
                    getKey()
            );
            String result = httpGet(url);
            // 解析JSON响应，简化处理
            ObjectMapper mapper = new ObjectMapper();
            DistanceVo jsonResponse = mapper.readValue(result, DistanceVo.class);
            log.info("距离API返回: {}", jsonResponse.getResults().get(0).getDistance());
            return Double.parseDouble(jsonResponse.getResults().get(0).getDistance());
        } catch (Exception e) {
            log.error("获取距离失败: {}", e.getMessage());
            return -1;
        }
    }

    /**
     * @param poiId POI的ID
     * @return POI详情的Map对象
     * @author : 李泽聿
     * @since : 2025-12-10 08:30
     */
    @Override
    public Map<String, Object> getPOIDetails(String poiId) {
        Map<String, Object> details = new HashMap<>();
        try {
            // 添加extensions=all参数获取详细信息
            String url = POI_DETAIL_URL + "?id=" + poiId + "&key=" + getKey() + "&extensions=all";
            String result = httpGet(url);

            log.info("POI详情结果: {}", result);
            ObjectMapper mapper = new ObjectMapper();
            POIDetailsVo jsonResponse = mapper.readValue(result, POIDetailsVo.class);

            // 检查API响应状态
            if (!"1".equals(jsonResponse.getStatus())) {
                return getDefaultDetails(poiId);
            }

            // 获取POI信息
            if (jsonResponse.getPois() != null && !jsonResponse.getPois().isEmpty()) {
                PoisInDetailsVo poi = jsonResponse.getPois().get(0);

                // 设置基本信息
                details.put("id", poi.getId());
                details.put("name", poi.getName());
                details.put("address", poi.getAddress());
                details.put("location", poi.getLocation());
                details.put("type", poi.getType());
                details.put("typecode", poi.getTypecode());

                // 处理电话
                if (poi.getTel() != null && !poi.getTel().isEmpty()) {
                    details.put("tel", String.join(",", poi.getTel()));
                } else {
                    details.put("tel", "暂无电话");
                }

                // 处理biz_ext中的消费和评分信息
                if (poi.getBiz_ext() != null) {
                    // 处理消费信息
                    List<String> costList = poi.getBiz_ext().getCost();
                    if (costList != null && !costList.isEmpty()) {
                        details.put("cost", String.join(",", costList));
                        // 尝试从消费信息中提取价格数字
                        try {
                            String costStr = costList.get(0);
                            String priceNum = costStr.replaceAll("[^0-9]", "");
                            if (!priceNum.isEmpty()) {
                                details.put("price", Integer.parseInt(priceNum));
                            } else {
                                details.put("price", 0);
                            }
                        } catch (Exception e) {
                            details.put("price", 0);
                        }
                    } else {
                        details.put("cost", "暂无消费信息");
                        details.put("price", 0);
                    }

                    // 处理评分
                    List<String> ratingList = poi.getBiz_ext().getRating();
                    if (ratingList != null && !ratingList.isEmpty()) {
                        try {
                            String ratingStr = ratingList.get(0);
                            // 高德评分通常是数字字符串
                            details.put("rating", Double.parseDouble(ratingStr));
                        } catch (NumberFormatException e) {
                            details.put("rating", 0.0);
                        }
                    } else {
                        details.put("rating", 0.0);
                    }
                } else {
                    details.put("cost", "暂无消费信息");
                    details.put("price", 0);
                    details.put("rating", 0.0);
                }

                // 营业时间（高德地图可能没有直接的openingHours字段）
                // 可以尝试从其他字段获取或设置默认值
                details.put("openingHours", "暂无营业时间信息");

                // 其他可能存在的字段
                details.put("cityname", poi.getCityname());
                details.put("adname", poi.getAdname());
                details.put("pname", poi.getPname());

                // 处理照片
                if (poi.getPhotos() != null && !poi.getPhotos().isEmpty()) {
                    List<String> photoUrls = new ArrayList<>();
                    for (PhotosInPoiDetailsVo photo : poi.getPhotos()) {
                        if (photo.getUrl() != null) {
                            photoUrls.add(photo.getUrl());
                        }
                    }
                    details.put("photos", photoUrls);
                }

                // 处理标签
                if (poi.getTag() != null && !poi.getTag().isEmpty()) {
                    details.put("tags", poi.getTag());
                }

            } else {
                log.warn("未找到POI详情信息，ID: {}", poiId);
                return getDefaultDetails(poiId);
            }

        } catch (Exception e) {
            log.error("获取POI详情失败: {}", e.getMessage());
            return getDefaultDetails(poiId);
        }
        return details;
    }

    /**
     * 获取默认的POI详情信息
     */
    private Map<String, Object> getDefaultDetails(String poiId) {
        Map<String, Object> defaultDetails = new HashMap<>();
        defaultDetails.put("id", poiId);
        defaultDetails.put("name", "未知地点");
        defaultDetails.put("address", "地址信息暂缺");
        defaultDetails.put("tel", "暂无电话");
        defaultDetails.put("openingHours", "暂无营业时间信息");
        defaultDetails.put("rating", 0.0);
        defaultDetails.put("price", 0);
        defaultDetails.put("cost", "暂无消费信息");
        defaultDetails.put("location", "");
        return defaultDetails;
    }

    /**
     *
     * @param city     城市名称
     * @param page     页码
     * @param pageSize 每页条数
     * @return : java.util.List<com.zeyuli.pojo.bo.POI>
     * @author : 李泽聿
     * @since : 2025-12-10 10:10
     */
    @Override
    public List<POI> getAttractionsByCity(String city, int page, int pageSize) {
        List<POI> attractions = new ArrayList<>();
        try {
            // 搜索城市的景点，types参数设置为景点类型
            String url = BASE_URL + "/place/text?keywords=景点&types=110000&city=" +
                    URLEncoder.encode(city, StandardCharsets.UTF_8) + "&offset=" + pageSize + "&page=" +
                    page + "&key=" + getKey();
            String result = httpGet(url);
            ObjectMapper mapper = new ObjectMapper();
            AttractionsByCityResultVo jsonResponse = mapper.readValue(result, AttractionsByCityResultVo.class);
            // 实际实现需要解析JSON并构建POI对象列表
            for (Pois i : jsonResponse.getPois()) {
                POI poi = new POI();
                poi.setName(i.getName());
                String[] split = i.getLocation().split(",");
                poi.setLat(Double.parseDouble(split[0]));
                poi.setLng(Double.parseDouble(split[1]));
                attractions.add(poi);
            }
            System.out.println("景点补全 " + attractions);
            return attractions;
        } catch (Exception e) {
            log.error("获取城市景点列表失败: {}", e.getMessage());
        }
        return attractions;
    }

    /**
     * 获取POI类型列表
     *
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     * @author : 李泽聿
     * @since : 2025-12-09 09:55
     */
    @Override
    public Map<String, Object> getPOIDetailsList() {
        ArrayList<Object> res = new ArrayList<>();
        for (POIEnum poiEnum : POIEnum.values()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(poiEnum.getCode(), poiEnum.getName());
            res.add(map);
        }
        return Response.success(res);
    }

}