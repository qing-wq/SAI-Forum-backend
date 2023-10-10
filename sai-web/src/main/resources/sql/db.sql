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

create database `sai-forum`;
use `sai-forum`;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article`
(
    `id`            int unsigned                                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`       int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '用户ID',
    `article_type`  tinyint                                                       NOT NULL DEFAULT '1' COMMENT '文章类型：1-博文，2-问答',
    `title`         varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
    `short_title`   varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '短标题',
    `picture`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章头图',
    `summary`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章摘要',
    `category_id`   int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '类目ID',
    `source`        tinyint                                                       NOT NULL DEFAULT '1' COMMENT '来源：1-转载，2-原创，3-翻译',
    `source_url`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '原文链接',
    `official_stat` int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '官方状态：0-非官方，1-官方',
    `topping_stat`  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '置顶状态：0-不置顶，1-置顶',
    `cream_stat`    int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '加精状态：0-不加精，1-加精',
    `status`        tinyint                                                       NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布，2-待审核，3-暂存',
    `deleted`       tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_title` (`title`),
    KEY `idx_short_title` (`short_title`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 150
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
    `content`     longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文章内容',
    `copy`        longtext COLLATE utf8mb4_general_ci COMMENT '文章副本',
    `deleted`     tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_article_version` (`article_id`, `version`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 54
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
    `article_type` tinyint               default 0 not null comment '0-草稿，1-文章',
    `deleted`      tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文章标签映射';
/*!40101 SET character_set_client = @saved_cs_client */;

create index idx_tag_id
    on `sai-forum`.article_tag (tag_id);

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category`
(
    `id`            int unsigned                                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类目名称',
    `status`        tinyint                                                      NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布',
    `rank`          tinyint                                                      NOT NULL DEFAULT '0' COMMENT '排序',
    `deleted`       tinyint                                                      NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`   timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='类目管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment`
(
    `id`                int unsigned                                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id`        int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '文章ID',
    `user_id`           int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '用户ID',
    `content`           varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评论内容',
    `top_comment_id`    int                                                           NOT NULL DEFAULT '0' COMMENT '顶级评论ID',
    `parent_comment_id` int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '父评论ID',
    `deleted`           tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 61
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
    `id`          int unsigned                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type`        tinyint                                                        NOT NULL DEFAULT '0' COMMENT '配置类型：1-首页，2-侧边栏，3-广告位，4-公告',
    `name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '名称',
    `banner_url`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '图片链接',
    `jump_url`    varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '跳转链接',
    `content`     varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '内容',
    `rank`        tinyint                                                        NOT NULL DEFAULT '0' COMMENT '排序',
    `status`      tinyint                                                        NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布',
    `tags`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '配置关联标签，英文逗号分隔 1 火 2 官方 3 推荐',
    `extra`       varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '{}' COMMENT '扩展信息',
    `deleted`     tinyint                                                        NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `databasechangelog`
--

DROP TABLE IF EXISTS `databasechangelog`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `databasechangelog`
(
    `ID`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `AUTHOR`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `FILENAME`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    `DATEEXECUTED`  datetime                                                      NOT NULL,
    `ORDEREXECUTED` int                                                           NOT NULL,
    `EXECTYPE`      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL,
    `MD5SUM`        varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `DESCRIPTION`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `COMMENTS`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `TAG`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LIQUIBASE`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `CONTEXTS`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `LABELS`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `DEPLOYMENT_ID` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `databasechangeloglock`
--

DROP TABLE IF EXISTS `databasechangeloglock`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `databasechangeloglock`
(
    `ID`          int    NOT NULL,
    `LOCKED`      bit(1) NOT NULL,
    `LOCKGRANTED` datetime                                                      DEFAULT NULL,
    `LOCKEDBY`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dict_common`
--

DROP TABLE IF EXISTS `dict_common`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dict_common`
(
    `id`          int unsigned                                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type_code`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型，sex, status 等',
    `dict_code`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型的值编码',
    `dict_desc`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型的值描述',
    `sort_no`     int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '排序编号',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT '' COMMENT '备注',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_code_dict_code` (`type_code`, `dict_code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 28
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='通用数据字典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `draft`
--

DROP TABLE IF EXISTS `draft`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `draft`
(
    `id`          int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `author`      int          NOT NULL DEFAULT '0' COMMENT '作者id',
    `title`       varchar(100)          DEFAULT '' COMMENT '文章标题',
    `content`     longtext     NOT NULL COMMENT '文章内容',
    `deleted`     tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_author` (`author`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='草稿箱';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `notify_msg`
--

DROP TABLE IF EXISTS `notify_msg`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notify_msg`
(
    `id`              int unsigned                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `related_id`      int unsigned                                                   NOT NULL DEFAULT '0' COMMENT '关联的主键',
    `notify_user_id`  int unsigned                                                   NOT NULL DEFAULT '0' COMMENT '通知的用户id',
    `operate_user_id` int unsigned                                                   NOT NULL DEFAULT '0' COMMENT '触发这个通知的用户id',
    `msg`             varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '消息内容',
    `type`            tinyint unsigned                                               NOT NULL DEFAULT '0' COMMENT '类型: 0-默认，1-评论，2-回复 3-点赞 4-收藏 5-关注 6-系统',
    `state`           tinyint unsigned                                               NOT NULL DEFAULT '0' COMMENT '阅读状态: 0-未读，1-已读',
    `create_time`     timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `key_notify_user_id_type_state` (`notify_user_id`, `type`, `state`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 85
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
  AUTO_INCREMENT = 47
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
    `id`          int unsigned                                                 NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `host`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '机器IP',
    `cnt`         int unsigned                                                 NOT NULL DEFAULT '0' COMMENT '访问计数',
    `date`        date                                                         NOT NULL COMMENT '当前日期',
    `create_time` timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_unique_id_date` (`date`, `host`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
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
    `id`          int unsigned                                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tag_name`    varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
    `tag_type`    tinyint                                                       NOT NULL DEFAULT '1' COMMENT '标签类型：1-系统标签，2-自定义标签',
    `category_id` int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '类目ID',
    `status`      tinyint                                                       NOT NULL DEFAULT '0' COMMENT '状态：0-未发布，1-已发布',
    `deleted`     tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 137
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
    `id`               int unsigned                                                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `third_account_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '第三方用户ID',
    `user_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `password`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
    `login_type`       tinyint                                                       NOT NULL DEFAULT '0' COMMENT '登录方式: 0-微信登录，1-账号密码登录',
    `deleted`          tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time`      timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`),
    KEY `key_third_account_id` (`third_account_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
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
  AUTO_INCREMENT = 92
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
    `id`          int unsigned                                                   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     int unsigned                                                   NOT NULL DEFAULT '0' COMMENT '用户ID',
    `user_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '用户名',
    `photo`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户图像',
    `student_id`  varchar(50) COLLATE utf8mb4_general_ci                         NOT NULL DEFAULT '' COMMENT '职位',
    `college`     varchar(50) COLLATE utf8mb4_general_ci                         NOT NULL DEFAULT '' COMMENT '学院',
    `email`       varchar(100) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '电子邮件',
    `profile`     varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '个人简介',
    `user_role`   int                                                            NOT NULL DEFAULT '0' COMMENT '0 普通用户 1 超管',
    `extend`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '扩展字段',
    `ip`          longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin             NOT NULL COMMENT '用户的ip信息',
    `deleted`     tinyint                                                        NOT NULL DEFAULT '0' COMMENT '是否删除',
    `create_time` timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `major`       varchar(100) COLLATE utf8mb4_general_ci                        NOT NULL DEFAULT '' COMMENT '专业',
    PRIMARY KEY (`id`),
    KEY `key_user_id` (`user_id`),
    CONSTRAINT `user_info_chk_1` CHECK (json_valid(`ip`))
) ENGINE = InnoDB
  AUTO_INCREMENT = 14
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
  AUTO_INCREMENT = 15
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户关系表';

create table `sai-forum`.drafts
(
    id          int unsigned auto_increment comment '主键ID'
        primary key,
    user_id     int unsigned default 0                   not null comment '用户ID',
    title       varchar(120) default ''                  not null comment '文章标题',
    short_title varchar(120) default ''                  not null comment '短标题',
    picture     varchar(128) default ''                  not null comment '文章头图',
    summary     varchar(300) default ''                  not null comment '文章摘要',
    category_id int unsigned default 0                   not null comment '分类ID',
    source      tinyint      default 1                   not null comment '来源：1-转载，2-原创，3-翻译',
    source_url  varchar(128) default '1'                 not null comment '原文链接',
    article_id  int unsigned default 0                   not null comment '关联的文章，0-没有',
    content     longtext                                 not null comment '内容',
    deleted     tinyint      default 0                   not null comment '是否删除',
    create_time timestamp    default current_timestamp() not null comment '创建时间',
    update_time timestamp    default current_timestamp() not null on update current_timestamp() comment '最后更新时间',
    constraint idx_article_id
        unique (article_id)
)
    comment '草稿表' charset = utf8mb4;

-- Dump completed on 2023-09-28 13:13:36
