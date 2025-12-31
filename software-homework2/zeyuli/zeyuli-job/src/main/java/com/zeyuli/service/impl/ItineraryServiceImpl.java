package com.zeyuli.service.impl;

import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.Route;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import com.zeyuli.service.ItineraryService;
import com.zeyuli.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 行程规划服务实现类
 * 实现价格锁定行程、实时智能纠错等核心功能
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
@Slf4j
public class ItineraryServiceImpl implements ItineraryService {

    @Autowired
    @Qualifier("amapMapService")
    private MapService mapService;

    @Override
    public ItineraryPlanVO planItineraryByBudget(String city, int days, double budget, Map<String, Object> preferences) {
        log.info("开始价格锁定行程规划：城市={}, 天数={}, 预算={}", city, days, budget);
        
        ItineraryPlanVO plan = new ItineraryPlanVO();
        plan.setCity(city);
        plan.setDays(days);
        plan.setTotalBudget(budget);
        plan.setPlanType("PRICE_LOCKED");
        plan.setPlanName(city + " " + days + "日游（预算" + budget + "元）");
        
        // 1. 计算预算分配
        double accommodationBudget = calculateAccommodationBudget(budget, days);
        double foodBudget = calculateFoodBudget(budget, days);
        double attractionBudget = calculateAttractionBudget(budget);
        double transportationBudget = calculateTransportationBudget(budget);
        double otherBudget = calculateOtherBudget(budget);
        
        // 2. 根据预算选择景点
        List<POI> optimizedAttractions = optimizeAttractionsByBudget(city, attractionBudget, days);
        
        // 3. 规划每日行程
        List<ItineraryPlanVO.DailyItinerary> dailyItineraries = new ArrayList<>();
        double totalCost = 0;
        
        // 为每天分配景点并规划路线
        int attractionsPerDay = Math.max(1, optimizedAttractions.size() / days);
        for (int i = 0; i < days; i++) {
            ItineraryPlanVO.DailyItinerary dailyPlan = new ItineraryPlanVO.DailyItinerary();
            dailyPlan.setDay(i + 1);
            dailyPlan.setWeather("晴朗"); // 模拟天气，实际应从天气API获取
            
            // 分配当天的景点
            List<POI> dayAttractions = new ArrayList<>();
            int startIndex = i * attractionsPerDay;
            int endIndex = Math.min(startIndex + attractionsPerDay, optimizedAttractions.size());
            for (int j = startIndex; j < endIndex; j++) {
                dayAttractions.add(optimizedAttractions.get(j));
            }
            dailyPlan.setAttractions(dayAttractions);
            
            // 规划景点之间的路线，优先使用公共交通
            List<Route> dayRoutes = planDailyRoutes(dayAttractions, "transit", transportationBudget / days);
            dailyPlan.setRoutes(dayRoutes);
            
            // 计算当日费用
            double dailyAttractionCost = calculateAttractionCost(dayAttractions);
            double dailyTransportCost = calculateTransportCost(dayRoutes);
            double dailyFoodCost = foodBudget / days;
            double dailyAccommodationCost = i < days - 1 ? accommodationBudget / days : 0; // 最后一天不住宿
            
            double dailyCost = dailyAttractionCost + dailyTransportCost + dailyFoodCost + dailyAccommodationCost;
            dailyPlan.setDailyCost(dailyCost);
            totalCost += dailyCost;
            
            // 添加当日建议
            List<String> suggestions = new ArrayList<>();
            suggestions.add("推荐使用公共交通出行，经济实惠");
            suggestions.add("午餐可选择当地特色小吃，性价比高");
            if (dayAttractions.size() > 3) {
                suggestions.add("景点较多，建议合理安排时间，避免赶场");
            }
            dailyPlan.setSuggestions(suggestions);
            
            dailyItineraries.add(dailyPlan);
        }
        
        plan.setDailyItineraries(dailyItineraries);
        plan.setEstimatedCost(totalCost);
        
        // 设置住宿建议
        plan.setAccommodationSuggestion(getAccommodationSuggestion(city, accommodationBudget / days));
        
        // 添加附加信息
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("budget_saving_tips", getBudgetSavingTips());
        additionalInfo.put("cost_breakdown", getCostBreakdown(optimizedAttractions, transportationBudget, 
                                                             accommodationBudget, foodBudget, otherBudget));
        plan.setAdditionalInfo(additionalInfo);
        
        log.info("价格锁定行程规划完成，最终费用：{}, 节省：{}", totalCost, budget - totalCost);
        return plan;
    }

