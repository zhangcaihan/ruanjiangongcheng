package com.zeyuli.service.impl;

import com.zeyuli.mappers.UserMapper;
import com.zeyuli.pojo.User;
import com.zeyuli.pojo.bo.Location;
import com.zeyuli.pojo.bo.POI;
import com.zeyuli.pojo.bo.PersonalityTest;
import com.zeyuli.pojo.bo.Route;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import com.zeyuli.pojo.vo.PersonalityTestResultVO;
import com.zeyuli.service.MapService;
import com.zeyuli.service.PersonalityTestService;
import com.zeyuli.service.UserService;
import com.zeyuli.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * 旅行人格测试服务实现类
 * 实现三分钟旅行人格测试相关功能
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
@Slf4j
public class PersonalityTestServiceImpl implements PersonalityTestService {

    @Autowired
    @Qualifier("amapMapService")
    private MapService mapService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;
    // 初始化人格测试数据
    private final PersonalityTest personalityTest;

    // 初始化人格类型映射
    private final Map<String, PersonalityTest.PersonalityType> personalityTypeMap;
    
    public PersonalityTestServiceImpl() {
        this.personalityTest = initializePersonalityTest();
        this.personalityTypeMap = initializePersonalityTypeMap();
    }
    
    @Override
    public PersonalityTest getPersonalityTest() {
        return personalityTest;
    }
    
    @Override
    public PersonalityTestResultVO calculateTestResult(Map<String, String> userAnswers) {

        log.info("开始计算旅行人格测试结果，用户答案数量：{}", userAnswers.size());
        String token = userAnswers.get("token");
        String id = jwtUtil.getUserInfo(token)[0];
        User user = userMapper.selectUserInfo(id);
        if (user==null
        || jwtUtil.isExpiration(token)){
            return null;
        }
        PersonalityTestResultVO result = new PersonalityTestResultVO();
        result.setTestId(personalityTest.getTestId());
        result.setUserAnswers(userAnswers);
        
        // 计算各项人格特质得分
        Map<String, Integer> traitScores = calculateTraitScores(userAnswers);
        result.setTraitScores(traitScores);
        
        // 确定主导人格类型
        String dominantType = determineDominantPersonalityType(traitScores);
        result.setDominantPersonalityType(dominantType);
        int i = userMapper.updateUserInfo(id, dominantType);
        if (i==0){
            return null;
        }

        // 获取人格类型详情

        PersonalityTest.PersonalityType personalityType = personalityTypeMap.get(dominantType);
        if (personalityType != null) {
            result.setDominantPersonalityDescription(personalityType.getDescription());
            result.setTravelPreference(personalityType.getTravelPreference());
            result.setAttractionPreferences(personalityType.getRecommendedAttractionTypes());
            result.setTransportationPreferences(personalityType.getRecommendedTransportationTypes());
            
            // 设置每日景点数量范围
            PersonalityTestResultVO.DailyAttractionCountRange countRange = new PersonalityTestResultVO.DailyAttractionCountRange();
            countRange.setMin(personalityType.getDailyAttractionCountRange().getMin());
            countRange.setMax(personalityType.getDailyAttractionCountRange().getMax());
            result.setIdealDailyAttractionCount(countRange);
            
            result.setIdealPace(personalityType.getDailyPace());
            result.setRecommendedAccommodationTypes(personalityType.getRecommendedAccommodationTypes());
            result.setUniqueItineraryElements(personalityType.getUniqueItineraryElements());
        }
        
        // 设置旅行风格标签
        result.setTravelStyleTags(generateTravelStyleTags(traitScores));
        
        // 设置个性化旅行建议
        result.setPersonalizedTips(generatePersonalizedTips(dominantType));
        
        // 设置相似人格类型
        result.setSimilarPersonalityTypes(findSimilarPersonalityTypes(traitScores, dominantType));
        
        // 设置时间戳
        result.setCompletedTimestamp(System.currentTimeMillis());
        
        log.info("旅行人格测试结果计算完成，主导人格类型：{}", dominantType);
        return result;
    }
    
