package com.mistakenotebook.web.api;

import com.adminpro.framework.base.entity.R;
import com.adminpro.framework.base.web.BaseSearchForm;
import com.adminpro.framework.jdbc.SearchParam;
import com.adminpro.framework.jdbc.query.QueryResultSet;
import com.mistakenotebook.ai.GeminiService;
import com.mistakenotebook.domain.mistake.MistakeEntity;
import com.mistakenotebook.domain.mistake.MistakeService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 错题管理 API
 *
 * @author simon
 */
@RestController
@RequestMapping("/api/mistakes")
@PreAuthorize("@ss.hasPermission('mistake:manage')")
public class MistakeController {

    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private GeminiService geminiService;

    /**
     * 分页查询错题列表
     */
    @PostMapping("/list")
    public R<QueryResultSet<MistakeEntity>> list(@RequestBody SearchForm searchForm) {
        SearchParam param = new SearchParam();
        param.setPageNo(searchForm.getPageNo());
        param.setPageSize(searchForm.getPageSize());

        if (StringUtils.isNotEmpty(searchForm.getUserId())) {
            param.addFilter("userId", searchForm.getUserId());
        }
        if (StringUtils.isNotEmpty(searchForm.getSubjectId())) {
            param.addFilter("subjectId", searchForm.getSubjectId());
        }
        if (StringUtils.isNotEmpty(searchForm.getMasteryStatus())) {
            param.addFilter("masteryStatus", searchForm.getMasteryStatus());
        }
        if (StringUtils.isNotEmpty(searchForm.getKeyword())) {
            param.addFilter("keyword", searchForm.getKeyword());
        }

        QueryResultSet<MistakeEntity> resultSet = mistakeService.search(param);
        return R.ok(resultSet);
    }

    /**
     * 获取错题详情
     */
    @GetMapping("/detail/{id}")
    public R<MistakeEntity> detail(@PathVariable String id) {
        MistakeEntity entity = mistakeService.findById(id);
        if (entity != null) {
            return R.ok(entity);
        }
        return R.error("错题不存在");
    }

    /**
     * 创建错题
     */
    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('mistake:create')")
    public R create(@RequestBody MistakeEntity mistake) {
        mistakeService.create(mistake);
        return R.ok();
    }

    /**
     * 更新错题
     */
    @PatchMapping("/edit")
    @PreAuthorize("@ss.hasPermission('mistake:edit')")
    public R edit(@RequestBody MistakeEntity mistake) {
        mistakeService.update(mistake);
        return R.ok();
    }

    /**
     * 删除错题
     */
    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('mistake:delete')")
    public R delete(@RequestParam("ids") String ids) {
        String[] idArray = StringUtils.split(ids, ",");
        for (String id : idArray) {
            mistakeService.delete(id);
        }
        return R.ok();
    }

    /**
     * 更新掌握状态
     */
    @PatchMapping("/{id}/mastery")
    @PreAuthorize("@ss.hasPermission('mistake:edit')")
    public R updateMastery(
            @PathVariable String id,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") Integer level) {
        mistakeService.updateMasteryStatus(id, status, level);
        return R.ok();
    }

    /**
     * AI 分析错题原因
     */
    @GetMapping("/{id}/analyze")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> analyzeWithAI(@PathVariable String id) {
        String analysis = mistakeService.analyzeWithAI(id);
        return R.ok(Map.of("analysis", analysis));
    }

    /**
     * AI 生成解题思路
     */
    @GetMapping("/{id}/solution")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> generateSolution(@PathVariable String id) {
        String solution = mistakeService.generateSolution(id);
        return R.ok(Map.of("solution", solution));
    }

    /**
     * 获取需要复习的错题
     */
    @GetMapping("/review")
    public R<List<MistakeEntity>> getReviewList(@RequestParam String userId) {
        List<MistakeEntity> mistakes = mistakeService.findNeedReview(userId);
        return R.ok(mistakes);
    }

    /**
     * 获取错题统计
     */
    @GetMapping("/stats")
    public R<Map<String, Object>> getStats(@RequestParam String userId) {
        List<Object[]> bySubject = mistakeService.getStatsBySubject(userId);
        List<Object[]> byErrorReason = mistakeService.getStatsByErrorReason(userId);

        return R.ok(Map.of(
                "bySubject", bySubject,
                "byErrorReason", byErrorReason));
    }

    /**
     * AI重新分类错误原因
     */
    @PostMapping("/{id}/reclassify")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> reclassifyErrorReason(@PathVariable String id) {
        String reason = mistakeService.reclassifyErrorReason(id);
        return R.ok(Map.of("errorReason", reason));
    }

    /**
     * 错题趋势预测
     */
    @GetMapping("/predict-trend")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> predictErrorTrend(@RequestParam String userId) {
        String prediction = mistakeService.predictErrorTrend(userId);
        return R.ok(Map.of("prediction", prediction));
    }

    /**
     * 知识点关联分析
     */
    @GetMapping("/analyze-knowledge")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> analyzeKnowledgeRelation(
            @RequestParam String userId,
            @RequestParam(required = false) String subjectId) {
        String analysis = mistakeService.analyzeKnowledgeRelation(userId, subjectId);
        return R.ok(Map.of("analysis", analysis));
    }

    /**
     * 生成变式题
     */
    @GetMapping("/{id}/variants")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> generateVariantQuestions(
            @PathVariable String id,
            @RequestParam(defaultValue = "3") int count) {
        String questions = mistakeService.generateVariantQuestions(id, count);
        return R.ok(Map.of("questions", questions));
    }

    /**
     * 生成巩固练习
     */
    @GetMapping("/practice")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> generatePracticeQuestions(
            @RequestParam String knowledgePointId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "5") int count) {
        String questions = mistakeService.generatePracticeQuestions(knowledgePointId, difficulty, count);
        return R.ok(Map.of("questions", questions));
    }

    @Data
    public static class SearchForm extends BaseSearchForm {
        private String userId;
        private String subjectId;
        private String masteryStatus;
        private String keyword;
    }
}