    @Override
    public ItineraryPlanVO adjustItineraryByCondition(ItineraryPlanVO originalPlan, String weatherCondition, String trafficCondition) {
        log.info("开始实时智能纠错行程，原计划：{}，天气状况：{}，交通状况：{}", 
                 originalPlan.getPlanName(), weatherCondition, trafficCondition);
        
        // 创建原计划的深拷贝，避免修改原数据
        ItineraryPlanVO adjustedPlan = deepCopyItineraryPlan(originalPlan);
        adjustedPlan.setPlanName(originalPlan.getPlanName() + "(智能调整版)");
        adjustedPlan.setPlanType("ADJUSTED_PLAN");
        
        // 根据天气和交通状况调整每日行程
        List<ItineraryPlanVO.DailyItinerary> dailyItineraries = adjustedPlan.getDailyItineraries();
        for (ItineraryPlanVO.DailyItinerary dailyPlan : dailyItineraries) {
            // 更新天气信息
            dailyPlan.setWeather(weatherCondition);
            
            // 根据天气调整景点
            List<POI> originalAttractions = new ArrayList<>(dailyPlan.getAttractions());
            List<POI> adjustedAttractions = adjustAttractionsByWeather(originalAttractions, weatherCondition);
            dailyPlan.setAttractions(adjustedAttractions);
            
            // 根据交通状况调整路线和交通方式
            List<Route> adjustedRoutes = adjustRoutesByTrafficAndWeather(
                originalAttractions, trafficCondition, weatherCondition);
            dailyPlan.setRoutes(adjustedRoutes);
            
            // 更新建议
            List<String> newSuggestions = generateAdjustedSuggestions(
                weatherCondition, trafficCondition, adjustedAttractions.size());
            dailyPlan.setSuggestions(newSuggestions);
            
            // 重新计算当日费用
            double dailyCost = recalculateDailyCost(dailyPlan, trafficCondition);
            dailyPlan.setDailyCost(dailyCost);
        }
        
        // 重新计算总费用
        double totalCost = 0;
        for (ItineraryPlanVO.DailyItinerary dailyPlan : dailyItineraries) {
            totalCost += dailyPlan.getDailyCost();
        }
        adjustedPlan.setEstimatedCost(totalCost);
        
        // 添加调整信息
        Map<String, Object> adjustmentInfo = new HashMap<>();
        adjustmentInfo.put("weather_condition", weatherCondition);
        adjustmentInfo.put("traffic_condition", trafficCondition);
        adjustmentInfo.put("cost_impact", totalCost - originalPlan.getEstimatedCost());
        adjustmentInfo.put("adjustment_reasons", generateAdjustmentReasons(weatherCondition, trafficCondition));
        adjustmentInfo.put("adaptation_strategies", generateAdaptationStrategies(weatherCondition, trafficCondition));
        
        // 合并附加信息
        Map<String, Object> additionalInfo = adjustedPlan.getAdditionalInfo();
        if (additionalInfo == null) {
            additionalInfo = new HashMap<>();
        }
        additionalInfo.put("adjustment_details", adjustmentInfo);
        adjustedPlan.setAdditionalInfo(additionalInfo);
        
        log.info("实时智能纠错行程完成，调整后的总费用：{}, 变动：{}", 
                 totalCost, totalCost - originalPlan.getEstimatedCost());
        return adjustedPlan;
    }

