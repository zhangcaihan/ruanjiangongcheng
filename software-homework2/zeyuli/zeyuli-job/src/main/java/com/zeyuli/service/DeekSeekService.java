package com.zeyuli.service;

import com.zeyuli.pojo.vo.FormatedMarkdownVo;
import com.zeyuli.pojo.vo.UserFormateVo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

public interface DeekSeekService {

    Flux<String> chat(String userInput, String toke, String StartCity, String EndCity, LocalDate startDate, LocalDate endDate);

    void clearConversationHistory(String token);

    int getConversationHistorySize(String token);

    Mono<FormatedMarkdownVo> formatUserInput(UserFormateVo userInput);
}
