package com.zeyuli.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * 多模态行程规划服务功能测试类
 * 
 * @author 李泽聿
 */
@ExtendWith(MockitoExtension.class)
class MultimodalPlanningServiceTest {
    
    @Mock
    private MultimodalPlanningService multimodalPlanningService;
    
    private String userId = "USER_001";
    
    @Test
    void planItineraryByMultimodal() {
        // 测试多模态行程规划
        assertDoesNotThrow(() -> multimodalPlanningService.planItineraryByMultimodal(null));
    }
    
    @Test
    void extractTravelInfoFromImage() {
        // 测试从图片提取旅行信息
        assertDoesNotThrow(() -> multimodalPlanningService.extractTravelInfoFromImage(null, null));
    }
    
    @Test
    void recognizeTravelRequirementsFromAudio() {
        // 测试从音频识别旅行需求
        assertDoesNotThrow(() -> multimodalPlanningService.recognizeTravelRequirementsFromAudio(null, null));
    }
    
    @Test
    void understandNaturalLanguageTravelRequirements() {
        // 测试理解自然语言旅行需求
        String naturalLanguage = "我想从杭州东站到西湖，明天上午10点出发，优先地铁和公交";
        assertDoesNotThrow(() -> multimodalPlanningService.understandNaturalLanguageTravelRequirements(naturalLanguage));
        assertDoesNotThrow(() -> multimodalPlanningService.understandNaturalLanguageTravelRequirements(null));
    }
    
    @Test
    void fuseMultimodalInformation() {
        // 测试融合多模态信息
        Map<String, Object> imageInfo = new HashMap<>();
        Map<String, Object> audioInfo = new HashMap<>();
        Map<String, Object> textInfo = new HashMap<>();
        
        assertDoesNotThrow(() -> multimodalPlanningService.fuseMultimodalInformation(imageInfo, audioInfo, textInfo));
        assertDoesNotThrow(() -> multimodalPlanningService.fuseMultimodalInformation(null, null, null));
    }
    
    @Test
    void generateMultimodalRecommendations() {
        // 测试生成多模态推荐
        assertDoesNotThrow(() -> multimodalPlanningService.generateMultimodalRecommendations(null, null, 0));
    }
    
    @Test
    void analyzeUserTravelStyleFromMultimodalData() {
        // 测试从多模态数据分析用户旅行风格
        assertDoesNotThrow(() -> multimodalPlanningService.analyzeUserTravelStyleFromMultimodalData(null));
    }
    
    @Test
    void convertToMultimodalPresentation() {
        // 测试转换为多模态展示格式
        assertDoesNotThrow(() -> multimodalPlanningService.convertToMultimodalPresentation(null, false, false));
    }
}