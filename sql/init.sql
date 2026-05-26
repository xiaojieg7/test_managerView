-- ============================================
-- 小区物业管理系统 数据库脚本
-- 数据库名: property_management
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS property_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE property_management;

-- ============================================
-- 1. 用户表 (管理员/业主/维修人员)
-- ============================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    role VARCHAR(20) NOT NULL COMMENT '角色: ADMIN/REPAIRER/OWNER',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ============================================
-- 2. 业主信息表
-- ============================================
DROP TABLE IF EXISTS owner;
CREATE TABLE owner (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '业主ID',
    user_id BIGINT NOT NULL COMMENT '关联用户ID',
    id_card VARCHAR(18) COMMENT '身份证号',
    building VARCHAR(50) COMMENT '楼栋',
    unit VARCHAR(20) COMMENT '单元',
    room_number VARCHAR(50) COMMENT '房号',
    check_in_date DATE COMMENT '入住日期',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-未入住 1-已入住',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_building (building),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业主信息表';

-- ============================================
-- 3. 报修表
-- ============================================
DROP TABLE IF EXISTS repair;
CREATE TABLE repair (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报修ID',
    owner_id BIGINT NOT NULL COMMENT '报修人(业主ID)',
    repairer_id BIGINT COMMENT '维修人员ID',
    title VARCHAR(200) NOT NULL COMMENT '报修标题',
    description TEXT COMMENT '报修描述',
    category VARCHAR(50) COMMENT '报修类别: 水电/门窗/家电/其他',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态: PENDING-待分配 PROCESSING-处理中 COMPLETED-已完成 CANCELLED-已取消',
    priority VARCHAR(20) DEFAULT 'NORMAL' COMMENT '优先级: LOW-低 NORMAL-普通 HIGH-高 URGENT-紧急',
    repair_remark TEXT COMMENT '维修备注',
    complete_time DATETIME COMMENT '完成时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_owner_id (owner_id),
    INDEX idx_repairer_id (repairer_id),
    INDEX idx_status (status),
    FOREIGN KEY (owner_id) REFERENCES owner(id) ON DELETE CASCADE,
    FOREIGN KEY (repairer_id) REFERENCES sys_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报修表';

-- ============================================
-- 4. 缴费记录表
-- ============================================
DROP TABLE IF EXISTS payment;
CREATE TABLE payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '缴费ID',
    owner_id BIGINT NOT NULL COMMENT '业主ID',
    payment_type VARCHAR(50) NOT NULL COMMENT '缴费类型: PROPERTY_FEE-物业费 WATER-水费 ELECTRIC-电费 GAS-燃气费',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    period VARCHAR(20) COMMENT '缴费周期: 2024-01',
    status VARCHAR(20) DEFAULT 'UNPAID' COMMENT '状态: UNPAID-未支付 PAID-已支付 OVERDUE-逾期',
    due_date DATE COMMENT '到期日期',
    paid_time DATETIME COMMENT '支付时间',
    payment_method VARCHAR(50) COMMENT '支付方式',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_owner_id (owner_id),
    INDEX idx_status (status),
    INDEX idx_period (period),
    FOREIGN KEY (owner_id) REFERENCES owner(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录表';

-- ============================================
-- 5. 公告表
-- ============================================
DROP TABLE IF EXISTS notice;
CREATE TABLE notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    type VARCHAR(50) DEFAULT 'NOTICE' COMMENT '类型: NOTICE-通知 ACTIVITY-活动 SYSTEM-系统公告',
    priority INT DEFAULT 0 COMMENT '优先级(数字越大越靠前)',
    publisher_id BIGINT COMMENT '发布人ID',
    publish_time DATETIME COMMENT '发布时间',
    status VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT '状态: DRAFT-草稿 PUBLISHED-已发布 ARCHIVED-已归档',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ============================================
-- 6. 楼栋信息表
-- ============================================
DROP TABLE IF EXISTS building;
CREATE TABLE building (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '楼栋ID',
    building_name VARCHAR(50) NOT NULL COMMENT '楼栋名称',
    building_code VARCHAR(20) UNIQUE COMMENT '楼栋编码',
    unit_count INT DEFAULT 1 COMMENT '单元数',
    floor_count INT DEFAULT 1 COMMENT '楼层数',
    description VARCHAR(500) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-停用 1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_building_code (building_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼栋信息表';

-- ============================================
-- 初始数据
-- ============================================

-- 插入管理员账号 (密码: 123456, BCrypt加密后的密码)
INSERT INTO sys_user (username, password, real_name, phone, email, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2', '系统管理员', '13800000000', 'admin@property.com', 'ADMIN', 1);

-- 插入维修人员账号
INSERT INTO sys_user (username, password, real_name, phone, email, role, status) VALUES
('repairer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2', '维修师傅', '13800000001', 'repairer@property.com', 'REPAIRER', 1),
('repairer2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2', '维修师傅2', '13800000002', 'repairer2@property.com', 'REPAIRER', 1);

-- 插入业主账号 (关联业主信息)
INSERT INTO sys_user (username, password, real_name, phone, email, role, status) VALUES
('owner1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2', '张三', '13900000001', 'owner1@property.com', 'OWNER', 1),
('owner2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2', '李四', '13900000002', 'owner2@property.com', 'OWNER', 1),
('owner3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E2', '王五', '13900000003', 'owner3@property.com', 'OWNER', 1);

-- 插入业主详细信息
INSERT INTO owner (user_id, id_card, building, unit, room_number, check_in_date, status) VALUES
(4, '110101199001011234', 'A栋', '1单元', '101', '2020-01-01', 1),
(5, '110101199002021234', 'A栋', '1单元', '102', '2020-01-01', 1),
(6, '110101199003031234', 'B栋', '2单元', '201', '2021-06-01', 1);

-- 插入楼栋信息
INSERT INTO building (building_name, building_code, unit_count, floor_count, description, status) VALUES
('A栋', 'A', 3, 6, 'A栋住宅楼，共3个单元，6层', 1),
('B栋', 'B', 4, 8, 'B栋住宅楼，共4个单元，8层', 1),
('C栋', 'C', 2, 5, 'C栋住宅楼，共2个单元，5层', 1);

-- 插入公告
INSERT INTO notice (title, content, type, priority, publisher_id, publish_time, status) VALUES
('关于缴纳2024年物业费的通知', '各位业主：2024年度物业费收缴工作已经开始，请于3月31日前完成缴纳。逾期未缴将产生滞纳金。', 'NOTICE', 10, 1, NOW(), 'PUBLISHED'),
('小区端午节活动安排', '端午节期间，小区将举办包粽子比赛活动，欢迎各位业主积极参与。活动时间：6月10日14:00，地点：小区广场。', 'ACTIVITY', 8, 1, NOW(), 'PUBLISHED'),
('本周设施维护通知', '本周将对小区电梯进行例行维护，届时可能会有短暂停运，请各位业主谅解。具体时间：周三9:00-12:00。', 'NOTICE', 5, 1, NOW(), 'PUBLISHED'),
('系统升级公告', '物业管理系统将于本周日凌晨2:00-6:00进行升级维护，届时系统将暂停服务。', 'SYSTEM', 9, 1, NOW(), 'PUBLISHED');

-- 插入报修记录
INSERT INTO repair (owner_id, repairer_id, title, description, category, status, priority, create_time) VALUES
(1, 2, '水龙头漏水', '厨房水龙头滴水，已影响正常使用', '水电', 'PROCESSING', 'NORMAL', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, NULL, '门锁损坏', '入户门锁芯损坏，需要更换', '门窗', 'PENDING', 'HIGH', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 3, '空调不制冷', '客厅空调不制冷，怀疑缺氟', '家电', 'PENDING', 'URGENT', NOW()),
(3, 2, '窗户关不严', '卧室窗户关不严，有漏风', '门窗', 'COMPLETED', 'LOW', DATE_SUB(NOW(), INTERVAL 5 DAY));

-- 更新已完成报修的完成时间
UPDATE repair SET complete_time = DATE_SUB(NOW(), INTERVAL 1 DAY), repair_remark = '已更换新锁芯，问题解决' WHERE id = 4;

-- 插入缴费记录
INSERT INTO payment (owner_id, payment_type, amount, period, status, due_date, paid_time, payment_method) VALUES
(1, 'PROPERTY_FEE', 2400.00, '2024-01', 'PAID', '2024-01-31', '2024-01-15', 'ONLINE'),
(1, 'WATER', 120.50, '2024-01', 'PAID', '2024-02-15', '2024-02-10', 'ONLINE'),
(1, 'ELECTRIC', 350.00, '2024-01', 'PAID', '2024-02-15', '2024-02-10', 'ONLINE'),
(1, 'PROPERTY_FEE', 2400.00, '2024-02', 'PAID', '2024-02-28', '2024-02-20', 'WECHAT'),
(1, 'PROPERTY_FEE', 2400.00, '2024-03', 'UNPAID', '2024-03-31', NULL, NULL),
(1, 'WATER', 98.30, '2024-02', 'UNPAID', '2024-03-15', NULL, NULL),
(2, 'PROPERTY_FEE', 2400.00, '2024-01', 'PAID', '2024-01-31', '2024-01-20', 'ALIPAY'),
(2, 'PROPERTY_FEE', 2400.00, '2024-02', 'PAID', '2024-02-28', '2024-02-25', 'ALIPAY'),
(2, 'PROPERTY_FEE', 2400.00, '2024-03', 'OVERDUE', '2024-03-31', NULL, NULL),
(3, 'PROPERTY_FEE', 1800.00, '2024-01', 'PAID', '2024-01-31', '2024-01-10', 'ONLINE'),
(3, 'PROPERTY_FEE', 1800.00, '2024-02', 'PAID', '2024-02-28', '2024-02-15', 'ONLINE'),
(3, 'PROPERTY_FEE', 1800.00, '2024-03', 'UNPAID', '2024-03-31', NULL, NULL);

-- ============================================
-- 验证查询
-- ============================================
SELECT '=== 用户表数据 ===' AS '';
SELECT id, username, real_name, role, status FROM sys_user;

SELECT '=== 业主表数据 ===' AS '';
SELECT o.id, u.username, u.real_name, o.building, o.unit, o.room_number 
FROM owner o 
JOIN sys_user u ON o.user_id = u.id;

SELECT '=== 公告表数据 ===' AS '';
SELECT id, title, type, status FROM notice;

SELECT '=== 报修表数据 ===' AS '';
SELECT r.id, r.title, r.status, r.category, 
       o.building, o.unit, o.room_number
FROM repair r
JOIN owner o ON r.owner_id = o.id;

SELECT '=== 缴费统计 ===' AS '';
SELECT o.building, o.unit, o.room_number,
       SUM(CASE WHEN p.status = 'UNPAID' OR p.status = 'OVERDUE' THEN p.amount ELSE 0 END) as unpaid_amount,
       COUNT(CASE WHEN p.status = 'UNPAID' OR p.status = 'OVERDUE' THEN 1 END) as unpaid_count
FROM owner o
LEFT JOIN payment p ON o.id = p.owner_id
GROUP BY o.id;
