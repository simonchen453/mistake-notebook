package com.mistakenotebook.web.api;

import com.adminpro.framework.base.entity.R;
import com.mistakenotebook.domain.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 学习报告 API
 *
 * @author simon
 */
@RestController
@RequestMapping("/api/reports")
@PreAuthorize("@ss.hasPermission('mistake:manage')")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 生成学习报告
     */
    @GetMapping("/study")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> generateStudyReport(
            @RequestParam String userId,
            @RequestParam(required = false) String subjectId) {
        String report = reportService.generateStudyReport(userId, subjectId);
        return R.ok(Map.of("report", report));
    }

    /**
     * 生成学习周报
     */
    @GetMapping("/weekly")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> generateWeeklyReport(@RequestParam String userId) {
        String report = reportService.generateWeeklyReport(userId);
        return R.ok(Map.of("report", report));
    }

    /**
     * 生成学习月报
     */
    @GetMapping("/monthly")
    @PreAuthorize("@ss.hasPermission('mistake:ai')")
    public R<Map<String, String>> generateMonthlyReport(@RequestParam String userId) {
        String report = reportService.generateMonthlyReport(userId);
        return R.ok(Map.of("report", report));
    }
}

