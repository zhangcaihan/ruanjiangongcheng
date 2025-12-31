package com.zeyuli.service;

import com.zeyuli.pojo.bo.PersonalityTest;
import com.zeyuli.pojo.vo.PersonalityTestResultVO;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 旅行人格测试服务接口
 * 提供旅行人格测试相关的功能
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
public interface PersonalityTestService {
    /**
     * 获取三分钟旅行人格测试问卷
     * @return 旅行人格测试对象
     */
    PersonalityTest getPersonalityTest();
    
    /**
     * 根据用户答案计算人格测试结果
     * @param userAnswers 用户答案映射，键为问题ID，值为选项ID
     * @return 人格测试结果VO
     */
    PersonalityTestResultVO calculateTestResult(Map<String, String> userAnswers);
    
    /**
     * 根据人格类型生成推荐行程
     * @param city 目标城市
     * @param days 行程天数
     * @param personalityType 人格类型代码
     * @return 行程计划VO
     */
    ItineraryPlanVO generatePersonalityItinerary(String city, int days, String personalityType);
    
    /**
     * 获取所有可用的人格类型信息
     * @return 人格类型映射，键为人格类型代码，值为人格类型对象
     */
    Map<String, PersonalityTest.PersonalityType> getAllPersonalityTypes();
    
    /**
     * 验证人格类型是否有效
     * @param personalityType 人格类型代码
     * @return 是否有效
     */
    boolean isValidPersonalityType(String personalityType);
    
    /**
     * 根据用户历史行为预测可能的人格类型
     * @param userId 用户ID
     * @return 预测的人格类型代码和置信度映射
     */
    Map<String, Double> predictPersonalityType(String userId);
}