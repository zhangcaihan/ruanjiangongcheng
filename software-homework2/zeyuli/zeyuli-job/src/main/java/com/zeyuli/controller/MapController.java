package com.zeyuli.controller;

import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.Point;
import com.zeyuli.pojo.bo.Location;
import com.zeyuli.pojo.bo.Route;
import com.zeyuli.service.MapService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 地图服务控制器
 * 处理地图相关的HTTP请求
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/map")
// todo 仅测试用
@CrossOrigin
public class MapController {

    @Autowired
    private MapService mapService;

    @ApiOperation(value = "获取地图中心点", notes = "根据位置获取地图中心点坐标")
    @GetMapping("/center")
    public Point getMapCenter(@RequestParam String location) {
        return mapService.getMapCenter(location);
    }

    @ApiOperation(value = "搜索位置", notes = "根据查询关键词搜索位置")
    @GetMapping("/search-locations")
    public List<Location> searchLocations(@RequestParam String query,
                                          @RequestParam String region,
                                          @RequestParam(required = false, defaultValue = "10") int count) {
        // 如果调用时没有提供 count 参数，默认返回10个结果
        return mapService.searchLocations(query, region, count);
    }

    @ApiOperation(value = "获取路线", notes = "获取两地之间的路线规划")
    @GetMapping("/route")
    public Route getRoute(@RequestParam String origin,
                          @RequestParam String destination,
                          @RequestParam(defaultValue = "driving") String mode) {
        return mapService.getRoute(origin, destination, mode);
    }

    @ApiOperation(value = "获取周边POI", notes = "获取指定位置周边的兴趣点")
    @GetMapping("/surrounding-pois")
    public List<POI> getSurroundingPOIs(@RequestParam String location,
                                        @RequestParam String radius,
                                        @RequestParam String types) {
        return mapService.getSurroundingPOIs(location, radius, types);
    }

    @ApiOperation(value = "获取地图截图", notes = "获取指定区域的地图截图")
    @GetMapping("/screenshot")
    public String getMapScreenshot(@RequestParam String center,
                                   @RequestParam String zoom,
                                   @RequestParam String width,
                                   @RequestParam String height) {
        return mapService.getMapScreenshot(center, zoom, width, height);
    }

    @ApiOperation(value = "获取天气信息", notes = "获取指定城市的天气信息")
    @GetMapping("/weather")
    public String getWeatherInfo(@RequestParam String cityCode) {
        return mapService.getWeatherInfo(cityCode);
    }

    @ApiOperation(value = "根据坐标获取地址", notes = "根据经纬度坐标获取详细地址信息")
    @GetMapping("/address-by-location")
    public String getAddressByLocation(@RequestParam double lat,
                                       @RequestParam double lng) {
        return mapService.getAddressByLocation(lat, lng);
    }

    @ApiOperation(value = "获取两点距离", notes = "计算两个地点之间的距离")
    @GetMapping("/distance")
    public double getDistance(@RequestParam String origin,
                              @RequestParam String destination) {
        return mapService.getDistance(origin, destination);
    }

    @ApiOperation(value = "获取景点详情", notes = "获取指定景点的详细信息")
    @GetMapping("/poi-details/{poiId}")
    public Map<String, Object> getPOIDetails(@PathVariable String poiId) {
        return mapService.getPOIDetails(poiId);
    }

    @ApiOperation(value = "获取城市景点", notes = "获取指定城市的景点列表")
    @GetMapping("/attractions-by-city")
    public List<POI> getAttractionsByCity(@RequestParam String city,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int pageSize) {
        return mapService.getAttractionsByCity(city, page, pageSize);
    }

    @ApiOperation(value = "获取POI详情", notes = "获取指定POI的详细信息列表")
    @GetMapping("/poi-details-list")
    public Map<String, Object> getPOIDetails() {
        return mapService.getPOIDetailsList();
    }

    @ApiOperation(value = "获取cityCode", notes = "获取城市编码")
    @GetMapping("/city-code")
    public String getCityCode(@RequestParam String cityName) {
        return mapService.getCityCode(cityName);
    }
}