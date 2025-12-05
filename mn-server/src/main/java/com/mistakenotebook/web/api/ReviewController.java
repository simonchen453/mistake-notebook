package com.mistakenotebook.web.api;

import com.adminpro.framework.base.entity.R;
import com.mistakenotebook.domain.mistake.MistakeEntity;
import com.mistakenotebook.domain.review.ReviewRecordEntity;
import com.mistakenotebook.domain.review.ReviewService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 复习中心 API
 *
 * @author simon
 */
@RestController
@RequestMapping("/api/review")
@PreAuthorize("@ss.hasPermission('review:manage')")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 获取待复习错题
     */
    @GetMapping("/pending")
    public R<List<MistakeEntity>> getPendingReviews(@RequestParam String userId) {
        return R.ok(reviewService.getPendingReviews(userId));
    }

    /**
     * 获取今日待复习数量
     */
    @GetMapping("/pending/count")
    public R<Map<String, Integer>> getPendingCount(@RequestParam String userId) {
        int count = reviewService.getTodayPendingCount(userId);
        return R.ok(Map.of("count", count));
    }

    /**
     * 记录复习结果
     */
    @PostMapping("/record")
    @PreAuthorize("@ss.hasPermission('review:record')")
    public R recordReview(@RequestBody ReviewRequest request) {
        reviewService.recordReview(
                request.getMistakeId(),
                request.getUserId(),
                request.getResult(),
                request.getNotes());
        return R.ok();
    }

    /**
     * 获取错题的复习历史
     */
    @GetMapping("/history/{mistakeId}")
    public R<List<ReviewRecordEntity>> getReviewHistory(@PathVariable String mistakeId) {
        return R.ok(reviewService.getReviewHistory(mistakeId));
    }

    /**
     * 智能优化复习计划
     */
    @GetMapping("/optimize-plan")
    @PreAuthorize("@ss.hasPermission('review:manage')")
    public R<Map<String, String>> optimizeReviewPlan(@RequestParam String userId) {
        String plan = reviewService.optimizeReviewPlan(userId);
        return R.ok(Map.of("plan", plan));
    }

    /**
     * 智能推荐复习难度
     */
    @GetMapping("/recommend-difficulty/{mistakeId}")
    @PreAuthorize("@ss.hasPermission('review:manage')")
    public R<Map<String, String>> recommendDifficulty(@PathVariable String mistakeId) {
        String recommendation = reviewService.recommendDifficulty(mistakeId);
        return R.ok(Map.of("recommendation", recommendation));
    }

    @Data
    public static class ReviewRequest {
        private String mistakeId;
        private String userId;
        private String result;
        private String notes;
    }
}
