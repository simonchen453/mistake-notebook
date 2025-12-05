-- liquibase formatted sql
-- changeset simon:mn_002_init_data

-- =========================================
-- 菜单数据 (Menu)
-- =========================================

-- 错题本主模块
INSERT INTO `sys_menu_tbl` (`col_id`, `col_name`, `col_display`, `col_parent_id`, `col_order_num`, `col_url`, `col_is_frame`, `col_type`, `col_visible`, `col_status`, `col_permission`, `col_icon`, `col_remark`, `col_created_date`) VALUES
('MN001', 'C_MISTAKE_NOTEBOOK', '错题本', '0', 1, NULL, 0, 'M', 'show', 'active', NULL, 'BookOutlined', '错题本主模块', NOW());

-- 错题管理
INSERT INTO `sys_menu_tbl` (`col_id`, `col_name`, `col_display`, `col_parent_id`, `col_order_num`, `col_url`, `col_is_frame`, `col_type`, `col_visible`, `col_status`, `col_permission`, `col_icon`, `col_remark`, `col_created_date`) VALUES
('MN002', 'M_MISTAKE', '错题管理', 'MN001', 1, '/mistake/list', 0, 'C', 'show', 'active', 'mistake:manage', 'FileTextOutlined', '错题列表', NOW()),
('MN003', 'B_MISTAKE_CREATE', '新增错题', 'MN002', 1, NULL, 0, 'F', 'show', 'active', 'mistake:create', NULL, NULL, NOW()),
('MN004', 'B_MISTAKE_EDIT', '编辑错题', 'MN002', 2, NULL, 0, 'F', 'show', 'active', 'mistake:edit', NULL, NULL, NOW()),
('MN005', 'B_MISTAKE_DELETE', '删除错题', 'MN002', 3, NULL, 0, 'F', 'show', 'active', 'mistake:delete', NULL, NULL, NOW()),
('MN006', 'B_MISTAKE_AI', 'AI分析', 'MN002', 4, NULL, 0, 'F', 'show', 'active', 'mistake:ai', NULL, NULL, NOW());

-- 复习中心
INSERT INTO `sys_menu_tbl` (`col_id`, `col_name`, `col_display`, `col_parent_id`, `col_order_num`, `col_url`, `col_is_frame`, `col_type`, `col_visible`, `col_status`, `col_permission`, `col_icon`, `col_remark`, `col_created_date`) VALUES
('MN010', 'M_REVIEW', '复习中心', 'MN001', 2, '/review', 0, 'C', 'show', 'active', 'review:manage', 'SyncOutlined', '复习中心', NOW()),
('MN011', 'B_REVIEW_RECORD', '记录复习', 'MN010', 1, NULL, 0, 'F', 'show', 'active', 'review:record', NULL, NULL, NOW());

-- 统计分析
INSERT INTO `sys_menu_tbl` (`col_id`, `col_name`, `col_display`, `col_parent_id`, `col_order_num`, `col_url`, `col_is_frame`, `col_type`, `col_visible`, `col_status`, `col_permission`, `col_icon`, `col_remark`, `col_created_date`) VALUES
('MN020', 'M_STATS', '统计分析', 'MN001', 3, '/stats', 0, 'C', 'show', 'active', 'mistake:manage', 'BarChartOutlined', '错题统计', NOW());

-- 科目管理模块
INSERT INTO `sys_menu_tbl` (`col_id`, `col_name`, `col_display`, `col_parent_id`, `col_order_num`, `col_url`, `col_is_frame`, `col_type`, `col_visible`, `col_status`, `col_permission`, `col_icon`, `col_remark`, `col_created_date`) VALUES
('MN030', 'C_SUBJECT_MGR', '科目管理', '0', 2, NULL, 0, 'M', 'show', 'active', NULL, 'AppstoreOutlined', '科目管理模块', NOW()),
('MN031', 'M_SUBJECT', '科目列表', 'MN030', 1, '/subject/list', 0, 'C', 'show', 'active', 'subject:manage', 'ReadOutlined', '科目管理', NOW()),
('MN032', 'B_SUBJECT_CREATE', '新增科目', 'MN031', 1, NULL, 0, 'F', 'show', 'active', 'subject:create', NULL, NULL, NOW()),
('MN033', 'B_SUBJECT_EDIT', '编辑科目', 'MN031', 2, NULL, 0, 'F', 'show', 'active', 'subject:edit', NULL, NULL, NOW()),
('MN034', 'B_SUBJECT_DELETE', '删除科目', 'MN031', 3, NULL, 0, 'F', 'show', 'active', 'subject:delete', NULL, NULL, NOW()),
('MN035', 'M_CHAPTER', '章节管理', 'MN030', 2, '/chapter/list', 0, 'C', 'show', 'active', 'subject:manage', 'OrderedListOutlined', '章节管理', NOW()),
('MN036', 'B_CHAPTER_CREATE', '新增章节', 'MN035', 1, NULL, 0, 'F', 'show', 'active', 'subject:chapter:create', NULL, NULL, NOW()),
('MN037', 'B_CHAPTER_EDIT', '编辑章节', 'MN035', 2, NULL, 0, 'F', 'show', 'active', 'subject:chapter:edit', NULL, NULL, NOW()),
('MN038', 'B_CHAPTER_DELETE', '删除章节', 'MN035', 3, NULL, 0, 'F', 'show', 'active', 'subject:chapter:delete', NULL, NULL, NOW());

