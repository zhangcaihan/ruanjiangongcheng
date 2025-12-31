package com.zeyuli.service.impl;

import com.zeyuli.pojo.ChatMessage;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import com.zeyuli.service.AITravelBuddyService;
import com.zeyuli.service.MapService;
import com.zeyuli.service.ItineraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * AI旅行搭子服务实现类
 * 提供智能旅行聊天助手功能
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
@Slf4j
public class AITravelBuddyServiceImpl implements AITravelBuddyService {
    
    @Autowired
    @Qualifier("amapMapService")
    private MapService mapService;
    
    @Autowired
    private ItineraryService itineraryService;
    
    // 存储会话信息的模拟数据库
    private final Map<String, List<ChatMessage>> sessionDatabase = new ConcurrentHashMap<>();
    
    // 存储用户偏好的模拟数据库
    private final Map<String, Map<String, Object>> userPreferencesDatabase = new ConcurrentHashMap<>();
    
    // 存储会话配置的模拟数据库
    private final Map<String, Map<String, String>> sessionConfigurations = new ConcurrentHashMap<>();
    
    // 可用的AI性格类型
    private final Set<String> availablePersonalities = new HashSet<>(Arrays.asList(
            "友好活泼", "专业知识型", "幽默风趣", "耐心细致", "冒险探索型"
    ));
    
    @Override
    public ChatMessage processUserMessage(ChatMessage message) {
        log.info("处理用户消息，会话ID: {}, 消息ID: {}", message.getSessionId(), message.getMessageId());
        
        try {
            // 验证会话是否存在，不存在则创建
            if (!sessionDatabase.containsKey(message.getSessionId())) {
                sessionDatabase.put(message.getSessionId(), new ArrayList<>());
            }
            
            // 存储用户消息
            sessionDatabase.get(message.getSessionId()).add(message);
            
            // 生成AI回复消息
            ChatMessage aiResponse = new ChatMessage();
            aiResponse.setMessageId("AI_" + UUID.randomUUID().toString());
            aiResponse.setSessionId(message.getSessionId());
            aiResponse.setSenderType("ai");
            aiResponse.setSendTime(new Date());
            aiResponse.setStatus("sent");
            
            // 根据用户消息内容决定回复类型
            String userContent = message.getContent().toLowerCase();
            
            if (userContent.contains("推荐") || userContent.contains("景点")) {
                // 推荐景点相关回复
                aiResponse.setMessageType("recommendation");
                aiResponse.setContent(generateAttractionRecommendationContent(userContent));
                
                // 添加推荐相关附加信息
                Map<String, Object> recommendationInfo = new HashMap<>();
                recommendationInfo.put("attractions_count", 3);
                recommendationInfo.put("based_on_query", userContent);
                aiResponse.setAdditionalInfo(recommendationInfo);
                
            } else if (userContent.contains("天气") || userContent.contains("下雨")) {
                // 天气相关回复
                aiResponse.setMessageType("information");
                aiResponse.setContent(generateWeatherInformationContent(userContent));
                
            } else if (userContent.contains("吃饭") || userContent.contains("美食")) {
                // 美食相关回复
                aiResponse.setMessageType("recommendation");
                aiResponse.setContent(generateFoodRecommendationContent(userContent));
                
            } else if (userContent.contains("帮助") || userContent.contains("怎么")) {
                // 帮助相关回复
                aiResponse.setMessageType("help");
                aiResponse.setContent(generateHelpContent());
                
                // 添加快速回复选项
                Map<String, String> quickReplies = new HashMap<>();
                quickReplies.put("景点", "推荐景点");
                quickReplies.put("美食", "推荐美食");
                quickReplies.put("交通", "查询交通");
                quickReplies.put("住宿", "推荐住宿");
                aiResponse.setQuickReplyOptions(quickReplies);
                aiResponse.setQuickReplyRequired(true);
                
            } else {
                // 默认回复
                aiResponse.setMessageType("text");
                aiResponse.setContent(generateDefaultResponseContent(userContent));
            }
            
            // 存储AI回复
            sessionDatabase.get(message.getSessionId()).add(aiResponse);
            
            // 设置回复关系
            aiResponse.setReplyToMessageId(message.getMessageId());
            
            // 情感分析
            aiResponse.setSentiment("positive");
            
            return aiResponse;
            
        } catch (Exception e) {
            log.error("处理用户消息失败: {}", e.getMessage(), e);
            
            // 返回错误消息
            ChatMessage errorResponse = new ChatMessage();
            errorResponse.setMessageId("ERROR_" + UUID.randomUUID().toString());
            errorResponse.setSessionId(message.getSessionId());
            errorResponse.setSenderType("system");
            errorResponse.setMessageType("error");
            errorResponse.setContent("抱歉，我正在处理您的请求时遇到了问题，请稍后再试。");
            errorResponse.setSendTime(new Date());
            errorResponse.setStatus("error");
            errorResponse.setErrorCode("SERVER_ERROR");
            errorResponse.setErrorMessage(e.getMessage());
            
            return errorResponse;
        }
    }
    
