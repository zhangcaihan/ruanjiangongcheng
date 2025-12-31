package com.zeyuli.service;

import com.zeyuli.pojo.ChatMessage;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AI旅行搭子服务接口
 * 提供旅行过程中的智能聊天助手功能
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
public interface AITravelBuddyService {
    
    /**
     * 处理用户消息并生成AI回复
     * 
     * @param message 用户消息对象
     * @return AI生成的回复消息
     */
    ChatMessage processUserMessage(ChatMessage message);
    
    /**
     * 创建新的聊天会话
     * 
     * @param userId 用户ID
     * @param initialMessage 初始消息内容
     * @param userPreferences 用户偏好设置
     * @return 创建的会话ID
     */
    String createNewSession(String userId, String initialMessage, Map<String, Object> userPreferences);
    
    /**
     * 获取会话的消息历史
     * 
     * @param sessionId 会话ID
     * @param limit 返回消息数量限制
     * @param offset 消息偏移量
     * @return 消息历史列表
     */
    List<ChatMessage> getSessionHistory(String sessionId, int limit, int offset);
    
    /**
     * 根据用户旅行情况提供智能建议
     * 
     * @param userId 用户ID
     * @param currentLocation 当前位置信息
     * @param currentTime 当前时间
     * @param context 上下文信息
     * @return 智能建议消息
     */
    ChatMessage provideIntelligentSuggestion(String userId, Map<String, Double> currentLocation, 
                                           String currentTime, Map<String, Object> context);
    
    /**
     * 根据用户问题推荐景点
     * 
     * @param query 用户查询内容
     * @param location 当前位置
     * @param preferences 用户偏好
     * @param numberOfRecommendations 推荐数量
     * @return 包含推荐内容的聊天消息
     */
    ChatMessage recommendAttractions(String query, Map<String, Double> location, 
                                   Map<String, Object> preferences, int numberOfRecommendations);
    
    /**
     * 分析用户旅行情绪并提供相应支持
     * 
     * @param userId 用户ID
     * @param sentimentScore 情绪评分
     * @param recentMessages 近期消息列表
     * @return 情绪支持消息
     */
    ChatMessage provideEmotionalSupport(String userId, double sentimentScore, 
                                     List<ChatMessage> recentMessages);
    
    /**
     * 回答用户关于旅行地点的问题
     * 
     * @param location 地点名称或描述
     * @param question 用户问题
     * @param language 语言类型
     * @return 包含答案的聊天消息
     */
    ChatMessage answerLocationQuestion(String location, String question, String language);
    
    /**
     * 生成旅行总结和回顾
     * 
     * @param userId 用户ID
     * @param itinerary 相关行程
     * @param includePhotos 是否包含照片
     * @return 旅行总结消息
     */
    ChatMessage generateTripSummary(String userId, ItineraryPlanVO itinerary, boolean includePhotos);
    
    /**
     * 获取用户的旅行聊天偏好
     * 
     * @param userId 用户ID
     * @return 用户偏好设置
     */
    Map<String, Object> getUserChatPreferences(String userId);
    
    /**
     * 更新用户的聊天偏好设置
     * 
     * @param userId 用户ID
     * @param preferences 新的偏好设置
     * @return 更新后的偏好设置
     */
    Map<String, Object> updateUserChatPreferences(String userId, Map<String, Object> preferences);
    
    /**
     * 设置AI旅行搭子的性格特征
     * 
     * @param sessionId 会话ID
     * @param personalityTraits 性格特征设置
     * @return 是否设置成功
     */
    boolean setBuddyPersonality(String sessionId, Map<String, String> personalityTraits);
    
    /**
     * 获取可用的AI旅行搭子性格类型
     * 
     * @return 可用的性格类型集合
     */
    Set<String> getAvailablePersonalities();
    
    /**
     * 处理多模态输入（文本+图片+语音）
     * 
     * @param message 包含多模态内容的消息
     * @return 处理后的回复消息
     */
    ChatMessage processMultimodalInput(ChatMessage message);
    
    /**
     * 生成实时天气提醒
     * 
     * @param location 位置信息
     * @param tripPlan 旅行计划
     * @return 天气提醒消息
     */
    ChatMessage generateWeatherAlert(Map<String, Double> location, ItineraryPlanVO tripPlan);
    
    /**
     * 关闭指定会话
     * 
     * @param sessionId 会话ID
     * @return 是否关闭成功
     */
    boolean closeSession(String sessionId);
    
    /**
     * 获取会话统计信息
     * 
     * @param sessionId 会话ID
     * @return 统计信息
     */
    Map<String, Object> getSessionStatistics(String sessionId);
    
    /**
     * 检测并处理紧急情况
     * 
     * @param userId 用户ID
     * @param emergencyType 紧急情况类型
     * @param location 当前位置
     * @return 紧急情况处理消息
     */
    ChatMessage handleEmergency(String userId, String emergencyType, Map<String, Double> location);
}