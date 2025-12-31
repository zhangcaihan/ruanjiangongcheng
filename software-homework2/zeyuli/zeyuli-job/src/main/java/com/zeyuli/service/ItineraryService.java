package com.zeyuli.service;

import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.Route;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 行程规划服务接口
 * 负责实现价格锁定行程、实时智能纠错等核心功能
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
public interface ItineraryService {
    
    /**
     * 根据预算规划最优行程
     * 价格锁定行程功能的核心方法
     *
     * @param city 城市名称
     * @param days 旅行天数
     * @param budget 预算金额
     * @param preferences 用户偏好（可选）
     * @return 优化后的行程方案
     */
    ItineraryPlanVO planItineraryByBudget(String city, int days, double budget, Map<String, Object> preferences);
    
    /**
     * 根据天气/拥堵等情况重新规划行程
     * 实时智能纠错行程功能的核心方法
     *
     * @param originalPlan 原始行程方案
     * @param weatherCondition 天气状况
     * @param trafficCondition 交通状况
     * @return 调整后的行程方案
     */
    ItineraryPlanVO adjustItineraryByCondition(ItineraryPlanVO originalPlan, String weatherCondition, String trafficCondition);
    
    /**
     * 根据用户人格类型生成专属行程
     *
     * @param city 城市名称
     * @param days 旅行天数
     * @param personalityType 人格类型
     * @return 个性化行程方案
     */
    ItineraryPlanVO generatePersonalityItinerary(String city, int days, String personalityType);
    
    /**
     * 获取学生专属线路
     *
     * @param university 高校名称
     * @param days 旅行天数
     * @param maxBudget 最大预算
     * @return 学生专属行程方案
     */
    ItineraryPlanVO getStudentItinerary(String university, int days, double maxBudget);
    
    /**
     * 按照旅行搭子人格生成行程
     *
     * @param city 城市名称
     * @param days 旅行天数
     * @param companionType 搭子类型（小穷游、吃货搭子、懒人搭子、摄影搭子等）
     * @return 人格化行程方案
     */
    ItineraryPlanVO generateCompanionItinerary(String city, int days, String companionType);
    
    /**
     * 计算行程总费用
     *
     * @param attractions 景点列表
     * @param routes 路线列表
     * @param accommodationCost 住宿费用
     * @return 总费用
     */
    double calculateTotalCost(List<POI> attractions, List<Route> routes, double accommodationCost);
    
    /**
     * 根据预算优化景点选择
     *
     * @param city 城市
     * @param budget 预算
     * @param days 天数
     * @return 优化后的景点列表
     */
    List<POI> optimizeAttractionsByBudget(String city, double budget, int days);
    
    /**
     * 优化交通方式以降低成本
     *
     * @param origin 起点
     * @param destination 终点
     * @param currentMode 当前交通方式
     * @param costLimit 费用限制
     * @return 更经济的交通方式和路线
     */
    Route optimizeTransportation(String origin, String destination, String currentMode, double costLimit);
}