package com.mistakenotebook.web.api;

import com.adminpro.framework.base.entity.R;
import com.mistakenotebook.domain.mistake.MistakeEntity;
import com.mistakenotebook.domain.recommendation.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 智能推荐 API
 *
 * @author simon
 */
@RestController
@RequestMapping("/api/recommendations")
@PreAuthorize("@ss.hasPermission('mistake:manage')")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    /**
     * 获取智能推荐的复习错题列表
     */
    @GetMapping("/mistakes")
    public R<List<MistakeEntity>> getRecommendedMistakes(
            @RequestParam String userId,
            @RequestParam(defaultValue = "10") int limit) {
        List<MistakeEntity> recommendations = recommendationService.getRecommendedMistakes(userId, limit);
        return R.ok(recommendations);
    }

    /**
     * 获取AI推荐的复习优先级说明
     */
    @GetMapping("/priority")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> getRecommendedPriority(@RequestParam String userId) {
        String priority = recommendationService.getRecommendedPriority(userId);
        return R.ok(Map.of("priority", priority));
    }

    /**
     * 根据薄弱知识点推荐错题
     */
    @GetMapping("/by-knowledge")
    public R<List<MistakeEntity>> getRecommendedByWeakPoint(
            @RequestParam String userId,
            @RequestParam String knowledgePointId) {
        List<MistakeEntity> recommendations = recommendationService.getRecommendedByWeakPoint(userId, knowledgePointId);
        return R.ok(recommendations);
    }

    /**
     * 获取今日推荐复习数量
     */
    @GetMapping("/today-count")
    public R<Map<String, Integer>> getTodayRecommendedCount(@RequestParam String userId) {
        int count = recommendationService.getTodayRecommendedCount(userId);
        return R.ok(Map.of("count", count));
    }
}

