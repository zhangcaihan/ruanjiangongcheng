package com.zeyuli.pojo.vo;

import lombok.Data;
import java.util.Map;
import java.util.List;

/**
 * 多模态输入类
 * 用于接收多种输入形式（文字、图片、语音等）的行程规划请求
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Data
public class MultimodalInput {
    
    /**
     * 输入类型
     * 可选值：text（文本）、image（图片）、audio（语音）、mixed（混合）
     */
    private String inputType;
    
    /**
     * 文本输入内容
     * 如："我想去成都玩3天，预算1500元，想看大熊猫和都江堰"
     */
    private String textInput;
    
    /**
     * 图片输入数据
     * Base64编码的图片数据
     */
    private String imageData;
    
    /**
     * 图片类型
     * 如：jpg、png、bmp等
     */
    private String imageType;
    
    /**
     * 音频输入数据
     * Base64编码的音频数据
     */
    private String audioData;
    
    /**
     * 音频类型
     * 如：mp3、wav等
     */
    private String audioType;
    
    /**
     * 用户偏好信息
     * 可包含：旅行风格、偏好景点类型、交通方式等
     */
    private Map<String, Object> userPreferences;
    
    /**
     * 约束条件
     * 如：预算、天数、必去景点等
     */
    private Map<String, Object> constraints;
    
    /**
     * 补充说明信息
     * 用户可以提供额外的说明
     */
    private String additionalNotes;
    
    /**
     * 语言类型
     * 如：zh-CN（简体中文）、en-US（美式英语）等
     */
    private String language;
    
    /**
     * 是否需要图片推荐
     */
    private boolean needImageRecommendations;
    
    /**
     * 是否需要语音解说
     */
    private boolean needAudioGuide;
    
    /**
     * 期望输出格式
     * 可选值：detailed（详细）、brief（简洁）、visual（视觉化）
     */
    private String outputFormat;
    
    /**
     * 历史旅行记录ID列表
     * 用于参考用户的历史旅行偏好
     */
    private List<String> historicalTravelIds;
    
    /**
     * 用户上传的参考图片列表
     * 多个参考图片的Base64编码数据
     */
    private List<String> referenceImages;
    
    /**
     * 文本输入的意图类型
     * 如：plan（规划）、query（查询）、adjust（调整）
     */
    private String textIntentType;
    
    /**
     * 地理上下文信息
     * 如：当前位置、最近访问的地点等
     */
    private Map<String, Double> geoContext;
    
    /**
     * 时间上下文信息
     * 如：当前时间、季节等
     */
    private Map<String, Object> timeContext;
    
    /**
     * 输入的置信度
     * 对于自动识别的输入内容的置信度评分
     */
    private Double confidenceScore;
}