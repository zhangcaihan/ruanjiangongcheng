package com.zeyuli.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class AiWebClientConfig {

    /**
     * 名字必须是 openAiWebClient，Spring-AI 1.0.x 会自动使用它
     */
    @Bean
    public WebClient openAiWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ZERO)          // 关闭响应超时
                .doOnConnected(conn ->
                        conn.addHandlerLast(
                                new ReadTimeoutHandler(0, TimeUnit.MILLISECONDS))); // 0=永不超时

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}