package com.mistakenotebook.domain.clazz;

import com.adminpro.framework.base.entity.BaseDao;
import com.adminpro.framework.jdbc.sqlbuilder.DeleteBuilder;
import com.adminpro.framework.jdbc.sqlbuilder.SelectBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 学生班级关联 DAO
 *
 * @author simon
 */
@Component
public class StudentClazzDao extends BaseDao<StudentClazzEntity, String> {

    public List<StudentClazzEntity> findByClazzId(String clazzId) {
        SelectBuilder<StudentClazzEntity> select = new SelectBuilder<>(StudentClazzEntity.class);
        select.addWhereAnd(StudentClazzEntity.COL_CLAZZ_ID + " = ?", clazzId);
        return execute(select);
    }

    public List<StudentClazzEntity> findByStudentId(String studentId) {
        SelectBuilder<StudentClazzEntity> select = new SelectBuilder<>(StudentClazzEntity.class);
        select.addWhereAnd(StudentClazzEntity.COL_STUDENT_ID + " = ?", studentId);
        return execute(select);
    }

    public void deleteByClazzIdAndStudentId(String clazzId, String studentId) {
        DeleteBuilder delete = new DeleteBuilder(StudentClazzEntity.TABLE_NAME);
        delete.addWhereAnd(StudentClazzEntity.COL_CLAZZ_ID + " = ?", clazzId);
        delete.addWhereAnd(StudentClazzEntity.COL_STUDENT_ID + " = ?", studentId);
        execute(delete);
    }
}