    @Override
    public String createNewSession(String userId, String initialMessage, Map<String, Object> userPreferences) {
        log.info("创建新会话，用户ID: {}", userId);
        
        // 生成唯一会话ID
        String sessionId = "SESSION_" + UUID.randomUUID().toString();
        
        // 创建会话记录
        List<ChatMessage> messages = new ArrayList<>();
        
        // 创建欢迎消息
        ChatMessage welcomeMessage = new ChatMessage();
        welcomeMessage.setMessageId("WELCOME_" + UUID.randomUUID().toString());
        welcomeMessage.setSessionId(sessionId);
        welcomeMessage.setSenderType("system");
        welcomeMessage.setContent("您好！我是您的AI旅行搭子，很高兴为您提供旅行服务。有什么我可以帮助您的吗？");
        welcomeMessage.setMessageType("text");
        welcomeMessage.setSendTime(new Date());
        welcomeMessage.setStatus("sent");
        
        messages.add(welcomeMessage);
        
        // 如果有初始消息，添加到会话中
        if (initialMessage != null && !initialMessage.trim().isEmpty()) {
            ChatMessage initialMsg = new ChatMessage();
            initialMsg.setMessageId("INITIAL_" + UUID.randomUUID().toString());
            initialMsg.setSessionId(sessionId);
            initialMsg.setSenderType("user");
            initialMsg.setContent(initialMessage);
            initialMsg.setMessageType("text");
            initialMsg.setSendTime(new Date());
            initialMsg.setStatus("sent");
            
            messages.add(initialMsg);
            
            // 自动生成回复
            ChatMessage autoReply = processUserMessage(initialMsg);
            messages.add(autoReply);
        }
        
        // 存储会话
        sessionDatabase.put(sessionId, messages);
        
        // 存储用户偏好
        if (userPreferences != null) {
            userPreferencesDatabase.put(userId, userPreferences);
        }
        
        // 创建默认会话配置
        Map<String, String> defaultConfig = new HashMap<>();
        defaultConfig.put("personality", "友好活泼");
        defaultConfig.put("language", "中文");
        defaultConfig.put("response_style", "balanced");
        sessionConfigurations.put(sessionId, defaultConfig);
        
        log.info("会话创建成功，会话ID: {}", sessionId);
        return sessionId;
    }
    
    @Override
    public List<ChatMessage> getSessionHistory(String sessionId, int limit, int offset) {
        log.info("获取会话历史，会话ID: {}, 限制: {}, 偏移: {}", sessionId, limit, offset);
        
        // 验证会话是否存在
        if (!sessionDatabase.containsKey(sessionId)) {
            log.warn("会话不存在，会话ID: {}", sessionId);
            return new ArrayList<>();
        }
        
        List<ChatMessage> sessionMessages = sessionDatabase.get(sessionId);
        
        // 计算返回范围
        int startIndex = Math.min(offset, sessionMessages.size());
        int endIndex = Math.min(startIndex + limit, sessionMessages.size());
        
        if (startIndex >= endIndex) {
            return new ArrayList<>();
        }
        
        return sessionMessages.subList(startIndex, endIndex);
    }
    
    @Override
    public ChatMessage provideIntelligentSuggestion(String userId, Map<String, Double> currentLocation, 
                                                  String currentTime, Map<String, Object> context) {
        log.info("提供智能建议，用户ID: {}, 时间: {}", userId, currentTime);
        
        ChatMessage suggestionMessage = new ChatMessage();
        suggestionMessage.setMessageId("SUGGEST_" + UUID.randomUUID().toString());
        suggestionMessage.setSenderType("ai");
        suggestionMessage.setSendTime(new Date());
        suggestionMessage.setStatus("sent");
        suggestionMessage.setMessageType("recommendation");
        suggestionMessage.setLocation(currentLocation);
        
        // 基于上下文生成智能建议
        StringBuilder suggestionContent = new StringBuilder();
        
        // 添加时间相关建议
        int hour = Integer.parseInt(currentTime.split(":")[0]);
        if (hour >= 6 && hour < 10) {
            suggestionContent.append("早上好！根据当前时间，我建议您可以");
            if (context.containsKey("has_breakfast") && !(Boolean)context.get("has_breakfast")) {
                suggestionContent.append("先去附近的早餐店品尝当地特色早餐。");
            } else {
                suggestionContent.append("前往一些早晨人少的景点，比如公园或历史街区，享受宁静的早晨时光。");
            }
        } else if (hour >= 10 && hour < 14) {
            suggestionContent.append("中午好！建议您");
            suggestionContent.append("在午餐时间尝试当地特色美食，推荐选择人气较高但不太拥挤的餐厅。");
        } else if (hour >= 14 && hour < 18) {
            suggestionContent.append("下午好！这个时间段");
            if (context.containsKey("weather") && "sunny".equals(context.get("weather"))) {
                suggestionContent.append("阳光明媚，非常适合参观户外景点，但请注意防晒。");
            } else {
                suggestionContent.append("可能有些热，建议您参观博物馆、艺术馆等室内景点，或者在咖啡馆休息。");
            }
        } else {
            suggestionContent.append("晚上好！晚上您可以");
            suggestionContent.append("欣赏城市夜景，品尝当地夜生活美食，或者参加一些夜间文化活动。");
        }
        
        // 添加位置相关建议
        if (currentLocation != null) {
            suggestionContent.append("\n根据您的位置，附近有几个值得一游的地方，需要我为您详细介绍吗？");
        }
        
        suggestionMessage.setContent(suggestionContent.toString());
        
        return suggestionMessage;
    }
    
