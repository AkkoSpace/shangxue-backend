# 建表脚本

-- 创建库
create database if not exists shangxue;

-- 切换库
use shangxue;

-- 用户表
CREATE TABLE
    IF
    NOT EXISTS user (
                        id BIGINT auto_increment COMMENT 'id' PRIMARY KEY,
                        userAccount VARCHAR ( 256 ) NOT NULL COMMENT '账号',
                        userPassword VARCHAR ( 512 ) NOT NULL COMMENT '密码',
                        unionId VARCHAR ( 256 ) NULL COMMENT '微信开放平台id',
                        mpOpenId VARCHAR ( 256 ) NULL COMMENT '公众号openId',
                        userName VARCHAR ( 256 ) NULL COMMENT '用户昵称',
                        userAvatar VARCHAR ( 1024 ) NULL COMMENT '用户头像',
                        userProfile VARCHAR ( 512 ) NULL COMMENT '用户简介',
                        userRole VARCHAR ( 256 ) DEFAULT 'user' NOT NULL COMMENT '用户角色：user/admin/ban',
                        createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                        updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除',
                        INDEX idx_unionId ( unionId )
) COMMENT '用户' COLLATE = utf8mb4_unicode_ci;

-- 交易表
CREATE TABLE transaction (
                             id INT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                             transactionDate DATE NOT NULL COMMENT '交易日期',
                             transactionId VARCHAR ( 255 ) NOT NULL COMMENT '交易单号',
                             amount DECIMAL ( 10, 2 ) NOT NULL COMMENT '交易金额',
                             description VARCHAR ( 255 ) DEFAULT NULL COMMENT '描述',
                             userId BIGINT NOT NULL COMMENT '创建用户 id',
                             status TINYINT DEFAULT 0 NOT NULL COMMENT '交易状态：0-未付款，1-已付款，2-已取消，3-已退款',
                             createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                             updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除'
) COMMENT '交易' COLLATE = utf8mb4_unicode_ci;

-- 产品表
CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                          productName VARCHAR ( 255 ) NOT NULL COMMENT '产品名称',
                          productPrice DECIMAL ( 10, 2 ) NOT NULL COMMENT '产品价格',
                          productSpec VARCHAR ( 255 ) NOT NULL COMMENT '产品规格',
                          productQuantity INT NOT NULL COMMENT '产品数量',
                          userId BIGINT NOT NULL COMMENT '创建用户 id',
                          createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                          updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除'
) COMMENT '产品' COLLATE = utf8mb4_unicode_ci;

-- 订单表
CREATE TABLE sales_order (
                             id INT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                             orderNumber VARCHAR ( 255 ) NOT NULL COMMENT '订单号',
                             productionDate DATE NOT NULL COMMENT '生产日期',
                             productId INT NOT NULL COMMENT '产品ID',
                             quantity INT NOT NULL COMMENT '销售数量',
                             totalPrice DECIMAL ( 10, 2 ) NOT NULL COMMENT '销售总价',
                             FOREIGN KEY ( productId ) REFERENCES products ( id ),
                             userId BIGINT NOT NULL COMMENT '创建用户 id',
                             createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                             updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除'
) COMMENT '订单' COLLATE = utf8mb4_unicode_ci;

-- 交易公司表
CREATE TABLE trading_companies (
                                   id INT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键',
                                   companyName VARCHAR ( 255 ) NOT NULL COMMENT '公司名称',
                                   companyAddress VARCHAR ( 255 ) NOT NULL COMMENT '公司地址',
                                   contactPhone VARCHAR ( 20 ) NOT NULL COMMENT '联系电话',
                                   contactEmail VARCHAR ( 255 ) NOT NULL COMMENT '联系邮箱',
                                   userId BIGINT NOT NULL COMMENT '创建用户 id',
                                   createTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
                                   updateTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   isDelete TINYINT DEFAULT 0 NOT NULL COMMENT '是否删除'
) COMMENT '交易公司' COLLATE = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';
