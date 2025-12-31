package com.zeyuli.pojo.vo;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 多模态规划结果类
 * 存储通过多模态输入规划得到的行程结果
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Data
public class MultimodalResult {
    
    /**
     * 结果ID
     */
    private String resultId;
    
    /**
     * 行程规划结果
     */
    private ItineraryPlanVO itinerary;
    
    /**
     * 推荐图片列表
     * 每个元素包含图片URL和描述
     */
    private List<Map<String, String>> recommendedImages;
    
    /**
     * 语音解说列表
     * 每个元素包含音频URL和描述
     */
    private List<Map<String, String>> audioGuides;
    
    /**
     * 结果匹配度
     * 表示该结果与用户需求的匹配程度，范围0-1
     */
    private Double matchScore;
    
    /**
     * 推荐理由
     */
    private List<String> recommendationReasons;
    
    /**
     * 亮点标签
     */
    private List<String> highlightTags;
    
    /**
     * 视觉化展示数据
     * 用于生成图表或地图可视化
     */
    private Map<String, Object> visualizationData;
    
    /**
     * 多媒体展示顺序
     * 指导前端如何排序展示不同模态的内容
     */
    private List<String> presentationOrder;
    
    /**
     * 结果生成时间
     */
    private Long generatedTime;
    
    /**
     * 主要输入模态
     * 表示主要基于哪种模态生成的结果
     */
    private String primaryModality;
    
    /**
     * 支持的交互方式
     * 如：edit（编辑）、share（分享）、save（保存）
     */
    private List<String> supportedInteractions;
    
    /**
     * 补充信息
     * 其他可能有用的信息
     */
    private Map<String, Object> additionalInformation;
    
    /**
     * 结果摘要
     * 简短描述结果的核心内容
     */
    private String resultSummary;
    
    /**
     * 可替代方案列表
     * 其他可能的行程建议
     */
    private List<MultimodalResult> alternativeResults;
    
    /**
     * 结果类型
     * 如：complete（完整行程）、partial（部分行程）、suggestion（建议）
     */
    private String resultType;
    
    /**
     * 用户反馈评分
     * 用户对该结果的评分，范围1-5
     */
    private Double userFeedbackScore;
    
    /**
     * 适用场景
     * 该结果适合的旅行场景描述
     */
    private List<String> applicableScenarios;
    
    /**
     * 媒体资源统计
     * 包含的图片、音频等资源数量
     */
    private Map<String, Integer> mediaResourceCount;
}