    @Override
    public ChatMessage recommendAttractions(String query, Map<String, Double> location, 
                                          Map<String, Object> preferences, int numberOfRecommendations) {
        log.info("推荐景点，查询: {}, 数量: {}", query, numberOfRecommendations);
        
        ChatMessage recommendationMessage = new ChatMessage();
        recommendationMessage.setMessageId("REC_" + UUID.randomUUID().toString());
        recommendationMessage.setSenderType("ai");
        recommendationMessage.setSendTime(new Date());
        recommendationMessage.setStatus("sent");
        recommendationMessage.setMessageType("recommendation");
        
        // 生成景点推荐内容
        StringBuilder content = new StringBuilder("根据您的需求，我为您推荐以下景点：\n\n");
        
        // 基于查询内容生成不同的推荐
        List<Map<String, String>> recommendations = generateAttractionRecommendations(query, numberOfRecommendations);
        
        for (int i = 0; i < recommendations.size(); i++) {
            Map<String, String> attraction = recommendations.get(i);
            content.append(i + 1).append(". ")
                   .append(attraction.get("name")).append("\n")
                   .append("   - 简介：").append(attraction.get("description")).append("\n")
                   .append("   - 推荐理由：").append(attraction.get("reason")).append("\n")
                   .append("   - 预计游览时间：").append(attraction.get("duration")).append("\n\n");
        }
        
        content.append("这些景点都很适合您的旅行需求，需要了解更多详情或者导航信息吗？");
        
        recommendationMessage.setContent(content.toString());
        
        // 添加附加信息
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("attractions", recommendations);
        additionalInfo.put("based_on_query", query);
        additionalInfo.put("location_based", location != null);
        recommendationMessage.setAdditionalInfo(additionalInfo);
        
        return recommendationMessage;
    }
    
    @Override
    public ChatMessage provideEmotionalSupport(String userId, double sentimentScore, 
                                          List<ChatMessage> recentMessages) {
        log.info("提供情绪支持，用户ID: {}, 情绪评分: {}", userId, sentimentScore);
        
        ChatMessage supportMessage = new ChatMessage();
        supportMessage.setMessageId("SUPPORT_" + UUID.randomUUID().toString());
        supportMessage.setSenderType("ai");
        supportMessage.setSendTime(new Date());
        supportMessage.setStatus("sent");
        supportMessage.setMessageType("text");
        
        StringBuilder content = new StringBuilder();
        
        // 根据情绪评分提供不同的支持信息
        if (sentimentScore > 0.5) {
            // 积极情绪
            content.append("看到您玩得很开心，我也感到非常高兴！");
            content.append("\n旅行中最美好的就是这些快乐时刻，希望您继续享受这段旅程！");
        } else if (sentimentScore > 0) {
            // 中性情绪
            content.append("旅行有时就是这样，有起有落，但每一段经历都是宝贵的回忆。");
            content.append("\n如果您需要任何帮助或者想聊点什么，我随时在这里陪伴您。");
        } else {
            // 消极情绪
            content.append("我注意到您似乎有些不开心，旅行中遇到不顺心的事情是很正常的。");
            content.append("\n您愿意和我分享一下发生了什么吗？也许我可以帮您一起解决问题。");
            content.append("\n或者，我可以为您推荐一些放松心情的活动，帮助您重新找回旅行的乐趣。");
        }
        
        // 分析最近消息，提取关键词提供更针对性的支持
        if (!recentMessages.isEmpty()) {
            String latestMessage = recentMessages.get(recentMessages.size() - 1).getContent().toLowerCase();
            if (latestMessage.contains("累") || latestMessage.contains("疲惫")) {
                content.append("\n\n旅行中适当休息很重要，您可以考虑在酒店休息一下，或者找个安静的咖啡馆放松身心。");
            } else if (latestMessage.contains("贵") || latestMessage.contains("贵了")) {
                content.append("\n\n我理解您的感受，旅游地区的消费有时确实较高。您想了解一些性价比更高的选择吗？");
            }
        }
        
        supportMessage.setContent(content.toString());
        
        // 设置情感标签
        if (sentimentScore > 0.5) {
            supportMessage.setSentiment("positive");
        } else if (sentimentScore > 0) {
            supportMessage.setSentiment("neutral");
        } else {
            supportMessage.setSentiment("negative");
        }
        
        return supportMessage;
    }
    
