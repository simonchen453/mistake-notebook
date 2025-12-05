package com.mistakenotebook.domain.subject;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 章节 DAO
 *
 * @author simon
 */
@Component
public class ChapterDao extends BaseDao<ChapterEntity, String> {

    public List<ChapterEntity> findBySubjectIdOrderBySortOrderAsc(String subjectId) {
        SelectBuilder<ChapterEntity> select = new SelectBuilder<>(ChapterEntity.class);
        select.addWhereAnd(ChapterEntity.COL_SUBJECT_ID + " = ?", subjectId);
        select.addOrderByAscending(ChapterEntity.COL_SORT_ORDER);
        return execute(select);
    }

    public List<ChapterEntity> findByParentIdOrderBySortOrderAsc(String parentId) {
        SelectBuilder<ChapterEntity> select = new SelectBuilder<>(ChapterEntity.class);
        select.addWhereAnd(ChapterEntity.COL_PARENT_ID + " = ?", parentId);
        select.addOrderByAscending(ChapterEntity.COL_SORT_ORDER);
        return execute(select);
    }
}
