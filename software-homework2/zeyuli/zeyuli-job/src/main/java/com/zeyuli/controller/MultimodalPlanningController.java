package com.zeyuli.controller;

import com.zeyuli.pojo.vo.ItineraryPlanVO;
import com.zeyuli.pojo.vo.MultimodalInput;
import com.zeyuli.pojo.vo.MultimodalResult;
import com.zeyuli.service.MultimodalPlanningService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 多模态规划控制器
 * 处理多模态行程规划相关的HTTP请求
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/multimodal-planning")
// todo 仅测试用
@CrossOrigin
public class MultimodalPlanningController {
    
    @Autowired
    private MultimodalPlanningService multimodalPlanningService;
    
    @ApiOperation(value = "多模态行程规划", notes = "通过多模态输入（文字、图片等）进行行程规划")
    @PostMapping("/plan-by-multimodal")
    public ItineraryPlanVO planItineraryByMultimodal(@RequestBody MultimodalInput input) {
        return multimodalPlanningService.planItineraryByMultimodal(input);
    }
    
    @ApiOperation(value = "从图片提取信息", notes = "从图片中提取旅行相关信息")
    @PostMapping("/extract-from-image")
    public Map<String, Object> extractTravelInfoFromImage(@RequestParam String imageData,
                                                       @RequestParam String imageType) {
        return multimodalPlanningService.extractTravelInfoFromImage(imageData, imageType);
    }
    
    @ApiOperation(value = "从语音识别需求", notes = "从语音中识别旅行需求文本")
    @PostMapping("/recognize-from-audio")
    public String recognizeTravelRequirementsFromAudio(@RequestParam String audioData,
                                                    @RequestParam String audioType) {
        return multimodalPlanningService.recognizeTravelRequirementsFromAudio(audioData, audioType);
    }
    
    @ApiOperation(value = "理解自然语言需求", notes = "理解自然语言描述的旅行需求")
    @PostMapping("/understand-natural-language")
    public Map<String, Object> understandNaturalLanguageTravelRequirements(@RequestBody String naturalLanguage) {
        return multimodalPlanningService.understandNaturalLanguageTravelRequirements(naturalLanguage);
    }
    
    @ApiOperation(value = "融合多模态信息", notes = "融合文本、图片、音频等多种输入模态的信息")
    @PostMapping("/fuse-information")
    public Map<String, Object> fuseMultimodalInformation(@RequestBody Map<String, Object> textInfo,
                                                      @RequestBody Map<String, Object> imageInfo,
                                                      @RequestBody Map<String, Object> audioInfo) {
        return multimodalPlanningService.fuseMultimodalInformation(textInfo, imageInfo, audioInfo);
    }
    
    @ApiOperation(value = "生成多模态推荐", notes = "生成多模态行程推荐结果")
    @PostMapping("/generate-recommendations")
    public List<MultimodalResult> generateMultimodalRecommendations(@RequestBody Map<String, Object> userPreferences,
                                                                 @RequestBody Map<String, Object> constraints,
                                                                 @RequestParam(defaultValue = "5") int limit) {
        return multimodalPlanningService.generateMultimodalRecommendations(userPreferences, constraints, limit);
    }
    
    @ApiOperation(value = "分析用户旅行风格", notes = "从多模态数据中分析用户旅行风格")
    @PostMapping("/analyze-travel-style")
    public Map<String, Double> analyzeUserTravelStyleFromMultimodalData(@RequestBody List<Map<String, Object>> multimodalData) {
        return multimodalPlanningService.analyzeUserTravelStyleFromMultimodalData(multimodalData);
    }
    
    @ApiOperation(value = "转换为多模态展示", notes = "将行程规划结果转换为多模态展示数据")
    @PostMapping("/convert-to-multimodal")
    public MultimodalResult convertToMultimodalPresentation(@RequestBody ItineraryPlanVO itinerary,
                                                          @RequestParam(defaultValue = "true") boolean includeImage,
                                                          @RequestParam(defaultValue = "false") boolean includeAudio) {
        return multimodalPlanningService.convertToMultimodalPresentation(itinerary, includeImage, includeAudio);
    }
}