    @Override
    public ChatMessage answerLocationQuestion(String location, String question, String language) {
        log.info("回答地点问题，地点: {}, 问题: {}, 语言: {}", location, question, language);
        
        ChatMessage answerMessage = new ChatMessage();
        answerMessage.setMessageId("ANSWER_" + UUID.randomUUID().toString());
        answerMessage.setSenderType("ai");
        answerMessage.setSendTime(new Date());
        answerMessage.setStatus("sent");
        answerMessage.setMessageType("information");
        
        StringBuilder content = new StringBuilder();
        content.append("关于").append(location).append("，我可以为您提供以下信息：\n\n");
        
        // 根据问题类型生成回答
        String questionLower = question.toLowerCase();
        if (questionLower.contains("怎么去") || questionLower.contains("交通")) {
            content.append("交通信息：\n");
            content.append("- 公共交通：您可以乘坐地铁、公交等公共交通工具前往。\n");
            content.append("- 出租车：从市中心乘坐出租车大约需要20-30分钟，费用约30-50元。\n");
            content.append("- 自驾：有专门的停车场，但节假日可能较为拥挤，建议提前规划。\n");
        } else if (questionLower.contains("门票") || questionLower.contains("价格")) {
            content.append("门票信息：\n");
            content.append("- 成人票价：约100元\n");
            content.append("- 学生票价：凭学生证享受半价优惠\n");
            content.append("- 开放时间：通常为08:00-17:30\n");
            content.append("- 建议提前在官方渠道购票，避免排队\n");
        } else if (questionLower.contains("好玩") || questionLower.contains("推荐")) {
            content.append("游览建议：\n");
            content.append("- 必看景点：景区内有多个著名景点，建议重点游览。\n");
            content.append("- 游览时间：建议预留3-4小时，可以充分体验景区特色。\n");
            content.append("- 最佳季节：春秋两季气候宜人，是游览的最佳时节。\n");
            content.append("- 小贴士：避开节假日人流高峰，选择工作日参观体验更佳。\n");
        } else {
            // 默认回答
            content.append("根据我的了解，").append(location).append("是一个很有特色的地方。\n");
            content.append("它以独特的自然风光/历史文化闻名，每年吸引大量游客前来参观。\n");
            content.append("您可以在这里体验当地的风土人情，品尝特色美食，留下美好的旅行回忆。\n");
        }
        
        content.append("\n如果您有更具体的问题，欢迎随时告诉我，我会为您提供更详细的信息！");
        
        answerMessage.setContent(content.toString());
        
        // 添加相关标签
        Map<String, String> tags = new HashMap<>();
        tags.put("location", location);
        tags.put("question_type", getQuestionType(question));
        tags.put("language", language);
        answerMessage.setTags(tags);
        
        return answerMessage;
    }
    