    @Override
    public ItineraryPlanVO generatePersonalityItinerary(String city, int days, String personalityType) {
        log.info("开始根据人格类型生成推荐行程，城市：{}，天数：{}，人格类型：{}", 
                 city, days, personalityType);
        
        // 验证人格类型
        if (!isValidPersonalityType(personalityType)) {
            log.warn("无效的人格类型：{}", personalityType);
            // 返回默认行程或抛出异常
            return generateDefaultItinerary(city, days);
        }
        
        // 获取人格类型详情
        PersonalityTest.PersonalityType type = personalityTypeMap.get(personalityType);
        ItineraryPlanVO plan = new ItineraryPlanVO();
        plan.setPlanName(city + " " + days + "日游（" + type.getTypeName() + "风格）");
        plan.setCity(city);
        plan.setDays(days);
        plan.setPlanType("PERSONALITY_BASED");
        
        // 根据人格类型设置预算（不同人格类型的消费水平可能不同）
        double totalBudget = calculateBudgetByPersonality(type);
        plan.setTotalBudget(totalBudget);
        
        // 生成符合人格特质的景点列表
        List<POI> personalityAttractions = generatePersonalityAttractions(city, type, days);
        for(POI personalityAttraction : personalityAttractions) {
            personalityAttraction.setName(city+personalityAttraction.getName());
        }
        // 规划每日行程
        List<ItineraryPlanVO.DailyItinerary> dailyItineraries = new ArrayList<>();
        double totalCost = 0;
        
        // 根据人格类型决定每日景点数量
        int minAttractionsPerDay = type.getDailyAttractionCountRange().getMin();
        int maxAttractionsPerDay = type.getDailyAttractionCountRange().getMax();

        // 1. 先复制总景点列表，用于动态移除已分配的景点（避免重复）
        List<POI> remainingAttractions = new ArrayList<>(personalityAttractions);
        // 2. 遍历天数分配景点
        for (int i = 0; i < days; i++) {
            ItineraryPlanVO.DailyItinerary dailyPlan = new ItineraryPlanVO.DailyItinerary();
            dailyPlan.setDay(i + 1);
            dailyPlan.setWeather(mapService.getWeatherInfo(city));

            // 根据人格类型选择当日景点数量
            int attractionsCount = selectDailyAttractionsCount(minAttractionsPerDay, maxAttractionsPerDay, i, days);
            // 修正：从剩余景点中动态分配，而非固定索引
            List<POI> dayAttractions = new ArrayList<>();

            // 安全校验：剩余景点数 < 需分配数时，取全部剩余
            int actualCount = Math.min(attractionsCount, remainingAttractions.size());
            if (actualCount > 0) {
                // 核心优化：按类型均衡选当日景点（避免单日类型集中）
                dayAttractions = selectBalancedDailyAttractions(remainingAttractions, actualCount, type.getRecommendedAttractionTypes(), i);
                // 从剩余列表中移除已分配的景点（避免重复）
                remainingAttractions.removeAll(dayAttractions);
            }

            dailyPlan.setAttractions(dayAttractions);

            // 后续逻辑（交通/路线/费用等）保持不变...
            String preferredTransport = "driving";
            List<Route> dayRoutes = planDailyRoutesByPersonality(dayAttractions, preferredTransport, type);
            dailyPlan.setRoutes(dayRoutes);
            double dailyCost = calculateDailyCostByPersonality(dayAttractions, dayRoutes, type);
            dailyPlan.setDailyCost(dailyCost);
            totalCost += dailyCost;
            List<String> suggestions = generatePersonalitySuggestions(type, dayAttractions.size());
            dailyPlan.setSuggestions(suggestions);

            dailyItineraries.add(dailyPlan);
        }
        
        plan.setDailyItineraries(dailyItineraries);
        plan.setEstimatedCost(totalCost);
        
        // 设置住宿建议（符合人格类型）
        plan.setAccommodationSuggestion(getPersonalityAccommodationSuggestion(city, type));
        
        // 添加人格相关的附加信息
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("personality_type", type.getTypeCode());
        additionalInfo.put("personality_name", type.getTypeName());
        additionalInfo.put("personality_description", type.getDescription());
        additionalInfo.put("travel_preference", type.getTravelPreference());
        additionalInfo.put("unique_elements", type.getUniqueItineraryElements());
        plan.setAdditionalInfo(additionalInfo);
        
        // 注意：不再设置生成的行程计划ID，因为ItineraryPlanVO没有对应的方法
        
        log.info("根据人格类型生成推荐行程完成，人格类型：{}，预计费用：{}", type.getTypeName(), totalCost);
        return plan;
    }
    /**
     * 从剩余景点中按类型均衡选择当日景点
     * @param remainingAttractions 剩余未分配的景点
     * @param needCount 当日需要的数量
     * @param typeWeights 人格类型的景点权重（如探险家：小众景点5、特色街区5）
     * @param dayIndex 当前天数（用于随机种子）
     * @return 类型均衡的当日景点
     */
    private List<POI> selectBalancedDailyAttractions(
            List<POI> remainingAttractions,
            int needCount,
            Map<String, Integer> typeWeights,
            int dayIndex
    ) {
        if (remainingAttractions.isEmpty() || needCount <= 0) {
            return new ArrayList<>();
        }

        // 1. 给剩余景点打类型标签（按名称匹配权重key）
        Map<POI, String> poiTypeMap = new HashMap<>();
        for (POI poi : remainingAttractions) {
            String poiName = poi.getName().toLowerCase();
            // 匹配最相关的类型（可根据实际场景优化）
            String matchedType = typeWeights.keySet().stream()
                    .filter(type -> poiName.contains(type.toLowerCase()))
                    .findFirst()
                    .orElse("通用景点");
            poiTypeMap.put(poi, matchedType);
        }

        // 2. 按类型分组
        Map<String, List<POI>> typeGroupedPois = new HashMap<>();
        for (POI poi : remainingAttractions) {
            String type = poiTypeMap.get(poi);
            typeGroupedPois.computeIfAbsent(type, k -> new ArrayList<>()).add(poi);
        }

        // 3. 按权重分配当日各类型数量（核心：优先选高权重类型，且覆盖多类型）
        List<POI> dayAttractions = new ArrayList<>();
        Random random = new Random(dayIndex * 100); // 固定种子，保证结果可复现
        int totalWeight = typeWeights.values().stream().mapToInt(Integer::intValue).sum();
        if (totalWeight == 0) totalWeight = 1;

        // 按权重排序类型（高权重优先）
        List<String> sortedTypes = typeWeights.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 4. 逐个类型选景点，确保覆盖多类型
        for (String type : sortedTypes) {
            if (dayAttractions.size() >= needCount) break;
            List<POI> typePois = typeGroupedPois.getOrDefault(type, new ArrayList<>());
            if (typePois.isEmpty()) continue;

            // 该类型当日分配数量（至少1个，不超过剩余需要）
            int typeNeed = Math.max(1, (int) Math.round((double) typeWeights.get(type) / totalWeight * needCount));
            typeNeed = Math.min(typeNeed, needCount - dayAttractions.size());

            // 随机选该类型的景点
            Collections.shuffle(typePois, random);
            dayAttractions.addAll(typePois.subList(0, Math.min(typeNeed, typePois.size())));
        }

        // 5. 补充剩余数量（随机选，避免类型单一）
        if (dayAttractions.size() < needCount) {
            List<POI> temp = new ArrayList<>(remainingAttractions);
            temp.removeAll(dayAttractions);
            Collections.shuffle(temp, random);
            dayAttractions.addAll(temp.subList(0, Math.min(needCount - dayAttractions.size(), temp.size())));
        }

        // 6. 打乱当日景点顺序
        Collections.shuffle(dayAttractions, random);
        return dayAttractions;
    }
    @Override
    public Map<String, PersonalityTest.PersonalityType> getAllPersonalityTypes() {
        return personalityTypeMap;
    }
    
    @Override
    public boolean isValidPersonalityType(String personalityType) {
        return personalityTypeMap.containsKey(personalityType);
    }
    
    @Override
    public Map<String, Double> predictPersonalityType(String userId) {
        // 这里可以实现用户历史行为分析，预测可能的人格类型
        // 当前返回默认预测结果
        Map<String, Double> prediction = new HashMap<>();
        prediction.put("ADVENTURER", 0.3);
        prediction.put("EXPLORER", 0.25);
        prediction.put("RELAXER", 0.2);
        prediction.put("CULTURIST", 0.15);
        prediction.put("FOODIE", 0.1);
        
        return prediction;
    }
    
