package com.mistakenotebook.scheduler;

import com.mistakenotebook.domain.review.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 复习提醒定时任务
 *
 * @author simon
 */
@Component
public class ReviewReminderScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ReviewReminderScheduler.class);

    @Autowired
    private ReviewService reviewService;

    /**
     * 每天早上8点检查待复习内容
     * TODO: 可扩展为发送消息通知
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkReviewReminders() {
        logger.info("开始检查复习提醒...");
        // 这里可以扩展为遍历所有用户，发送通知
        // 目前仅记录日志
        logger.info("复习提醒检查完成");
    }
}
