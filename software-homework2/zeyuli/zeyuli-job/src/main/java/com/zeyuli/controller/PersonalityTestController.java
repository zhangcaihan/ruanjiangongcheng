package com.zeyuli.controller;

import com.zeyuli.pojo.bo.PersonalityTest;
import com.zeyuli.pojo.vo.ItineraryPlanVO;
import com.zeyuli.pojo.vo.PersonalityTestResultVO;
import com.zeyuli.service.PersonalityTestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 旅行人格测试控制器
 * 处理旅行人格测试相关的HTTP请求
 *
 * @author 李泽聿
 * @since 2025-11-01
 */
@RestController
@RequestMapping("/personality-test")
// todo 仅测试用
@CrossOrigin
public class PersonalityTestController {
    
    @Autowired
    private PersonalityTestService personalityTestService;
    
    @ApiOperation(value = "获取人格测试问卷", notes = "获取三分钟旅行人格测试问卷")
    @GetMapping("/questionnaire")
    public PersonalityTest getPersonalityTest() {
        return personalityTestService.getPersonalityTest();
    }
    
    @ApiOperation(value = "计算测试结果", notes = "根据用户答案计算人格测试结果")
    @PostMapping("/calculate-result")
    public PersonalityTestResultVO calculateTestResult(@RequestBody Map<String, String> userAnswers) {
        return personalityTestService.calculateTestResult(userAnswers);
    }
    
    @ApiOperation(value = "生成人格行程", notes = "根据人格类型生成推荐行程")
    @GetMapping("/generate-itinerary")
    public ItineraryPlanVO generatePersonalityItinerary(@RequestParam String city,
                                                     @RequestParam int days,
                                                     @RequestParam String personalityType) {
        return personalityTestService.generatePersonalityItinerary(city, days, personalityType);
    }
    
    @ApiOperation(value = "获取所有人格类型", notes = "获取所有可用的人格类型信息")
    @GetMapping("/all-personality-types")
    public Map<String, PersonalityTest.PersonalityType> getAllPersonalityTypes() {
        return personalityTestService.getAllPersonalityTypes();
    }
    
    @ApiOperation(value = "验证人格类型", notes = "验证人格类型是否有效")
    @GetMapping("/validate-personality-type")
    public boolean isValidPersonalityType(@RequestParam String personalityType) {
        return personalityTestService.isValidPersonalityType(personalityType);
    }
    
    @ApiOperation(value = "预测人格类型", notes = "根据用户历史行为预测可能的人格类型")
    @GetMapping("/predict-personality/{userId}")
    public Map<String, Double> predictPersonalityType(@PathVariable String userId) {
        return personalityTestService.predictPersonalityType(userId);
    }
}