    @Override
    public ChatMessage generateTripSummary(String userId, ItineraryPlanVO itinerary, boolean includePhotos) {
        log.info("生成旅行总结，用户ID: {}, 城市: {}, 天数: {}", userId, itinerary.getCity(), itinerary.getDays());
        
        ChatMessage summaryMessage = new ChatMessage();
        summaryMessage.setMessageId("SUMMARY_" + UUID.randomUUID().toString());
        summaryMessage.setSenderType("ai");
        summaryMessage.setSendTime(new Date());
        summaryMessage.setStatus("sent");
        summaryMessage.setMessageType("text");
        
        StringBuilder content = new StringBuilder();
        content.append("亲爱的旅行者，您的")
               .append(itinerary.getCity()).append(itinerary.getDays()).append("天之旅总结如下：\n\n");
        
        // 添加行程概览
        content.append("📋 行程概览\n");
        content.append("- 目的地：").append(itinerary.getCity()).append("\n");
        content.append("- 行程天数：").append(itinerary.getDays()).append("天\n");
        // 使用默认值替代不存在的getTravelStyle方法
        content.append("- 旅行风格：平衡型\n");
        // 使用estimatedCost替代不存在的getAverageCost方法
        double cost = itinerary.getEstimatedCost() > 0 ? itinerary.getEstimatedCost() : 2000.0;
        content.append("- 平均花费：约").append(cost).append("元\n\n");
        
        // 添加每日行程摘要
        content.append("📅 每日行程摘要\n");
        if (itinerary.getDailyItineraries() != null && !itinerary.getDailyItineraries().isEmpty()) {
            for (int i = 0; i < itinerary.getDailyItineraries().size(); i++) {
                ItineraryPlanVO.DailyItinerary day = itinerary.getDailyItineraries().get(i);
                content.append("第").append(i + 1).append("天：\n");
                content.append("  - 主要活动：");
                // 简化处理，实际应遍历day.getActivities()
                content.append("参观多个景点，体验当地文化\n");
                // 使用estimatedCost替代不存在的averageCost，并使用不同变量名避免重复定义
                double dailyCost = itinerary.getEstimatedCost() > 0 ? itinerary.getEstimatedCost() : 2000.0;
                content.append("  - 预计花费：约").append(dailyCost / itinerary.getDays()).append("元\n");
            }
        }
        
        // 添加特色亮点
        content.append("\n✨ 行程亮点\n");
        content.append("- 文化体验：探索当地历史文化，参观多处历史遗迹\n");
        content.append("- 美食之旅：品尝多种当地特色美食，满足味蕾享受\n");
        content.append("- 自然风光：欣赏壮丽的自然景观，留下美好回忆\n");
        content.append("- 人文交流：与当地居民互动，了解真实的本地生活\n");
        
        // 添加小贴士
        content.append("\n💡 旅行小贴士\n");
        content.append("- 最佳出行季节：春秋两季气候宜人，是游览的最佳时节\n");
        content.append("- 必备物品：建议携带舒适的步行鞋、防晒用品和常用药品\n");
        content.append("- 当地特色：不要错过当地的特色手工艺品和纪念品\n");
        content.append("- 安全提示：保管好个人财物，注意交通安全\n");
        
        // 添加结束语
        content.append("\n希望您在这次旅行中有美好的体验和珍贵的回忆！\n");
        content.append("期待下次为您规划更多精彩旅程！");
        
        summaryMessage.setContent(content.toString());
        
        // 如果需要包含照片信息
        if (includePhotos) {
            Map<String, Object> photoInfo = new HashMap<>();
            photoInfo.put("photos_available", true);
            photoInfo.put("photo_categories", Arrays.asList("景点风光", "美食特写", "人文纪实"));
            summaryMessage.setAdditionalInfo(photoInfo);
        }
        
        return summaryMessage;
    }
    
    @Override
    public Map<String, Object> getUserChatPreferences(String userId) {
        log.info("获取用户聊天偏好，用户ID: {}", userId);
        
        // 如果用户偏好不存在，返回默认偏好
        if (!userPreferencesDatabase.containsKey(userId)) {
            Map<String, Object> defaultPreferences = new HashMap<>();
            defaultPreferences.put("preferred_language", "中文");
            defaultPreferences.put("response_style", "balanced");
            defaultPreferences.put("notification_enabled", true);
            defaultPreferences.put("interest_topics", Arrays.asList("美食", "历史", "自然风光"));
            
            userPreferencesDatabase.put(userId, defaultPreferences);
            return defaultPreferences;
        }
        
        return userPreferencesDatabase.get(userId);
    }
    
    @Override
    public Map<String, Object> updateUserChatPreferences(String userId, Map<String, Object> preferences) {
        log.info("更新用户聊天偏好，用户ID: {}", userId);
        
        // 获取现有偏好
        Map<String, Object> existingPreferences = getUserChatPreferences(userId);
        
        // 更新偏好
        existingPreferences.putAll(preferences);
        
        // 保存更新后的偏好
        userPreferencesDatabase.put(userId, existingPreferences);
        
        log.info("用户偏好更新成功");
        return existingPreferences;
    }
    
    @Override
    public boolean setBuddyPersonality(String sessionId, Map<String, String> personalityTraits) {
        log.info("设置AI旅行搭子性格，会话ID: {}", sessionId);
        
        // 验证会话是否存在
        if (!sessionConfigurations.containsKey(sessionId)) {
            log.warn("会话不存在，无法设置性格，会话ID: {}", sessionId);
            return false;
        }
        
        // 验证性格类型是否有效
        if (personalityTraits.containsKey("personality") && 
            !availablePersonalities.contains(personalityTraits.get("personality"))) {
            log.warn("无效的性格类型: {}", personalityTraits.get("personality"));
            return false;
        }
        
        // 更新会话配置
        sessionConfigurations.get(sessionId).putAll(personalityTraits);
        
        log.info("AI旅行搭子性格设置成功");
        return true;
    }
    
