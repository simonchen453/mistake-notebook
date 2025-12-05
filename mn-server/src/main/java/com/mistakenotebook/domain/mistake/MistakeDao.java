package com.mistakenotebook.domain.mistake;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.SearchParam;
import com.adminpro.framework.jdbc.query.QueryResultSet;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 错题 DAO
 *
 * @author simon
 */
@Component
public class MistakeDao extends BaseDao<MistakeEntity, String> {

    /**
     * 分页搜索
     */
    public QueryResultSet<MistakeEntity> search(SearchParam param) {
        SelectBuilder<MistakeEntity> select = new SelectBuilder<>(MistakeEntity.class);
        select.setSearchParam(param);
        prepareSelectBuilder(select, param);
        return search(select);
    }

    /**
     * 根据条件查询列表
     */
    public List<MistakeEntity> findByParam(SearchParam param) {
        SelectBuilder<MistakeEntity> select = new SelectBuilder<>(MistakeEntity.class);
        prepareSelectBuilder(select, param);
        return execute(select);
    }

    /**
     * 准备查询条件
     */
    private void prepareSelectBuilder(SelectBuilder<MistakeEntity> select, SearchParam param) {
        Map<String, Object> filters = param.getFilters();
        String userId = (String) filters.get("userId");
        String subjectId = (String) filters.get("subjectId");
        String masteryStatus = (String) filters.get("masteryStatus");
        String keyword = (String) filters.get("keyword");

        if (StringUtils.isNotEmpty(userId)) {
            select.addWhereAnd(MistakeEntity.COL_USER_ID + " = ?", userId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            select.addWhereAnd(MistakeEntity.COL_SUBJECT_ID + " = ?", subjectId);
        }
        if (StringUtils.isNotEmpty(masteryStatus)) {
            select.addWhereAnd(MistakeEntity.COL_MASTERY_STATUS + " = ?", masteryStatus);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            select.addWhereAnd(MistakeEntity.COL_TITLE + " like ?", "%" + keyword + "%");
        }
        select.addOrderByDescending(MistakeEntity.COL_CREATED_DATE);
    }

    /**
     * 根据用户ID查询
     */
    public List<MistakeEntity> findByUserId(String userId) {
        SelectBuilder<MistakeEntity> select = new SelectBuilder<>(MistakeEntity.class);
        select.addWhereAnd(MistakeEntity.COL_USER_ID + " = ?", userId);
        select.addOrderByDescending(MistakeEntity.COL_CREATED_DATE);
        return execute(select);
    }

    /**
     * 根据用户ID和科目ID查询
     */
    public List<MistakeEntity> findByUserIdAndSubjectId(String userId, String subjectId) {
        SelectBuilder<MistakeEntity> select = new SelectBuilder<>(MistakeEntity.class);
        select.addWhereAnd(MistakeEntity.COL_USER_ID + " = ?", userId);
        select.addWhereAnd(MistakeEntity.COL_SUBJECT_ID + " = ?", subjectId);
        select.addOrderByDescending(MistakeEntity.COL_CREATED_DATE);
        return execute(select);
    }

    /**
     * 查询需要复习的错题
     */
    public List<MistakeEntity> findNeedReview(String userId) {
        SelectBuilder<MistakeEntity> select = new SelectBuilder<>(MistakeEntity.class);
        select.addWhereAnd(MistakeEntity.COL_USER_ID + " = ?", userId);
        select.addWhereAnd(MistakeEntity.COL_MASTERY_STATUS + " != ?", "mastered");
        select.addWhereAnd(MistakeEntity.COL_NEXT_REVIEW_TIME + " <= ?", LocalDateTime.now());
        select.addOrderByAscending(MistakeEntity.COL_NEXT_REVIEW_TIME);
        return execute(select);
    }

    /**
     * 更新掌握状态
     */
    public void updateMastery(String id, String status, Integer level) {
        MistakeEntity entity = findById(id);
        if (entity != null) {
            entity.setMasteryStatus(status);
            entity.setMasteryLevel(level);
            update(entity);
        }
    }

    /**
     * 按科目统计
     */
    public List<Object[]> getStatsBySubject(String userId) {
        String sql = "SELECT " + MistakeEntity.COL_SUBJECT_ID + ", COUNT(*) " +
                "FROM " + MistakeEntity.TABLE_NAME +
                " WHERE " + MistakeEntity.COL_USER_ID + " = ? " +
                "GROUP BY " + MistakeEntity.COL_SUBJECT_ID;
        return getJdbcTemplate().query(sql, (rs, i) -> new Object[] { rs.getString(1), rs.getLong(2) }, userId);
    }

    /**
     * 按错误原因统计
     */
    public List<Object[]> getStatsByErrorReason(String userId) {
        String sql = "SELECT " + MistakeEntity.COL_ERROR_REASON + ", COUNT(*) " +
                "FROM " + MistakeEntity.TABLE_NAME +
                " WHERE " + MistakeEntity.COL_USER_ID + " = ? " +
                "GROUP BY " + MistakeEntity.COL_ERROR_REASON;
        return getJdbcTemplate().query(sql, (rs, i) -> new Object[] { rs.getString(1), rs.getLong(2) }, userId);
    }
}
