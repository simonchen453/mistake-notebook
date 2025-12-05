package com.mistakenotebook.domain.clazz;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 班级 DAO
 *
 * @author simon
 */
@Component
public class ClazzDao extends BaseDao<ClazzEntity, String> {

    public List<ClazzEntity> findByTeacherId(String teacherId) {
        SelectBuilder<ClazzEntity> select = new SelectBuilder<>(ClazzEntity.class);
        select.addWhereAnd(ClazzEntity.COL_TEACHER_ID + " = ?", teacherId);
        return execute(select);
    }

    public List<ClazzEntity> findAll() {
        SelectBuilder<ClazzEntity> select = new SelectBuilder<>(ClazzEntity.class);
        return execute(select);
    }
}