    // 初始化人格测试数据
    private PersonalityTest initializePersonalityTest() {
        PersonalityTest test = new PersonalityTest();
        test.setTestId("PERSONALITY_TEST_001");
        test.setTestName("三分钟旅行人格测试");
        
        List<PersonalityTest.TestQuestion> questions = new ArrayList<>();
        
        // 添加测试问题
        questions.add(createQuestion("Q1", "旅行时，你更偏好哪种类型的景点？", 10));
        questions.add(createQuestion("Q2", "选择住宿时，你最看重什么？", 8));
        questions.add(createQuestion("Q3", "旅行中，你通常每天参观多少个景点？", 9));
        questions.add(createQuestion("Q4", "你更喜欢哪种交通方式？", 7));
        questions.add(createQuestion("Q5", "在旅行中，你最期待的是什么？", 10));
        
        test.setQuestions(questions);
        
        // 初始化人格类型
        Map<String, PersonalityTest.PersonalityType> types = new HashMap<>();
        types.put("ADVENTURER", createAdventurerType());
        types.put("EXPLORER", createExplorerType());
        types.put("RELAXER", createRelaxerType());
        types.put("CULTURIST", createCulturistType());
        types.put("FOODIE", createFoodieType());
        
        test.setPersonalityTypes(types);
        
        return test;
    }
    
    // 创建测试问题
    private PersonalityTest.TestQuestion createQuestion(String questionId, String content, int weight) {
        PersonalityTest.TestQuestion question = new PersonalityTest.TestQuestion();
        question.setQuestionId(questionId);
        question.setContent(content);
        question.setWeight(weight);
        
        List<PersonalityTest.TestOption> options = new ArrayList<>();
        
        // 根据问题ID设置不同的选项
        if (questionId.equals("Q1")) {
            options.add(createOption("Q1A", "自然景观（山川、湖泊、森林等）", Map.of("ADVENTURER", 3, "EXPLORER", 2, "RELAXER", 1)));
            options.add(createOption("Q1B", "历史文化景点（博物馆、古迹等）", Map.of("CULTURIST", 3, "EXPLORER", 1)));
            options.add(createOption("Q1C", "美食街、特色餐厅", Map.of("FOODIE", 3, "RELAXER", 1)));
            options.add(createOption("Q1D", "主题公园、娱乐场所", Map.of("ADVENTURER", 2, "EXPLORER", 1)));
        } else if (questionId.equals("Q2")) {
            options.add(createOption("Q2A", "地理位置优越，交通便利", Map.of("EXPLORER", 3, "ADVENTURER", 2)));
            options.add(createOption("Q2B", "环境安静，适合休息", Map.of("RELAXER", 3, "CULTURIST", 1)));
            options.add(createOption("Q2C", "特色鲜明，有当地风格", Map.of("CULTURIST", 3, "FOODIE", 1)));
            options.add(createOption("Q2D", "设施齐全，服务周到", Map.of("FOODIE", 2, "RELAXER", 2)));
        } else if (questionId.equals("Q3")) {
            options.add(createOption("Q3A", "3个以下，慢慢体验", Map.of("RELAXER", 3, "CULTURIST", 1)));
            options.add(createOption("Q3B", "3-5个，合理安排时间", Map.of("EXPLORER", 3, "CULTURIST", 2)));
            options.add(createOption("Q3C", "5个以上，尽量多体验", Map.of("ADVENTURER", 3, "EXPLORER", 1)));
            options.add(createOption("Q3D", "不确定，随性安排", Map.of("RELAXER", 2, "FOODIE", 1)));
        } else if (questionId.equals("Q4")) {
            options.add(createOption("Q4A", "公共交通（地铁、公交）", Map.of("EXPLORER", 3, "CULTURIST", 2)));
            options.add(createOption("Q4B", "打车或自驾", Map.of("ADVENTURER", 2, "FOODIE", 3)));
            options.add(createOption("Q4C", "步行或骑行", Map.of("RELAXER", 3, "CULTURIST", 1)));
            options.add(createOption("Q4D", "包车或导游服务", Map.of("FOODIE", 2, "RELAXER", 1)));
        } else if (questionId.equals("Q5")) {
            options.add(createOption("Q5A", "探索未知，寻找刺激", Map.of("ADVENTURER", 3, "EXPLORER", 1)));
            options.add(createOption("Q5B", "了解当地的文化遗产", Map.of("CULTURIST", 3, "EXPLORER", 2)));
            options.add(createOption("Q5C", "品尝特色美食", Map.of("FOODIE", 3, "RELAXER", 1)));
            options.add(createOption("Q5D", "放松身心，享受假期", Map.of("RELAXER", 3, "FOODIE", 1)));
        }
        
        question.setOptions(options);
        return question;
    }
    
    // 创建测试选项
    private PersonalityTest.TestOption createOption(String optionId, String content, Map<String, Integer> traitScores) {
        PersonalityTest.TestOption option = new PersonalityTest.TestOption();
        option.setOptionId(optionId);
        option.setContent(content);
        option.setTraitScores(traitScores);
        return option;
    }
    
    // 创建各个人格类型
    private PersonalityTest.PersonalityType createAdventurerType() {
        PersonalityTest.PersonalityType type = new PersonalityTest.PersonalityType();
        type.setTypeCode("ADVENTURER");
        type.setTypeName("冒险者");
        type.setDescription("充满活力，喜欢刺激和挑战，热衷于尝试新鲜事物");
        type.setTravelPreference("喜欢冒险和刺激的活动，偏好自然景观和户外活动");
        
        Map<String, Integer> attractionTypes = new HashMap<>();
        attractionTypes.put("自然景观", 5);
        attractionTypes.put("户外运动", 5);
        attractionTypes.put("主题乐园", 4);
        attractionTypes.put("奇遇活动", 5);
        type.setRecommendedAttractionTypes(attractionTypes);
        
        Map<String, Integer> transportTypes = new HashMap<>();
        transportTypes.put("自驾", 5);
        transportTypes.put("骑行", 4);
        transportTypes.put("包车", 3);
        type.setRecommendedTransportationTypes(transportTypes);
        
        PersonalityTest.AttractionCountRange countRange = new PersonalityTest.AttractionCountRange();
        countRange.setMin(5);
        countRange.setMax(8);
        type.setDailyAttractionCountRange(countRange);
        
        type.setDailyPace("紧凑充实，高效率游玩多个景点");
        
        List<String> accommodationTypes = Arrays.asList("特色民宿", "青年旅舍", "露营地");
        type.setRecommendedAccommodationTypes(accommodationTypes);
        
        List<String> uniqueElements = Arrays.asList("户外运动", "探险活动", "极限体验", "自然探索", "自由行");
        type.setUniqueItineraryElements(uniqueElements);
        
        return type;
    }
    
