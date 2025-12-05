package com.mistakenotebook.web.api;

import com.adminpro.framework.base.entity.R;
import com.mistakenotebook.domain.clazz.ClazzEntity;
import com.mistakenotebook.domain.clazz.ClazzService;
import com.mistakenotebook.domain.clazz.StudentClazzEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班级管理 API
 *
 * @author simon
 */
@RestController
@RequestMapping("/api/clazzs")
@PreAuthorize("@ss.hasPermission('clazz:manage')")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 获取教师的班级列表
     */
    @GetMapping
    public R<List<ClazzEntity>> list(@RequestParam String teacherId) {
        return R.ok(clazzService.getClazzsByTeacher(teacherId));
    }

    /**
     * 获取所有班级
     */
    @GetMapping("/all")
    public R<List<ClazzEntity>> listAll() {
        return R.ok(clazzService.getAllClazzs());
    }

    /**
     * 获取班级详情
     */
    @GetMapping("/detail/{id}")
    public R<ClazzEntity> detail(@PathVariable String id) {
        ClazzEntity entity = clazzService.findById(id);
        if (entity != null) {
            return R.ok(entity);
        }
        return R.error("班级不存在");
    }

    /**
     * 创建班级
     */
    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('clazz:create')")
    public R create(@RequestBody ClazzEntity clazz) {
        clazzService.create(clazz);
        return R.ok();
    }

    /**
     * 更新班级
     */
    @PatchMapping("/edit")
    @PreAuthorize("@ss.hasPermission('clazz:edit')")
    public R edit(@RequestBody ClazzEntity clazz) {
        clazzService.update(clazz);
        return R.ok();
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@ss.hasPermission('clazz:delete')")
    public R delete(@PathVariable String id) {
        clazzService.delete(id);
        return R.ok();
    }

    /**
     * 获取班级学生列表
     */
    @GetMapping("/{clazzId}/students")
    public R<List<StudentClazzEntity>> listStudents(@PathVariable String clazzId) {
        return R.ok(clazzService.getStudentsByClazz(clazzId));
    }

    /**
     * 添加学生到班级
     */
    @PostMapping("/{clazzId}/students")
    @PreAuthorize("@ss.hasPermission('clazz:student:add')")
    public R addStudent(
            @PathVariable String clazzId,
            @RequestBody AddStudentRequest request) {
        clazzService.addStudentToClazz(request.getStudentId(), clazzId);
        return R.ok();
    }

    /**
     * 从班级移除学生
     */
    @DeleteMapping("/{clazzId}/students/{studentId}")
    @PreAuthorize("@ss.hasPermission('clazz:student:remove')")
    public R removeStudent(
            @PathVariable String clazzId,
            @PathVariable String studentId) {
        clazzService.removeStudentFromClazz(studentId, clazzId);
        return R.ok();
    }

    @Data
    public static class AddStudentRequest {
        private String studentId;
    }
}
