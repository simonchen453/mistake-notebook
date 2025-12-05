package com.mistakenotebook.ai;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mistakenotebook.config.GeminiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Gemini API 客户端
 * 封装与 Google Gemini API 的交互
 * 
 * @author simon
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final GeminiProperties properties;
    private final Gson gson = new Gson();
    private WebClient webClient;

    private WebClient getWebClient() {
        if (webClient == null) {
            webClient = WebClient.builder()
                    .baseUrl(properties.getBaseUrl())
                    .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .build();
        }
        return webClient;
    }

    /**
     * 发送消息给 Gemini 并获取响应
     * 
     * @param prompt 用户提示词
     * @return AI 响应文本
     */
    public String chat(String prompt) {
        return chat(prompt, null);
    }

    /**
     * 发送消息给 Gemini 并获取响应（带历史记录）
     * 
     * @param prompt  用户提示词
     * @param history 历史对话记录
     * @return AI 响应文本
     */
    public String chat(String prompt, List<ChatMessage> history) {
        try {
            String requestBody = buildRequestBody(prompt, history);
            String url = String.format("/models/%s:generateContent?key=%s",
                    properties.getModel(), properties.getApiKey());

            log.debug("Gemini API Request URL: {}", url);
            log.debug("Gemini API Request Body: {}", requestBody);

            String response = getWebClient()
                    .post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(properties.getTimeout()))
                    .onErrorResume(e -> {
                        log.error("Gemini API call failed: {}", e.getMessage());
                        return Mono.just("{\"error\": \"" + e.getMessage() + "\"}");
                    })
                    .block();

            log.debug("Gemini API Response: {}", response);
            return parseResponse(response);

        } catch (Exception e) {
            log.error("Error calling Gemini API", e);
            return "抱歉，AI 服务暂时不可用，请稍后重试。";
        }
    }

    /**
     * 流式调用 Gemini API
     * 
     * @param prompt 用户提示词
     * @return 流式响应
     */
    public Mono<String> chatStream(String prompt) {
        String requestBody = buildRequestBody(prompt, null);
        String url = String.format("/models/%s:streamGenerateContent?key=%s",
                properties.getModel(), properties.getApiKey());

        return getWebClient()
                .post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(properties.getTimeout()));
    }

    /**
     * 构建请求体
     */
    private String buildRequestBody(String prompt, List<ChatMessage> history) {
        JsonObject request = new JsonObject();
        JsonArray contents = new JsonArray();

        // 添加历史记录
        if (history != null && !history.isEmpty()) {
            for (ChatMessage msg : history) {
                JsonObject content = new JsonObject();
                content.addProperty("role", msg.getRole());

                JsonArray parts = new JsonArray();
                JsonObject part = new JsonObject();
                part.addProperty("text", msg.getContent());
                parts.add(part);

                content.add("parts", parts);
                contents.add(content);
            }
        }

        // 添加当前消息
        JsonObject userContent = new JsonObject();
        userContent.addProperty("role", "user");

        JsonArray userParts = new JsonArray();
        JsonObject userPart = new JsonObject();
        userPart.addProperty("text", prompt);
        userParts.add(userPart);

        userContent.add("parts", userParts);
        contents.add(userContent);

        request.add("contents", contents);

        // 添加生成配置
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.7);
        generationConfig.addProperty("topK", 40);
        generationConfig.addProperty("topP", 0.95);
        generationConfig.addProperty("maxOutputTokens", 8192);
        request.add("generationConfig", generationConfig);

        return gson.toJson(request);
    }

    /**
     * 解析 Gemini API 响应
     */
    private String parseResponse(String response) {
        try {
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);

            if (jsonResponse.has("error")) {
                String errorMessage = jsonResponse.getAsJsonObject("error")
                        .get("message").getAsString();
                log.error("Gemini API Error: {}", errorMessage);
                return "AI 请求失败：" + errorMessage;
            }

            if (jsonResponse.has("candidates")) {
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                if (!candidates.isEmpty()) {
                    JsonObject candidate = candidates.get(0).getAsJsonObject();
                    JsonObject content = candidate.getAsJsonObject("content");
                    JsonArray parts = content.getAsJsonArray("parts");
                    if (!parts.isEmpty()) {
                        return parts.get(0).getAsJsonObject().get("text").getAsString();
                    }
                }
            }

            return "无法解析 AI 响应";
        } catch (Exception e) {
            log.error("Error parsing Gemini response: {}", e.getMessage());
            return "解析 AI 响应失败";
        }
    }
}
