package com.mistakenotebook.ai;

import com.mistakenotebook.domain.mistake.MistakeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

/**
 * Gemini AI 服务
 *
 * @author simon
 */
@Service
public class GeminiService {
    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent}")
    private String apiUrl;

    private final WebClient webClient;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * 分析错误原因
     */
    public String analyzeError(MistakeEntity mistake) {
        String prompt = buildAnalysisPrompt(mistake);
        return callGeminiApi(prompt);
    }

    /**
     * 生成解题思路
     */
    public String generateSolution(MistakeEntity mistake) {
        String prompt = buildSolutionPrompt(mistake);
        return callGeminiApi(prompt);
    }

    /**
     * 生成相似题目
     */
    public List<String> generateSimilarQuestions(MistakeEntity mistake) {
        String prompt = buildSimilarQuestionsPrompt(mistake);
        String response = callGeminiApi(prompt);
        // 简单分割，实际可能需要更复杂的解析
        return List.of(response.split("\n"));
    }

    private String buildAnalysisPrompt(MistakeEntity mistake) {
        return String.format("""
                请分析以下错题的错误原因:

                题目: %s
                题目内容: %s
                正确答案: %s
                我的答案: %s

                请从以下角度分析:
                1. 知识点掌握情况
                2. 解题思路问题
                3. 计算或理解错误
                4. 建议的改进方法
                """,
                mistake.getTitle(),
                mistake.getContent(),
                mistake.getCorrectAnswer(),
                mistake.getMyAnswer());
    }

    private String buildSolutionPrompt(MistakeEntity mistake) {
        return String.format("""
                请为以下题目生成详细的解题思路:

                题目: %s
                题目内容: %s

                请提供:
                1. 分析题目关键信息
                2. 确定解题方向
                3. 详细解题步骤
                4. 答案验证方法
                """,
                mistake.getTitle(),
                mistake.getContent());
    }

    private String buildSimilarQuestionsPrompt(MistakeEntity mistake) {
        return String.format("""
                请根据以下题目生成3道类似的练习题:

                原题: %s
                题目内容: %s

                要求:
                1. 考察相同知识点
                2. 难度相近
                3. 题型类似
                """,
                mistake.getTitle(),
                mistake.getContent());
    }

    private String callGeminiApi(String prompt) {
        if (apiKey == null || apiKey.isEmpty()) {
            logger.warn("Gemini API key未配置，返回模拟响应");
            return "AI分析功能暂未启用，请配置Gemini API密钥。";
        }

        try {
            Map<String, Object> request = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)))));

            String response = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseGeminiResponse(response);
        } catch (Exception e) {
            logger.error("调用Gemini API失败", e);
            return "AI服务暂时不可用，请稍后重试。";
        }
    }

    private String parseGeminiResponse(String response) {
        // 简化处理，实际需要解析JSON
        if (response == null) {
            return "无法获取AI响应";
        }
        // 这里应该使用JSON解析库解析响应
        // 简化返回
        return response;
    }
}
