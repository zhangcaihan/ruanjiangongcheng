package com.zeyuli.service.impl;

import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.Route;
import com.zeyuli.pojo.bo.StudentRoute;
import com.zeyuli.service.MapService;
import com.zeyuli.service.StudentRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生专属线路服务实现类
 * 实现学生专属旅行线路的相关服务
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
@Slf4j
public class StudentRouteServiceImpl implements StudentRouteService {
    
    @Autowired
    @Qualifier("amapMapService")
    private MapService mapService;
    
    // 模拟数据库存储的学生线路
    private final Map<String, StudentRoute> studentRouteMap = new HashMap<>();
    // 模拟数据库存储的标签
    private final Map<String, RouteTag> tagMap = new HashMap<>();
    // 模拟数据库存储的浏览次数
    private final Map<String, Integer> viewCountMap = new HashMap<>();
    // 模拟数据库存储的收藏用户
    private final Map<String, Set<String>> favoriteUserMap = new HashMap<>();
    // 模拟数据库存储的评分
    private final Map<String, List<Double>> ratingMap = new HashMap<>();

    /**
     * 初始化示例数据
     */
    public StudentRouteServiceImpl() {
        initializeMockData();
    }

    /**
     * 初始化示例数据
     */
    private void initSampleData() {
        // 初始化标签
        initTags();
        
        // 初始化线路
        // 这里会在initializeMockData中处理
        
        // 初始化优惠信息
        // 这里会在createMockStudentRoute中处理
    }

    /**
     * 初始化标签数据
     */
    private void initTags() {
        tagMap.put("budget", new RouteTag("budget", "经济实惠", "适合预算有限的学生"));
        tagMap.put("weekend", new RouteTag("weekend", "周末短途", "适合周末出行的短途线路"));
        tagMap.put("nature", new RouteTag("nature", "自然风光", "以自然风光为主的线路"));
        tagMap.put("culture", new RouteTag("culture", "文化历史", "以文化历史景点为主的线路"));
        tagMap.put("adventure", new RouteTag("adventure", "探险刺激", "具有挑战性的探险线路"));
        tagMap.put("food", new RouteTag("food", "美食之旅", "以品尝当地美食为主的线路"));
    }

    /**
     * 创建POI对象
     */
     private POI createPOI(String id, String name, String description, double price, double rating, String openingHours) {
         POI poi = new POI();
         // 移除不存在的setter方法
         poi.setName(name); // 只保留存在的方法
         return poi;
     }

