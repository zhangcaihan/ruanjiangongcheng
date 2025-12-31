package com.zeyuli.service;

import com.zeyuli.pojo.bo.StudentRoute;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 学生专属线路服务接口
 * 提供学生专属旅行线路的相关服务
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
public interface StudentRouteService {
    
    /**
     * 获取推荐的学生专属线路列表
     * 
     * @param city 城市名称
     * @param studentType 学生类型
     * @param days 行程天数
     * @param limit 返回数量限制
     * @return 学生专属线路列表
     */
    List<StudentRoute> getRecommendedStudentRoutes(String city, String studentType, int days, int limit);
    
    /**
     * 根据条件查询学生专属线路
     * 
     * @param city 城市名称（可选）
     * @param minBudget 最低预算（可选）
     * @param maxBudget 最高预算（可选）
     * @param days 行程天数（可选）
     * @param tags 标签筛选（可选）
     * @param studentType 学生类型（可选）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 查询结果和分页信息
     */
    Map<String, Object> searchStudentRoutes(String city, Double minBudget, Double maxBudget, 
                                          Integer days, List<String> tags, String studentType, 
                                          int page, int pageSize);
    
    /**
     * 根据ID获取学生专属线路详情
     * 
     * @param routeId 线路ID
     * @return 学生专属线路详情
     */
    StudentRoute getStudentRouteById(String routeId);
    
    /**
     * 获取热门学生专属线路
     * 
     * @param limit 返回数量限制
     * @return 热门学生专属线路列表
     */
    List<StudentRoute> getHotStudentRoutes(int limit);
    
    /**
     * 获取季节性推荐线路
     * 
     * @param season 季节（春季、夏季、秋季、冬季）
     * @param limit 返回数量限制
     * @return 季节性推荐线路列表
     */
    List<StudentRoute> getSeasonalRecommendedRoutes(String season, int limit);
    
    /**
     * 获取学生专属优惠信息
     * 
     * @param city 城市名称
     * @param attractionType 景点类型（可选）
     * @return 学生专属优惠信息列表
     */
    List<StudentRoute.DiscountInfo> getStudentDiscounts(String city, String attractionType);
    
    /**
     * 增加线路浏览次数
     * 
     * @param routeId 线路ID
     * @return 更新后的浏览次数
     */
    int incrementViewCount(String routeId);
    
    /**
     * 收藏或取消收藏学生专属线路
     * 
     * @param routeId 线路ID
     * @param userId 用户ID
     * @param isFavorite 是否收藏
     * @return 操作结果
     */
    boolean toggleFavorite(String routeId, String userId, boolean isFavorite);
    
    /**
     * 为学生专属线路评分
     * 
     * @param routeId 线路ID
     * @param userId 用户ID
     * @param rating 评分（1-5分）
     * @return 更新后的平均评分
     */
    double rateStudentRoute(String routeId, String userId, double rating);
    
    /**
     * 根据用户偏好推荐学生线路
     * 
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 个性化推荐的学生线路列表
     */
    List<StudentRoute> getPersonalizedRecommendedRoutes(String userId, int limit);
    
    /**
     * 获取学生友好型住宿推荐
     * 
     * @param city 城市名称
     * @param priceRange 价格范围（可选）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 学生友好型住宿列表
     */
    List<StudentRoute.StudentFriendlyHotel> getStudentFriendlyHotels(String city, 
                                                                   Map<String, Double> priceRange, 
                                                                   int page, int pageSize);
    
    /**
     * 获取线路可用的学生专属优惠
     * 
     * @param routeId 线路ID
     * @return 线路可用的学生专属优惠列表
     */
    List<StudentRoute.DiscountInfo> getAvailableDiscountsForRoute(String routeId);
    
    /**
     * 对比多条学生专属线路
     * 
     * @param routeIds 线路ID列表
     * @return 线路对比结果
     */
    Map<String, Object> compareStudentRoutes(List<String> routeIds);
    
    /**
     * 根据人格测试结果推荐学生线路
     * 
     * @param personalityType 人格类型
     * @param city 城市（可选）
     * @param limit 返回数量限制
     * @return 推荐的学生线路列表
     */
    List<StudentRoute> getPersonalityBasedStudentRoutes(String personalityType, String city, int limit);
}