    // 创建探险家类型（类似冒险者但更注重探索）
    private PersonalityTest.PersonalityType createExplorerType() {
        PersonalityTest.PersonalityType type = new PersonalityTest.PersonalityType();
        type.setTypeCode("EXPLORER");
        type.setTypeName("探险家");
        type.setDescription("好奇宝宝，喜欢探索未知，对新鲜事物充满兴趣");
        type.setTravelPreference("喜欢深度探索当地文化和生活，偏好小众景点和独特体验");
        
        Map<String, Integer> attractionTypes = new HashMap<>();
        attractionTypes.put("历史文化", 4);
        attractionTypes.put("自然风光", 4);
        attractionTypes.put("特色街区", 5);
        attractionTypes.put("小众景点", 5);
        type.setRecommendedAttractionTypes(attractionTypes);
        
        Map<String, Integer> transportTypes = new HashMap<>();
        transportTypes.put("公共交通", 5);
        transportTypes.put("步行", 4);
        transportTypes.put("共享单车", 3);
        type.setRecommendedTransportationTypes(transportTypes);
        
        PersonalityTest.AttractionCountRange countRange = new PersonalityTest.AttractionCountRange();
        countRange.setMin(3);
        countRange.setMax(5);
        type.setDailyAttractionCountRange(countRange);
        
        type.setDailyPace("中等节奏，有足够时间深入了解每个景点");
        
        List<String> accommodationTypes = Arrays.asList("特色民宿", "当地酒店", "青年旅舍");
        type.setRecommendedAccommodationTypes(accommodationTypes);
        
        List<String> uniqueElements = Arrays.asList("深度体验", "当地生活", "小众景点", "文化探索", "自由行");
        type.setUniqueItineraryElements(uniqueElements);
        
        return type;
    }
    
    // 创建放松者类型
    private PersonalityTest.PersonalityType createRelaxerType() {
        PersonalityTest.PersonalityType type = new PersonalityTest.PersonalityType();
        type.setTypeCode("RELAXER");
        type.setTypeName("放松者");
        type.setDescription("注重休闲和舒适，旅行是为了放松身心，远离日常压力");
        type.setTravelPreference("喜欢轻松舒适的旅行节奏，偏好度假型目的地");
        
        Map<String, Integer> attractionTypes = new HashMap<>();
        attractionTypes.put("海滩度假", 5);
        attractionTypes.put("温泉SPA", 5);
        attractionTypes.put("公园绿地", 4);
        attractionTypes.put("休闲街区", 3);
        type.setRecommendedAttractionTypes(attractionTypes);
        
        Map<String, Integer> transportTypes = new HashMap<>();
        transportTypes.put("出租车", 4);
        transportTypes.put("包车", 5);
        transportTypes.put("酒店接送", 4);
        type.setRecommendedTransportationTypes(transportTypes);
        
        PersonalityTest.AttractionCountRange countRange = new PersonalityTest.AttractionCountRange();
        countRange.setMin(1);
        countRange.setMax(3);
        type.setDailyAttractionCountRange(countRange);
        
        type.setDailyPace("轻松缓慢，有充足的休息和自由时间");
        
        List<String> accommodationTypes = Arrays.asList("高档酒店", "度假村", "温泉酒店");
        type.setRecommendedAccommodationTypes(accommodationTypes);
        
        List<String> uniqueElements = Arrays.asList("休闲放松", "SPA体验", "美食享受", "海滩活动", "自由安排");
        type.setUniqueItineraryElements(uniqueElements);
        
        return type;
    }
    
    // 创建文化爱好者类型
    private PersonalityTest.PersonalityType createCulturistType() {
        PersonalityTest.PersonalityType type = new PersonalityTest.PersonalityType();
        type.setTypeCode("CULTURIST");
        type.setTypeName("文化爱好者");
        type.setDescription("对历史文化充满兴趣，喜欢深入了解当地的文化底蕴");
        type.setTravelPreference("偏好历史古迹、博物馆、艺术展览等文化类景点");
        
        Map<String, Integer> attractionTypes = new HashMap<>();
        attractionTypes.put("博物馆", 5);
        attractionTypes.put("历史古迹", 5);
        attractionTypes.put("艺术展览", 4);
        attractionTypes.put("文化街区", 4);
        type.setRecommendedAttractionTypes(attractionTypes);
        
        Map<String, Integer> transportTypes = new HashMap<>();
        transportTypes.put("公共交通", 4);
        transportTypes.put("步行", 5);
        transportTypes.put("导游服务", 4);
        type.setRecommendedTransportationTypes(transportTypes);
        
        PersonalityTest.AttractionCountRange countRange = new PersonalityTest.AttractionCountRange();
        countRange.setMin(2);
        countRange.setMax(4);
        type.setDailyAttractionCountRange(countRange);
        
        type.setDailyPace("缓慢深入，每个景点都有足够的时间细细品味");
        
        List<String> accommodationTypes = Arrays.asList("历史酒店", "文化主题酒店", "市中心酒店");
        type.setRecommendedAccommodationTypes(accommodationTypes);
        
        List<String> uniqueElements = Arrays.asList("文化体验", "历史讲解", "艺术欣赏", "传统活动", "文化交流");
        type.setUniqueItineraryElements(uniqueElements);
        
        return type;
    }
    
    // 创建美食爱好者类型
    private PersonalityTest.PersonalityType createFoodieType() {
        PersonalityTest.PersonalityType type = new PersonalityTest.PersonalityType();
        type.setTypeCode("FOODIE");
        type.setTypeName("美食家");
        type.setDescription("对美食有着浓厚的兴趣和追求，旅行很大程度上是为了品尝当地特色美食");
        type.setTravelPreference("偏好美食街、特色餐厅、当地小吃等与美食相关的体验");
        
        Map<String, Integer> attractionTypes = new HashMap<>();
        attractionTypes.put("美食街", 5);
        attractionTypes.put("特色餐厅", 5);
        attractionTypes.put("美食市场", 4);
        attractionTypes.put("休闲街区", 3);
        type.setRecommendedAttractionTypes(attractionTypes);
        
        Map<String, Integer> transportTypes = new HashMap<>();
        transportTypes.put("出租车", 5);
        transportTypes.put("包车", 4);
        transportTypes.put("公共交通", 2);
        type.setRecommendedTransportationTypes(transportTypes);
        
        PersonalityTest.AttractionCountRange countRange = new PersonalityTest.AttractionCountRange();
        countRange.setMin(3);
        countRange.setMax(5);
        type.setDailyAttractionCountRange(countRange);
        
        type.setDailyPace("中等节奏，重点安排在餐饮体验上，景点游览作为辅助");
        
        List<String> accommodationTypes = Arrays.asList("美食街区酒店", "高档酒店", "市中心酒店");
        type.setRecommendedAccommodationTypes(accommodationTypes);
        
        List<String> uniqueElements = Arrays.asList("美食体验", "特色餐厅", "当地小吃", "烹饪课程", "美食节");
        type.setUniqueItineraryElements(uniqueElements);
        
        return type;
    }
    