    @Override
    public ItineraryPlanVO generatePersonalityItinerary(String city, int days, String personalityType) {
        // 三分钟旅行人格测试功能将在后续实现
        return new ItineraryPlanVO();
    }

    @Override
    public ItineraryPlanVO getStudentItinerary(String university, int days, double maxBudget) {
        // 学生专属线路库功能将在后续实现
        return new ItineraryPlanVO();
    }

    @Override
    public ItineraryPlanVO generateCompanionItinerary(String city, int days, String companionType) {
        // AI旅行搭子聊天模式功能将在后续实现
        return new ItineraryPlanVO();
    }

    @Override
    public double calculateTotalCost(List<POI> attractions, List<Route> routes, double accommodationCost) {
        double attractionCost = calculateAttractionCost(attractions);
        double transportCost = calculateTransportCost(routes);
        return attractionCost + transportCost + accommodationCost;
    }

    @Override
    public List<POI> optimizeAttractionsByBudget(String city, double budget, int days) {
        List<POI> optimizedAttractions = new ArrayList<>();
        
        // 获取城市景点列表
        List<POI> allAttractions = mapService.getAttractionsByCity(city, 1, 30);
        
        // 如果API返回为空，使用模拟数据
        if (allAttractions.isEmpty()) {
            allAttractions = generateMockAttractions(city);
        }
        
        // 按门票价格排序，优先选择免费或低价景点
        allAttractions.sort(Comparator.comparing(poi -> getPOITicketPrice(poi)));
        
        // 选择景点，确保总门票费用不超过预算
        double totalTicketCost = 0;
        double avgBudgetPerDay = budget / days;
        
        for (POI attraction : allAttractions) {
            double ticketPrice = getPOITicketPrice(attraction);
            if (totalTicketCost + ticketPrice <= budget) {
                optimizedAttractions.add(attraction);
                totalTicketCost += ticketPrice;
                
                // 每天至少1-3个景点
                if (optimizedAttractions.size() >= days * 3) {
                    break;
                }
            }
        }
        
        log.info("优化后选择了{}个景点，总门票费用：{}", optimizedAttractions.size(), totalTicketCost);
        return optimizedAttractions;
    }

    @Override
    public Route optimizeTransportation(String origin, String destination, String currentMode, double costLimit) {
        // 尝试不同的交通方式，选择费用最低且不超过限制的
        String[] transportModes = {"walking", "bicycling", "transit", "driving", "taxi"};
        
        for (String mode : transportModes) {
            Route route = mapService.getRoute(origin, destination, mode);
            if (route != null && route.getEstimatedCost() <= costLimit) {
                log.info("优化交通方式：从{}改为{}, 费用从{}降至{}", currentMode, mode, 
                         mapService.getRoute(origin, destination, currentMode).getEstimatedCost(), 
                         route.getEstimatedCost());
                return route;
            }
        }
        
        // 如果没有找到合适的，返回当前模式
        return mapService.getRoute(origin, destination, currentMode);
    }
    
    // 辅助方法：计算各类预算分配
    private double calculateAccommodationBudget(double totalBudget, int days) {
        return totalBudget * 0.3; // 住宿占30%
    }
    
    private double calculateFoodBudget(double totalBudget, int days) {
        return totalBudget * 0.25; // 餐饮占25%
    }
    
    private double calculateAttractionBudget(double totalBudget) {
        return totalBudget * 0.2; // 景点门票占20%
    }
    
    private double calculateTransportationBudget(double totalBudget) {
        return totalBudget * 0.15; // 交通占15%
    }
    
    private double calculateOtherBudget(double totalBudget) {
        return totalBudget * 0.1; // 其他费用占10%
    }
    
