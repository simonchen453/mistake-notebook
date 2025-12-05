package com.mistakenotebook.domain.report;

import com.mistakenotebook.ai.GeminiService;
import com.mistakenotebook.domain.mistake.MistakeDao;
import com.mistakenotebook.domain.mistake.MistakeEntity;
import com.mistakenotebook.domain.mistake.MistakeService;
import com.mistakenotebook.domain.review.ReviewRecordDao;
import com.mistakenotebook.domain.review.ReviewRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学习报告服务
 *
 * @author simon
 */
@Service
public class ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private MistakeDao mistakeDao;

    @Autowired
    private MistakeService mistakeService;

    @Autowired
    private ReviewRecordDao reviewRecordDao;

    @Autowired
    private GeminiService geminiService;

    /**
     * 生成学习报告
     */
    public String generateStudyReport(String userId, String subjectId) {
        List<MistakeEntity> mistakes;
        if (subjectId != null) {
            mistakes = mistakeDao.findByUserIdAndSubjectId(userId, subjectId);
        } else {
            mistakes = mistakeDao.findByUserId(userId);
        }

        if (mistakes.isEmpty()) {
            return "暂无错题数据，无法生成学习报告";
        }

        List<Object[]> subjectStats = mistakeService.getStatsBySubject(userId);
        List<Object[]> errorReasonStats = mistakeService.getStatsByErrorReason(userId);

        List<ReviewRecordEntity> allReviews = reviewRecordDao.findByUserId(userId);
        int totalReviewCount = allReviews.size();

        long masteredCount = mistakes.stream()
                .filter(m -> "mastered".equals(m.getMasteryStatus()))
                .count();

        String subject = subjectId != null ? subjectId : "全部科目";

        return geminiService.generateStudyReport(userId, subject, mistakes, 
                subjectStats, errorReasonStats, totalReviewCount, (int) masteredCount);
    }

    /**
     * 生成周报
     */
    public String generateWeeklyReport(String userId) {
        List<MistakeEntity> allMistakes = mistakeDao.findByUserId(userId);
        
        // 获取本周创建的错题
        Date weekAgo = Date.from(LocalDateTime.now().minusDays(7).atZone(ZoneId.systemDefault()).toInstant());
        List<MistakeEntity> weeklyMistakes = allMistakes.stream()
                .filter(m -> m.getCreatedDate() != null && m.getCreatedDate().after(weekAgo))
                .collect(Collectors.toList());

        // 获取本周的复习记录
        List<ReviewRecordEntity> weeklyReviews = reviewRecordDao.findByUserId(userId).stream()
                .filter(r -> r.getReviewTime() != null && r.getReviewTime().isAfter(LocalDateTime.now().minusDays(7)))
                .collect(Collectors.toList());

        StringBuilder reportData = new StringBuilder();
        reportData.append(String.format("本周新增错题: %d道\n", weeklyMistakes.size()));
        reportData.append(String.format("本周复习次数: %d次\n", weeklyReviews.size()));
        
        long weeklyMastered = weeklyMistakes.stream()
                .filter(m -> "mastered".equals(m.getMasteryStatus()))
                .count();
        reportData.append(String.format("本周掌握: %d道\n", weeklyMastered));

        if (!weeklyMistakes.isEmpty()) {
            Map<String, Long> errorReasonCount = weeklyMistakes.stream()
                    .filter(m -> m.getErrorReason() != null)
                    .collect(Collectors.groupingBy(MistakeEntity::getErrorReason, Collectors.counting()));
            
            reportData.append("\n本周错误原因分布:\n");
            errorReasonCount.forEach((reason, count) -> 
                    reportData.append(String.format("- %s: %d道\n", reason, count)));
        }

        String prompt = String.format("""
                请根据以下本周学习数据，生成学习周报：
                
                %s
                
                请生成一份鼓励性的周报，包括：
                1. 本周学习总结
                2. 进步亮点
                3. 需要改进的地方
                4. 下周学习建议
                """,
                reportData.toString());

        return geminiService.chat(prompt, null);
    }

    /**
     * 生成月报
     */
    public String generateMonthlyReport(String userId) {
        List<MistakeEntity> allMistakes = mistakeDao.findByUserId(userId);
        
        // 获取本月创建的错题
        Date monthAgo = Date.from(LocalDateTime.now().minusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        List<MistakeEntity> monthlyMistakes = allMistakes.stream()
                .filter(m -> m.getCreatedDate() != null && m.getCreatedDate().after(monthAgo))
                .collect(Collectors.toList());

        // 获取本月的复习记录
        List<ReviewRecordEntity> monthlyReviews = reviewRecordDao.findByUserId(userId).stream()
                .filter(r -> r.getReviewTime() != null && r.getReviewTime().isAfter(LocalDateTime.now().minusDays(30)))
                .collect(Collectors.toList());

        List<Object[]> subjectStats = mistakeService.getStatsBySubject(userId);
        List<Object[]> errorReasonStats = mistakeService.getStatsByErrorReason(userId);

        StringBuilder reportData = new StringBuilder();
        reportData.append(String.format("本月新增错题: %d道\n", monthlyMistakes.size()));
        reportData.append(String.format("本月复习次数: %d次\n", monthlyReviews.size()));
        
        long monthlyMastered = monthlyMistakes.stream()
                .filter(m -> "mastered".equals(m.getMasteryStatus()))
                .count();
        reportData.append(String.format("本月掌握: %d道\n", monthlyMastered));
        reportData.append(String.format("总错题数: %d道\n", allMistakes.size()));

        if (subjectStats != null && !subjectStats.isEmpty()) {
            reportData.append("\n按科目分布:\n");
            for (Object[] stat : subjectStats) {
                reportData.append(String.format("- %s: %d道\n", stat[0], stat[1]));
            }
        }

        String prompt = String.format("""
                请根据以下本月学习数据，生成详细的学习月报：
                
                %s
                
                请生成一份全面的月报，包括：
                1. 本月学习概况
                2. 学习成果分析
                3. 薄弱环节识别
                4. 下月学习规划
                5. 长期学习建议
                """,
                reportData.toString());

        return geminiService.chat(prompt, null);
    }
}

