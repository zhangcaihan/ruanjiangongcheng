package com.zeyuli.controller;

import com.zeyuli.pojo.bo.StudentRoute;
import com.zeyuli.service.StudentRouteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生专属线路控制器
 * 处理学生专属旅行线路相关的HTTP请求
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/student-route")
// todo 仅测试用
@CrossOrigin
public class StudentRouteController {

    @Autowired
    private StudentRouteService studentRouteService;

    @ApiOperation(value = "获取推荐的学生专属线路", notes = "根据城市、学生类型和行程天数获取推荐的学生专属线路")
    @GetMapping("/recommended")
    public List<StudentRoute> getRecommendedStudentRoutes(@RequestParam String city,
                                                         @RequestParam String studentType,
                                                         @RequestParam int days,
                                                         @RequestParam(defaultValue = "5") int limit) {
        return studentRouteService.getRecommendedStudentRoutes(city, studentType, days, limit);
    }

    @ApiOperation(value = "搜索学生专属线路", notes = "根据条件查询学生专属线路，支持分页")
    @GetMapping("/search")
    public Map<String, Object> searchStudentRoutes(@RequestParam(required = false) String city,
                                                 @RequestParam(required = false) Double minBudget,
                                                 @RequestParam(required = false) Double maxBudget,
                                                 @RequestParam(required = false) Integer days,
                                                 @RequestParam(required = false) List<String> tags,
                                                 @RequestParam(required = false) String studentType,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return studentRouteService.searchStudentRoutes(city, minBudget, maxBudget, days, tags, studentType, page, pageSize);
    }

    @ApiOperation(value = "获取线路详情", notes = "根据ID获取学生专属线路的详细信息")
    @GetMapping("/detail/{routeId}")
    public StudentRoute getStudentRouteById(@PathVariable String routeId) {
        return studentRouteService.getStudentRouteById(routeId);
    }

    @ApiOperation(value = "获取热门线路", notes = "获取热门的学生专属线路列表")
    @GetMapping("/hot")
    public List<StudentRoute> getHotStudentRoutes(@RequestParam(defaultValue = "10") int limit) {
        return studentRouteService.getHotStudentRoutes(limit);
    }

    @ApiOperation(value = "获取季节性推荐线路", notes = "根据季节获取推荐的学生专属线路")
    @GetMapping("/seasonal")
    public List<StudentRoute> getSeasonalRecommendedRoutes(@RequestParam String season,
                                                          @RequestParam(defaultValue = "5") int limit) {
        return studentRouteService.getSeasonalRecommendedRoutes(season, limit);
    }

    @ApiOperation(value = "获取学生专属优惠", notes = "获取指定城市的学生专属优惠信息")
    @GetMapping("/discounts")
    public List<StudentRoute.DiscountInfo> getStudentDiscounts(@RequestParam String city,
                                                             @RequestParam(required = false) String attractionType) {
        return studentRouteService.getStudentDiscounts(city, attractionType);
    }

    @ApiOperation(value = "增加浏览次数", notes = "增加指定线路的浏览次数")
    @PostMapping("/view-count/{routeId}")
    public int incrementViewCount(@PathVariable String routeId) {
        return studentRouteService.incrementViewCount(routeId);
    }

    @ApiOperation(value = "收藏/取消收藏线路", notes = "收藏或取消收藏指定的学生专属线路")
    @PostMapping("/favorite")
    public boolean toggleFavorite(@RequestParam String routeId,
                                 @RequestParam String userId,
                                 @RequestParam boolean isFavorite) {
        return studentRouteService.toggleFavorite(routeId, userId, isFavorite);
    }

    @ApiOperation(value = "线路评分", notes = "为指定的学生专属线路评分")
    @PostMapping("/rate")
    public double rateStudentRoute(@RequestParam String routeId,
                                  @RequestParam String userId,
                                  @RequestParam double rating) {
        return studentRouteService.rateStudentRoute(routeId, userId, rating);
    }

    @ApiOperation(value = "个性化推荐", notes = "根据用户偏好推荐学生线路")
    @GetMapping("/personalized/{userId}")
    public List<StudentRoute> getPersonalizedRecommendedRoutes(@PathVariable String userId,
                                                             @RequestParam(defaultValue = "5") int limit) {
        return studentRouteService.getPersonalizedRecommendedRoutes(userId, limit);
    }

    @ApiOperation(value = "获取学生友好型住宿", notes = "获取指定城市的学生友好型住宿推荐")
    @GetMapping("/hotels")
    public List<StudentRoute.StudentFriendlyHotel> getStudentFriendlyHotels(@RequestParam String city,
                                                                           @RequestParam(required = false) Map<String, Double> priceRange,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int pageSize) {
        return studentRouteService.getStudentFriendlyHotels(city, priceRange, page, pageSize);
    }

    @ApiOperation(value = "获取线路可用优惠", notes = "获取指定线路可用的学生专属优惠")
    @GetMapping("/{routeId}/discounts")
    public List<StudentRoute.DiscountInfo> getAvailableDiscountsForRoute(@PathVariable String routeId) {
        return studentRouteService.getAvailableDiscountsForRoute(routeId);
    }

    @ApiOperation(value = "对比线路", notes = "对比多条学生专属线路")
    @PostMapping("/compare")
    public Map<String, Object> compareStudentRoutes(@RequestBody List<String> routeIds) {
        return studentRouteService.compareStudentRoutes(routeIds);
    }

    @ApiOperation(value = "根据人格测试结果推荐", notes = "根据人格测试结果推荐学生线路")
    @GetMapping("/personality-based")
    public List<StudentRoute> getPersonalityBasedStudentRoutes(@RequestParam String personalityType,
                                                              @RequestParam(required = false) String city,
                                                              @RequestParam(defaultValue = "5") int limit) {
        return studentRouteService.getPersonalityBasedStudentRoutes(personalityType, city, limit);
    }
}