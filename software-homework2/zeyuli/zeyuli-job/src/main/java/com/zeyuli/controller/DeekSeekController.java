package com.zeyuli.controller;


import com.zeyuli.pojo.vo.FormatedMarkdownVo;
import com.zeyuli.pojo.vo.UserFormateVo;
import com.zeyuli.service.DeekSeekService;
import com.zeyuli.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-10-21 15:02
 */

@RestController
@RequestMapping("/deekseek")
// todo 仅测试用
@CrossOrigin
@Slf4j
public class DeekSeekController {

    @Autowired
    private DeekSeekService deekSeekService;

    @ApiOperation(value = "根据用户输入的出发地、目的地、出发日期、返回日期，生成旅行计划", notes = "根据用户输入的出发地、目的地、出发日期、返回日期，生成旅行计划")
    @GetMapping(path = "/planItinerary", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> chat(@RequestParam("startCity") String startCity,
                             @RequestParam("endCity") String endCity,
                             @RequestParam("startDate") LocalDate startDate,
                             @RequestParam("endDate") LocalDate endDate,
                             @RequestParam("token") String token,
                             @RequestParam(value = "userInput", defaultValue = "") String userInput) {
        return deekSeekService.chat(userInput, token, startCity, endCity, startDate, endDate);
    }

    @ApiOperation(value = "格式化用户输入", notes = "将用户输入的文字格式化为JSON的格式")
    @PostMapping("/formatUserInput")
    public Mono<FormatedMarkdownVo> formatUserInput(@RequestBody UserFormateVo userInput) {
        return deekSeekService.formatUserInput(userInput)
                .timeout(Duration.ofSeconds(120))
                .onErrorResume(e -> {
                    log.error("请求超时: {}", e.getMessage());
                    FormatedMarkdownVo errorResult = new FormatedMarkdownVo();
                    errorResult.setDays(new ArrayList<>());
                    return Mono.just(errorResult);
                });
    }

}
