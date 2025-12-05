-- liquibase formatted sql
-- changeset simon:mn_001_init

-- 错题表
CREATE TABLE `mn_mistake_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT '错题ID',
    `col_user_id` varchar(64) NOT NULL COMMENT '用户ID',
    `col_subject_id` varchar(64) DEFAULT NULL COMMENT '科目ID',
    `col_chapter_id` varchar(64) DEFAULT NULL COMMENT '章节ID',
    `col_knowledge_point_id` varchar(64) DEFAULT NULL COMMENT '知识点ID',
    `col_title` varchar(255) NOT NULL COMMENT '题目标题',
    `col_content` mediumtext COMMENT '题目内容',
    `col_correct_answer` mediumtext COMMENT '正确答案',
    `col_my_answer` mediumtext COMMENT '我的答案',
    `col_error_reason` varchar(64) DEFAULT NULL COMMENT '错误原因类型',
    `col_error_analysis` mediumtext COMMENT '错误分析',
    `col_source` varchar(255) DEFAULT NULL COMMENT '题目来源',
    `col_image_urls` mediumtext COMMENT '图片URL列表(JSON)',
    `col_mastery_status` varchar(64) DEFAULT 'not_mastered' COMMENT '掌握状态',
    `col_mastery_level` int(11) DEFAULT 0 COMMENT '掌握等级(0-100)',
    `col_difficulty` varchar(64) DEFAULT NULL COMMENT '难度等级',
    `col_tags` varchar(500) DEFAULT NULL COMMENT '标签(逗号分隔)',
    `col_review_count` int(11) DEFAULT 0 COMMENT '复习次数',
    `col_next_review_time` datetime DEFAULT NULL COMMENT '下次复习时间',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    INDEX `idx_user_id`(`col_user_id`),
    INDEX `idx_subject_id`(`col_subject_id`),
    INDEX `idx_mastery_status`(`col_mastery_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错题表';

-- 科目表
CREATE TABLE `mn_subject_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT '科目ID',
    `col_name` varchar(128) NOT NULL COMMENT '科目名称',
    `col_icon` varchar(255) DEFAULT NULL COMMENT '图标',
    `col_sort_order` int(11) DEFAULT 0 COMMENT '排序',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    UNIQUE INDEX `unq_name`(`col_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科目表';

-- 章节表
CREATE TABLE `mn_chapter_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT '章节ID',
    `col_subject_id` varchar(64) NOT NULL COMMENT '科目ID',
    `col_parent_id` varchar(64) DEFAULT NULL COMMENT '父章节ID',
    `col_name` varchar(255) NOT NULL COMMENT '章节名称',
    `col_sort_order` int(11) DEFAULT 0 COMMENT '排序',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    INDEX `idx_subject_id`(`col_subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='章节表';

-- 知识点表
CREATE TABLE `mn_knowledge_point_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT '知识点ID',
    `col_chapter_id` varchar(64) NOT NULL COMMENT '章节ID',
    `col_name` varchar(255) NOT NULL COMMENT '知识点名称',
    `col_description` mediumtext COMMENT '描述',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    INDEX `idx_chapter_id`(`col_chapter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识点表';

-- 班级表
CREATE TABLE `mn_clazz_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT '班级ID',
    `col_name` varchar(128) NOT NULL COMMENT '班级名称',
    `col_grade` varchar(64) DEFAULT NULL COMMENT '年级',
    `col_teacher_id` varchar(64) DEFAULT NULL COMMENT '班主任ID',
    `col_description` varchar(500) DEFAULT NULL COMMENT '描述',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    INDEX `idx_teacher_id`(`col_teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 学生班级关联表
CREATE TABLE `mn_student_clazz_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT 'ID',
    `col_student_id` varchar(64) NOT NULL COMMENT '学生ID',
    `col_clazz_id` varchar(64) NOT NULL COMMENT '班级ID',
    `col_join_time` datetime DEFAULT NULL COMMENT '加入时间',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    UNIQUE INDEX `unq_student_clazz`(`col_student_id`, `col_clazz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生班级关联表';

-- 家长学生关联表
CREATE TABLE `mn_parent_student_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT 'ID',
    `col_parent_id` varchar(64) NOT NULL COMMENT '家长ID',
    `col_student_id` varchar(64) NOT NULL COMMENT '学生ID',
    `col_relationship` varchar(64) DEFAULT NULL COMMENT '关系(father/mother/other)',
    `col_bind_time` datetime DEFAULT NULL COMMENT '绑定时间',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    UNIQUE INDEX `unq_parent_student`(`col_parent_id`, `col_student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长学生关联表';

-- 复习记录表
CREATE TABLE `mn_review_record_tbl` (
    `col_id` varchar(64) NOT NULL COMMENT '记录ID',
    `col_mistake_id` varchar(64) NOT NULL COMMENT '错题ID',
    `col_user_id` varchar(64) NOT NULL COMMENT '用户ID',
    `col_review_time` datetime NOT NULL COMMENT '复习时间',
    `col_review_result` varchar(64) DEFAULT NULL COMMENT '复习结果(REMEMBERED/PARTIALLY/FORGOT)',
    `col_review_count` int(11) DEFAULT 1 COMMENT '第几次复习',
    `col_next_review_time` datetime DEFAULT NULL COMMENT '下次复习时间',
    `col_notes` mediumtext COMMENT '复习笔记',
    
    `col_created_by_user_domain` varchar(64) DEFAULT NULL COMMENT '创建人Domain',
    `col_created_by_user_id` varchar(64) DEFAULT NULL COMMENT '创建人ID',
    `col_created_date` datetime DEFAULT NULL COMMENT '创建日期',
    `col_updated_by_user_domain` varchar(64) DEFAULT NULL COMMENT '更新人Domain',
    `col_updated_by_user_id` varchar(64) DEFAULT NULL COMMENT '更新人ID',
    `col_updated_date` datetime DEFAULT NULL COMMENT '更新日期',
    PRIMARY KEY (`col_id`),
    INDEX `idx_mistake_id`(`col_mistake_id`),
    INDEX `idx_user_id`(`col_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='复习记录表';

-- 初始化科目数据
INSERT INTO `mn_subject_tbl` (`col_id`, `col_name`, `col_icon`, `col_sort_order`, `col_created_date`) VALUES 
('1', '语文', 'BookOutlined', 1, NOW()),
('2', '数学', 'CalculatorOutlined', 2, NOW()),
('3', '英语', 'GlobalOutlined', 3, NOW()),
('4', '物理', 'ExperimentOutlined', 4, NOW()),
('5', '化学', 'ExperimentOutlined', 5, NOW()),
('6', '生物', 'BugOutlined', 6, NOW()),
('7', '历史', 'HistoryOutlined', 7, NOW()),
('8', '地理', 'CompassOutlined', 8, NOW()),
('9', '政治', 'TeamOutlined', 9, NOW());
