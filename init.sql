-- 初始化数据库脚本

-- 初始化科目
INSERT INTO mn_subject (id, name, icon, sort_order) VALUES 
(1, '语文', 'book', 1),
(2, '数学', 'calculator', 2),
(3, '英语', 'globe', 3),
(4, '物理', 'atom', 4),
(5, '化学', 'flask', 5),
(6, '生物', 'leaf', 6),
(7, '历史', 'history', 7),
(8, '地理', 'map', 8),
(9, '政治', 'landmark', 9)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 初始化数学章节（七年级上）
INSERT INTO mn_chapter (id, subject_id, parent_id, name, sort_order) VALUES
(1, 2, NULL, '七年级上册', 1),
(2, 2, 1, '第一章 有理数', 1),
(3, 2, 1, '第二章 整式的加减', 2),
(4, 2, 1, '第三章 一元一次方程', 3),
(5, 2, 1, '第四章 几何图形初步', 4)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 初始化知识点
INSERT INTO mn_knowledge_point (id, chapter_id, name, description) VALUES
(1, 2, '正数和负数', '正数、负数的概念及其表示'),
(2, 2, '有理数', '有理数的分类：整数和分数'),
(3, 2, '有理数的加减法', '同号相加、异号相加的法则'),
(4, 2, '有理数的乘除法', '乘法法则、除法法则'),
(5, 3, '整式', '单项式和多项式的概念'),
(6, 3, '整式的加减', '合并同类项、去括号'),
(7, 4, '一元一次方程', '方程的概念、解方程的基本步骤'),
(8, 4, '实际问题与一元一次方程', '列方程解应用题')
ON DUPLICATE KEY UPDATE name = VALUES(name);