-- 班级管理模块
INSERT INTO `sys_menu_tbl` (`col_id`, `col_name`, `col_display`, `col_parent_id`, `col_order_num`, `col_url`, `col_is_frame`, `col_type`, `col_visible`, `col_status`, `col_permission`, `col_icon`, `col_remark`, `col_created_date`) VALUES
('MN040', 'C_CLAZZ_MGR', '班级管理', '0', 3, NULL, 0, 'M', 'show', 'active', NULL, 'TeamOutlined', '班级管理模块', NOW()),
('MN041', 'M_CLAZZ', '班级列表', 'MN040', 1, '/clazz/list', 0, 'C', 'show', 'active', 'clazz:manage', 'BankOutlined', '班级管理', NOW()),
('MN042', 'B_CLAZZ_CREATE', '新增班级', 'MN041', 1, NULL, 0, 'F', 'show', 'active', 'clazz:create', NULL, NULL, NOW()),
('MN043', 'B_CLAZZ_EDIT', '编辑班级', 'MN041', 2, NULL, 0, 'F', 'show', 'active', 'clazz:edit', NULL, NULL, NOW()),
('MN044', 'B_CLAZZ_DELETE', '删除班级', 'MN041', 3, NULL, 0, 'F', 'show', 'active', 'clazz:delete', NULL, NULL, NOW()),
('MN045', 'B_CLAZZ_STUDENT_ADD', '添加学生', 'MN041', 4, NULL, 0, 'F', 'show', 'active', 'clazz:student:add', NULL, NULL, NOW()),
('MN046', 'B_CLAZZ_STUDENT_REMOVE', '移除学生', 'MN041', 5, NULL, 0, 'F', 'show', 'active', 'clazz:student:remove', NULL, NULL, NOW());

-- 家长端模块
INSERT INTO `sys_menu_tbl` (`col_id`, `col_name`, `col_display`, `col_parent_id`, `col_order_num`, `col_url`, `col_is_frame`, `col_type`, `col_visible`, `col_status`, `col_permission`, `col_icon`, `col_remark`, `col_created_date`) VALUES
('MN050', 'C_PARENT', '家长中心', '0', 4, NULL, 0, 'M', 'show', 'active', NULL, 'UserOutlined', '家长端模块', NOW()),
('MN051', 'M_PARENT_CHILDREN', '孩子管理', 'MN050', 1, '/parent/children', 0, 'C', 'show', 'active', 'parent:manage', 'UsergroupAddOutlined', '绑定孩子', NOW()),
('MN052', 'B_PARENT_BIND', '绑定孩子', 'MN051', 1, NULL, 0, 'F', 'show', 'active', 'parent:bind', NULL, NULL, NOW()),
('MN053', 'B_PARENT_UNBIND', '解绑孩子', 'MN051', 2, NULL, 0, 'F', 'show', 'active', 'parent:unbind', NULL, NULL, NOW()),
('MN054', 'B_PARENT_VIEW', '查看错题', 'MN051', 3, NULL, 0, 'F', 'show', 'active', 'parent:view', NULL, NULL, NOW());

-- =========================================
-- 角色数据 (Role)
-- =========================================

INSERT INTO `sys_role_tbl` (`col_id`, `col_name`, `col_display`, `col_status`, `col_is_system`, `col_created_date`) VALUES
('MN_ROLE_STUDENT', 'MN_STUDENT_ROLE', '学生', 'active', b'1', NOW()),
('MN_ROLE_TEACHER', 'MN_TEACHER_ROLE', '教师', 'active', b'1', NOW()),
('MN_ROLE_PARENT', 'MN_PARENT_ROLE', '家长', 'active', b'1', NOW()),
('MN_ROLE_ADMIN', 'MN_ADMIN_ROLE', '管理员', 'active', b'1', NOW());