    @Override
    public Set<String> getAvailablePersonalities() {
        return new HashSet<>(availablePersonalities);
    }
    
    @Override
    public ChatMessage processMultimodalInput(ChatMessage message) {
        log.info("处理多模态输入，消息ID: {}", message.getMessageId());
        
        ChatMessage response = new ChatMessage();
        response.setMessageId("MULTIMODAL_" + UUID.randomUUID().toString());
        response.setSessionId(message.getSessionId());
        response.setSenderType("ai");
        response.setSendTime(new Date());
        response.setStatus("sent");
        
        // 根据多模态输入类型生成不同的回复
        if ("image".equals(message.getMessageType()) && message.getAdditionalInfo() != null) {
            // 处理图片输入
            response.setMessageType("text");
            response.setContent("我看到您分享了一张图片！这看起来很有趣。这是您在旅行中拍摄的吗？您想了解这个地方的更多信息吗？");
            
            // 在实际应用中，这里应该有更复杂的图片分析逻辑
            Map<String, Object> imageResponseInfo = new HashMap<>();
            imageResponseInfo.put("image_analyzed", true);
            imageResponseInfo.put("suggested_actions", Arrays.asList("识别地点", "提供相关信息", "查找类似景点"));
            response.setAdditionalInfo(imageResponseInfo);
        } else if ("audio".equals(message.getMessageType())) {
            // 处理音频输入
            response.setMessageType("text");
            response.setContent("我已收到您的语音消息！在实际应用中，这里会有语音识别和处理逻辑。请告诉我您需要什么帮助？");
        } else {
            // 处理混合输入或默认情况
            response.setMessageType("text");
            response.setContent("感谢您的多模态输入！我已收到您的消息，正在为您处理。您有什么具体需要帮助的吗？");
        }
        
        return response;
    }
    
    @Override
    public ChatMessage generateWeatherAlert(Map<String, Double> location, ItineraryPlanVO tripPlan) {
        log.info("生成天气提醒，地点: {}, 行程城市: {}", location, tripPlan.getCity());
        
        ChatMessage weatherAlert = new ChatMessage();
        weatherAlert.setMessageId("WEATHER_" + UUID.randomUUID().toString());
        weatherAlert.setSenderType("ai");
        weatherAlert.setSendTime(new Date());
        weatherAlert.setStatus("sent");
        weatherAlert.setMessageType("system");
        weatherAlert.setLocation(location);
        
        // 模拟天气信息
        String weatherCondition = "晴朗"; // 在实际应用中，这里应该调用天气API
        String temperatureRange = "18-25°C";
        String suggestion = "天气很好，适合户外活动";
        
        StringBuilder content = new StringBuilder();
        content.append("⚠️ 天气提醒 ⚠️\n\n");
        content.append("目的地：").append(tripPlan.getCity()).append("\n");
        content.append("当前天气：").append(weatherCondition).append("，温度：").append(temperatureRange).append("\n\n");
        content.append("💡 今日建议：\n");
        content.append("- ").append(suggestion).append("\n");
        
        // 根据天气提供不同的行程建议
        if (weatherCondition.contains("雨")) {
            content.append("- 建议调整行程，选择室内景点\n");
            content.append("- 记得携带雨具\n");
        } else if (weatherCondition.contains("晴") && temperatureRange.contains("30")) {
            content.append("- 天气炎热，注意防晒和补水\n");
            content.append("- 建议避免正午时分的户外活动\n");
        } else {
            content.append("- 非常适合按照原定行程游览\n");
            content.append("- 记得带上相机，记录美好瞬间\n");
        }
        
        content.append("\n祝您旅途愉快！");
        
        weatherAlert.setContent(content.toString());
        
        // 添加天气相关标签
        Map<String, String> tags = new HashMap<>();
        tags.put("alert_type", "weather");
        tags.put("priority", "normal");
        weatherAlert.setTags(tags);
        
        return weatherAlert;
    }
    
    @Override
    public boolean closeSession(String sessionId) {
        log.info("关闭会话，会话ID: {}", sessionId);
        
        // 检查会话是否存在
        if (!sessionDatabase.containsKey(sessionId)) {
            log.warn("会话不存在，无法关闭，会话ID: {}", sessionId);
            return false;
        }
        
        // 移除会话及其配置
        sessionDatabase.remove(sessionId);
        sessionConfigurations.remove(sessionId);
        
        log.info("会话关闭成功，会话ID: {}", sessionId);
        return true;
    }
    
