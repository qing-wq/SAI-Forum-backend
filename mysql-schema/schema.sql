-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sai-forum
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

create database if not exists `sai-forum`;
use `sai-forum`;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article`
(
    `id`             int unsigned                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`        int unsigned                            NOT NULL DEFAULT '0' COMMENT '用户ID',
    `article_type`   tinyint                                 NOT NULL DEFAULT '1' COMMENT '文章类型：1-博文，2-问答',
    `title`          varchar(120) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
    `short_title`    varchar(120) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '短标题',
    `picture`        varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章头图',
    `summary`        varchar(300) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章摘要',
    `category_id`    int unsigned                            NOT NULL DEFAULT '0' COMMENT '类目ID',
    `source`         tinyint                                 NOT NULL DEFAULT '1' COMMENT '来源：1-转载，2-原创，3-翻译',
    `source_url`     varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '原文链接',
    `official_stat`  int unsigned                            NOT NULL DEFAULT '0' COMMENT '官方状态：0-非官方，1-官方',
    `topping_stat`   int unsigned                            NOT NULL DEFAULT '0' COMMENT '置顶状态：0-不置顶，1-置顶',
    `recommend_stat` int unsigned                            NOT NULL DEFAULT '0' COMMENT '加精状态：0-不加精，1-加精',
    `status`         tinyint                                 NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布，2-待审核，3-暂存',
    `deleted`        tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`    timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_title` (`title`),
    KEY `idx_short_title` (`short_title`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文章表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_detail`
--

DROP TABLE IF EXISTS `article_detail`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_detail`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id`  int unsigned NOT NULL DEFAULT '0' COMMENT '文章ID',
    `version`     int unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
    `content`     longtext COLLATE utf8mb4_general_ci COMMENT '文章内容',
    `copy`        longtext COLLATE utf8mb4_general_ci COMMENT '文章副本',
    `deleted`     tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_article_version` (`article_id`, `version`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文章详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `article_tag`
--

DROP TABLE IF EXISTS `article_tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article_tag`
(
    `id`           int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id`   int unsigned NOT NULL DEFAULT '0' COMMENT '文章ID',
    `tag_id`       int          NOT NULL DEFAULT '0' COMMENT '标签',
    `article_type` tinyint      NOT NULL DEFAULT '0' COMMENT '0-草稿，1-文章',
    `deleted`      tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文章标签映射';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category`
(
    `id`            int unsigned                           NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类目名称',
    `status`        tinyint                                NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布',
    `rank`          tinyint                                NOT NULL DEFAULT '0' COMMENT '排序',
    `deleted`       tinyint                                NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`   timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='类目管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category`
    DISABLE KEYS */;
INSERT INTO `category` (`id`, `category_name`, `status`, `rank`, `deleted`, `create_time`, `update_time`)
VALUES (1, '全部', 1, 0, 0, '2023-03-23 03:03:54', '2023-12-30 07:19:08'),
       (2, '人工智能', 1, 1, 0, '2023-03-23 03:03:54', '2024-11-09 11:28:32'),
       (3, '前端', 1, 2, 0, '2023-03-23 03:03:54', '2024-11-09 11:34:34'),
       (4, 'Android', 1, 4, 0, '2023-03-23 03:03:54', '2024-11-09 11:35:57'),
       (5, '小车', 1, 5, 0, '2023-03-23 03:03:54', '2024-11-09 11:35:57'),
       (6, '定位', 1, 6, 0, '2023-03-23 03:03:54', '2024-11-09 11:35:57'),
       (7, '无人机', 1, 7, 0, '2023-03-23 03:03:54', '2024-11-09 11:35:57'),
       (8, 'ros', 1, 8, 0, '2023-03-23 03:03:54', '2024-11-09 11:35:57'),
       (9, '后端', 1, 3, 0, '2024-11-09 11:34:34', '2024-11-09 11:35:57');
/*!40000 ALTER TABLE `category`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment`
(
    `id`                int unsigned                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id`        int unsigned                            NOT NULL DEFAULT '0' COMMENT '文章ID',
    `user_id`           int unsigned                            NOT NULL DEFAULT '0' COMMENT '用户ID',
    `content`           varchar(300) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评论内容',
    `top_comment_id`    int                                     NOT NULL DEFAULT '0' COMMENT '顶级评论ID',
    `parent_comment_id` int unsigned                            NOT NULL DEFAULT '0' COMMENT '父评论ID',
    `deleted`           tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`       timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config`
(
    `id`          int unsigned                             NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type`        tinyint                                  NOT NULL DEFAULT '0' COMMENT '配置类型：1-首页，2-侧边栏，3-广告位，4-公告',
    `name`        varchar(64) COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '名称',
    `banner_url`  varchar(256) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '图片链接',
    `jump_url`    varchar(256) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跳转链接',
    `content`     varchar(256) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '内容',
    `rank`        tinyint                                  NOT NULL DEFAULT '0' COMMENT '排序',
    `status`      tinyint                                  NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布',
    `tags`        varchar(64) COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '配置关联标签，英文逗号分隔 1 火 2 官方 3 推荐',
    `extra`       varchar(1024) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '{}' COMMENT '扩展信息',
    `deleted`     tinyint                                  NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dict_common`
--

DROP TABLE IF EXISTS `dict_common`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_common`
(
    `id`          int unsigned                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type_code`   varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型，sex, status 等',
    `dict_code`   varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型的值编码',
    `dict_desc`   varchar(200) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型的值描述',
    `sort_no`     int unsigned                            NOT NULL DEFAULT '0' COMMENT '排序编号',
    `remark`      varchar(500) COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '备注',
    `create_time` timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_code_dict_code` (`type_code`, `dict_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='通用数据字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `drafts`
--

DROP TABLE IF EXISTS `drafts`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `drafts`
(
    `id`          int unsigned                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     int unsigned                            NOT NULL DEFAULT '0' COMMENT '用户ID',
    `title`       varchar(120) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
    `short_title` varchar(120) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '短标题',
    `picture`     varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章头图',
    `summary`     varchar(300) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章摘要',
    `category_id` int unsigned                            NOT NULL DEFAULT '0' COMMENT '分类ID',
    `source`      tinyint                                 NOT NULL DEFAULT '1' COMMENT '来源：1-转载，2-原创，3-翻译',
    `source_url`  varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '原文链接',
    `article_id`  int unsigned                                NULL             COMMENT '关联的文章，0-没有',
    `content`     longtext COLLATE utf8mb4_general_ci COMMENT '内容',
    `deleted`     tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_article_id` (`article_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='草稿表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `global_conf`
--

DROP TABLE IF EXISTS `global_conf`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `global_conf`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `key`         varchar(128) NOT NULL DEFAULT '' COMMENT '配置key',
    `value`       varchar(512) NOT NULL DEFAULT '' COMMENT '配置value',
    `comment`     varchar(128) NOT NULL DEFAULT '' COMMENT '注释',
    `deleted`     tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除 0 未删除 1 已删除',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_key` (`key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='全局配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notify_msg`
--

DROP TABLE IF EXISTS `notify_msg`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notify_msg`
(
    `id`              int unsigned                             NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `related_id`      int unsigned                             NOT NULL DEFAULT '0' COMMENT '关联的主键',
    `notify_user_id`  int unsigned                             NOT NULL DEFAULT '0' COMMENT '通知的用户id',
    `operate_user_id` int unsigned                             NOT NULL DEFAULT '0' COMMENT '触发这个通知的用户id',
    `msg`             varchar(1024) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息内容',
    `type`            tinyint unsigned                         NOT NULL DEFAULT '0' COMMENT '类型: 0-默认，1-评论，2-回复 3-点赞 4-收藏 5-关注 6-系统',
    `state`           tinyint unsigned                         NOT NULL DEFAULT '0' COMMENT '阅读状态: 0-未读，1-已读',
    `create_time`     timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `key_notify_user_id_type_state` (`notify_user_id`, `type`, `state`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='消息通知列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `read_count`
--

DROP TABLE IF EXISTS `read_count`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `read_count`
(
    `id`            int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `document_id`   int unsigned NOT NULL DEFAULT '0' COMMENT '文档ID（文章/评论）',
    `document_type` tinyint      NOT NULL DEFAULT '1' COMMENT '文档类型：1-文章，2-评论',
    `cnt`           int unsigned NOT NULL DEFAULT '0' COMMENT '访问计数',
    `create_time`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_document_id_type` (`document_id`, `document_type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='计数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `request_count`
--

DROP TABLE IF EXISTS `request_count`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request_count`
(
    `id`          int unsigned                           NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `host`        varchar(32) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '机器IP',
    `cnt`         int unsigned                           NOT NULL DEFAULT '0' COMMENT '访问计数',
    `date`        date                                   NOT NULL COMMENT '当前日期',
    `create_time` timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_unique_id_date` (`date`, `host`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='请求计数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag`
(
    `id`          int unsigned                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tag_name`    varchar(120) COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
    `tag_type`    tinyint                                 NOT NULL DEFAULT '1' COMMENT '标签类型：1-系统标签，2-自定义标签',
    `category_id` int unsigned                            NOT NULL DEFAULT '0' COMMENT '类目ID',
    `status`      tinyint                                 NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布',
    `deleted`     tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='标签管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user`
(
    `id`               int unsigned                            NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `third_account_id` varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '第三方用户ID',
    `user_name`        varchar(64) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `password`         varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
    `login_type`       tinyint                                 NOT NULL DEFAULT '0' COMMENT '登录方式: 0-微信登录，1-账号密码登录',
    `deleted`          tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`      timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `key_third_account_id` (`third_account_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户登录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_foot`
--

DROP TABLE IF EXISTS `user_foot`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_foot`
(
    `id`               int unsigned     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`          int unsigned     NOT NULL DEFAULT '0' COMMENT '用户ID',
    `document_id`      int unsigned     NOT NULL DEFAULT '0' COMMENT '文档ID（文章/评论）',
    `document_type`    tinyint          NOT NULL DEFAULT '1' COMMENT '文档类型：1-文章，2-评论',
    `document_user_id` int unsigned     NOT NULL DEFAULT '0' COMMENT '发布该文档的用户ID',
    `collection_stat`  tinyint unsigned NOT NULL DEFAULT '0' COMMENT '收藏状态: 0-未收藏，1-已收藏，2-取消收藏',
    `read_stat`        tinyint unsigned NOT NULL DEFAULT '0' COMMENT '阅读状态: 0-未读，1-已读',
    `comment_stat`     tinyint unsigned NOT NULL DEFAULT '0' COMMENT '评论状态: 0-未评论，1-已评论，2-删除评论',
    `praise_stat`      tinyint unsigned NOT NULL DEFAULT '0' COMMENT '点赞状态: 0-未点赞，1-已点赞，2-取消点赞',
    `create_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_doucument` (`user_id`, `document_id`, `document_type`),
    KEY `idx_doucument_id` (`document_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户足迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_info`
(
    `id`          int unsigned                             NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     int unsigned                             NOT NULL DEFAULT '0' COMMENT '用户ID',
    `user_name`   varchar(50) COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '用户名',
    `photo`       varchar(128) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户图像',
    `student_id`  varchar(50) COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '职位',
    `college`     varchar(50) COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '学院',
    `email`       varchar(100) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '电子邮件',
    `profile`     varchar(225) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '个人简介',
    `user_role`   int                                      NOT NULL DEFAULT '0' COMMENT '0 普通用户 1 超管',
    `extend`      varchar(1024) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扩展字段',
    `ip`          longtext COLLATE utf8mb4_bin             NOT NULL COMMENT '用户的ip信息',
    `deleted`     tinyint                                  NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `major`       varchar(100) COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '专业',
    PRIMARY KEY (`id`),
    KEY `key_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户个人信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_relation`
--

DROP TABLE IF EXISTS `user_relation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_relation`
(
    `id`             int unsigned     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`        int unsigned     NOT NULL DEFAULT '0' COMMENT '用户ID',
    `follow_user_id` int unsigned     NOT NULL COMMENT '关注userId的用户id，即粉丝userId',
    `follow_state`   tinyint unsigned NOT NULL DEFAULT '0' COMMENT '阅读状态: 0-未关注，1-已关注，2-取消关注',
    `create_time`    timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_follow` (`user_id`, `follow_user_id`),
    KEY `key_follow_user_id` (`follow_user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户关系表';

-- Dump completed on 2023-09-28 13:13:36
