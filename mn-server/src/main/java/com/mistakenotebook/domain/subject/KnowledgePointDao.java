package com.mistakenotebook.domain.subject;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 知识点 DAO
 *
 * @author simon
 */
@Component
public class KnowledgePointDao extends BaseDao<KnowledgePointEntity, String> {

    public List<KnowledgePointEntity> findByChapterId(String chapterId) {
        SelectBuilder<KnowledgePointEntity> select = new SelectBuilder<>(KnowledgePointEntity.class);
        select.addWhereAnd(KnowledgePointEntity.COL_CHAPTER_ID + " = ?", chapterId);
        return execute(select);
    }
}