    @Override
    public List<StudentRoute> getRecommendedStudentRoutes(String city, String studentType, int days, int limit) {
        log.info("获取推荐学生线路，城市：{}，学生类型：{}，天数：{}，数量：{}", 
                 city, studentType, days, limit);
        
        // 过滤符合条件的线路
        List<StudentRoute> filteredRoutes = studentRouteMap.values().stream()
                .filter(route -> (city == null || route.getCity().equals(city)) &&
                        (studentType == null || route.getStudentType().equals(studentType)) &&
                        (days <= 0 || route.getDays() == days))
                .collect(Collectors.toList());
        
        // 按评分和浏览量排序
        filteredRoutes.sort((r1, r2) -> {
            double score1 = r1.getRating() * 0.7 + r1.getViewCount() * 0.3;
            double score2 = r2.getRating() * 0.7 + r2.getViewCount() * 0.3;
            return Double.compare(score2, score1);
        });
        
        // 返回限制数量的结果
        return filteredRoutes.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> searchStudentRoutes(String city, Double minBudget, Double maxBudget, 
                                                Integer days, List<String> tags, String studentType, 
                                                int page, int pageSize) {
        log.info("搜索学生线路，城市：{}，预算范围：{}-{}，天数：{}，页码：{}，每页数量：{}", 
                 city, minBudget, maxBudget, days, page, pageSize);
        
        // 过滤符合条件的线路
        List<StudentRoute> filteredRoutes = studentRouteMap.values().stream()
                .filter(route -> {
                    // 城市过滤
                    if (city != null && !route.getCity().equals(city)) {
                        return false;
                    }
                    
                    // 预算过滤
                    if (minBudget != null && route.getMaxBudget() < minBudget) {
                        return false;
                    }
                    if (maxBudget != null && route.getMinBudget() > maxBudget) {
                        return false;
                    }
                    
                    // 天数过滤
                    if (days != null && route.getDays() != days) {
                        return false;
                    }
                    
                    // 学生类型过滤
                    if (studentType != null && !route.getStudentType().equals(studentType)) {
                        return false;
                    }
                    
                    // 标签过滤
                    if (tags != null && !tags.isEmpty()) {
                        boolean hasTag = false;
                        for (String tag : tags) {
                            if (route.getTags().contains(tag)) {
                                hasTag = true;
                                break;
                            }
                        }
                        if (!hasTag) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
        
        // 排序（按评分降序）
        filteredRoutes.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
        
        // 计算分页
        int total = filteredRoutes.size();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<StudentRoute> pageRoutes;
        if (start >= total) {
            pageRoutes = Collections.emptyList();
        } else {
            pageRoutes = filteredRoutes.subList(start, end);
        }
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageRoutes);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("pages", (total + pageSize - 1) / pageSize);
        
        return result;
    }
    
    @Override
    public StudentRoute getStudentRouteById(String routeId) {
        log.info("获取学生线路详情，线路ID：{}", routeId);
        
        StudentRoute route = studentRouteMap.get(routeId);
        if (route != null) {
            // 增加浏览次数
            incrementViewCount(routeId);
            // 更新浏览次数
            route.setViewCount(viewCountMap.getOrDefault(routeId, 0));
        }
        
        return route;
    }
    
    @Override
    public List<StudentRoute> getHotStudentRoutes(int limit) {
        log.info("获取热门学生线路，数量限制：{}", limit);
        
        // 按浏览量和收藏量排序
        return studentRouteMap.values().stream()
                .sorted((r1, r2) -> {
                    double score1 = r1.getViewCount() * 0.6 + r1.getFavoriteCount() * 0.4;
                    double score2 = r2.getViewCount() * 0.6 + r2.getFavoriteCount() * 0.4;
                    return Double.compare(score2, score1);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<StudentRoute> getSeasonalRecommendedRoutes(String season, int limit) {
        log.info("获取季节性推荐线路，季节：{}，数量限制：{}", season, limit);
        
        // 过滤推荐季节包含指定季节的线路
        return studentRouteMap.values().stream()
                .filter(route -> route.getRecommendedSeasons().contains(season))
                .sorted((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<StudentRoute.DiscountInfo> getStudentDiscounts(String city, String attractionType) {
        log.info("获取学生优惠信息，城市：{}，景点类型：{}", city, attractionType);
        
        Set<StudentRoute.DiscountInfo> discounts = new HashSet<>();
        
        // 收集所有符合条件的优惠信息
        for (StudentRoute route : studentRouteMap.values()) {
            if (route.getCity().equals(city)) {
                discounts.addAll(route.getStudentDiscounts());
            }
        }
        
        // 如果指定了景点类型，则进一步过滤
        if (attractionType != null && !attractionType.isEmpty()) {
            discounts = discounts.stream()
                    .filter(discount -> discount.getApplicableTo().contains(attractionType))
                    .collect(Collectors.toSet());
        }
        
        // 按折扣力度排序（从高到低）
        return discounts.stream()
                .filter(StudentRoute.DiscountInfo::isValid)
                .sorted((d1, d2) -> Double.compare(d2.getDiscountValue(), d1.getDiscountValue()))
                .collect(Collectors.toList());
    }
    
    @Override
    public int incrementViewCount(String routeId) {
        log.info("增加线路浏览次数，线路ID：{}", routeId);
        
        int currentCount = viewCountMap.getOrDefault(routeId, 0);
        viewCountMap.put(routeId, currentCount + 1);
        
        // 更新线路对象中的浏览次数
        StudentRoute route = studentRouteMap.get(routeId);
        if (route != null) {
            route.setViewCount(currentCount + 1);
        }
        
        return currentCount + 1;
    }
    
    @Override
    public boolean toggleFavorite(String routeId, String userId, boolean isFavorite) {
        log.info("切换线路收藏状态，线路ID：{}，用户ID：{}，收藏状态：{}", 
                 routeId, userId, isFavorite);
        
        if (!studentRouteMap.containsKey(routeId)) {
            return false;
        }
        
        // 初始化收藏用户集合
        favoriteUserMap.putIfAbsent(routeId, new HashSet<>());
        Set<String> favoriteUsers = favoriteUserMap.get(routeId);
        
        boolean result;
        if (isFavorite) {
            // 添加收藏
            result = favoriteUsers.add(userId);
        } else {
            // 取消收藏
            result = favoriteUsers.remove(userId);
        }
        
        // 更新收藏数量
        if (result) {
            StudentRoute route = studentRouteMap.get(routeId);
            route.setFavoriteCount(favoriteUsers.size());
        }
        
        return result;
    }
    
    @Override
    public double rateStudentRoute(String routeId, String userId, double rating) {
        log.info("为线路评分，线路ID：{}，用户ID：{}，评分：{}", routeId, userId, rating);
        
        if (!studentRouteMap.containsKey(routeId) || rating < 1 || rating > 5) {
            return -1;
        }
        
        // 初始化评分列表（简化实现，不考虑用户重复评分）
        ratingMap.putIfAbsent(routeId, new ArrayList<>());
        List<Double> ratings = ratingMap.get(routeId);
        ratings.add(rating);
        
        // 计算平均分
        double averageRating = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        averageRating = Math.round(averageRating * 10) / 10.0; // 保留一位小数
        
        // 更新线路评分
        StudentRoute route = studentRouteMap.get(routeId);
        route.setRating(averageRating);
        route.setRatingCount(ratings.size());
        
        return averageRating;
    }
    
    @Override
    public List<StudentRoute> getPersonalizedRecommendedRoutes(String userId, int limit) {
        log.info("获取个性化推荐学生线路，用户ID：{}，数量限制：{}", userId, limit);
        
        // 这里可以根据用户历史行为进行个性化推荐
        // 当前简单实现：优先返回已收藏的类似线路
        Set<String> favoriteRoutes = new HashSet<>();
        for (Map.Entry<String, Set<String>> entry : favoriteUserMap.entrySet()) {
            if (entry.getValue().contains(userId)) {
                favoriteRoutes.add(entry.getKey());
            }
        }
        
        List<StudentRoute> recommendedRoutes = new ArrayList<>();
        
        // 如果用户有收藏线路，推荐相似线路
        if (!favoriteRoutes.isEmpty()) {
            // 收集用户收藏线路的标签
            Set<String> userTags = new HashSet<>();
            Set<String> userCities = new HashSet<>();
            
            for (String routeId : favoriteRoutes) {
                StudentRoute route = studentRouteMap.get(routeId);
                if (route != null) {
                    userTags.addAll(route.getTags());
                    userCities.add(route.getCity());
                }
            }
            
            // 基于标签和城市相似度推荐
            List<StudentRoute> similarRoutes = studentRouteMap.values().stream()
                    .filter(route -> !favoriteRoutes.contains(route.getRouteId()))
                    .map(route -> {
                        // 计算相似度分数
                        int commonTags = 0;
                        for (String tag : route.getTags()) {
                            if (userTags.contains(tag)) {
                                commonTags++;
                            }
                        }
                        int commonCity = userCities.contains(route.getCity()) ? 1 : 0;
                        
                        double similarity = (commonTags * 0.7 + commonCity * 0.3) * route.getRating();
                        route.setAdditionalInfo(Map.of("similarity_score", similarity));
                        return route;
                    })
                    .sorted((r1, r2) -> {
                        double score1 = (double) r1.getAdditionalInfo().getOrDefault("similarity_score", 0.0);
                        double score2 = (double) r2.getAdditionalInfo().getOrDefault("similarity_score", 0.0);
                        return Double.compare(score2, score1);
                    })
                    .limit(limit)
                    .collect(Collectors.toList());
            
            recommendedRoutes.addAll(similarRoutes);
        }
        
        // 如果推荐数量不足，补充热门线路
        if (recommendedRoutes.size() < limit) {
            List<StudentRoute> hotRoutes = getHotStudentRoutes(limit - recommendedRoutes.size());
            // 过滤掉已推荐的线路
            Set<String> recommendedIds = recommendedRoutes.stream()
                    .map(StudentRoute::getRouteId)
                    .collect(Collectors.toSet());
            
            hotRoutes = hotRoutes.stream()
                    .filter(route -> !recommendedIds.contains(route.getRouteId()))
                    .collect(Collectors.toList());
            
            recommendedRoutes.addAll(hotRoutes);
        }
        
        return recommendedRoutes;
    }
    
    @Override
    public List<StudentRoute.StudentFriendlyHotel> getStudentFriendlyHotels(String city, 
                                                                         Map<String, Double> priceRange, 
                                                                         int page, int pageSize) {
        log.info("获取学生友好型住宿，城市：{}，页码：{}，每页数量：{}", city, page, pageSize);
        
        Set<StudentRoute.StudentFriendlyHotel> hotels = new HashSet<>();
        
        // 收集所有符合条件的学生友好型酒店
        for (StudentRoute route : studentRouteMap.values()) {
            if (route.getCity().equals(city) && route.getAccommodationInfo() != null) {
                hotels.addAll(route.getAccommodationInfo().getStudentFriendlyHotels());
            }
        }
        
        // 价格过滤
        if (priceRange != null) {
            Double minPrice = priceRange.get("min");
            Double maxPrice = priceRange.get("max");
            
            hotels = hotels.stream()
                    .filter(hotel -> {
                        if (minPrice != null && hotel.getStudentPrice() < minPrice) {
                            return false;
                        }
                        if (maxPrice != null && hotel.getStudentPrice() > maxPrice) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toSet());
        }
        
        // 按学生价格排序
        List<StudentRoute.StudentFriendlyHotel> sortedHotels = hotels.stream()
                .sorted(Comparator.comparing(StudentRoute.StudentFriendlyHotel::getStudentPrice))
                .collect(Collectors.toList());
        
        // 分页
        int total = sortedHotels.size();
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        if (start >= total) {
            return Collections.emptyList();
        }
        
        return sortedHotels.subList(start, end);
    }
    
    @Override
    public List<StudentRoute.DiscountInfo> getAvailableDiscountsForRoute(String routeId) {
        log.info("获取线路可用优惠，线路ID：{}", routeId);
        
        StudentRoute route = studentRouteMap.get(routeId);
        if (route != null) {
            // 只返回有效的优惠
            return route.getStudentDiscounts().stream()
                    .filter(StudentRoute.DiscountInfo::isValid)
                    .collect(Collectors.toList());
        }
        
        return Collections.emptyList();
    }
    
    @Override
    public Map<String, Object> compareStudentRoutes(List<String> routeIds) {
        log.info("对比学生线路，线路ID列表：{}", routeIds);
        
        List<StudentRoute> routes = new ArrayList<>();
        for (String routeId : routeIds) {
            StudentRoute route = studentRouteMap.get(routeId);
            if (route != null) {
                routes.add(route);
            }
        }
        
        // 构建对比结果
        Map<String, Object> result = new HashMap<>();
        result.put("routes", routes);
        
        // 计算各方面的对比数据
        if (!routes.isEmpty()) {
            // 价格对比
            double minPrice = routes.stream().mapToDouble(StudentRoute::getMinBudget).min().orElse(0);
            double maxPrice = routes.stream().mapToDouble(StudentRoute::getMaxBudget).max().orElse(0);
            double avgPrice = routes.stream().mapToDouble(StudentRoute::getAverageCost).average().orElse(0);
            
            result.put("price_comparison", Map.of(
                    "minimum", minPrice,
                    "maximum", maxPrice,
                    "average", avgPrice
            ));
            
            // 评分对比
            Map<String, Double> ratingComparison = new HashMap<>();
            for (StudentRoute route : routes) {
                ratingComparison.put(route.getRouteName(), route.getRating());
            }
            result.put("rating_comparison", ratingComparison);
            
            // 景点数量对比
            Map<String, Integer> attractionCount = new HashMap<>();
            for (StudentRoute route : routes) {
                attractionCount.put(route.getRouteName(), route.getAttractions().size());
            }
            result.put("attraction_count", attractionCount);
        }
        
        return result;
    }
    
    @Override
    public List<StudentRoute> getPersonalityBasedStudentRoutes(String personalityType, String city, int limit) {
        log.info("根据人格类型推荐学生线路，人格类型：{}，城市：{}，数量限制：{}", 
                 personalityType, city, limit);
        
        // 根据人格类型推荐对应的线路类型
        String preferredRouteType = getPreferredRouteTypeByPersonality(personalityType);
        
        // 过滤符合条件的线路
        List<StudentRoute> filteredRoutes = studentRouteMap.values().stream()
                .filter(route -> (city == null || route.getCity().equals(city)) &&
                        (preferredRouteType == null || route.getRouteType().equals(preferredRouteType)))
                .collect(Collectors.toList());
        
        // 按评分排序
        filteredRoutes.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
        
        return filteredRoutes.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    // 获取每周特价优惠线路
    public List<StudentRoute> getWeeklySpecialOffers() {
        log.info("获取每周特价优惠线路");
        // 这里可以实现获取每周特价优惠线路的逻辑
        // 为了演示，返回评分最高的前3条线路
        return getHotStudentRoutes(3);
    }

    // 获取所有可用的优惠信息
    public List<StudentRoute.DiscountInfo> getAllDiscounts() {
        log.info("获取所有可用的优惠信息");
        // 这里可以实现获取所有优惠信息的逻辑
        return new ArrayList<>();
    }

    // 添加新的优惠信息
    public boolean addDiscountInfo(String location, StudentRoute.DiscountInfo discountInfo) {
        log.info("添加新的优惠信息，位置：{}", location);
        // 这里可以实现添加优惠信息的逻辑
        return true;
    }
    
    // 根据人格类型获取推荐的线路类型
    private String getPreferredRouteTypeByPersonality(String personalityType) {
        switch (personalityType) {
            case "ADVENTURER":
                return "自然";
            case "EXPLORER":
                return "探索";
            case "RELAXER":
                return "休闲";
            case "CULTURIST":
                return "文化";
            case "FOODIE":
                return "美食";
            default:
                return null;
        }
    }
    
    // 初始化模拟数据
    private void initializeMockData() {
        // 创建示例线路1：成都文化探索之旅
        createMockStudentRoute(
                "STU_ROUTE_001",
                "成都文化探索之旅",
                "探索成都的历史文化，体验蜀文化魅力",
                "成都",
                3,
                "大学生",
                800,
                1500,
                1200,
                "文化",
                Arrays.asList("文化古迹", "美食", "学生友好", "性价比高"),
                Arrays.asList("春季", "秋季")
        );
        
        // 创建示例线路2：西安历史文化之旅
        createMockStudentRoute(
                "STU_ROUTE_002",
                "西安历史文化之旅",
                "穿越千年，探索古都西安的历史魅力",
                "西安",
                4,
                "大学生",
                1000,
                2000,
                1500,
                "文化",
                Arrays.asList("历史古迹", "博物馆", "学生优惠", "深度体验"),
                Arrays.asList("春季", "秋季")
        );
        
        // 创建示例线路3：杭州西湖休闲之旅
        createMockStudentRoute(
                "STU_ROUTE_003",
                "杭州西湖休闲之旅",
                "漫步西湖，感受杭州的自然风光与人文底蕴",
                "杭州",
                2,
                "大学生",
                600,
                1200,
                900,
                "自然",
                Arrays.asList("自然风光", "休闲度假", "学生友好", "短途旅行"),
                Arrays.asList("春季", "夏季", "秋季")
        );
        
        // 创建示例线路4：北京高校文化之旅
        createMockStudentRoute(
                "STU_ROUTE_004",
                "北京高校文化之旅",
                "参观北京知名高校，感受学术氛围",
                "北京",
                3,
                "大学生",
                900,
                1800,
                1300,
                "文化",
                Arrays.asList("高校", "文化", "学生优惠", "教育意义"),
                Arrays.asList("春季", "秋季")
        );
        
        // 创建示例线路5：上海都市探索之旅
        createMockStudentRoute(
                "STU_ROUTE_005",
                "上海都市探索之旅",
                "探索魔都上海的现代魅力与都市风情",
                "上海",
                3,
                "大学生",
                1200,
                2500,
                1800,
                "都市",
                Arrays.asList("现代建筑", "购物中心", "美食", "学生友好"),
                Arrays.asList("四季皆宜")
        );
    }
    
    // 创建模拟学生线路
    private void createMockStudentRoute(String routeId, String routeName, String description, String city, 
                                     int days, String studentType, double minBudget, double maxBudget, 
                                     double averageCost, String routeType, List<String> tags, 
                                     List<String> recommendedSeasons) {
        StudentRoute route = new StudentRoute();
        route.setRouteId(routeId);
        route.setRouteName(routeName);
        route.setDescription(description);
        route.setCity(city);
        route.setDays(days);
        route.setStudentType(studentType);
        route.setMinBudget(minBudget);
        route.setMaxBudget(maxBudget);
        route.setAverageCost(averageCost);
        route.setRouteType(routeType);
        route.setTags(tags);
        route.setRecommendedSeasons(recommendedSeasons);
        
        // 添加模拟景点
        List<POI> attractions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            POI poi = new POI();
            poi.setName(city + "景点" + (i + 1));
            poi.setLat(30.0 + i * 0.1);
            poi.setLng(104.0 + i * 0.1);
            attractions.add(poi);
        }
        route.setAttractions(attractions);
        
        // 添加模拟路线
        List<Route> routes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Route routeObj = new Route();
            routeObj.setDuration(30 + i * 10);
            routeObj.setDistance(5 + i * 2);
            routeObj.setMode("transit");
            routes.add(routeObj);
        }
        route.setRoutes(routes);
        
        // 添加推荐理由
        route.setRecommendedReasons(Arrays.asList(
                "学生专属优惠，性价比高",
                "行程安排合理，适合学生群体",
                "包含多个学生友好型景点",
                "交通便利，易于规划"
        ));
        
        // 添加学生优惠信息
        List<StudentRoute.DiscountInfo> discounts = new ArrayList<>();
        StudentRoute.DiscountInfo discount = new StudentRoute.DiscountInfo();
        discount.setDiscountName("学生票折扣");
        discount.setDescription("凭学生证享受门票5折优惠");
        discount.setDiscountValue(0.5);
        discount.setApplicableTo("所有景点");
        discount.setRequiredDocuments("学生证");
        discount.setValidFrom(System.currentTimeMillis() - 86400000);
        discount.setValidTo(System.currentTimeMillis() + 31536000000L);
        discount.setValid(true);
        discounts.add(discount);
        route.setStudentDiscounts(discounts);
        
        // 设置默认统计数据
        route.setViewCount(100 + new Random().nextInt(900));
        route.setFavoriteCount(20 + new Random().nextInt(80));
        route.setRating(4.0 + new Random().nextDouble() * 0.9);
        route.setRatingCount(30 + new Random().nextInt(70));
        route.setHot(new Random().nextBoolean());
        route.setRecommended(new Random().nextBoolean());
        
        // 初始化统计数据映射
        viewCountMap.put(routeId, route.getViewCount());
        favoriteUserMap.put(routeId, new HashSet<>());
        ratingMap.put(routeId, new ArrayList<>());
        
        // 添加到存储
        studentRouteMap.put(routeId, route);
    }
    
    // 内部类：线路标签
    public static class RouteTag {
        private String tagId;
        private String tagName;
        private String description;
        
        public RouteTag(String tagId, String tagName, String description) {
            this.tagId = tagId;
            this.tagName = tagName;
            this.description = description;
        }
        
        // Getters and Setters
        public String getTagId() { return tagId; }
        public void setTagId(String tagId) { this.tagId = tagId; }
        public String getTagName() { return tagName; }
        public void setTagName(String tagName) { this.tagName = tagName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}