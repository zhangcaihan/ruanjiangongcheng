package com.zeyuli.pojo;

import lombok.Data;
import java.util.Date;
import java.util.Map;

/**
 * 聊天消息实体类
 * 用于存储AI旅行搭子聊天模式的消息数据
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Data
public class ChatMessage {
    
    /**
     * 消息ID
     */
    private String messageId;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 发送者类型
     * user - 用户
     * ai - AI旅行搭子
     */
    private String senderType;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型
     * text - 文本
     * image - 图片
     * audio - 音频
     * system - 系统消息
     * recommendation - 推荐消息
     */
    private String messageType;
    
    /**
     * 发送时间
     */
    private Date sendTime;
    
    /**
     * 消息状态
     * sent - 已发送
     * read - 已读
     * processing - 处理中
     * error - 发送失败
     */
    private String status;
    
    /**
     * 相关行程ID
     */
    private String relatedItineraryId;
    
    /**
     * 相关景点ID
     */
    private String relatedAttractionId;
    
    /**
     * 附加信息
     * 可以包含推荐详情、图片URL等
     */
    private Map<String, Object> additionalInfo;
    
    /**
     * 回复消息的ID
     * 用于消息引用
     */
    private String replyToMessageId;
    
    /**
     * 消息情感分析结果
     * positive - 积极
     * neutral - 中性
     * negative - 消极
     */
    private String sentiment;
    
    /**
     * 消息处理时间（毫秒）
     */
    private Long processingTime;
    
    /**
     * 是否需要快速回复
     */
    private boolean quickReplyRequired;
    
    /**
     * 快速回复选项
     */
    private Map<String, String> quickReplyOptions;
    
    /**
     * 消息所属上下文类型
     * planning - 行程规划阶段
     * during_trip - 旅行进行中
     * after_trip - 旅行结束后
     */
    private String contextType;
    
    /**
     * 地理位置信息
     * 用于基于位置的推荐
     */
    private Map<String, Double> location;
    
    /**
     * 消息标签
     */
    private Map<String, String> tags;
    
    /**
     * 错误码
     */
    private String errorCode;
    
    /**
     * 错误信息
     */
    private String errorMessage;
}