package com.zeyuli.service;

import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.Point;
import com.zeyuli.pojo.bo.Location;
import com.zeyuli.pojo.bo.Route;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 地图服务接口
 *
 * @author 李泽聿
 * @since 2025-11-01 10:32
 */
@Service
public interface MapService {
    /**
     * 获取地图中心点
     */
    Point getMapCenter(String location);

    /**
     * 搜索位置
     */
    List<Location> searchLocations(String query, String region);

    /**
     * 返回{@code count}个的搜索结果
     */
    List<Location> searchLocations(String query, String region, int coount);

    /**
     * 获取路线
     */
    Route getRoute(String origin, String destination, String mode);

    /**
     * 获取周边POI
     */
    List<POI> getSurroundingPOIs(String location, String radius, String types);

    /**
     * 获取地图截图
     */
    String getMapScreenshot(String center, String zoom, String width, String height);

    /**
     * 获取天气信息
     */
    String getWeatherInfo(String cityCode);

    /**
     * 根据坐标获取地址信息
     */
    String getAddressByLocation(double lat, double lng);

    /**
     * 获取两点之间的距离
     */
    double getDistance(String origin, String destination);

    /**
     * 获取景点信息，包括门票价格等
     */
    Map<String, Object> getPOIDetails(String poiId);

    /**
     * 根据城市获取景点列表
     */
    List<POI> getAttractionsByCity(String city, int page, int pageSize);

    /**
     * 获取景点列表
     */
    Map<String, Object> getPOIDetailsList();

    String getCityCode(String cityName);
}