    // 规划每日路线
    private List<Route> planDailyRoutes(List<POI> attractions, String preferredMode, double dailyBudget) {
        List<Route> routes = new ArrayList<>();
        if (attractions.size() < 2) {
            return routes;
        }
        
        // 按地理距离排序，规划最优路径
        POI currentLocation = attractions.get(0);
        List<POI> remainingAttractions = new ArrayList<>(attractions.subList(1, attractions.size()));
        
        double avgRouteBudget = dailyBudget / (attractions.size() - 1);
        
        while (!remainingAttractions.isEmpty()) {
            POI nearestAttraction = findNearestAttraction(currentLocation, remainingAttractions);
            String origin = currentLocation.getLng() + "," + currentLocation.getLat();
            String destination = nearestAttraction.getLng() + "," + nearestAttraction.getLat();
            
            // 优化交通方式
            Route route = optimizeTransportation(origin, destination, preferredMode, avgRouteBudget);
            routes.add(route);
            
            currentLocation = nearestAttraction;
            remainingAttractions.remove(nearestAttraction);
        }
        
        return routes;
    }
    
    // 查找最近的景点
    private POI findNearestAttraction(POI current, List<POI> attractions) {
        POI nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (POI attraction : attractions) {
            String origin = current.getLng() + "," + current.getLat();
            String destination = attraction.getLng() + "," + attraction.getLat();
            double distance = mapService.getDistance(origin, destination);
            
            if (distance < minDistance) {
                minDistance = distance;
                nearest = attraction;
            }
        }
        
        return nearest;
    }
    
    // 获取POI门票价格
    private double getPOITicketPrice(POI poi) {
        // 实际应从景点详情中获取，这里使用模拟数据
        String name = poi.getName();
        if (name.contains("博物馆") || name.contains("公园")) {
            return Math.random() > 0.5 ? 10 : 0; // 博物馆和公园可能免费或低价
        } else if (name.contains("山") || name.contains("湖")) {
            return 20 + Math.random() * 30; // 自然景观可能需要门票
        } else if (name.contains("寺") || name.contains("庙")) {
            return 15 + Math.random() * 25; // 寺庙可能需要香火钱
        } else if (name.contains("故居") || name.contains("纪念馆")) {
            return 10 + Math.random() * 20; // 故居纪念馆门票
        }
        return 0; // 默认免费
    }
    
    // 生成模拟景点数据
    private List<POI> generateMockAttractions(String city) {
        List<POI> attractions = new ArrayList<>();
        
        // 添加一些免费或低价景点
        addMockPOI(attractions, city + "人民公园", 30.6, 104.0, 0); // 免费公园
        addMockPOI(attractions, city + "博物馆", 30.6, 104.1, 0); // 免费博物馆
        addMockPOI(attractions, city + "老街", 30.7, 104.0, 0); // 免费老街
        addMockPOI(attractions, city + "图书馆", 30.7, 104.1, 0); // 免费图书馆
        addMockPOI(attractions, city + "青年公园", 30.65, 104.05, 0); // 免费公园
        addMockPOI(attractions, city + "文化广场", 30.65, 104.15, 0); // 免费广场
        addMockPOI(attractions, city + "滨江步道", 30.55, 104.0, 0); // 免费步道
        addMockPOI(attractions, city + "动物园", 30.55, 104.1, 30); // 动物园
        addMockPOI(attractions, city + "植物园", 30.75, 104.05, 20); // 植物园
        addMockPOI(attractions, city + "历史街区", 30.75, 104.15, 10); // 历史街区
        
        return attractions;
    }
    
    private void addMockPOI(List<POI> attractions, String name, double lat, double lng, double price) {
        POI poi = new POI();
        poi.setName(name);
        poi.setLat(lat);
        poi.setLng(lng);
        // 这里可以存储价格信息到POI的扩展字段中
        attractions.add(poi);
    }
    
    // 计算各项费用
    private double calculateAttractionCost(List<POI> attractions) {
        double total = 0;
        for (POI poi : attractions) {
            total += getPOITicketPrice(poi);
        }
        return total;
    }
    
