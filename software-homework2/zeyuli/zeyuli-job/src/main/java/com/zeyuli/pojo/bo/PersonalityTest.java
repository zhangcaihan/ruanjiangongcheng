package com.zeyuli.pojo.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 旅行人格测试类
 * 包含测试问题和人格类型信息
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Data
public class PersonalityTest {
    /**
     * 测试ID
     */
    private String testId;
    
    /**
     * 测试名称
     */
    private String testName;
    
    /**
     * 测试问题列表
     */
    private List<TestQuestion> questions;
    
    /**
     * 人格类型映射表
     */
    private Map<String, PersonalityType> personalityTypes;
    
    /**
     * 测试问题类
     */
    @Data
    public static class TestQuestion {
        /**
         * 问题ID
         */
        private String questionId;
        
        /**
         * 问题内容
         */
        private String content;
        
        /**
         * 选项列表
         */
        private List<TestOption> options;
        
        /**
         * 问题权重
         */
        private int weight;
    }
    
    /**
     * 测试选项类
     */
    @Data
    public static class TestOption {
        /**
         * 选项ID
         */
        private String optionId;
        
        /**
         * 选项内容
         */
        private String content;
        
        /**
         * 选项对应的人格特质得分映射
         * 键为人格特质代码，值为得分
         */
        private Map<String, Integer> traitScores;
    }
    
    /**
     * 人格类型类
     */
    @Data
    public static class PersonalityType {
        /**
         * 人格类型代码
         */
        private String typeCode;
        
        /**
         * 人格类型名称
         */
        private String typeName;
        
        /**
         * 人格类型描述
         */
        private String description;
        
        /**
         * 旅行偏好描述
         */
        private String travelPreference;
        
        /**
         * 推荐景点类型权重
         * 键为景点类型，值为权重
         */
        private Map<String, Integer> recommendedAttractionTypes;
        
        /**
         * 推荐交通方式权重
         */
        private Map<String, Integer> recommendedTransportationTypes;
        
        /**
         * 每日推荐景点数量范围
         */
        private AttractionCountRange dailyAttractionCountRange;
        
        /**
         * 每日行程节奏描述
         */
        private String dailyPace;
        
        /**
         * 推荐住宿类型
         */
        private List<String> recommendedAccommodationTypes;
        
        /**
         * 特色行程元素
         */
        private List<String> uniqueItineraryElements;
    }
    
    /**
     * 景点数量范围类
     */
    @Data
    public static class AttractionCountRange {
        /**
         * 最小数量
         */
        private int min;
        
        /**
         * 最大数量
         */
        private int max;
    }
}