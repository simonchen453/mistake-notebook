package com.mistakenotebook.web.api;

import com.adminpro.framework.base.entity.R;
import com.mistakenotebook.domain.subject.ChapterEntity;
import com.mistakenotebook.domain.subject.KnowledgePointEntity;
import com.mistakenotebook.domain.subject.SubjectEntity;
import com.mistakenotebook.domain.subject.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 科目管理 API
 *
 * @author simon
 */
@RestController
@RequestMapping("/api/subjects")
@PreAuthorize("@ss.hasPermission('subject:manage')")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    /**
     * 获取所有科目
     */
    @GetMapping
    public R<List<SubjectEntity>> list() {
        return R.ok(subjectService.getAllSubjects());
    }

    /**
     * 获取科目详情
     */
    @GetMapping("/detail/{id}")
    public R<SubjectEntity> detail(@PathVariable String id) {
        SubjectEntity entity = subjectService.findById(id);
        if (entity != null) {
            return R.ok(entity);
        }
        return R.error("科目不存在");
    }

    /**
     * 创建科目
     */
    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('subject:create')")
    public R create(@RequestBody SubjectEntity subject) {
        subjectService.create(subject);
        return R.ok();
    }

    /**
     * 更新科目
     */
    @PatchMapping("/edit")
    @PreAuthorize("@ss.hasPermission('subject:edit')")
    public R edit(@RequestBody SubjectEntity subject) {
        subjectService.update(subject);
        return R.ok();
    }

    /**
     * 删除科目
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@ss.hasPermission('subject:delete')")
    public R delete(@PathVariable String id) {
        subjectService.delete(id);
        return R.ok();
    }

    // ========== Chapter ==========

    /**
     * 获取科目的章节列表
     */
    @GetMapping("/{subjectId}/chapters")
    public R<List<ChapterEntity>> listChapters(@PathVariable String subjectId) {
        return R.ok(subjectService.getChaptersBySubject(subjectId));
    }

    /**
     * 获取子章节
     */
    @GetMapping("/chapters/{parentId}/children")
    public R<List<ChapterEntity>> listSubChapters(@PathVariable String parentId) {
        return R.ok(subjectService.getSubChapters(parentId));
    }

    /**
     * 创建章节
     */
    @PostMapping("/chapters/create")
    @PreAuthorize("@ss.hasPermission('subject:chapter:create')")
    public R createChapter(@RequestBody ChapterEntity chapter) {
        subjectService.createChapter(chapter);
        return R.ok();
    }

    /**
     * 更新章节
     */
    @PatchMapping("/chapters/edit")
    @PreAuthorize("@ss.hasPermission('subject:chapter:edit')")
    public R editChapter(@RequestBody ChapterEntity chapter) {
        subjectService.updateChapter(chapter);
        return R.ok();
    }

    /**
     * 删除章节
     */
    @DeleteMapping("/chapters/delete/{id}")
    @PreAuthorize("@ss.hasPermission('subject:chapter:delete')")
    public R deleteChapter(@PathVariable String id) {
        subjectService.deleteChapter(id);
        return R.ok();
    }

    // ========== KnowledgePoint ==========

    /**
     * 获取章节的知识点列表
     */
    @GetMapping("/chapters/{chapterId}/points")
    public R<List<KnowledgePointEntity>> listKnowledgePoints(@PathVariable String chapterId) {
        return R.ok(subjectService.getKnowledgePointsByChapter(chapterId));
    }

    /**
     * 创建知识点
     */
    @PostMapping("/points/create")
    @PreAuthorize("@ss.hasPermission('subject:point:create')")
    public R createKnowledgePoint(@RequestBody KnowledgePointEntity point) {
        subjectService.createKnowledgePoint(point);
        return R.ok();
    }

    /**
     * 更新知识点
     */
    @PatchMapping("/points/edit")
    @PreAuthorize("@ss.hasPermission('subject:point:edit')")
    public R editKnowledgePoint(@RequestBody KnowledgePointEntity point) {
        subjectService.updateKnowledgePoint(point);
        return R.ok();
    }

    /**
     * 删除知识点
     */
    @DeleteMapping("/points/delete/{id}")
    @PreAuthorize("@ss.hasPermission('subject:point:delete')")
    public R deleteKnowledgePoint(@PathVariable String id) {
        subjectService.deleteKnowledgePoint(id);
        return R.ok();
    }
}