    private double calculateTransportCost(List<Route> routes) {
        double total = 0;
        for (Route route : routes) {
            total += route.getEstimatedCost();
        }
        return total;
    }
    
    // 获取住宿建议
    private String getAccommodationSuggestion(String city, double budgetPerNight) {
        if (budgetPerNight < 100) {
            return "建议选择青旅或经济型旅馆，每晚预算" + budgetPerNight + "元以下，推荐位于交通便利的市中心区域";
        } else if (budgetPerNight < 200) {
            return "建议选择经济型连锁酒店，每晚预算" + budgetPerNight + "元左右，推荐靠近地铁站的位置";
        } else {
            return "建议选择舒适型商务酒店，每晚预算" + budgetPerNight + "元左右，可选择景点附近的住宿";
        }
    }
    
    // 获取省钱小贴士
    private List<String> getBudgetSavingTips() {
        List<String> tips = new ArrayList<>();
        tips.add("购买城市一卡通或景点联票，可节省门票费用");
        tips.add("选择公共交通出行，避免打车");
        tips.add("在景区外就餐，价格更实惠");
        tips.add("住宿选择青旅或民宿，性价比更高");
        tips.add("避开节假日和周末出行，价格更优惠");
        tips.add("提前预订可享受更多折扣");
        return tips;
    }
    
    // 获取费用明细
    private ItineraryPlanVO.CostBreakdown getCostBreakdown(List<POI> attractions, double transportationBudget, 
                                                          double accommodationBudget, double foodBudget, double otherBudget) {
        ItineraryPlanVO.CostBreakdown breakdown = new ItineraryPlanVO.CostBreakdown();
        
        double totalTicketCost = 0;
        for (POI poi : attractions) {
            totalTicketCost += getPOITicketPrice(poi);
        }
        
        breakdown.setAttractionTickets(totalTicketCost);
        breakdown.setTransportation(transportationBudget);
        breakdown.setAccommodation(accommodationBudget);
        breakdown.setFood(foodBudget);
        breakdown.setOther(otherBudget);
        
        return breakdown;
    }
    
    // 深拷贝行程计划
    private ItineraryPlanVO deepCopyItineraryPlan(ItineraryPlanVO plan) {
        ItineraryPlanVO copy = new ItineraryPlanVO();
        copy.setPlanName(plan.getPlanName());
        copy.setCity(plan.getCity());
        copy.setDays(plan.getDays());
        copy.setTotalBudget(plan.getTotalBudget());
        copy.setEstimatedCost(plan.getEstimatedCost());
        copy.setPlanType(plan.getPlanType());
        copy.setAccommodationSuggestion(plan.getAccommodationSuggestion());
        
        // 拷贝每日行程
        List<ItineraryPlanVO.DailyItinerary> dailyCopies = new ArrayList<>();
        for (ItineraryPlanVO.DailyItinerary daily : plan.getDailyItineraries()) {
            ItineraryPlanVO.DailyItinerary dailyCopy = new ItineraryPlanVO.DailyItinerary();
            dailyCopy.setDay(daily.getDay());
            dailyCopy.setWeather(daily.getWeather());
            dailyCopy.setDailyCost(daily.getDailyCost());
            dailyCopy.setAttractions(new ArrayList<>(daily.getAttractions()));
            dailyCopy.setRoutes(new ArrayList<>(daily.getRoutes()));
            dailyCopy.setSuggestions(new ArrayList<>(daily.getSuggestions()));
            dailyCopies.add(dailyCopy);
        }
        copy.setDailyItineraries(dailyCopies);
        
        // 拷贝附加信息
        if (plan.getAdditionalInfo() != null) {
            copy.setAdditionalInfo(new HashMap<>(plan.getAdditionalInfo()));
        }
        
        return copy;
    }
    
