package com.mistakenotebook.domain.review;

import com.adminpro.framework.base.entity.BaseService;
import com.mistakenotebook.ai.GeminiService;
import com.mistakenotebook.domain.mistake.MistakeDao;
import com.mistakenotebook.domain.mistake.MistakeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 复习服务 - 艾宾浩斯遗忘曲线算法
 *
 * @author simon
 */
@Service
public class ReviewService extends BaseService<ReviewRecordEntity, String> {

    // 艾宾浩斯遗忘曲线复习间隔(天)
    private static final int[] EBBINGHAUS_INTERVALS = { 1, 2, 4, 7, 15, 30 };

    private ReviewRecordDao dao;

    @Autowired
    private MistakeDao mistakeDao;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    protected ReviewService(ReviewRecordDao dao) {
        super(dao);
        this.dao = dao;
    }

    /**
     * 获取待复习的错题
     */
    public List<MistakeEntity> getPendingReviews(String userId) {
        return mistakeDao.findNeedReview(userId);
    }

    /**
     * 记录复习结果
     */
    @Transactional
    public void recordReview(String mistakeId, String userId, String result, String notes) {
        // 获取最新的复习记录
        ReviewRecordEntity latestRecord = dao.findLatestByMistakeId(mistakeId);
        int reviewCount = latestRecord != null ? latestRecord.getReviewCount() + 1 : 1;

        // 创建新的复习记录
        ReviewRecordEntity record = new ReviewRecordEntity();
        record.setMistakeId(mistakeId);
        record.setUserId(userId);
        record.setReviewTime(LocalDateTime.now());
        record.setReviewResult(result);
        record.setReviewCount(reviewCount);
        record.setNotes(notes);

        // 计算下次复习时间
        LocalDateTime nextReviewTime = calculateNextReviewTime(reviewCount, result);
        record.setNextReviewTime(nextReviewTime);

        create(record);

        // 更新错题的掌握程度
        updateMistakeMastery(mistakeId, result, reviewCount);
    }

    /**
     * 计算下次复习时间（智能优化版）
     */
    private LocalDateTime calculateNextReviewTime(int reviewCount, String result) {
        int intervalIndex;

        if ("FORGOT".equals(result)) {
            intervalIndex = 0;
        } else if ("PARTIALLY".equals(result)) {
            intervalIndex = Math.min(reviewCount - 1, EBBINGHAUS_INTERVALS.length - 1);
        } else {
            intervalIndex = Math.min(reviewCount, EBBINGHAUS_INTERVALS.length - 1);
        }

        int days = EBBINGHAUS_INTERVALS[intervalIndex];
        return LocalDateTime.now().plusDays(days);
    }

    /**
     * 智能优化复习计划
     */
    public String optimizeReviewPlan(String userId) {
        List<MistakeEntity> mistakes = mistakeDao.findByUserId(userId);
        if (mistakes.isEmpty()) {
            return "暂无错题数据";
        }

        List<ReviewRecordEntity> reviewHistory = getUserReviewRecords(userId);
        
        StringBuilder reviewData = new StringBuilder();
        reviewData.append(String.format("总错题数: %d\n", mistakes.size()));
        reviewData.append(String.format("总复习次数: %d\n", reviewHistory.size()));
        
        long masteredCount = mistakes.stream()
                .filter(m -> "mastered".equals(m.getMasteryStatus()))
                .count();
        reviewData.append(String.format("已掌握: %d\n", masteredCount));
        
        long needReviewCount = mistakes.stream()
                .filter(m -> m.getNextReviewTime() != null 
                        && m.getNextReviewTime().isBefore(LocalDateTime.now()))
                .count();
        reviewData.append(String.format("待复习: %d\n", needReviewCount));

        String prompt = String.format("""
                请根据以下复习数据，优化复习计划：
                
                %s
                
                请提供：
                1. 复习时间安排建议（每天复习多少道题）
                2. 复习优先级建议（哪些错题需要优先复习）
                3. 复习方法建议（如何提高复习效率）
                4. 长期规划（如何系统性地掌握所有错题）
                """,
                reviewData.toString());
        
        return geminiService.chat(prompt, null);
    }

    /**
     * 智能推荐复习难度
     */
    public String recommendDifficulty(String mistakeId) {
        MistakeEntity mistake = mistakeDao.findById(mistakeId);
        if (mistake == null) {
            return "错题不存在";
        }

        List<ReviewRecordEntity> history = getReviewHistory(mistakeId);
        
        StringBuilder historyInfo = new StringBuilder();
        historyInfo.append(String.format("当前掌握度: %d%%\n", mistake.getMasteryLevel() != null ? mistake.getMasteryLevel() : 0));
        historyInfo.append(String.format("复习次数: %d\n", mistake.getReviewCount() != null ? mistake.getReviewCount() : 0));
        historyInfo.append(String.format("当前状态: %s\n", mistake.getMasteryStatus() != null ? mistake.getMasteryStatus() : "未掌握"));
        
        if (!history.isEmpty()) {
            ReviewRecordEntity lastReview = history.get(history.size() - 1);
            historyInfo.append(String.format("上次复习结果: %s\n", lastReview.getReviewResult() != null ? lastReview.getReviewResult() : "未知"));
        }

        String prompt = String.format("""
                请根据以下错题复习情况，推荐合适的复习难度和方式：
                
                %s
                
                请提供：
                1. 推荐复习难度（简单/中等/困难）
                2. 推荐复习方式（快速回顾/详细分析/变式练习）
                3. 预期复习时间（预计需要多长时间）
                4. 复习重点（应该重点关注哪些方面）
                """,
                historyInfo.toString());
        
        return geminiService.chat(prompt, null);
    }

    /**
     * 更新错题掌握程度
     */
    private void updateMistakeMastery(String mistakeId, String result, int reviewCount) {
        int masteryDelta;
        if ("REMEMBERED".equals(result)) {
            masteryDelta = 20;
        } else if ("PARTIALLY".equals(result)) {
            masteryDelta = 10;
        } else {
            masteryDelta = -10;
        }

        MistakeEntity mistake = mistakeDao.findById(mistakeId);
        if (mistake != null) {
            int currentMastery = mistake.getMasteryLevel() != null ? mistake.getMasteryLevel() : 0;
            int newMastery = Math.max(0, Math.min(100, currentMastery + masteryDelta));

            String newStatus;
            if (newMastery >= 80) {
                newStatus = "mastered";
            } else if (newMastery >= 40) {
                newStatus = "reviewing";
            } else {
                newStatus = "not_mastered";
            }

            mistakeDao.updateMastery(mistakeId, newStatus, newMastery);
        }
    }

    /**
     * 获取错题的复习历史
     */
    public List<ReviewRecordEntity> getReviewHistory(String mistakeId) {
        return dao.findByMistakeId(mistakeId);
    }

    /**
     * 获取用户所有复习记录
     */
    public List<ReviewRecordEntity> getUserReviewRecords(String userId) {
        return dao.findByUserId(userId);
    }

    /**
     * 获取今日待复习数量
     */
    public int getTodayPendingCount(String userId) {
        List<MistakeEntity> pending = getPendingReviews(userId);
        return pending.size();
    }
}
