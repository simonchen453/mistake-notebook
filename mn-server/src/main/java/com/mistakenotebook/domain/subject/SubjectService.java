package com.mistakenotebook.domain.subject;

import com.adminpro.framework.base.entity.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 科目服务
 *
 * @author simon
 */
@Service
public class SubjectService extends BaseService<SubjectEntity, String> {

    private SubjectDao dao;

    @Autowired
    private ChapterDao chapterDao;

    @Autowired
    private KnowledgePointDao knowledgePointDao;

    @Autowired
    protected SubjectService(SubjectDao dao) {
        super(dao);
        this.dao = dao;
    }

    // ========== Subject ==========

    public List<SubjectEntity> getAllSubjects() {
        return dao.findAllByOrderBySortOrderAsc();
    }

    // ========== Chapter ==========

    public List<ChapterEntity> getChaptersBySubject(String subjectId) {
        return chapterDao.findBySubjectIdOrderBySortOrderAsc(subjectId);
    }

    public List<ChapterEntity> getSubChapters(String parentId) {
        return chapterDao.findByParentIdOrderBySortOrderAsc(parentId);
    }

    public ChapterEntity getChapterById(String id) {
        return chapterDao.findById(id);
    }

    @Transactional
    public void createChapter(ChapterEntity chapter) {
        chapterDao.create(chapter);
    }

    @Transactional
    public void updateChapter(ChapterEntity chapter) {
        chapterDao.update(chapter);
    }

    @Transactional
    public void deleteChapter(String id) {
        chapterDao.delete(id);
    }

    // ========== KnowledgePoint ==========

    public List<KnowledgePointEntity> getKnowledgePointsByChapter(String chapterId) {
        return knowledgePointDao.findByChapterId(chapterId);
    }

    public KnowledgePointEntity getKnowledgePointById(String id) {
        return knowledgePointDao.findById(id);
    }

    @Transactional
    public void createKnowledgePoint(KnowledgePointEntity knowledgePoint) {
        knowledgePointDao.create(knowledgePoint);
    }

    @Transactional
    public void updateKnowledgePoint(KnowledgePointEntity knowledgePoint) {
        knowledgePointDao.update(knowledgePoint);
    }

    @Transactional
    public void deleteKnowledgePoint(String id) {
        knowledgePointDao.delete(id);
    }
}