    // 根据天气调整景点
    private List<POI> adjustAttractionsByWeather(List<POI> originalAttractions, String weatherCondition) {
        List<POI> adjustedAttractions = new ArrayList<>();
        
        for (POI attraction : originalAttractions) {
            // 判断景点是否适合当前天气
            if (isAttractionSuitableForWeather(attraction, weatherCondition)) {
                adjustedAttractions.add(attraction);
            } else {
                log.info("由于天气{}, 移除不适合的景点：{}", weatherCondition, attraction.getName());
            }
        }
        
        // 如果移除了太多景点，添加适合当前天气的替代景点
        int attractionsToAdd = Math.max(0, originalAttractions.size() - adjustedAttractions.size());
        if (attractionsToAdd > 0) {
            List<POI> alternativeAttractions = getAlternativeIndoorAttractions(
                originalAttractions.get(0), attractionsToAdd, weatherCondition);
            adjustedAttractions.addAll(alternativeAttractions);
            log.info("添加了{}个适合当前天气的替代景点", alternativeAttractions.size());
        }
        
        return adjustedAttractions;
    }
    
    // 判断景点是否适合当前天气
    private boolean isAttractionSuitableForWeather(POI attraction, String weatherCondition) {
        String name = attraction.getName();
        
        // 室内景点通常不受天气影响
        if (name.contains("博物馆") || name.contains("图书馆") || 
            name.contains("商场") || name.contains("电影院") || 
            name.contains("购物中心") || name.contains("展览馆") ||
            name.contains("艺术馆") || name.contains("纪念馆")) {
            return true;
        }
        
        // 雨天不适合露天景点
        if (weatherCondition.contains("雨")) {
            if (name.contains("山") || name.contains("湖") || 
                name.contains("公园") || name.contains("广场") || 
                name.contains("海滩") || name.contains("户外")) {
                return false;
            }
        }
        
        // 高温天气不适合长时间户外活动
        if (weatherCondition.contains("高温")) {
            if (name.contains("山") || name.contains("公园") || 
                name.contains("海滩") || name.contains("户外")) {
                return false;
            }
        }
        
        // 大风天气不适合高空景点
        if (weatherCondition.contains("风")) {
            if (name.contains("观景台") || name.contains("高空") || 
                name.contains("缆车") || name.contains("索道")) {
                return false;
            }
        }
        
        return true;
    }
    
    // 获取替代室内景点
    private List<POI> getAlternativeIndoorAttractions(POI referencePoint, int count, String weatherCondition) {
        List<POI> alternatives = new ArrayList<>();
        
        // 生成模拟室内景点数据
        if (weatherCondition.contains("雨") || weatherCondition.contains("高温")) {
            addMockPOI(alternatives, "城市博物馆", referencePoint.getLat() + 0.01, 
                       referencePoint.getLng() + 0.01, 20);
            addMockPOI(alternatives, "现代艺术馆", referencePoint.getLat() + 0.02, 
                       referencePoint.getLng() - 0.01, 30);
            addMockPOI(alternatives, "科技馆", referencePoint.getLat() - 0.01, 
                       referencePoint.getLng() + 0.02, 25);
            addMockPOI(alternatives, "购物中心", referencePoint.getLat() - 0.02, 
                       referencePoint.getLng() - 0.02, 0);
            addMockPOI(alternatives, "历史展览馆", referencePoint.getLat() + 0.03, 
                       referencePoint.getLng() - 0.03, 15);
        } else if (weatherCondition.contains("雪") || weatherCondition.contains("低温")) {
            addMockPOI(alternatives, "室内冰雪乐园", referencePoint.getLat() + 0.01, 
                       referencePoint.getLng() + 0.01, 80);
            addMockPOI(alternatives, "温泉度假区", referencePoint.getLat() + 0.02, 
                       referencePoint.getLng() - 0.01, 150);
            addMockPOI(alternatives, "历史博物馆", referencePoint.getLat() - 0.01, 
                       referencePoint.getLng() + 0.02, 20);
        }
        
        // 如果模拟数据不足，使用更多通用室内景点
        int remaining = count - alternatives.size();
        for (int i = 0; i < remaining; i++) {
            addMockPOI(alternatives, "室内景点" + (i + 1), referencePoint.getLat() + 0.005 * i, 
                       referencePoint.getLng() + 0.005 * i, 10 + i * 5);
        }
        
        // 截取需要的数量
        return alternatives.subList(0, Math.min(count, alternatives.size()));
    }
    
