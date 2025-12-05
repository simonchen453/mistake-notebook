package com.mistakenotebook.web.api;

import com.mistakenotebook.ai.ChatMessage;
import com.mistakenotebook.ai.GeminiService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AI 助手 API
 * 
 * @author simon
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final GeminiService geminiService;

    /**
     * AI 对话
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody ChatRequest request) {
        List<ChatMessage> history = new ArrayList<>();
        if (request.getHistory() != null) {
            history.addAll(request.getHistory());
        }

        String response = geminiService.chat(request.getMessage(), history);
        return ResponseEntity.ok(Map.of("response", response));
    }

    /**
     * 分析错误原因
     */
    @PostMapping("/analyze-error")
    public ResponseEntity<Map<String, String>> analyzeError(@RequestBody AnalyzeRequest request) {
        String analysis = geminiService.analyzeError(
                request.getQuestion(),
                request.getCorrectAnswer(),
                request.getWrongAnswer());
        return ResponseEntity.ok(Map.of("analysis", analysis));
    }

    /**
     * 生成解题思路
     */
    @PostMapping("/solution")
    public ResponseEntity<Map<String, String>> generateSolution(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String solution = geminiService.generateSolution(question);
        return ResponseEntity.ok(Map.of("solution", solution));
    }

    /**
     * 推荐相似题
     */
    @PostMapping("/similar-questions")
    public ResponseEntity<Map<String, String>> recommendSimilar(@RequestBody SimilarQuestionsRequest request) {
        String questions = geminiService.recommendSimilarQuestions(
                request.getQuestion(),
                request.getKnowledgePoint(),
                request.getCount());
        return ResponseEntity.ok(Map.of("questions", questions));
    }

    /**
     * 生成学习建议
     */
    @PostMapping("/study-suggestion")
    public ResponseEntity<Map<String, String>> generateSuggestion(@RequestBody StudySuggestionRequest request) {
        String suggestion = geminiService.generateStudySuggestion(
                request.getSubject(),
                request.getWeakPoints(),
                request.getErrorCount());
        return ResponseEntity.ok(Map.of("suggestion", suggestion));
    }

    @Data
    public static class ChatRequest {
        private String message;
        private List<ChatMessage> history;
    }

    @Data
    public static class AnalyzeRequest {
        private String question;
        private String correctAnswer;
        private String wrongAnswer;
    }

    @Data
    public static class SimilarQuestionsRequest {
        private String question;
        private String knowledgePoint;
        private int count = 3;
    }

    @Data
    public static class StudySuggestionRequest {
        private String subject;
        private List<String> weakPoints;
        private Map<String, Integer> errorCount;
    }
}
