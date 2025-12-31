package com.zeyuli.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 学生专属线路服务功能测试类
 * 
 * @author 李泽聿
 */
@ExtendWith(MockitoExtension.class)
class StudentRouteVoServiceTest {
    
    @Mock
    private StudentRouteService studentRouteService;
    
    private String userId = "USER_001";
    private String routeId = "ROUTE_001";
    
    @Test
    void getRecommendedStudentRoutes() {
        // 测试获取推荐学生线路
        assertDoesNotThrow(() -> studentRouteService.getRecommendedStudentRoutes(userId, "春季", 0, 10));
    }
    
    @Test
    void searchStudentRoutes() {
        // 测试搜索学生线路
        List<String> tags = new ArrayList<>();
        assertDoesNotThrow(() -> studentRouteService.searchStudentRoutes("杭州", 30.2741, 120.1551, 1000, tags, "地铁", 0, 10));
    }
    
    @Test
    void getStudentRouteById() {
        // 测试获取线路详情
        assertDoesNotThrow(() -> studentRouteService.getStudentRouteById(routeId));
    }
    
    @Test
    void getHotStudentRoutes() {
        // 测试获取热门学生线路
        assertDoesNotThrow(() -> studentRouteService.getHotStudentRoutes(10));
    }
    
    @Test
    void getSeasonalRecommendedRoutes() {
        // 测试获取季节性推荐线路
        assertDoesNotThrow(() -> studentRouteService.getSeasonalRecommendedRoutes("春季", 5));
    }
    
    @Test
    void getStudentDiscounts() {
        // 测试获取学生优惠
        assertDoesNotThrow(() -> studentRouteService.getStudentDiscounts(userId, routeId));
    }
    
    @Test
    void incrementViewCount() {
        // 测试增加浏览次数
        assertDoesNotThrow(() -> studentRouteService.incrementViewCount(routeId));
    }
    
    @Test
    void toggleFavorite() {
        // 测试切换收藏状态
        assertDoesNotThrow(() -> studentRouteService.toggleFavorite(userId, routeId, true));
    }
    
    @Test
    void rateStudentRoute() {
        // 测试评分线路
        assertDoesNotThrow(() -> studentRouteService.rateStudentRoute(userId, routeId, 5));
    }
    
    @Test
    void getPersonalizedRecommendedRoutes() {
        // 测试获取个性化推荐线路
        assertDoesNotThrow(() -> studentRouteService.getPersonalizedRecommendedRoutes(userId, 10));
    }
    
    @Test
    void getStudentFriendlyHotels() {
        // 测试获取学生友好型酒店
        Map<String, Double> location = new HashMap<>();
        location.put("latitude", 30.2741);
        location.put("longitude", 120.1551);
        assertDoesNotThrow(() -> studentRouteService.getStudentFriendlyHotels(userId, location, 1000, 10));
    }
    
    @Test
    void getAvailableDiscountsForRoute() {
        // 测试获取线路可用优惠
        assertDoesNotThrow(() -> studentRouteService.getAvailableDiscountsForRoute(routeId));
    }
    
    @Test
    void compareStudentRoutes() {
        // 测试比较学生线路
        assertDoesNotThrow(() -> studentRouteService.compareStudentRoutes(List.of(routeId, "ROUTE_002")));
    }
    
    @Test
    void getPersonalityBasedStudentRoutes() {
        // 测试获取基于个性的学生线路
        assertDoesNotThrow(() -> studentRouteService.getPersonalityBasedStudentRoutes(userId, "冒险型", 10));
    }
}