    // 根据交通和天气调整路线
    private List<Route> adjustRoutesByTrafficAndWeather(List<POI> attractions, 
                                                      String trafficCondition, String weatherCondition) {
        List<Route> adjustedRoutes = new ArrayList<>();
        if (attractions.size() < 2) {
            return adjustedRoutes;
        }
        
        // 根据交通状况选择合适的交通方式
        String suitableMode = selectSuitableTransportMode(trafficCondition, weatherCondition);
        
        // 重新规划路线
        for (int i = 0; i < attractions.size() - 1; i++) {
            POI origin = attractions.get(i);
            POI destination = attractions.get(i + 1);
            
            String originStr = origin.getLng() + "," + origin.getLat();
            String destinationStr = destination.getLng() + "," + destination.getLat();
            
            // 获取调整后的路线
            Route adjustedRoute = getAdjustedRoute(originStr, destinationStr, suitableMode, 
                                                 trafficCondition, weatherCondition);
            adjustedRoutes.add(adjustedRoute);
        }
        
        return adjustedRoutes;
    }
    
    // 选择合适的交通方式
    private String selectSuitableTransportMode(String trafficCondition, String weatherCondition) {
        // 拥堵时避免自驾
        if (trafficCondition.contains("拥堵") || trafficCondition.contains("严重拥堵")) {
            return "transit"; // 优先选择公共交通
        }
        
        // 雨天不适合骑行
        if (weatherCondition.contains("雨")) {
            return "transit"; // 优先选择公共交通
        }
        
        // 高温或寒冷天气，考虑出租车
        if (weatherCondition.contains("高温") || weatherCondition.contains("低温") || weatherCondition.contains("雪")) {
            return "taxi"; // 更舒适但费用更高
        }
        
        // 一般情况
        if (trafficCondition.contains("畅通")) {
            return "driving"; // 自驾更自由
        }
        
        return "transit"; // 默认选择公共交通
    }
    
    // 获取调整后的路线
    private Route getAdjustedRoute(String origin, String destination, String mode, 
                                  String trafficCondition, String weatherCondition) {
        // 调用地图服务获取路线
        Route route = mapService.getRoute(origin, destination, mode);
        
        // 根据交通和天气调整路线信息
        if (route != null) {
            // 拥堵时增加预计时间
            if (trafficCondition.contains("拥堵")) {
                double additionalTimeFactor = trafficCondition.contains("严重") ? 1.5 : 1.2;
                route.setDuration(Math.round(route.getDuration() * additionalTimeFactor));
                log.info("由于交通拥堵，路线预计时间调整为：{}分钟", route.getDuration());
            }
            
            // 恶劣天气可能增加费用
            if (weatherCondition.contains("雨") || weatherCondition.contains("雪") || 
                weatherCondition.contains("高温") || weatherCondition.contains("低温")) {
                double additionalCostFactor = 1.1; // 增加10%的预估费用
                route.setEstimatedCost(Math.round(route.getEstimatedCost() * additionalCostFactor * 100) / 100.0);
                log.info("由于恶劣天气，路线预计费用调整为：{}元", route.getEstimatedCost());
            }
            
            // 设置交通方式
            route.setMode(mode);
        }
        
        return route;
    }
    