    @Override
    public Map<String, Object> getSessionStatistics(String sessionId) {
        log.info("获取会话统计信息，会话ID: {}", sessionId);
        
        // 验证会话是否存在
        if (!sessionDatabase.containsKey(sessionId)) {
            log.warn("会话不存在，无法获取统计信息，会话ID: {}", sessionId);
            return new HashMap<>();
        }
        
        List<ChatMessage> messages = sessionDatabase.get(sessionId);
        
        // 计算统计信息
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total_messages", messages.size());
        
        // 计算用户消息数和AI消息数
        long userMessagesCount = messages.stream()
                .filter(msg -> "user".equals(msg.getSenderType()))
                .count();
        long aiMessagesCount = messages.stream()
                .filter(msg -> "ai".equals(msg.getSenderType()))
                .count();
        
        statistics.put("user_messages", userMessagesCount);
        statistics.put("ai_messages", aiMessagesCount);
        
        // 获取会话时长
        if (!messages.isEmpty()) {
            Date firstMessageTime = messages.get(0).getSendTime();
            Date lastMessageTime = messages.get(messages.size() - 1).getSendTime();
            long durationMs = lastMessageTime.getTime() - firstMessageTime.getTime();
            statistics.put("session_duration_ms", durationMs);
            statistics.put("first_message_time", firstMessageTime);
            statistics.put("last_message_time", lastMessageTime);
        }
        
        // 计算消息类型分布
        Map<String, Long> messageTypeDistribution = messages.stream()
                .collect(Collectors.groupingBy(ChatMessage::getMessageType, Collectors.counting()));
        statistics.put("message_type_distribution", messageTypeDistribution);
        
        return statistics;
    }
    
    @Override
    public ChatMessage handleEmergency(String userId, String emergencyType, Map<String, Double> location) {
        log.info("处理紧急情况，用户ID: {}, 紧急类型: {}", userId, emergencyType);
        
        ChatMessage emergencyResponse = new ChatMessage();
        emergencyResponse.setMessageId("EMERGENCY_" + UUID.randomUUID().toString());
        emergencyResponse.setSenderType("system");
        emergencyResponse.setSendTime(new Date());
        emergencyResponse.setStatus("sent");
        emergencyResponse.setMessageType("system");
        emergencyResponse.setLocation(location);
        
        StringBuilder content = new StringBuilder();
        content.append("🚨 紧急情况处理 🚨\n\n");
        
        // 根据紧急情况类型提供不同的处理建议
        switch (emergencyType.toLowerCase()) {
            case "health":
                content.append("健康紧急情况\n");
                content.append("- 立即拨打急救电话：120\n");
                content.append("- 附近医院信息：在实际应用中，这里会显示最近的医院位置和联系方式\n");
                content.append("- 保持冷静，等待专业救援\n");
                break;
            case "lost":
                content.append("迷路情况\n");
                content.append("- 请站在原地不要移动\n");
                content.append("- 您的当前位置已被记录\n");
                content.append("- 建议联系当地警方或景区工作人员\n");
                break;
            case "theft":
                content.append("财物丢失\n");
                content.append("- 立即拨打报警电话：110\n");
                content.append("- 记录丢失物品清单\n");
                content.append("- 联系当地警方报案\n");
                break;
            default:
                content.append("紧急情况\n");
                content.append("- 保持冷静，评估情况\n");
                content.append("- 根据具体情况联系相关紧急服务\n");
                content.append("- 确保人身安全是首要任务\n");
        }
        
        content.append("\n我们正在为您提供支持，请保持通讯畅通。");
        
        emergencyResponse.setContent(content.toString());
        
        // 设置紧急标签
        Map<String, String> tags = new HashMap<>();
        tags.put("alert_type", "emergency");
        tags.put("priority", "high");
        tags.put("emergency_type", emergencyType);
        emergencyResponse.setTags(tags);
        
        return emergencyResponse;
    }
    
    // ===== 辅助方法 =====
    
    /**
     * 生成景点推荐内容
     */
    private String generateAttractionRecommendationContent(String query) {
        // 模拟推荐内容生成
        if (query.contains("故宫")) {
            return "故宫是中国明清两代的皇家宫殿，非常值得一游！建议您安排半天到一天的时间参观，最好提前在网上购票以避免排队。如果您对历史感兴趣，可以考虑请一位专业导游。您想了解更多关于故宫的详细信息吗？";
        } else if (query.contains("长城")) {
            return "长城是世界文化遗产，北京周边有多个长城景点可供选择，如八达岭、慕田峪和司马台等。八达岭最为著名但游客较多，慕田峪相对来说人少景美，司马台则适合寻求刺激的游客。您更偏好哪个类型的长城体验呢？";
        } else {
            return "很高兴为您推荐景点！请问您具体想了解哪个城市或地区的景点？另外，您有什么特别的兴趣偏好吗，比如自然风光、历史文化、美食购物等，这样我可以为您提供更有针对性的推荐。";
        }
    }
    
