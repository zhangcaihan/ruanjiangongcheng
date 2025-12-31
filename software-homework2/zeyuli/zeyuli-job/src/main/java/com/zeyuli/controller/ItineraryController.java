package com.zeyuli.controller;

import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.Route;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import com.zeyuli.service.ItineraryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 行程规划控制器
 * 处理行程规划相关的HTTP请求
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/itinerary")
// todo 仅测试用
@CrossOrigin
public class ItineraryController {
    
    @Autowired
    private ItineraryService itineraryService;
    
    @ApiOperation(value = "按预算规划行程", notes = "根据预算金额生成优化的行程方案")
    @PostMapping("/plan-by-budget")
    public ItineraryPlanVO planItineraryByBudget(@RequestParam String city,
                                              @RequestParam int days,
                                              @RequestParam double budget,
                                              @RequestBody(required = false) Map<String, Object> preferences) {
        return itineraryService.planItineraryByBudget(city, days, budget, preferences);
    }
    
    @ApiOperation(value = "根据条件调整行程", notes = "根据天气、交通等情况重新规划行程")
    @PostMapping("/adjust-by-condition")
    public ItineraryPlanVO adjustItineraryByCondition(@RequestBody ItineraryPlanVO originalPlan,
                                                   @RequestParam String weatherCondition,
                                                   @RequestParam String trafficCondition) {
        return itineraryService.adjustItineraryByCondition(originalPlan, weatherCondition, trafficCondition);
    }
    
    @ApiOperation(value = "生成人格化行程", notes = "根据用户人格类型生成专属行程")
    @GetMapping("/generate-personality")
    public ItineraryPlanVO generatePersonalityItinerary(@RequestParam String city,
                                                     @RequestParam int days,
                                                     @RequestParam String personalityType) {
        return itineraryService.generatePersonalityItinerary(city, days, personalityType);
    }
    
    @ApiOperation(value = "获取学生专属行程", notes = "获取针对高校学生的专属行程")
    @GetMapping("/student-itinerary")
    public ItineraryPlanVO getStudentItinerary(@RequestParam String university,
                                             @RequestParam int days,
                                             @RequestParam double maxBudget) {
        return itineraryService.getStudentItinerary(university, days, maxBudget);
    }
    
    @ApiOperation(value = "生成搭子行程", notes = "根据旅行搭子人格生成个性化行程")
    @GetMapping("/generate-companion")
    public ItineraryPlanVO generateCompanionItinerary(@RequestParam String city,
                                                   @RequestParam int days,
                                                   @RequestParam String companionType) {
        return itineraryService.generateCompanionItinerary(city, days, companionType);
    }
    
    @ApiOperation(value = "计算行程费用", notes = "计算指定景点和路线的总费用")
    @PostMapping("/calculate-cost")
    public double calculateTotalCost(@RequestBody List<POI> attractions,
                                   @RequestBody List<Route> routes,
                                   @RequestParam double accommodationCost) {
        return itineraryService.calculateTotalCost(attractions, routes, accommodationCost);
    }
    
    @ApiOperation(value = "优化景点选择", notes = "根据预算优化景点选择")
    @GetMapping("/optimize-attractions")
    public List<POI> optimizeAttractionsByBudget(@RequestParam String city,
                                              @RequestParam double budget,
                                              @RequestParam int days) {
        return itineraryService.optimizeAttractionsByBudget(city, budget, days);
    }
    
    @ApiOperation(value = "优化交通方式", notes = "优化交通方式以降低成本")
    @GetMapping("/optimize-transportation")
    public Route optimizeTransportation(@RequestParam String origin,
                                      @RequestParam String destination,
                                      @RequestParam String currentMode,
                                      @RequestParam double costLimit) {
        return itineraryService.optimizeTransportation(origin, destination, currentMode, costLimit);
    }
}