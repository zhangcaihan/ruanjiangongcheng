package com.zeyuli.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置DeekSeek的角色
 *
 * @author 李泽聿
 * @since 2025-10-21 15:26
 */
@Configuration
public class DeekSeekConfig {
    @Value("${DeekSeek.role}")
    private String role;

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem(role)
                .build();
    }
}