    /**
     * 生成天气信息内容
     */
    private String generateWeatherInformationContent(String query) {
        // 模拟天气信息生成
        return "今天天气晴好，温度适宜，非常适合外出游玩。明天可能会有小雨，建议您随身携带雨具。您需要了解未来几天的详细天气预报，或者有什么其他天气相关的问题吗？";
    }
    
    /**
     * 生成美食推荐内容
     */
    private String generateFoodRecommendationContent(String query) {
        // 模拟美食推荐生成
        return "美食推荐：当地有很多特色美食值得尝试，比如烤鸭、炸酱面、豆汁焦圈等。如果您喜欢，我可以为您推荐几家口碑不错的餐厅，或者介绍一些当地的小吃街。您有什么特别的饮食偏好或忌口吗？";
    }
    
    /**
     * 生成帮助内容
     */
    private String generateHelpContent() {
        return "我是您的AI旅行搭子，很高兴为您提供帮助！以下是我可以为您提供的服务：\n\n1. 景点推荐和介绍\n2. 美食攻略和餐厅推荐\n3. 交通路线规划\n4. 旅行天气查询\n5. 行程安排建议\n6. 当地文化和历史知识\n\n您可以直接告诉我您的需求，或者点击下方的快速回复按钮选择服务类型。";
    }
    
    /**
     * 生成默认回复内容
     */
    private String generateDefaultResponseContent(String query) {
        // 模拟默认回复生成
        String[] responses = {
            "我理解您的问题，让我为您提供相关信息...",
            "这个问题很有趣！根据我的了解...",
            "感谢您的提问，关于这个方面...",
            "很高兴能帮助您，以下是您需要的信息...",
            "好的，我来为您解答..."
        };
        
        // 随机选择一个回复
        int randomIndex = new Random().nextInt(responses.length);
        return responses[randomIndex];
    }
    
    /**
     * 生成景点推荐列表
     */
    private List<Map<String, String>> generateAttractionRecommendations(String query, int count) {
        List<Map<String, String>> recommendations = new ArrayList<>();
        
        // 模拟推荐数据
        Map<String, Map<String, String>> attractionDatabase = new HashMap<>();
        
        Map<String, String> attraction1 = new HashMap<>();
        attraction1.put("name", "故宫博物院");
        attraction1.put("description", "中国明清两代的皇家宫殿，世界上现存规模最大、保存最为完整的木质结构古建筑之一。");
        attraction1.put("reason", "历史文化价值极高，是了解中国古代皇家生活的最佳场所。");
        attraction1.put("duration", "3-4小时");
        attractionDatabase.put("故宫", attraction1);
        
        Map<String, String> attraction2 = new HashMap<>();
        attraction2.put("name", "八达岭长城");
        attraction2.put("description", "明长城中保存最好的一段，也是最具代表性的一段。");
        attraction2.put("reason", "交通便利，设施完善，是游览长城的首选地。");
        attraction2.put("duration", "4-5小时");
        attractionDatabase.put("长城", attraction2);
        
        Map<String, String> attraction3 = new HashMap<>();
        attraction3.put("name", "颐和园");
        attraction3.put("description", "中国清朝时期皇家园林，是以昆明湖、万寿山为基址的大型山水园林。");
        attraction3.put("reason", "园林景观优美，融合了中国古典园林设计精华，适合休闲游览。");
        attraction3.put("duration", "2-3小时");
        attractionDatabase.put("颐和园", attraction3);
        
        // 根据查询选择推荐
        for (String key : attractionDatabase.keySet()) {
            if (query.contains(key) && recommendations.size() < count) {
                recommendations.add(attractionDatabase.get(key));
            }
        }
        
        // 如果推荐数量不足，添加默认推荐
        if (recommendations.size() < count) {
            // 添加一些默认推荐
            Map<String, String> defaultAttraction = new HashMap<>();
            defaultAttraction.put("name", "天坛公园");
            defaultAttraction.put("description", "明清两代皇帝祭天、祈谷的圣地。");
            defaultAttraction.put("reason", "建筑风格独特，历史意义重大，是北京必游景点之一。");
            defaultAttraction.put("duration", "1-2小时");
            recommendations.add(defaultAttraction);
        }
        
        return recommendations.subList(0, Math.min(count, recommendations.size()));
    }
    
    /**
     * 判断问题类型
     */
    private String getQuestionType(String question) {
        String lowerQuestion = question.toLowerCase();
        if (lowerQuestion.contains("怎么去") || lowerQuestion.contains("交通")) {
            return "transportation";
        } else if (lowerQuestion.contains("门票") || lowerQuestion.contains("价格")) {
            return "ticket";
        } else if (lowerQuestion.contains("好吃") || lowerQuestion.contains("美食")) {
            return "food";
        } else if (lowerQuestion.contains("历史") || lowerQuestion.contains("文化")) {
            return "history";
        } else {
            return "general";
        }
    }
}