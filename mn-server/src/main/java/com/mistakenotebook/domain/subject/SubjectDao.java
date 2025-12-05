package com.mistakenotebook.domain.subject;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 科目 DAO
 *
 * @author simon
 */
@Component
public class SubjectDao extends BaseDao<SubjectEntity, String> {

    public List<SubjectEntity> findAllByOrderBySortOrderAsc() {
        SelectBuilder<SubjectEntity> select = new SelectBuilder<>(SubjectEntity.class);
        select.addOrderByAscending(SubjectEntity.COL_SORT_ORDER);
        return execute(select);
    }
}
