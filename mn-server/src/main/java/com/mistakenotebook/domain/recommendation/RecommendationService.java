package com.mistakenotebook.domain.recommendation;

import com.mistakenotebook.ai.GeminiService;
import com.mistakenotebook.domain.mistake.MistakeDao;
import com.mistakenotebook.domain.mistake.MistakeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 智能推荐服务
 *
 * @author simon
 */
@Service
public class RecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    @Autowired
    private MistakeDao mistakeDao;

    @Autowired
    private GeminiService geminiService;

    /**
     * 获取智能推荐的复习错题列表
     * 根据掌握度、复习间隔、错误频率等因素推荐
     */
    public List<MistakeEntity> getRecommendedMistakes(String userId, int limit) {
        List<MistakeEntity> allMistakes = mistakeDao.findByUserId(userId);
        
        if (allMistakes.isEmpty()) {
            return List.of();
        }

        LocalDateTime now = LocalDateTime.now();
        
        return allMistakes.stream()
                .filter(m -> !"mastered".equals(m.getMasteryStatus()))
                .sorted(Comparator
                        .comparing((MistakeEntity m) -> {
                            // 优先级1: 需要复习的（nextReviewTime已过）
                            if (m.getNextReviewTime() != null && m.getNextReviewTime().isBefore(now)) {
                                return 0;
                            }
                            return 1;
                        })
                        .thenComparing((MistakeEntity m) -> {
                            // 优先级2: 掌握度越低越优先
                            return m.getMasteryLevel() != null ? m.getMasteryLevel() : 0;
                        })
                        .thenComparing((MistakeEntity m) -> {
                            // 优先级3: 复习次数越少越优先
                            return m.getReviewCount() != null ? m.getReviewCount() : 0;
                        })
                        .thenComparing((MistakeEntity m) -> {
                            // 优先级4: 错误原因严重程度（概念错误 > 方法错误 > 计算错误 > 粗心）
                            return getErrorReasonPriority(m.getErrorReason());
                        }))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 获取错误原因优先级（数字越小优先级越高）
     */
    private int getErrorReasonPriority(String errorReason) {
        if (errorReason == null) return 5;
        return switch (errorReason) {
            case "concept_error" -> 1;
            case "method_error" -> 2;
            case "calculation_error" -> 3;
            case "reading_error" -> 4;
            case "careless_error" -> 5;
            default -> 6;
        };
    }

    /**
     * 获取AI推荐的复习优先级说明
     */
    public String getRecommendedPriority(String userId) {
        List<MistakeEntity> mistakes = mistakeDao.findByUserId(userId);
        if (mistakes.isEmpty()) {
            return "暂无错题数据";
        }
        
        return geminiService.recommendReviewPriority(mistakes);
    }

    /**
     * 根据薄弱知识点推荐错题
     */
    public List<MistakeEntity> getRecommendedByWeakPoint(String userId, String knowledgePointId) {
        List<MistakeEntity> mistakes = mistakeDao.findByUserId(userId);
        
        return mistakes.stream()
                .filter(m -> knowledgePointId.equals(m.getKnowledgePointId()))
                .filter(m -> !"mastered".equals(m.getMasteryStatus()))
                .sorted(Comparator
                        .comparing((MistakeEntity m) -> m.getMasteryLevel() != null ? m.getMasteryLevel() : 0)
                        .thenComparing((MistakeEntity m) -> m.getReviewCount() != null ? m.getReviewCount() : 0))
                .collect(Collectors.toList());
    }

    /**
     * 获取今日推荐复习数量
     */
    public int getTodayRecommendedCount(String userId) {
        List<MistakeEntity> recommended = getRecommendedMistakes(userId, 100);
        LocalDateTime now = LocalDateTime.now();
        
        return (int) recommended.stream()
                .filter(m -> m.getNextReviewTime() == null || m.getNextReviewTime().isBefore(now))
                .count();
    }
}

