package com.mistakenotebook.web.api;

import com.adminpro.framework.base.entity.R;
import com.adminpro.framework.jdbc.SearchParam;
import com.mistakenotebook.domain.clazz.ClazzService;
import com.mistakenotebook.domain.clazz.ParentStudentEntity;
import com.mistakenotebook.domain.mistake.MistakeService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 家长端 API
 *
 * @author simon
 */
@RestController
@RequestMapping("/api/parent")
@PreAuthorize("@ss.hasPermission('parent:manage')")
public class ParentController {

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private MistakeService mistakeService;

    /**
     * 获取绑定的孩子列表
     */
    @GetMapping("/children")
    public R<List<ParentStudentEntity>> listChildren(@RequestParam String parentId) {
        return R.ok(clazzService.getChildrenByParent(parentId));
    }

    /**
     * 申请绑定孩子
     */
    @PostMapping("/bind")
    @PreAuthorize("@ss.hasPermission('parent:bind')")
    public R requestBinding(@RequestBody BindRequest request) {
        clazzService.bindParentToStudent(
                request.getParentId(),
                request.getStudentId(),
                request.getRelationship());
        return R.ok();
    }

    /**
     * 解除绑定
     */
    @DeleteMapping("/unbind/{bindingId}")
    @PreAuthorize("@ss.hasPermission('parent:unbind')")
    public R unbind(@PathVariable String bindingId) {
        clazzService.unbindParentFromStudent(bindingId);
        return R.ok();
    }

    /**
     * 查看孩子的错题（只读）
     */
    @GetMapping("/children/{studentId}/mistakes")
    @PreAuthorize("@ss.hasPermission('parent:view')")
    public R<?> viewChildMistakes(
            @PathVariable String studentId,
            @RequestParam String parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 验证绑定关系
        List<ParentStudentEntity> bindings = clazzService.getChildrenByParent(parentId);
        boolean hasBinding = bindings.stream()
                .anyMatch(b -> b.getStudentId().equals(studentId));
        if (!hasBinding) {
            return R.error("无权访问");
        }

        SearchParam param = new SearchParam();
        param.setPageNo(page);
        param.setPageSize(size);
        param.addFilter("userId", studentId);

        return R.ok(mistakeService.search(param));
    }

    /**
     * 查看孩子的错题统计
     */
    @GetMapping("/children/{studentId}/stats")
    @PreAuthorize("@ss.hasPermission('parent:view')")
    public R<?> viewChildStats(
            @PathVariable String studentId,
            @RequestParam String parentId) {

        // 验证绑定关系
        List<ParentStudentEntity> bindings = clazzService.getChildrenByParent(parentId);
        boolean hasBinding = bindings.stream()
                .anyMatch(b -> b.getStudentId().equals(studentId));
        if (!hasBinding) {
            return R.error("无权访问");
        }

        List<Object[]> bySubject = mistakeService.getStatsBySubject(studentId);
        List<Object[]> byErrorReason = mistakeService.getStatsByErrorReason(studentId);

        return R.ok(Map.of(
                "bySubject", bySubject,
                "byErrorReason", byErrorReason));
    }

    @Data
    public static class BindRequest {
        private String parentId;
        private String studentId;
        private String relationship;
    }
}
