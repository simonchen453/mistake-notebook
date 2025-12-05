package com.mistakenotebook.domain.clazz;

import com.adminpro.framework.base.entity.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 班级服务
 *
 * @author simon
 */
@Service
public class ClazzService extends BaseService<ClazzEntity, String> {

    private ClazzDao dao;

    @Autowired
    private StudentClazzDao studentClazzDao;

    @Autowired
    private ParentStudentDao parentStudentDao;

    @Autowired
    protected ClazzService(ClazzDao dao) {
        super(dao);
        this.dao = dao;
    }

    // ========== Clazz ==========

    public List<ClazzEntity> getClazzsByTeacher(String teacherId) {
        return dao.findByTeacherId(teacherId);
    }

    public List<ClazzEntity> getAllClazzs() {
        return dao.findAll();
    }

    // ========== StudentClazz ==========

    public List<StudentClazzEntity> getStudentsByClazz(String clazzId) {
        return studentClazzDao.findByClazzId(clazzId);
    }

    public List<StudentClazzEntity> getClazzsByStudent(String studentId) {
        return studentClazzDao.findByStudentId(studentId);
    }

    @Transactional
    public void addStudentToClazz(String studentId, String clazzId) {
        StudentClazzEntity sc = new StudentClazzEntity();
        sc.setStudentId(studentId);
        sc.setClazzId(clazzId);
        sc.setJoinTime(LocalDateTime.now());
        studentClazzDao.create(sc);
    }

    @Transactional
    public void removeStudentFromClazz(String studentId, String clazzId) {
        studentClazzDao.deleteByClazzIdAndStudentId(clazzId, studentId);
    }

    // ========== ParentStudent ==========

    public List<ParentStudentEntity> getChildrenByParent(String parentId) {
        return parentStudentDao.findByParentId(parentId);
    }

    public List<ParentStudentEntity> getParentsByStudent(String studentId) {
        return parentStudentDao.findByStudentId(studentId);
    }

    @Transactional
    public void bindParentToStudent(String parentId, String studentId, String relationship) {
        ParentStudentEntity ps = new ParentStudentEntity();
        ps.setParentId(parentId);
        ps.setStudentId(studentId);
        ps.setRelationship(relationship);
        ps.setBindTime(LocalDateTime.now());
        parentStudentDao.create(ps);
    }

    @Transactional
    public void unbindParentFromStudent(String bindingId) {
        parentStudentDao.delete(bindingId);
    }
}
