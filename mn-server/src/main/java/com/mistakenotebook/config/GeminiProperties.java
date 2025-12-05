package com.mistakenotebook.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Gemini API 配置属性
 * 
 * @author simon
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gemini")
public class GeminiProperties {

    /**
     * API Key
     */
    private String apiKey;

    /**
     * 模型名称
     */
    private String model = "gemini-2.0-flash-exp";

    /**
     * API 基础 URL
     */
    private String baseUrl = "https://generativelanguage.googleapis.com/v1beta";

    /**
     * 超时时间(毫秒)
     */
    private int timeout = 30000;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;
}