-- =========================================
-- 角色菜单权限分配 (Role-Menu)
-- =========================================

-- 学生角色权限
INSERT INTO `sys_role_menu_assign_tbl` (`col_id`, `col_role_name`, `col_menu_name`, `col_created_date`) VALUES
('RM001', 'MN_STUDENT_ROLE', 'C_MISTAKE_NOTEBOOK', NOW()),
('RM002', 'MN_STUDENT_ROLE', 'M_MISTAKE', NOW()),
('RM003', 'MN_STUDENT_ROLE', 'B_MISTAKE_CREATE', NOW()),
('RM004', 'MN_STUDENT_ROLE', 'B_MISTAKE_EDIT', NOW()),
('RM005', 'MN_STUDENT_ROLE', 'B_MISTAKE_DELETE', NOW()),
('RM006', 'MN_STUDENT_ROLE', 'B_MISTAKE_AI', NOW()),
('RM007', 'MN_STUDENT_ROLE', 'M_REVIEW', NOW()),
('RM008', 'MN_STUDENT_ROLE', 'B_REVIEW_RECORD', NOW()),
('RM009', 'MN_STUDENT_ROLE', 'M_STATS', NOW());

-- 教师角色权限 (拥有学生权限 + 班级管理 + 科目管理)
INSERT INTO `sys_role_menu_assign_tbl` (`col_id`, `col_role_name`, `col_menu_name`, `col_created_date`) VALUES
('RM020', 'MN_TEACHER_ROLE', 'C_MISTAKE_NOTEBOOK', NOW()),
('RM021', 'MN_TEACHER_ROLE', 'M_MISTAKE', NOW()),
('RM022', 'MN_TEACHER_ROLE', 'M_REVIEW', NOW()),
('RM023', 'MN_TEACHER_ROLE', 'M_STATS', NOW()),
('RM024', 'MN_TEACHER_ROLE', 'C_SUBJECT_MGR', NOW()),
('RM025', 'MN_TEACHER_ROLE', 'M_SUBJECT', NOW()),
('RM026', 'MN_TEACHER_ROLE', 'B_SUBJECT_CREATE', NOW()),
('RM027', 'MN_TEACHER_ROLE', 'B_SUBJECT_EDIT', NOW()),
('RM028', 'MN_TEACHER_ROLE', 'M_CHAPTER', NOW()),
('RM029', 'MN_TEACHER_ROLE', 'B_CHAPTER_CREATE', NOW()),
('RM030', 'MN_TEACHER_ROLE', 'B_CHAPTER_EDIT', NOW()),
('RM031', 'MN_TEACHER_ROLE', 'C_CLAZZ_MGR', NOW()),
('RM032', 'MN_TEACHER_ROLE', 'M_CLAZZ', NOW()),
('RM033', 'MN_TEACHER_ROLE', 'B_CLAZZ_STUDENT_ADD', NOW()),
('RM034', 'MN_TEACHER_ROLE', 'B_CLAZZ_STUDENT_REMOVE', NOW());

-- 家长角色权限
INSERT INTO `sys_role_menu_assign_tbl` (`col_id`, `col_role_name`, `col_menu_name`, `col_created_date`) VALUES
('RM040', 'MN_PARENT_ROLE', 'C_PARENT', NOW()),
('RM041', 'MN_PARENT_ROLE', 'M_PARENT_CHILDREN', NOW()),
('RM042', 'MN_PARENT_ROLE', 'B_PARENT_BIND', NOW()),
('RM043', 'MN_PARENT_ROLE', 'B_PARENT_UNBIND', NOW()),
('RM044', 'MN_PARENT_ROLE', 'B_PARENT_VIEW', NOW());

