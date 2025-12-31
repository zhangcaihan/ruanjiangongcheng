package com.zeyuli.pojo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 旅行人格测试结果VO类
 * 存储测试结果和个性化旅行推荐信息
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@Data
public class PersonalityTestResultVO {
    /**
     * 测试ID
     */
    private String testId;
    
    /**
     * 用户答案映射
     * 键为问题ID，值为选项ID
     */
    private Map<String, String> userAnswers;
    
    /**
     * 人格特质得分映射
     * 键为人格特质代码，值为得分
     */
    private Map<String, Integer> traitScores;
    
    /**
     * 主导人格类型
     */
    private String dominantPersonalityType;
    
    /**
     * 主导人格类型描述
     */
    private String dominantPersonalityDescription;
    
    /**
     * 旅行偏好描述
     */
    private String travelPreference;
    
    /**
     * 旅行风格标签列表
     */
    private List<String> travelStyleTags;
    
    /**
     * 个性化旅行建议
     */
    private List<String> personalizedTips;
    
    /**
     * 景点偏好权重
     * 键为景点类型，值为权重
     */
    private Map<String, Integer> attractionPreferences;
    
    /**
     * 交通方式偏好权重
     */
    private Map<String, Integer> transportationPreferences;
    
    /**
     * 理想每日景点数量范围
     */
    private DailyAttractionCountRange idealDailyAttractionCount;
    
    /**
     * 理想行程节奏
     */
    private String idealPace;
    
    /**
     * 推荐住宿类型
     */
    private List<String> recommendedAccommodationTypes;
    
    /**
     * 特色行程元素
     */
    private List<String> uniqueItineraryElements;
    
    /**
     * 推荐的相似人格类型
     */
    private List<String> similarPersonalityTypes;
    
    /**
     * 人格测试完成时间戳
     */
    private long completedTimestamp;
    
    /**
     * 生成的行程计划ID（如果直接生成行程）
     */
    private String generatedItineraryId;
    
    /**
     * 每日景点数量范围类
     */
    @Data
    public static class DailyAttractionCountRange {
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