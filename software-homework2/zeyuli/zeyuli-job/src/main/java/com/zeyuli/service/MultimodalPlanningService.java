package com.zeyuli.service;

import com.zeyuli.pojo.vo.MultimodalInput;
import com.zeyuli.pojo.vo.MultimodalResult;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 多模态规划服务接口
 * 提供通过多种输入方式（文字、图片等）进行行程规划的功能
 * 
 * @author 李泽聿
 * @since 2025-11-01
 */
@Service
public interface MultimodalPlanningService {
    
    /**
     * 通过多模态输入规划行程
     * 支持文字、图片等多种输入方式
     * 
     * @param input 多模态输入数据
     * @return 行程规划结果
     */
    ItineraryPlanVO planItineraryByMultimodal(MultimodalInput input);
    
    /**
     * 从图片中提取旅行相关信息
     * 
     * @param imageData 图片数据（Base64编码）
     * @param imageType 图片类型（jpg、png等）
     * @return 提取的旅行信息
     */
    Map<String, Object> extractTravelInfoFromImage(String imageData, String imageType);
    
    /**
     * 从语音中识别旅行需求
     * 
     * @param audioData 音频数据（Base64编码）
     * @param audioType 音频类型（mp3、wav等）
     * @return 识别的旅行需求文本
     */
    String recognizeTravelRequirementsFromAudio(String audioData, String audioType);
    
    /**
     * 理解自然语言旅行需求
     * 
     * @param naturalLanguage 自然语言描述的旅行需求
     * @return 结构化的旅行需求
     */
    Map<String, Object> understandNaturalLanguageTravelRequirements(String naturalLanguage);
    
    /**
     * 融合多种输入模态的信息
     * 
     * @param textInfo 文本信息
     * @param imageInfo 图片信息
     * @param audioInfo 音频信息
     * @return 融合后的综合信息
     */
    Map<String, Object> fuseMultimodalInformation(
            Map<String, Object> textInfo, 
            Map<String, Object> imageInfo, 
            Map<String, Object> audioInfo);
    
    /**
     * 生成多模态行程推荐
     * 
     * @param userPreferences 用户偏好
     * @param constraints 约束条件
     * @param limit 推荐数量限制
     * @return 多模态行程推荐结果列表
     */
    List<MultimodalResult> generateMultimodalRecommendations(
            Map<String, Object> userPreferences, 
            Map<String, Object> constraints, 
            int limit);
    
    /**
     * 分析用户旅行风格从多模态数据
     * 
     * @param multimodalData 多模态数据
     * @return 用户旅行风格分析结果
     */
    Map<String, Double> analyzeUserTravelStyleFromMultimodalData(
            List<Map<String, Object>> multimodalData);
    
    /**
     * 将行程规划结果转换为多模态展示数据
     * 
     * @param itinerary 行程规划结果
     * @param includeImage 是否包含图片
     * @param includeAudio 是否包含音频
     * @return 多模态展示数据
     */
    MultimodalResult convertToMultimodalPresentation(
            ItineraryPlanVO itinerary, 
            boolean includeImage, 
            boolean includeAudio);
}