package com.mistakenotebook.domain.review;

import com.adminpro.framework.base.entity.BaseService;
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
     * 计算下次复习时间
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
