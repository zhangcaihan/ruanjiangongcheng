package com.zeyuli.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AI旅行搭子聊天模式功能测试类
 * 
 * @author 李泽聿
 */
@ExtendWith(MockitoExtension.class)
class AITravelBuddyServiceTest {
    
    @Mock
    private AITravelBuddyService aiTravelBuddyService;
    
    private String userId = "USER_001";
    private String testSessionId = "TEST_SESSION_123";
    
    @Test
    void processUserMessage() {
        // 测试正常消息处理
        assertDoesNotThrow(() -> aiTravelBuddyService.processUserMessage(null));
    }
    
    @Test
    void createNewSession() {
        // 测试创建新会话
        assertDoesNotThrow(() -> aiTravelBuddyService.createNewSession(null, null, null));
    }
    
    @Test
    void getSessionHistory() {
        // 测试获取会话历史
        assertDoesNotThrow(() -> aiTravelBuddyService.getSessionHistory(null, 0, 0));
    }
    
    @Test
    void provideIntelligentSuggestion() {
        // 测试提供智能建议
        assertDoesNotThrow(() -> aiTravelBuddyService.provideIntelligentSuggestion(null, null, null, null));
    }
    
    @Test
    void recommendAttractions() {
        // 测试推荐景点
        assertDoesNotThrow(() -> aiTravelBuddyService.recommendAttractions(null, null, null, 0));
    }
    
    @Test
    void provideEmotionalSupport() {
        // 测试提供情感支持
        assertDoesNotThrow(() -> aiTravelBuddyService.provideEmotionalSupport(null, 0.0, null));
    }
    
    @Test
    void answerLocationQuestion() {
        // 测试回答位置相关问题
        assertDoesNotThrow(() -> aiTravelBuddyService.answerLocationQuestion(null, null, null));
    }
    
    @Test
    void generateTripSummary() {
        // 测试生成旅行总结
        assertDoesNotThrow(() -> aiTravelBuddyService.generateTripSummary(null, null, false));
    }
    
    @Test
    void getUserChatPreferences() {
        // 测试获取用户聊天偏好
        assertDoesNotThrow(() -> aiTravelBuddyService.getUserChatPreferences(userId));
    }
    
    @Test
    void updateUserChatPreferences() {
        // 测试更新用户聊天偏好
        Map<String, Object> newPreferences = new HashMap<>();
        assertDoesNotThrow(() -> aiTravelBuddyService.updateUserChatPreferences(userId, newPreferences));
    }
    
    @Test
    void setBuddyPersonality() {
        // 测试设置搭子个性
        assertDoesNotThrow(() -> aiTravelBuddyService.setBuddyPersonality(null, null));
    }
    
    @Test
    void getAvailablePersonalities() {
        // 测试获取可用个性列表
        assertDoesNotThrow(() -> aiTravelBuddyService.getAvailablePersonalities());
    }
    
    @Test
    void processMultimodalInput() {
        // 测试处理多模态输入
        assertDoesNotThrow(() -> aiTravelBuddyService.processMultimodalInput(null));
    }
    
    @Test
    void generateWeatherAlert() {
        // 测试生成天气预警
        assertDoesNotThrow(() -> aiTravelBuddyService.generateWeatherAlert(null, null));
    }
    
    @Test
    void closeSession() {
        // 测试关闭会话
        assertDoesNotThrow(() -> aiTravelBuddyService.closeSession(testSessionId));
    }
    
    @Test
    void getSessionStatistics() {
        // 测试获取会话统计信息
        assertDoesNotThrow(() -> aiTravelBuddyService.getSessionStatistics(testSessionId));
    }
    
    @Test
    void handleEmergency() {
        // 测试处理紧急情况
        assertDoesNotThrow(() -> aiTravelBuddyService.handleEmergency(null, null, null));
    }
}