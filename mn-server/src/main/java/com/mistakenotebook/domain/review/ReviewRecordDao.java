package com.mistakenotebook.domain.review;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 复习记录 DAO
 *
 * @author simon
 */
@Component
public class ReviewRecordDao extends BaseDao<ReviewRecordEntity, String> {

    public List<ReviewRecordEntity> findByMistakeId(String mistakeId) {
        SelectBuilder<ReviewRecordEntity> select = new SelectBuilder<>(ReviewRecordEntity.class);
        select.addWhereAnd(ReviewRecordEntity.COL_MISTAKE_ID + " = ?", mistakeId);
        select.addOrderByDescending(ReviewRecordEntity.COL_REVIEW_TIME);
        return execute(select);
    }

    public List<ReviewRecordEntity> findByUserId(String userId) {
        SelectBuilder<ReviewRecordEntity> select = new SelectBuilder<>(ReviewRecordEntity.class);
        select.addWhereAnd(ReviewRecordEntity.COL_USER_ID + " = ?", userId);
        select.addOrderByDescending(ReviewRecordEntity.COL_REVIEW_TIME);
        return execute(select);
    }

    public List<ReviewRecordEntity> findDueReviews(String userId) {
        SelectBuilder<ReviewRecordEntity> select = new SelectBuilder<>(ReviewRecordEntity.class);
        select.addWhereAnd(ReviewRecordEntity.COL_USER_ID + " = ?", userId);
        select.addWhereAnd(ReviewRecordEntity.COL_NEXT_REVIEW_TIME + " <= ?", LocalDateTime.now());
        select.addOrderByAscending(ReviewRecordEntity.COL_NEXT_REVIEW_TIME);
        return execute(select);
    }

    public ReviewRecordEntity findLatestByMistakeId(String mistakeId) {
        SelectBuilder<ReviewRecordEntity> select = new SelectBuilder<>(ReviewRecordEntity.class);
        select.addWhereAnd(ReviewRecordEntity.COL_MISTAKE_ID + " = ?", mistakeId);
        select.addOrderByDescending(ReviewRecordEntity.COL_REVIEW_TIME);
        select.setLimit(1);
        return executeSingle(select);
    }
}