    // 生成调整后的建议
    private List<String> generateAdjustedSuggestions(String weatherCondition, String trafficCondition, int attractionsCount) {
        List<String> suggestions = new ArrayList<>();
        
        // 天气相关建议
        if (weatherCondition.contains("雨")) {
            suggestions.add("携带雨具，注意路面湿滑");
            suggestions.add("选择室内景点为主，减少淋雨风险");
        } else if (weatherCondition.contains("高温")) {
            suggestions.add("注意防暑降温，多补充水分");
            suggestions.add("尽量避开中午高温时段外出");
            suggestions.add("选择有空调的交通工具");
        } else if (weatherCondition.contains("雪")) {
            suggestions.add("注意保暖，穿着防滑鞋");
            suggestions.add("部分景区可能关闭，提前确认");
        }
        
        // 交通相关建议
        if (trafficCondition.contains("拥堵")) {
            suggestions.add("避开高峰期出行，提前规划路线");
            suggestions.add("建议使用公共交通或地铁");
            suggestions.add("预留充足的路上时间，避免赶场");
        }
        
        // 通用建议
        suggestions.add("随时关注天气和交通状况更新");
        if (attractionsCount > 3) {
            suggestions.add("由于行程调整，景点较多，请合理安排时间");
        }
        
        return suggestions;
    }
    
    // 重新计算每日费用
    private double recalculateDailyCost(ItineraryPlanVO.DailyItinerary dailyPlan, String trafficCondition) {
        // 重新计算景点费用
        double attractionCost = calculateAttractionCost(dailyPlan.getAttractions());
        
        // 重新计算交通费用
        double transportCost = calculateTransportCost(dailyPlan.getRoutes());
        
        // 拥堵时可能增加交通费用
        if (trafficCondition.contains("拥堵")) {
            transportCost *= 1.3; // 拥堵可能增加30%的交通费用
        }
        
        // 餐饮费用
        double foodCost = 100; // 假设每人每天餐饮费用
        
        // 住宿费用（除最后一天外）
        double accommodationCost = dailyPlan.getDay() < 10 ? 200 : 0; // 假设住宿费用
        
        // 其他费用
        double otherCost = 20; // 假设其他费用
        
        return attractionCost + transportCost + foodCost + accommodationCost + otherCost;
    }
    
    // 生成调整原因
    private List<String> generateAdjustmentReasons(String weatherCondition, String trafficCondition) {
        List<String> reasons = new ArrayList<>();
        
        if (weatherCondition.contains("雨")) {
            reasons.add("雨天不适合户外活动，需调整为室内景点");
            reasons.add("路面湿滑，可能影响行程安全");
        } else if (weatherCondition.contains("高温")) {
            reasons.add("高温天气不宜长时间户外活动，需减少户外景点");
            reasons.add("高温可能影响游客体验，需增加休息时间");
        } else if (weatherCondition.contains("雪")) {
            reasons.add("雪天道路湿滑，部分景点可能关闭");
            reasons.add("天气寒冷，需选择更舒适的交通方式");
        }
        
        if (trafficCondition.contains("拥堵")) {
            reasons.add("道路拥堵，自驾或打车时间延长");
            reasons.add("需要选择公共交通或避开拥堵路段");
            reasons.add("拥堵可能导致景点间转移时间增加，影响行程安排");
        }
        
        return reasons;
    }
    
    // 生成适应策略
    private List<String> generateAdaptationStrategies(String weatherCondition, String trafficCondition) {
        List<String> strategies = new ArrayList<>();
        
        if (weatherCondition.contains("雨")) {
            strategies.add("增加室内景点比例，减少户外活动");
            strategies.add("为每个景点之间预留更多转移时间");
            strategies.add("准备雨具和替换衣物");
        } else if (weatherCondition.contains("高温")) {
            strategies.add("调整行程，避开中午高温时段外出");
            strategies.add("增加空调场所停留时间，如商场、博物馆等");
            strategies.add("准备防晒用品和足够饮水");
        }
        
        if (trafficCondition.contains("拥堵")) {
            strategies.add("选择地铁等不受路面交通影响的交通方式");
            strategies.add("将相邻景点安排在同一区域，减少远距离移动");
            strategies.add("早上提前出发，避开高峰期");
        }
        
        return strategies;
    }
}