    // 初始化人格类型映射
    private Map<String, PersonalityTest.PersonalityType> initializePersonalityTypeMap() {
        Map<String, PersonalityTest.PersonalityType> map = new HashMap<>();
        map.put("ADVENTURER", createAdventurerType());
        map.put("EXPLORER", createExplorerType());
        map.put("RELAXER", createRelaxerType());
        map.put("CULTURIST", createCulturistType());
        map.put("FOODIE", createFoodieType());
        return map;
    }
    
    // 计算人格特质得分
    private Map<String, Integer> calculateTraitScores(Map<String, String> userAnswers) {
        Map<String, Integer> traitScores = new HashMap<>();
        
        // 遍历所有问题
        for (PersonalityTest.TestQuestion question : personalityTest.getQuestions()) {
            String questionId = question.getQuestionId();
            if (userAnswers.containsKey(questionId)) {
                String selectedOptionId = userAnswers.get(questionId);
                
                // 找到用户选择的选项
                for (PersonalityTest.TestOption option : question.getOptions()) {
                    if (option.getOptionId().equals(selectedOptionId)) {
                        // 累加得分（考虑问题权重）
                        for (Map.Entry<String, Integer> entry : option.getTraitScores().entrySet()) {
                            String trait = entry.getKey();
                            int score = entry.getValue() * question.getWeight();
                            traitScores.put(trait, traitScores.getOrDefault(trait, 0) + score);
                        }
                        break;
                    }
                }
            }
        }
        
        return traitScores;
    }
    
    // 确定主导人格类型
    private String determineDominantPersonalityType(Map<String, Integer> traitScores) {
        // 找出得分最高的人格类型
        String dominantType = "EXPLORER"; // 默认类型
        int highestScore = 0;
        
        for (Map.Entry<String, Integer> entry : traitScores.entrySet()) {
            if (entry.getValue() > highestScore) {
                highestScore = entry.getValue();
                dominantType = entry.getKey();
            }
        }
        
        return dominantType;
    }
    
    // 生成旅行风格标签
    private List<String> generateTravelStyleTags(Map<String, Integer> traitScores) {
        List<String> tags = new ArrayList<>();
        
        // 根据得分生成标签
        if (traitScores.getOrDefault("ADVENTURER", 0) > 20) {
            tags.add("冒险精神");
            tags.add("活力四射");
        }
        if (traitScores.getOrDefault("EXPLORER", 0) > 20) {
            tags.add("好奇宝宝");
            tags.add("深度探索");
        }
        if (traitScores.getOrDefault("RELAXER", 0) > 20) {
            tags.add("轻松自在");
            tags.add("享受生活");
        }
        if (traitScores.getOrDefault("CULTURIST", 0) > 20) {
            tags.add("文化达人");
            tags.add("历史爱好者");
        }
        if (traitScores.getOrDefault("FOODIE", 0) > 20) {
            tags.add("吃货一枚");
            tags.add("美食猎人");
        }
        
        // 添加通用标签
        if (tags.isEmpty()) {
            tags.add("探索者");
            tags.add("旅行者");
        }
        
        return tags;
    }
    
    // 生成个性化旅行建议
    private List<String> generatePersonalizedTips(String personalityType) {
        List<String> tips = new ArrayList<>();
        
        switch (personalityType) {
            case "ADVENTURER":
                tips.add("选择包含户外活动和探险项目的目的地");
                tips.add("预留充足的体力和时间，安排紧凑但不过度劳累的行程");
                tips.add("携带适合户外活动的装备，注意安全");
                break;
            case "EXPLORER":
                tips.add("选择历史文化丰富或自然风光多样的目的地");
                tips.add("提前研究当地特色，寻找小众但有趣的景点");
                tips.add("尝试与当地人交流，体验真实的当地生活");
                break;
            case "RELAXER":
                tips.add("选择以度假为主的目的地，如海滩、温泉等");
                tips.add("不要安排过多景点，留出足够的自由活动和休息时间");
                tips.add("选择舒适的住宿，注重住宿体验");
                break;
            case "CULTURIST":
                tips.add("选择历史文化底蕴深厚的城市或地区");
                tips.add("提前了解当地历史背景，让参观更有收获");
                tips.add("考虑参加专业讲解的文化导览团");
                break;
            case "FOODIE":
                tips.add("以美食为中心规划行程，选择美食丰富的城市");
                tips.add("提前预订热门餐厅，避免错过当地特色美食");
                tips.add("尝试参加当地美食之旅或烹饪课程");
                break;
            default:
                tips.add("根据自己的兴趣偏好选择适合的目的地");
                tips.add("平衡景点游览和休息时间，避免过度劳累");
                break;
        }
        
        return tips;
    }
    
    // 寻找相似人格类型
    private List<String> findSimilarPersonalityTypes(Map<String, Integer> traitScores, String dominantType) {
        List<String> similarTypes = new ArrayList<>();
        
        // 按得分排序，排除主导类型，取得分最高的两个作为相似类型
        List<Map.Entry<String, Integer>> sortedEntries = traitScores.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(dominantType))
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
        
        // 取前两个作为相似类型
        for (int i = 0; i < Math.min(2, sortedEntries.size()); i++) {
            similarTypes.add(sortedEntries.get(i).getKey());
        }
        
