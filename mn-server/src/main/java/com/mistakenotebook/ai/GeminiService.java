package com.mistakenotebook.ai;

import com.mistakenotebook.domain.mistake.MistakeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * AI 对话
     */
    public String chat(String message, List<ChatMessage> history) {
        StringBuilder prompt = new StringBuilder();
        if (history != null && !history.isEmpty()) {
            for (ChatMessage msg : history) {
                prompt.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
            }
        }
        prompt.append("user: ").append(message);
        return callGeminiApi(prompt.toString());
    }

    /**
     * 分析错误原因（基于字符串参数）
     */
    public String analyzeError(String question, String correctAnswer, String wrongAnswer) {
        String prompt = String.format("""
                请分析以下错题的错误原因:

                题目: %s
                正确答案: %s
                我的答案: %s

                请从以下角度分析:
                1. 知识点掌握情况
                2. 解题思路问题
                3. 计算或理解错误
                4. 建议的改进方法
                """, question, correctAnswer, wrongAnswer);
        return callGeminiApi(prompt);
    }

    /**
     * 生成解题思路（基于字符串参数）
     */
    public String generateSolution(String question) {
        String prompt = String.format("""
                请为以下题目生成详细的解题思路:

                题目: %s

                请提供:
                1. 分析题目关键信息
                2. 确定解题方向
                3. 详细解题步骤
                4. 答案验证方法
                """, question);
        return callGeminiApi(prompt);
    }

    /**
     * 推荐相似题目
     */
    public String recommendSimilarQuestions(String question, String knowledgePoint, int count) {
        String prompt = String.format("""
                请根据以下题目生成%d道类似的练习题:

                原题: %s
                知识点: %s

                要求:
                1. 考察相同知识点
                2. 难度相近
                3. 题型类似
                """, count, question, knowledgePoint);
        return callGeminiApi(prompt);
    }

    /**
     * 生成学习建议
     */
    public String generateStudySuggestion(String subject, List<String> weakPoints, Map<String, Integer> errorCount) {
        StringBuilder weakPointsStr = new StringBuilder();
        if (weakPoints != null && !weakPoints.isEmpty()) {
            weakPointsStr.append(String.join("、", weakPoints));
        }

        StringBuilder errorCountStr = new StringBuilder();
        if (errorCount != null && !errorCount.isEmpty()) {
            errorCountStr.append(errorCount.entrySet().stream()
                    .map(e -> e.getKey() + ": " + e.getValue() + "次")
                    .collect(Collectors.joining("，")));
        }

        String prompt = String.format("""
                请为以下学习情况生成个性化的学习建议:

                科目: %s
                薄弱知识点: %s
                错题统计: %s

                请提供:
                1. 学习重点建议
                2. 复习计划
                3. 练习方向
                4. 时间分配建议
                """, subject, weakPointsStr.toString(), errorCountStr.toString());
        return callGeminiApi(prompt);
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

    /**
     * 智能错题原因自动分类
     * 返回分类结果：concept_error(概念理解错误)、calculation_error(计算错误)、reading_error(审题不清)、method_error(方法错误)、careless_error(粗心大意)
     */
    public String classifyErrorReason(MistakeEntity mistake) {
        String prompt = String.format("""
                请分析以下错题，并判断错误原因类型。请只返回以下类型之一：
                - concept_error: 概念理解错误
                - calculation_error: 计算错误
                - reading_error: 审题不清
                - method_error: 方法错误
                - careless_error: 粗心大意
                
                题目: %s
                题目内容: %s
                正确答案: %s
                我的答案: %s
                
                请只返回类型代码，不要返回其他内容。
                """,
                mistake.getTitle() != null ? mistake.getTitle() : "",
                mistake.getContent() != null ? mistake.getContent() : "",
                mistake.getCorrectAnswer() != null ? mistake.getCorrectAnswer() : "",
                mistake.getMyAnswer() != null ? mistake.getMyAnswer() : "");
        
        String response = callGeminiApi(prompt);
        // 提取类型代码
        if (response != null) {
            response = response.trim().toLowerCase();
            if (response.contains("concept_error")) return "concept_error";
            if (response.contains("calculation_error")) return "calculation_error";
            if (response.contains("reading_error")) return "reading_error";
            if (response.contains("method_error")) return "method_error";
            if (response.contains("careless_error")) return "careless_error";
        }
        return "unknown";
    }

    /**
     * 知识点关联分析
     * 分析错题之间的知识点关联，发现知识盲区
     */
    public String analyzeKnowledgeRelation(List<MistakeEntity> mistakes) {
        StringBuilder mistakeList = new StringBuilder();
        for (int i = 0; i < mistakes.size() && i < 10; i++) {
            MistakeEntity m = mistakes.get(i);
            mistakeList.append(String.format("错题%d: %s (知识点: %s, 错误原因: %s)\n",
                    i + 1,
                    m.getTitle() != null ? m.getTitle() : "",
                    m.getKnowledgePointId() != null ? m.getKnowledgePointId() : "未知",
                    m.getErrorReason() != null ? m.getErrorReason() : "未知"));
        }

        String prompt = String.format("""
                请分析以下错题列表，找出知识点之间的关联关系和知识盲区：
                
                %s
                
                请提供：
                1. 薄弱知识点链条（哪些知识点经常一起出错）
                2. 知识盲区识别（哪些基础知识点掌握不牢）
                3. 学习建议（如何系统性地补强这些知识点）
                """, mistakeList.toString());
        
        return callGeminiApi(prompt);
    }

    /**
     * 智能出题 - 生成变式题
     */
    public String generateVariantQuestions(MistakeEntity mistake, int count) {
        String prompt = String.format("""
                请根据以下错题生成%d道变式练习题：
                
                原题: %s
                题目内容: %s
                知识点: %s
                难度: %s
                
                要求：
                1. 考察相同的核心知识点
                2. 难度与原题相近或略低
                3. 题型可以有所变化
                4. 每道题请包含：题目、选项（如果是选择题）、答案、简要解析
                
                请按以下格式输出：
                题目1: [题目内容]
                选项: A. [选项A] B. [选项B] C. [选项C] D. [选项D]
                答案: [正确答案]
                解析: [解析内容]
                
                ---
                """,
                count,
                mistake.getTitle() != null ? mistake.getTitle() : "",
                mistake.getContent() != null ? mistake.getContent() : "",
                mistake.getKnowledgePointId() != null ? mistake.getKnowledgePointId() : "未知",
                mistake.getDifficulty() != null ? mistake.getDifficulty() : "中等");
        
        return callGeminiApi(prompt);
    }

    /**
     * 智能出题 - 生成巩固练习
     */
    public String generatePracticeQuestions(String knowledgePoint, String difficulty, int count) {
        String prompt = String.format("""
                请生成%d道巩固练习题：
                
                知识点: %s
                难度: %s
                
                要求：
                1. 全面覆盖该知识点的各个考察角度
                2. 难度为%s
                3. 包含选择题、填空题、解答题等多种题型
                4. 每道题请包含：题目、答案、解析
                
                请按以下格式输出：
                题目1: [题目内容]
                答案: [正确答案]
                解析: [解析内容]
                
                ---
                """,
                count,
                knowledgePoint,
                difficulty != null ? difficulty : "中等",
                difficulty != null ? difficulty : "中等");
        
        return callGeminiApi(prompt);
    }

    /**
     * 错题趋势预测
     */
    public String predictErrorTrend(List<MistakeEntity> recentMistakes, List<MistakeEntity> allMistakes) {
        StringBuilder recentInfo = new StringBuilder();
        for (int i = 0; i < recentMistakes.size() && i < 5; i++) {
            MistakeEntity m = recentMistakes.get(i);
            recentInfo.append(String.format("- %s (错误原因: %s, 知识点: %s)\n",
                    m.getTitle() != null ? m.getTitle() : "",
                    m.getErrorReason() != null ? m.getErrorReason() : "未知",
                    m.getKnowledgePointId() != null ? m.getKnowledgePointId() : "未知"));
        }

        String prompt = String.format("""
                请分析以下错题数据，预测未来可能出现的错误趋势：
                
                最近错题（最近5道）：
                %s
                
                总错题数: %d道
                
                请提供：
                1. 错误趋势分析（哪些类型的错误在增加）
                2. 可能出错的知识点预测
                3. 预防建议（如何避免这些错误）
                """,
                recentInfo.toString(),
                allMistakes.size());
        
        return callGeminiApi(prompt);
    }

    /**
     * 生成学习报告
     */
    public String generateStudyReport(String userId, String subject, List<MistakeEntity> mistakes,
                                     List<Object[]> subjectStats, List<Object[]> errorReasonStats,
                                     int totalReviewCount, int masteredCount) {
        StringBuilder mistakeSummary = new StringBuilder();
        mistakeSummary.append(String.format("总错题数: %d\n", mistakes.size()));
        mistakeSummary.append(String.format("已掌握: %d\n", masteredCount));
        mistakeSummary.append(String.format("总复习次数: %d\n", totalReviewCount));
        
        if (subjectStats != null && !subjectStats.isEmpty()) {
            mistakeSummary.append("\n按科目分布:\n");
            for (Object[] stat : subjectStats) {
                mistakeSummary.append(String.format("- %s: %d道\n", stat[0], stat[1]));
            }
        }
        
        if (errorReasonStats != null && !errorReasonStats.isEmpty()) {
            mistakeSummary.append("\n按错误原因分布:\n");
            for (Object[] stat : errorReasonStats) {
                mistakeSummary.append(String.format("- %s: %d道\n", stat[0], stat[1]));
            }
        }

        String prompt = String.format("""
                请为以下学习情况生成详细的学习报告：
                
                科目: %s
                
                学习数据：
                %s
                
                请生成一份完整的学习报告，包括：
                1. 学习概况（整体掌握情况）
                2. 薄弱环节分析（哪些知识点需要加强）
                3. 进步亮点（哪些方面有提升）
                4. 改进建议（具体的学习建议和复习计划）
                5. 下阶段目标（建议的学习目标）
                
                报告要求：
                - 语言鼓励性、积极正面
                - 建议具体可执行
                - 数据支撑充分
                """,
                subject != null ? subject : "全部科目",
                mistakeSummary.toString());
        
        return callGeminiApi(prompt);
    }

    /**
     * 智能推荐复习优先级
     */
    public String recommendReviewPriority(List<MistakeEntity> mistakes) {
        StringBuilder mistakeInfo = new StringBuilder();
        for (int i = 0; i < mistakes.size() && i < 20; i++) {
            MistakeEntity m = mistakes.get(i);
            mistakeInfo.append(String.format("错题%d: %s (掌握度: %d%%, 复习次数: %d, 状态: %s)\n",
                    i + 1,
                    m.getTitle() != null ? m.getTitle() : "",
                    m.getMasteryLevel() != null ? m.getMasteryLevel() : 0,
                    m.getReviewCount() != null ? m.getReviewCount() : 0,
                    m.getMasteryStatus() != null ? m.getMasteryStatus() : "未掌握"));
        }

        String prompt = String.format("""
                请根据以下错题数据，推荐复习优先级：
                
                %s
                
                请按照以下标准推荐：
                1. 掌握度低且复习次数少的优先
                2. 错误原因严重的优先（概念错误 > 方法错误 > 计算错误 > 粗心）
                3. 距离上次复习时间长的优先
                
                请输出前10道优先复习的错题，并说明推荐理由。
                """,
                mistakeInfo.toString());
        
        return callGeminiApi(prompt);
    }

    /**
     * 智能难度评估
     */
    public String assessDifficulty(MistakeEntity mistake) {
        String prompt = String.format("""
                请评估以下题目的难度等级：
                
                题目: %s
                题目内容: %s
                知识点: %s
                
                请只返回以下难度等级之一：
                - easy: 简单
                - medium: 中等
                - hard: 困难
                
                请只返回难度代码，不要返回其他内容。
                """,
                mistake.getTitle() != null ? mistake.getTitle() : "",
                mistake.getContent() != null ? mistake.getContent() : "",
                mistake.getKnowledgePointId() != null ? mistake.getKnowledgePointId() : "未知");
        
        String response = callGeminiApi(prompt);
        if (response != null) {
            response = response.trim().toLowerCase();
            if (response.contains("hard") || response.contains("困难")) return "hard";
            if (response.contains("medium") || response.contains("中等")) return "medium";
            if (response.contains("easy") || response.contains("简单")) return "easy";
        }
        return "medium";
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
