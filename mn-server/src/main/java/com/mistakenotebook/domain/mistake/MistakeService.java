package com.mistakenotebook.domain.mistake;

import com.adminpro.framework.base.entity.BaseService;
import com.adminpro.framework.jdbc.SearchParam;
import com.adminpro.framework.jdbc.query.QueryResultSet;
import com.mistakenotebook.ai.GeminiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 错题服务
 *
 * @author simon
 */
@Service
public class MistakeService extends BaseService<MistakeEntity, String> {
    private static final Logger logger = LoggerFactory.getLogger(MistakeService.class);

    private MistakeDao dao;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    protected MistakeService(MistakeDao dao) {
        super(dao);
        this.dao = dao;
    }

    /**
     * 分页搜索
     */
    public QueryResultSet<MistakeEntity> search(SearchParam param) {
        return dao.search(param);
    }

    /**
     * 根据条件查询
     */
    public List<MistakeEntity> findByParam(SearchParam param) {
        return dao.findByParam(param);
    }

    /**
     * 根据用户ID查询
     */
    public List<MistakeEntity> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }

    /**
     * 根据用户ID和科目ID查询
     */
    public List<MistakeEntity> findByUserIdAndSubjectId(String userId, String subjectId) {
        return dao.findByUserIdAndSubjectId(userId, subjectId);
    }

    /**
     * 查询需要复习的错题
     */
    public List<MistakeEntity> findNeedReview(String userId) {
        return dao.findNeedReview(userId);
    }

    /**
     * 创建错题（带AI自动分类）
     */
    @Transactional
    @Override
    public void create(MistakeEntity entity) {
        logger.info("创建错题: userId={}, subjectId={}", entity.getUserId(), entity.getSubjectId());
        if (entity.getMasteryStatus() == null) {
            entity.setMasteryStatus("not_mastered");
        }
        if (entity.getMasteryLevel() == null) {
            entity.setMasteryLevel(0);
        }
        if (entity.getReviewCount() == null) {
            entity.setReviewCount(0);
        }
        
        // AI自动分类错误原因
        if (entity.getErrorReason() == null && entity.getContent() != null 
                && entity.getCorrectAnswer() != null && entity.getMyAnswer() != null) {
            try {
                String classifiedReason = geminiService.classifyErrorReason(entity);
                entity.setErrorReason(classifiedReason);
                logger.info("AI自动分类错误原因: {}", classifiedReason);
            } catch (Exception e) {
                logger.warn("AI自动分类失败，使用默认值", e);
                entity.setErrorReason("unknown");
            }
        }
        
        // AI自动评估难度
        if (entity.getDifficulty() == null && entity.getContent() != null) {
            try {
                String difficulty = geminiService.assessDifficulty(entity);
                entity.setDifficulty(difficulty);
                logger.info("AI自动评估难度: {}", difficulty);
            } catch (Exception e) {
                logger.warn("AI自动评估难度失败，使用默认值", e);
                entity.setDifficulty("medium");
            }
        }
        
        super.create(entity);
    }

    /**
     * 更新掌握状态
     */
    @Transactional
    public void updateMasteryStatus(String id, String status, Integer level) {
        logger.info("更新掌握状态: id={}, status={}, level={}", id, status, level);
        dao.updateMastery(id, status, level);
    }

    /**
     * 按科目统计
     */
    public List<Object[]> getStatsBySubject(String userId) {
        return dao.getStatsBySubject(userId);
    }

    /**
     * 按错误原因统计
     */
    public List<Object[]> getStatsByErrorReason(String userId) {
        return dao.getStatsByErrorReason(userId);
    }

    /**
     * AI分析错题
     */
    public String analyzeWithAI(String id) {
        MistakeEntity mistake = findById(id);
        if (mistake == null) {
            return "错题不存在";
        }
        return geminiService.analyzeError(mistake);
    }

    /**
     * AI生成解题思路
     */
    public String generateSolution(String id) {
        MistakeEntity mistake = findById(id);
        if (mistake == null) {
            return "错题不存在";
        }
        return geminiService.generateSolution(mistake);
    }

    /**
     * AI重新分类错误原因
     */
    public String reclassifyErrorReason(String id) {
        MistakeEntity mistake = findById(id);
        if (mistake == null) {
            return "错题不存在";
        }
        String classifiedReason = geminiService.classifyErrorReason(mistake);
        mistake.setErrorReason(classifiedReason);
        update(mistake);
        return classifiedReason;
    }

    /**
     * 错题趋势预测
     */
    public String predictErrorTrend(String userId) {
        List<MistakeEntity> allMistakes = findByUserId(userId);
        if (allMistakes.isEmpty()) {
            return "暂无错题数据，无法进行趋势预测";
        }
        
        // 获取最近的错题（最近创建的）
        List<MistakeEntity> recentMistakes = allMistakes.stream()
                .sorted((a, b) -> {
                    if (a.getCreatedDate() == null || b.getCreatedDate() == null) return 0;
                    return b.getCreatedDate().compareTo(a.getCreatedDate());
                })
                .limit(10)
                .toList();
        
        return geminiService.predictErrorTrend(recentMistakes, allMistakes);
    }

    /**
     * 知识点关联分析
     */
    public String analyzeKnowledgeRelation(String userId, String subjectId) {
        List<MistakeEntity> mistakes;
        if (subjectId != null) {
            mistakes = findByUserIdAndSubjectId(userId, subjectId);
        } else {
            mistakes = findByUserId(userId);
        }
        
        if (mistakes.isEmpty()) {
            return "暂无错题数据，无法进行关联分析";
        }
        
        return geminiService.analyzeKnowledgeRelation(mistakes);
    }

    /**
     * 生成变式题
     */
    public String generateVariantQuestions(String id, int count) {
        MistakeEntity mistake = findById(id);
        if (mistake == null) {
            return "错题不存在";
        }
        return geminiService.generateVariantQuestions(mistake, count);
    }

    /**
     * 生成巩固练习
     */
    public String generatePracticeQuestions(String knowledgePointId, String difficulty, int count) {
        return geminiService.generatePracticeQuestions(knowledgePointId, difficulty, count);
    }
}