        return similarTypes;
    }

    /**
     * 按权重和天数生成景点列表
     * @param city 城市
     * @param type 人格类型
     * @param days 天数
     * @return 最终景点列表（去重 + 按权重分配）
     */
    private List<POI> generatePersonalityAttractions(String city, PersonalityTest.PersonalityType type, int days) {
        // 1. 基础配置：每日4个景点，总数量 = 天数 × 4
        int totalCount = days * 8;
        Map<String, Integer> attractionWeights = type.getRecommendedAttractionTypes();
        System.out.println("推荐类型======="+type.getRecommendedAttractionTypes());
        List<POI> allAttractions = new ArrayList<>();
        // 用于去重的Set（按景点名称去重）
        Set<String> attractionNames = new HashSet<>();

        // 2. 计算权重总和（用于按比例分配数量）
        int totalWeight = attractionWeights.values().stream().mapToInt(Integer::intValue).sum();
        if (totalWeight == 0) {
            totalWeight = 1; // 避免除以0
        }

        // 3. 按权重分配各类型景点数量（遍历所有权重Key）
        for (Map.Entry<String, Integer> entry : attractionWeights.entrySet()) {
            String key = entry.getKey();
            int weight = entry.getValue();
            if (weight <= 3) {
                continue; // 跳过权重≤0的类型
            }
            // 按权重比例分配数量（权重越高，数量越多）
            int allocateCount = (int) Math.round((double) weight / totalWeight * totalCount);
            if (allocateCount < 1) {
                allocateCount = 1; // 至少选1个（避免权重低的类型无景点）
            }
            // 4. 根据Key获取对应类型的景点（精准关键词映射）
            List<Location> locations = getLocationsByKey(key, city, allocateCount);
            List<POI> pois = convertLocationToPOI(locations);
            // 5. 去重并添加到总列表
            for (POI poi : pois) {
                if (!attractionNames.contains(poi.getName()) && allAttractions.size() < totalCount) {
                    attractionNames.add(poi.getName());
                    allAttractions.add(poi);
                }
            }
        }
        // 6. 兜底补充：如果总数量不足，用通用景点补齐
        if (allAttractions.size() < totalCount) {
            int needMore = totalCount - allAttractions.size();
            List<POI> defaultPOIs = mapService.getAttractionsByCity(city, 1, needMore);
            for (POI poi : defaultPOIs) {
                if (!attractionNames.contains(poi.getName()) && allAttractions.size() < totalCount) {
                    attractionNames.add(poi.getName());
                    allAttractions.add(poi);
                }
            }
        }
        log.info("最终生成景点数量：{}（目标：{}）", allAttractions.size(), totalCount);
        return allAttractions;
    }

    /**
     * 权重Key → 高德搜索关键词映射（精准匹配）
     * @param key 权重Key（如“小众景点”“特色街区”）
     * @param city 城市
     * @param count 需要的数量
     * @return 对应类型的Location列表
     */
    private List<Location> getLocationsByKey(String key, String city, int count) {
        return switch (key) {
            case "自然风光" -> mapService.searchLocations("自然风光", city, count);
            case "历史文化" -> mapService.searchLocations("博物馆", city, count);
            case "小众景点" -> mapService.searchLocations("小众景点", city, count);
            case "特色街区" -> mapService.searchLocations("特色街区", city, count);
            // 可扩展其他类型（如“美食街”“主题乐园”）

            default -> mapService.searchLocations(key, city, count);
        };
    }

    /**
     * Location → POI转换（带空值防御）
     */
    private List<POI> convertLocationToPOI(List<Location> locations) {
        List<POI> pois = new ArrayList<>();
        if (locations == null || locations.isEmpty()) {
            return pois;
        }
        for (Location loc : locations) {
            POI poi = new POI();
            poi.setName(loc.getName());
            poi.setLat(loc.getLat());
            poi.setLng(loc.getLng());
            pois.add(poi);
        }
        return pois;
    }
    
    // 根据人格类型选择交通方式
    private String selectTransportByPersonality(PersonalityTest.PersonalityType type, List<POI> attractions) {
        Map<String, Integer> transportWeights = type.getRecommendedTransportationTypes();
        
        // 找出权重最高的交通方式
        String preferredTransport = "transit"; // 默认公共交通
        int highestWeight = 0;
        
        for (Map.Entry<String, Integer> entry : transportWeights.entrySet()) {
            if (entry.getValue() > highestWeight) {
                highestWeight = entry.getValue();
                preferredTransport = entry.getKey();
            }
        }
        
        // 根据交通方式映射到地图服务支持的模式
        switch (preferredTransport) {
            case "自驾":
                return "driving";
            case "骑行":
                return "bicyling";
            case "出租车":
                return "taxi";
            case "步行":
                return "walking"; // 假设地图服务支持步行模式
            default:
                return "transit";
        }
    }
    
    // 根据人格类型规划每日路线
    private List<Route> planDailyRoutesByPersonality(List<POI> attractions, String transportMode, 
                                                   PersonalityTest.PersonalityType type) {
        List<Route> routes = new ArrayList<>();
        if (attractions.size() < 2) {
            return routes;
        }
        
        // 冒险者和探险家可能更注重效率，采用最近邻算法
        // 放松者可能更注重舒适，不一定按最优路径
        
        POI currentLocation = attractions.get(0);
        List<POI> remainingAttractions = new ArrayList<>(attractions.subList(1, attractions.size()));

        while (!remainingAttractions.isEmpty()) {
            POI nextAttraction;
            // 冒险者和探险家选择最近的景点
            if (type.getTypeCode().equals("ADVENTURER") || type.getTypeCode().equals("EXPLORER")) {
                nextAttraction = findNearestAttraction(currentLocation, remainingAttractions);
            } else {
                // 其他类型随机选择或按顺序
                nextAttraction = remainingAttractions.get(0);
            }
            String origin = currentLocation.getName();
            String destination = nextAttraction.getName();
            Route route = mapService.getRoute(origin, destination, transportMode);
            if (route != null) {
                routes.add(route);
            }
            
            currentLocation = nextAttraction;
            remainingAttractions.remove(nextAttraction);
        }
        
        return routes;
    }
    
    // 查找最近的景点
    private POI findNearestAttraction(POI current, List<POI> attractions) {
        POI nearest = null;
        double minDistance = Double.MAX_VALUE;
        for (POI attraction : attractions) {
            String origin = current.getLat() + "," + current.getLng();
            String destination = attraction.getLat() + "," + attraction.getLng();
            System.out.println("current====== " + current.getName());
            System.out.println("attraction====== " + attraction.getName());
            double distance = mapService.getDistance(origin, destination);
            
            if (distance < minDistance) {
                minDistance = distance;
                nearest = attraction;
            }
        }
        return nearest;
    }
    
    // 根据人格类型计算每日费用
    private double calculateDailyCostByPersonality(List<POI> attractions, List<Route> routes, 
                                                PersonalityTest.PersonalityType type) {
        double attractionCost = calculateAttractionCost(attractions);
        double transportCost = calculateTransportCost(routes);
        
        // 不同人格类型的餐饮和住宿预算不同
        double foodCost;
        double accommodationCost = 200; // 基础住宿费用
        
        switch (type.getTypeCode()) {
            case "ADVENTURER":
            case "EXPLORER":
                foodCost = 80; // 经济实惠
                accommodationCost *= 0.8; // 青年旅舍等经济住宿
                break;
            case "RELAXER":
                foodCost = 150; // 较高餐饮预算
                accommodationCost *= 1.5; // 高档酒店
                break;
            case "CULTURIST":
                foodCost = 100; // 适中餐饮预算
                accommodationCost *= 1.2; // 文化主题酒店
                break;
            case "FOODIE":
                foodCost = 200; // 高餐饮预算
                accommodationCost *= 1.3; // 美食街区酒店
                break;
            default:
                foodCost = 100;
                break;
        }
        
        return attractionCost + transportCost + foodCost + accommodationCost;
    }
    
    // 生成符合人格类型的建议
    private List<String> generatePersonalitySuggestions(PersonalityTest.PersonalityType type, int attractionCount) {
        List<String> suggestions = new ArrayList<>();
        
        switch (type.getTypeCode()) {
            case "ADVENTURER":
                suggestions.add("今天安排了" + attractionCount + "个景点，保持活力，尽情探索！");
                suggestions.add("建议携带足够的水和零食，补充体力");
                suggestions.add("可以尝试一些刺激的活动，挑战自我");
                break;
            case "EXPLORER":
                suggestions.add("今天有" + attractionCount + "个值得探索的地方，记得带上好奇心");
                suggestions.add("可以和当地人交流，了解更多背后的故事");
                suggestions.add("关注细节，发现意想不到的惊喜");
                break;
            case "RELAXER":
                suggestions.add("轻松享受今天的" + attractionCount + "个景点，不要赶时间");
                suggestions.add("找个舒适的咖啡馆休息一下，享受慢时光");
                suggestions.add("记得留出足够的自由活动时间，按自己的节奏来");
                break;
            case "CULTURIST":
                suggestions.add("今天的" + attractionCount + "个景点都蕴含丰富的文化内涵");
                suggestions.add("建议提前了解背景知识，体验会更有深度");
                suggestions.add("可以考虑参加专业讲解，获取更多细节信息");
                break;
            case "FOODIE":
                suggestions.add("今天除了" + attractionCount + "个景点，别忘了享受当地美食");
                suggestions.add("推荐尝试当地特色小吃，体验地道风味");
                suggestions.add("午餐和晚餐建议提前预订热门餐厅");
                break;
            default:
                suggestions.add("按自己的节奏享受今天的行程");
                suggestions.add("注意休息，保持良好的旅行状态");
                break;
        }
        
        return suggestions;
    }
    
    // 获取符合人格类型的住宿建议
    private String getPersonalityAccommodationSuggestion(String city, PersonalityTest.PersonalityType type) {
        StringBuilder suggestion = new StringBuilder("为您推荐的住宿类型：");
        
        List<String> types = type.getRecommendedAccommodationTypes();
        for (int i = 0; i < types.size(); i++) {
            suggestion.append(types.get(i));
            if (i < types.size() - 1) {
                suggestion.append("、");
            }
        }
        
        suggestion.append("。");
        
        // 根据人格类型添加具体建议
        switch (type.getTypeCode()) {
            case "ADVENTURER":
                suggestion.append("建议选择位于市中心或交通便利的位置，方便第二天出行。");
                break;
            case "EXPLORER":
                suggestion.append("推荐选择有当地特色的民宿，可以更好地融入当地生活。");
                break;
            case "RELAXER":
                suggestion.append("建议选择设施齐全、环境安静的高档酒店，确保良好的休息质量。");
                break;
            case "CULTURIST":
                suggestion.append("推荐选择位于历史文化街区或市中心的酒店，方便参观景点。");
                break;
            case "FOODIE":
                suggestion.append("建议选择位于美食街区附近的酒店，方便品尝各种美食。");
                break;
        }
        
        return suggestion.toString();
    }
    
    // 计算根据人格类型的预算
    private double calculateBudgetByPersonality(PersonalityTest.PersonalityType type) {
        // 基础预算，不同人格类型有不同的预算系数
        switch (type.getTypeCode()) {
            case "ADVENTURER":
            case "EXPLORER":
                return 1500; // 中等预算
            case "RELAXER":
                return 2500; // 高预算
            case "CULTURIST":
                return 1800; // 中高预算
            case "FOODIE":
                return 2000; // 中高预算，重点在餐饮
            default:
                return 1500;
        }
    }
    
    // 选择每日景点数量
    private int selectDailyAttractionsCount(int min, int max, int currentDay, int totalDays) {
        // 第一天和最后一天可能安排较少景点
        if (currentDay == 0 || currentDay == totalDays - 1) {
            return min;
        }
        System.out.println("day: " + currentDay+"返回"+min + (int)(Math.random() * (max - min + 1)));
        // 中间天数安排较多景点
        return min + (int)(Math.random() * (max - min + 1));
    }
    
    // 生成默认行程（当人格类型无效时）
    private ItineraryPlanVO generateDefaultItinerary(String city, int days) {
        ItineraryPlanVO plan = new ItineraryPlanVO();
        plan.setPlanName(city + " " + days + "日游（默认行程）");
        plan.setCity(city);
        plan.setDays(days);
        plan.setPlanType("DEFAULT_PLAN");
        plan.setTotalBudget(1500);
        plan.setEstimatedCost(1300);
        
        return plan;
    }
    
    // 辅助方法：计算景点费用
    private double calculateAttractionCost(List<POI> attractions) {
        // 简化计算，假设每个景点平均50元门票
        return attractions.size() * 50;
    }
    
    // 辅助方法：计算交通费用
    private double calculateTransportCost(List<Route> routes) {
        double total = 0;
        for (Route route : routes) {
            total += route.getEstimatedCost();
        }
        return total;
    }
    
    // 创建模拟POI
    private POI createMockPOI(String name, double lat, double lng) {
        POI poi = new POI();
        poi.setName(name);
        poi.setLat(lat);
        poi.setLng(lng);
        return poi;
    }
    
    // 生成各类模拟景点
    private List<POI> generateNatureAttractions(String city, int count) {
        List<POI> attractions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            attractions.add(createMockPOI(city + "国家森林公园" + i, 30.6 + i * 0.01, 104.0));
        }
        return attractions;
    }
    
    private List<POI> generateCulturalAttractions(String city, int count) {
        List<POI> attractions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            attractions.add(createMockPOI(city + "古城墙遗址" + i, 30.7 + i * 0.01, 104.1));
        }
        return attractions;
    }
    
    private List<POI> generateFoodAttractions(String city, int count) {
        List<POI> attractions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            attractions.add(createMockPOI(city + "美食街" + i, 30.65 + i * 0.01, 104.05));
        }
        return attractions;
    }
    
    private List<POI> generateMuseumAttractions(String city, int count) {
        List<POI> attractions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            attractions.add(createMockPOI(city + "博物馆" + i, 30.55 + i * 0.01, 104.15));
        }
        return attractions;
    }
    
    private List<POI> generateThemeParkAttractions(String city, int count) {
        List<POI> attractions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            attractions.add(createMockPOI(city + "主题乐园" + i, 30.5 + i * 0.01, 104.0));
        }
        return attractions;
    }
    
    /**
     * 根据个性测试结果生成定制行程
     * @param userId 用户ID
     * @param testResult 个性测试结果
     * @param budget 预算
     * @param days 天数
     * @return 定制行程计划
     */
    public ItineraryPlanVO generateCustomizedItinerary(String userId, PersonalityTestResultVO testResult, Integer budget, Integer days) {
        log.info("根据个性测试结果生成定制行程");
        
        try {
            // 分析个性测试结果，确定旅行风格和偏好
            // 注意：使用traitScores替代不存在的personalityScores
            Map<String, Integer> traitScores = testResult.getTraitScores();
            // 将Integer类型的得分转换为Double类型以兼容现有方法
            Map<String, Double> personalityScores = new HashMap<>();
            if (traitScores != null) {
                traitScores.forEach((key, value) -> personalityScores.put(key, value.doubleValue()));
            }
            
            String travelStyle = determineTravelStyle(personalityScores);
            
            // 创建行程计划
            ItineraryPlanVO plan = new ItineraryPlanVO();
            // 使用默认城市，因为testResult没有getPreferredCity方法
            plan.setCity("北京");
            // 使用传入的days参数或默认值，因为testResult没有getPreferredDuration方法
            plan.setDays(days != null ? days : 3);
            // 注意：移除对不存在的setTravelStyle方法的调用
            
            // 根据个性特点设置其他行程属性
            setItineraryAttributesByPersonality(plan, personalityScores);
            
            // 添加个性化推荐景点
            List<String> recommendedAttractions = recommendAttractionsByPersonality(personalityScores, "北京");
            
            // 返回生成的行程计划
            return plan;
        } catch (Exception e) {
            log.error("生成定制行程失败", e);
            return createDefaultItinerary(userId);
        }
    }
    
    /**
     * 确定旅行风格
     * @param personalityScores 个性评分
     * @return 旅行风格
     */
    private String determineTravelStyle(Map<String, Double> personalityScores) {
        // 根据个性评分确定旅行风格
        if (personalityScores.containsKey("ADVENTURER") && personalityScores.get("ADVENTURER") > 0.7) {
            return "adventure";
        } else if (personalityScores.containsKey("RELAXER") && personalityScores.get("RELAXER") > 0.7) {
            return "relaxation";
        } else if (personalityScores.containsKey("CULTURIST") && personalityScores.get("CULTURIST") > 0.7) {
            return "cultural";
        } else if (personalityScores.containsKey("FOODIE") && personalityScores.get("FOODIE") > 0.7) {
            return "culinary";
        } else {
            return "balanced";
        }
    }
    
    /**
     * 根据个性设置行程属性
     * @param plan 行程计划
     * @param personalityScores 个性评分
     */
    private void setItineraryAttributesByPersonality(ItineraryPlanVO plan, Map<String, Double> personalityScores) {
        // 根据个性特点设置行程属性
        // 注意：由于ItineraryPlanVO没有setPace方法，不再设置行程节奏
        log.info("根据个性特点设置行程属性，个性评分数组大小: {}", personalityScores.size());
    }
    
    /**
     * 根据个性推荐景点
     * @param personalityScores 个性评分
     * @param city 城市
     * @return 推荐景点列表
     */
    private List<String> recommendAttractionsByPersonality(Map<String, Double> personalityScores, String city) {
        List<String> attractions = new ArrayList<>();
        
        // 根据个性特点推荐景点
        if (personalityScores.containsKey("ADVENTURER") && personalityScores.get("ADVENTURER") > 0.5) {
            attractions.add(city + "国家森林公园");
            attractions.add(city + "户外探险基地");
        }
        
        if (personalityScores.containsKey("CULTURIST") && personalityScores.get("CULTURIST") > 0.5) {
            attractions.add(city + "博物馆");
            attractions.add(city + "历史古迹");
        }
        
        if (personalityScores.containsKey("FOODIE") && personalityScores.get("FOODIE") > 0.5) {
            attractions.add(city + "美食街");
            attractions.add(city + "特色餐厅");
        }
        
        if (personalityScores.containsKey("RELAXER") && personalityScores.get("RELAXER") > 0.5) {
            attractions.add(city + "公园");
            attractions.add(city + "温泉");
        }
        
        // 添加通用景点
        if (attractions.isEmpty()) {
            attractions.add(city + "市中心广场");
            attractions.add(city + "购物中心");
        }
        
        return attractions;
    }
    
    /**
     * 创建默认行程
     * @return 默认行程计划
     */
    private ItineraryPlanVO createDefaultItinerary() {
        ItineraryPlanVO plan = new ItineraryPlanVO();
        plan.setPlanName("默认旅行计划");
        plan.setCity("北京");
        plan.setDays(3);
        
        // 注意：由于ItineraryPlanVO没有setPace和setTravelStyle方法，不再设置这些属性
        log.info("创建默认行程");
        
        return plan;
    }
    
    /**
     * 创建默认行程（带用户ID参数）
     * @param userId 用户ID
     * @return 默认行程计划
     */
    private ItineraryPlanVO createDefaultItinerary(String userId) {
        ItineraryPlanVO plan = new ItineraryPlanVO();
        plan.setPlanName("默认旅行计划");
        plan.setCity("北京");
        plan.setDays(3);
        
        // 注意：由于ItineraryPlanVO没有setPace和setTravelStyle方法，不再设置这些属性
        log.info("创建默认行程，用户ID: {}", userId);
        
        return plan;
    }

}