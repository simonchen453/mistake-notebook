package com.mistakenotebook.domain.clazz;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 家长学生关联 DAO
 *
 * @author simon
 */
@Component
public class ParentStudentDao extends BaseDao<ParentStudentEntity, String> {

    public List<ParentStudentEntity> findByParentId(String parentId) {
        SelectBuilder<ParentStudentEntity> select = new SelectBuilder<>(ParentStudentEntity.class);
        select.addWhereAnd(ParentStudentEntity.COL_PARENT_ID + " = ?", parentId);
        return execute(select);
    }

    public List<ParentStudentEntity> findByStudentId(String studentId) {
        SelectBuilder<ParentStudentEntity> select = new SelectBuilder<>(ParentStudentEntity.class);
        select.addWhereAnd(ParentStudentEntity.COL_STUDENT_ID + " = ?", studentId);
        return execute(select);
    }
}