-- 管理员角色权限 (所有权限)
INSERT INTO `sys_role_menu_assign_tbl` (`col_id`, `col_role_name`, `col_menu_name`, `col_created_date`) VALUES
('RM050', 'MN_ADMIN_ROLE', 'C_MISTAKE_NOTEBOOK', NOW()),
('RM051', 'MN_ADMIN_ROLE', 'M_MISTAKE', NOW()),
('RM052', 'MN_ADMIN_ROLE', 'B_MISTAKE_CREATE', NOW()),
('RM053', 'MN_ADMIN_ROLE', 'B_MISTAKE_EDIT', NOW()),
('RM054', 'MN_ADMIN_ROLE', 'B_MISTAKE_DELETE', NOW()),
('RM055', 'MN_ADMIN_ROLE', 'B_MISTAKE_AI', NOW()),
('RM056', 'MN_ADMIN_ROLE', 'M_REVIEW', NOW()),
('RM057', 'MN_ADMIN_ROLE', 'B_REVIEW_RECORD', NOW()),
('RM058', 'MN_ADMIN_ROLE', 'M_STATS', NOW()),
('RM059', 'MN_ADMIN_ROLE', 'C_SUBJECT_MGR', NOW()),
('RM060', 'MN_ADMIN_ROLE', 'M_SUBJECT', NOW()),
('RM061', 'MN_ADMIN_ROLE', 'B_SUBJECT_CREATE', NOW()),
('RM062', 'MN_ADMIN_ROLE', 'B_SUBJECT_EDIT', NOW()),
('RM063', 'MN_ADMIN_ROLE', 'B_SUBJECT_DELETE', NOW()),
('RM064', 'MN_ADMIN_ROLE', 'M_CHAPTER', NOW()),
('RM065', 'MN_ADMIN_ROLE', 'B_CHAPTER_CREATE', NOW()),
('RM066', 'MN_ADMIN_ROLE', 'B_CHAPTER_EDIT', NOW()),
('RM067', 'MN_ADMIN_ROLE', 'B_CHAPTER_DELETE', NOW()),
('RM068', 'MN_ADMIN_ROLE', 'C_CLAZZ_MGR', NOW()),
('RM069', 'MN_ADMIN_ROLE', 'M_CLAZZ', NOW()),
('RM070', 'MN_ADMIN_ROLE', 'B_CLAZZ_CREATE', NOW()),
('RM071', 'MN_ADMIN_ROLE', 'B_CLAZZ_EDIT', NOW()),
('RM072', 'MN_ADMIN_ROLE', 'B_CLAZZ_DELETE', NOW()),
('RM073', 'MN_ADMIN_ROLE', 'B_CLAZZ_STUDENT_ADD', NOW()),
('RM074', 'MN_ADMIN_ROLE', 'B_CLAZZ_STUDENT_REMOVE', NOW()),
('RM075', 'MN_ADMIN_ROLE', 'C_PARENT', NOW()),
('RM076', 'MN_ADMIN_ROLE', 'M_PARENT_CHILDREN', NOW()),
('RM077', 'MN_ADMIN_ROLE', 'B_PARENT_BIND', NOW()),
('RM078', 'MN_ADMIN_ROLE', 'B_PARENT_UNBIND', NOW()),
('RM079', 'MN_ADMIN_ROLE', 'B_PARENT_VIEW', NOW());

-- =========================================
-- 苏州初二科目和章节数据 (江苏教育版)
-- =========================================

-- 科目数据已在schema.sql中初始化，这里添加章节和知识点

