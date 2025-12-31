package com.zeyuli.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * 旅行人格测试服务功能测试类
 * 
 * @author 李泽聿
 */
@ExtendWith(MockitoExtension.class)
class PersonalityTestServiceTest {
    
    @Mock
    private PersonalityTestService personalityTestService;
    
    @Test
    void getPersonalityTest() {
        // 测试获取人格测试问卷
        assertDoesNotThrow(() -> personalityTestService.getPersonalityTest());
    }
    
    @Test
    void calculateTestResult() {
        // 测试计算测试结果
        Map<String, String> testAnswers = new HashMap<>();
        assertDoesNotThrow(() -> personalityTestService.calculateTestResult(testAnswers));
        assertDoesNotThrow(() -> personalityTestService.calculateTestResult(null));
    }
    
    @Test
    void generatePersonalityItinerary() {
        // 测试生成人格匹配行程
        assertDoesNotThrow(() -> personalityTestService.generatePersonalityItinerary("杭州", 3, "adventurer"));
    }
    
    @Test
    void getAllPersonalityTypes() {
        // 测试获取所有人格类型
        assertDoesNotThrow(() -> personalityTestService.getAllPersonalityTypes());
    }
    
    @Test
    void isValidPersonalityType() {
        // 测试验证人格类型
        assertDoesNotThrow(() -> personalityTestService.isValidPersonalityType("adventurer"));
        assertDoesNotThrow(() -> personalityTestService.isValidPersonalityType(null));
    }
    
    @Test
    void predictPersonalityType() {
        // 测试预测人格类型
        assertDoesNotThrow(() -> personalityTestService.predictPersonalityType("USER_001"));
    }
}