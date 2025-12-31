package com.zeyuli.controller;

import com.zeyuli.pojo.ChatMessage;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import com.zeyuli.service.AITravelBuddyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AI旅行搭子控制器
 * 处理AI旅行助手相关的HTTP请求
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/ai-travel-buddy")
// todo 仅测试用
@CrossOrigin
public class AITravelBuddyController {
    
    @Autowired
    private AITravelBuddyService aiTravelBuddyService;
    
    @ApiOperation(value = "处理用户消息", notes = "接收用户消息并生成AI回复")
    @PostMapping("/process-message")
    public ChatMessage processUserMessage(@RequestBody ChatMessage message) {
        return aiTravelBuddyService.processUserMessage(message);
    }
    
    @ApiOperation(value = "创建新会话", notes = "创建新的聊天会话")
    @PostMapping("/create-session")
    public String createNewSession(@RequestParam String userId,
                                 @RequestParam String initialMessage,
                                 @RequestBody(required = false) Map<String, Object> userPreferences) {
        return aiTravelBuddyService.createNewSession(userId, initialMessage, userPreferences);
    }
    
    @ApiOperation(value = "获取会话历史", notes = "获取指定会话的消息历史")
    @GetMapping("/session-history")
    public List<ChatMessage> getSessionHistory(@RequestParam String sessionId,
                                             @RequestParam(defaultValue = "50") int limit,
                                             @RequestParam(defaultValue = "0") int offset) {
        return aiTravelBuddyService.getSessionHistory(sessionId, limit, offset);
    }
    
    @ApiOperation(value = "获取智能建议", notes = "根据用户当前位置和时间提供智能建议")
    @PostMapping("/intelligent-suggestion")
    public ChatMessage provideIntelligentSuggestion(@RequestParam String userId,
                                                  @RequestBody Map<String, Double> currentLocation,
                                                  @RequestParam String currentTime,
                                                  @RequestBody(required = false) Map<String, Object> context) {
        return aiTravelBuddyService.provideIntelligentSuggestion(userId, currentLocation, currentTime, context);
    }
    
    @ApiOperation(value = "推荐景点", notes = "根据用户查询推荐相关景点")
    @PostMapping("/recommend-attractions")
    public ChatMessage recommendAttractions(@RequestParam String query,
                                          @RequestBody Map<String, Double> location,
                                          @RequestBody(required = false) Map<String, Object> preferences,
                                          @RequestParam(defaultValue = "5") int numberOfRecommendations) {
        return aiTravelBuddyService.recommendAttractions(query, location, preferences, numberOfRecommendations);
    }
    
    @ApiOperation(value = "生成旅行总结", notes = "生成用户旅行的总结和回顾")
    @PostMapping("/trip-summary")
    public ChatMessage generateTripSummary(@RequestParam String userId,
                                        @RequestBody ItineraryPlanVO itinerary,
                                        @RequestParam(defaultValue = "false") boolean includePhotos) {
        return aiTravelBuddyService.generateTripSummary(userId, itinerary, includePhotos);
    }
    
    @ApiOperation(value = "获取用户偏好", notes = "获取用户的旅行聊天偏好设置")
    @GetMapping("/user-preferences/{userId}")
    public Map<String, Object> getUserChatPreferences(@PathVariable String userId) {
        return aiTravelBuddyService.getUserChatPreferences(userId);
    }
    
    @ApiOperation(value = "更新用户偏好", notes = "更新用户的聊天偏好设置")
    @PutMapping("/user-preferences/{userId}")
    public Map<String, Object> updateUserChatPreferences(@PathVariable String userId,
                                                       @RequestBody Map<String, Object> preferences) {
        return aiTravelBuddyService.updateUserChatPreferences(userId, preferences);
    }
    
    @ApiOperation(value = "设置AI性格", notes = "设置AI旅行搭子的性格特征")
    @PostMapping("/set-personality/{sessionId}")
    public boolean setBuddyPersonality(@PathVariable String sessionId,
                                     @RequestBody Map<String, String> personalityTraits) {
        return aiTravelBuddyService.setBuddyPersonality(sessionId, personalityTraits);
    }
    
    @ApiOperation(value = "获取可用性格类型", notes = "获取系统支持的AI旅行搭子性格类型")
    @GetMapping("/available-personalities")
    public Set<String> getAvailablePersonalities() {
        return aiTravelBuddyService.getAvailablePersonalities();
    }
    
    @ApiOperation(value = "处理多模态输入", notes = "处理包含文本、图片、语音的多模态输入")
    @PostMapping("/process-multimodal")
    public ChatMessage processMultimodalInput(@RequestBody ChatMessage message) {
        return aiTravelBuddyService.processMultimodalInput(message);
    }
    
    @ApiOperation(value = "生成天气提醒", notes = "生成基于当前位置和旅行计划的天气提醒")
    @PostMapping("/weather-alert")
    public ChatMessage generateWeatherAlert(@RequestBody Map<String, Double> location,
                                         @RequestBody ItineraryPlanVO tripPlan) {
        return aiTravelBuddyService.generateWeatherAlert(location, tripPlan);
    }
    
    @ApiOperation(value = "关闭会话", notes = "关闭指定的聊天会话")
    @DeleteMapping("/close-session/{sessionId}")
    public boolean closeSession(@PathVariable String sessionId) {
        return aiTravelBuddyService.closeSession(sessionId);
    }
}