-- 语文 (初二上册 苏教版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_YW_01', '1', NULL, '第一单元 诗词诵读', 1, NOW()),
('CH_YW_02', '1', NULL, '第二单元 红色经典', 2, NOW()),
('CH_YW_03', '1', NULL, '第三单元 生命之美', 3, NOW()),
('CH_YW_04', '1', NULL, '第四单元 散文天地', 4, NOW()),
('CH_YW_05', '1', NULL, '第五单元 古文阅读', 5, NOW()),
('CH_YW_06', '1', NULL, '第六单元 名著导读', 6, NOW());

-- 数学 (初二上册 苏科版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_SX_01', '2', NULL, '第1章 全等三角形', 1, NOW()),
('CH_SX_02', '2', NULL, '第2章 轴对称图形', 2, NOW()),
('CH_SX_03', '2', NULL, '第3章 勾股定理', 3, NOW()),
('CH_SX_04', '2', NULL, '第4章 实数', 4, NOW()),
('CH_SX_05', '2', NULL, '第5章 平面直角坐标系', 5, NOW()),
('CH_SX_06', '2', NULL, '第6章 一次函数', 6, NOW());

-- 数学知识点
INSERT INTO `mn_knowledge_point_tbl` (`col_id`, `col_chapter_id`, `col_name`, `col_description`, `col_created_date`) VALUES
('KP_SX_0101', 'CH_SX_01', '全等三角形的定义', '两个能够完全重合的三角形叫做全等三角形', NOW()),
('KP_SX_0102', 'CH_SX_01', '全等三角形的判定(SSS)', '三边对应相等的两个三角形全等', NOW()),
('KP_SX_0103', 'CH_SX_01', '全等三角形的判定(SAS)', '两边和它们的夹角对应相等的两个三角形全等', NOW()),
('KP_SX_0104', 'CH_SX_01', '全等三角形的判定(ASA)', '两角和它们的夹边对应相等的两个三角形全等', NOW()),
('KP_SX_0105', 'CH_SX_01', '全等三角形的判定(AAS)', '两角和其中一角的对边对应相等的两个三角形全等', NOW()),
('KP_SX_0106', 'CH_SX_01', '全等三角形的判定(HL)', '斜边和一条直角边对应相等的两个直角三角形全等', NOW()),
('KP_SX_0301', 'CH_SX_03', '勾股定理', '直角三角形两直角边的平方和等于斜边的平方', NOW()),
('KP_SX_0302', 'CH_SX_03', '勾股定理的逆定理', '如果三角形三边满足a²+b²=c²，则这个三角形是直角三角形', NOW()),
('KP_SX_0401', 'CH_SX_04', '平方根', '如果x²=a，则x叫做a的平方根', NOW()),
('KP_SX_0402', 'CH_SX_04', '算术平方根', '正数a的正的平方根叫做a的算术平方根', NOW()),
('KP_SX_0403', 'CH_SX_04', '立方根', '如果x³=a，则x叫做a的立方根', NOW()),
('KP_SX_0601', 'CH_SX_06', '一次函数的定义', '形如y=kx+b(k≠0)的函数叫做一次函数', NOW()),
('KP_SX_0602', 'CH_SX_06', '一次函数的图像', '一次函数的图像是一条直线', NOW()),
('KP_SX_0603', 'CH_SX_06', '一次函数的性质', '当k>0时，y随x增大而增大；当k<0时，y随x增大而减小', NOW());

-- 英语 (初二上册 译林版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_YY_01', '3', NULL, 'Unit 1 Friends', 1, NOW()),
('CH_YY_02', '3', NULL, 'Unit 2 School life', 2, NOW()),
('CH_YY_03', '3', NULL, 'Unit 3 A day out', 3, NOW()),
('CH_YY_04', '3', NULL, 'Unit 4 Do it yourself', 4, NOW()),
('CH_YY_05', '3', NULL, 'Unit 5 Wild animals', 5, NOW()),
('CH_YY_06', '3', NULL, 'Unit 6 Bird watching', 6, NOW()),
('CH_YY_07', '3', NULL, 'Unit 7 Seasons', 7, NOW()),
('CH_YY_08', '3', NULL, 'Unit 8 Natural disasters', 8, NOW());

-- 英语知识点
INSERT INTO `mn_knowledge_point_tbl` (`col_id`, `col_chapter_id`, `col_name`, `col_description`, `col_created_date`) VALUES
('KP_YY_0101', 'CH_YY_01', '形容词比较级', '形容词比较级的构成和用法', NOW()),
('KP_YY_0102', 'CH_YY_01', '形容词最高级', '形容词最高级的构成和用法', NOW()),
('KP_YY_0201', 'CH_YY_02', '现在完成时', 'have/has + 过去分词', NOW()),
('KP_YY_0301', 'CH_YY_03', '动词不定式', 'to + 动词原形作宾语', NOW()),
('KP_YY_0501', 'CH_YY_05', '条件状语从句', 'if引导的条件状语从句', NOW()),
('KP_YY_0801', 'CH_YY_08', '过去进行时', 'was/were + 现在分词', NOW());

-- 物理 (初二上册 苏科版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_WL_01', '4', NULL, '第一章 声现象', 1, NOW()),
('CH_WL_02', '4', NULL, '第二章 物态变化', 2, NOW()),
('CH_WL_03', '4', NULL, '第三章 光现象', 3, NOW()),
('CH_WL_04', '4', NULL, '第四章 光的折射 透镜', 4, NOW()),
('CH_WL_05', '4', NULL, '第五章 物体的运动', 5, NOW());

-- 物理知识点
INSERT INTO `mn_knowledge_point_tbl` (`col_id`, `col_chapter_id`, `col_name`, `col_description`, `col_created_date`) VALUES
('KP_WL_0101', 'CH_WL_01', '声音的产生', '声音是由物体振动产生的', NOW()),
('KP_WL_0102', 'CH_WL_01', '声音的传播', '声音需要介质传播，真空不能传声', NOW()),
('KP_WL_0103', 'CH_WL_01', '声音的三要素', '音调、响度、音色', NOW()),
('KP_WL_0201', 'CH_WL_02', '温度计的使用', '温度计的原理和使用方法', NOW()),
('KP_WL_0202', 'CH_WL_02', '熔化和凝固', '固态和液态之间的转化', NOW()),
('KP_WL_0203', 'CH_WL_02', '汽化和液化', '液态和气态之间的转化', NOW()),
('KP_WL_0301', 'CH_WL_03', '光的直线传播', '光在同种均匀介质中沿直线传播', NOW()),
('KP_WL_0302', 'CH_WL_03', '光的反射定律', '反射角等于入射角', NOW()),
('KP_WL_0303', 'CH_WL_03', '平面镜成像', '平面镜成的像是虚像，与物等大', NOW()),
('KP_WL_0401', 'CH_WL_04', '光的折射', '光从一种介质斜射入另一种介质时，传播方向发生改变', NOW()),
('KP_WL_0402', 'CH_WL_04', '凸透镜成像规律', 'u>2f成缩小实像，f<u<2f成放大实像，u<f成放大虚像', NOW()),
('KP_WL_0501', 'CH_WL_05', '速度公式', 'v=s/t', NOW());

-- 生物 (初二上册 苏科版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_SW_01', '6', NULL, '第一单元 动物的运动和行为', 1, NOW()),
('CH_SW_02', '6', NULL, '第二单元 生物的生殖发育', 2, NOW()),
('CH_SW_03', '6', NULL, '第三单元 生物的遗传和变异', 3, NOW());

-- 历史 (初二上册 部编版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_LS_01', '7', NULL, '第一单元 中国开始沦为半殖民地半封建社会', 1, NOW()),
('CH_LS_02', '7', NULL, '第二单元 近代化的早期探索与民族危机的加剧', 2, NOW()),
('CH_LS_03', '7', NULL, '第三单元 资产阶级民主革命与中华民国的建立', 3, NOW()),
('CH_LS_04', '7', NULL, '第四单元 新时代的曙光', 4, NOW()),
('CH_LS_05', '7', NULL, '第五单元 从国共合作到国共对峙', 5, NOW()),
('CH_LS_06', '7', NULL, '第六单元 中华民族的抗日战争', 6, NOW()),
('CH_LS_07', '7', NULL, '第七单元 解放战争', 7, NOW()),
('CH_LS_08', '7', NULL, '第八单元 近代经济、社会生活与教育文化事业的发展', 8, NOW());

-- 地理 (初二上册 人教版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_DL_01', '8', NULL, '第一章 从世界看中国', 1, NOW()),
('CH_DL_02', '8', NULL, '第二章 中国的自然环境', 2, NOW()),
('CH_DL_03', '8', NULL, '第三章 中国的自然资源', 3, NOW()),
('CH_DL_04', '8', NULL, '第四章 中国的经济发展', 4, NOW());

-- 道德与法治 (初二上册 部编版)
INSERT INTO `mn_chapter_tbl` (`col_id`, `col_subject_id`, `col_parent_id`, `col_name`, `col_sort_order`, `col_created_date`) VALUES
('CH_ZZ_01', '9', NULL, '第一单元 走进社会生活', 1, NOW()),
('CH_ZZ_02', '9', NULL, '第二单元 遵守社会规则', 2, NOW()),
('CH_ZZ_03', '9', NULL, '第三单元 勇担社会责任', 3, NOW()),
('CH_ZZ_04', '9', NULL, '第四单元 维护国家利益', 4, NOW());

-- =========================================
-- 示例班级数据
-- =========================================

INSERT INTO `mn_clazz_tbl` (`col_id`, `col_name`, `col_grade`, `col_teacher_id`, `col_description`, `col_created_date`) VALUES
('CLAZZ_0801', '八年级(1)班', '八年级', 'TEACHER_001', '苏州市XX中学八年级(1)班', NOW()),
('CLAZZ_0802', '八年级(2)班', '八年级', 'TEACHER_001', '苏州市XX中学八年级(2)班', NOW()),
('CLAZZ_0803', '八年级(3)班', '八年级', 'TEACHER_002', '苏州市XX中学八年级(3)班', NOW()),
('CLAZZ_0804', '八年级(4)班', '八年级', 'TEACHER_002', '苏州市XX中学八年级(4)班', NOW());
