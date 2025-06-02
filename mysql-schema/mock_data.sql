-- Mock Data for SAI-Forum
-- Generated on 2025-05-27

-- Users
LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user` (`id`, `third_account_id`, `user_name`, `password`, `deleted`, `create_time`, `update_time`)
VALUES (101, NULL, 'user_alpha', 'password_hash_1', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (102, NULL, 'user_beta', 'password_hash_2', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (103, NULL, 'user_gamma', 'password_hash_3', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (104, NULL, 'user_delta', 'password_hash_4', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (105, NULL, 'user_epsilon', 'password_hash_5', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00');
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

-- User Info
LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info`
    DISABLE KEYS */;
INSERT INTO `user_info` (`id`, `user_id`, `user_name`, `photo`, `email`, `college`, `profile`, `extend`, `deleted`,
                         `create_time`, `update_time`)
VALUES (101, 101, 'Alpha User', 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png', 'alpha@example.com',
        'Alpha Corp', 'Profile of Alpha User', '{}', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (102, 102, 'Beta User', 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png', 'beta@example.com',
        'Beta Inc', 'Profile of Beta User', '{}', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (103, 103, 'Gamma User', 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png', 'gamma@example.com',
        'Gamma LLC', 'Profile of Gamma User', '{}', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (104, 104, 'Delta User', 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_4.png', 'delta@example.com',
        'Delta Co', 'Profile of Delta User', '{}', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (105, 105, 'Epsilon User', 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png', 'epsilon@example.com',
        'Epsilon Ltd', 'Profile of Epsilon User', '{}', 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00');
/*!40000 ALTER TABLE `user_info`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Categories (Skipped as per requirement, assuming category_id 0-7 are predefined)
-- No insertion for category table as it's not required.

-- Tags
LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag`
    DISABLE KEYS */;
INSERT INTO `tag` (`id`, `tag_name`, `status`, `deleted`, `create_time`, `update_time`)
VALUES (401, 'Java', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (402, 'Spring Boot', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (403, 'AI', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (404, 'Machine Learning', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (405, 'Web Development', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (406, 'Databases', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (407, 'Cloud Computing', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (408, 'Python', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (409, 'JavaScript', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (410, 'Tutorial', 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00');
/*!40000 ALTER TABLE `tag`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Articles
LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article`
    DISABLE KEYS */;
INSERT INTO `article` (`id`, `user_id`, `title`, `short_title`, `picture`, `summary`, `category_id`, `article_type`,
                       `status`, `deleted`, `create_time`, `update_time`)
VALUES (201, 101, 'Introduction to Java Programming', 'Java Intro', 'http://example.com/pic/java.jpg',
        'A comprehensive guide to starting with Java.', 3, 1, 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (202, 102, 'The Future of Artificial Intelligence', 'Future of AI', 'http://example.com/pic/ai.jpg',
        'Exploring upcoming trends in AI.', 1, 1, 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (203, 103, 'Healthy Living Tips', 'Healthy Living', 'http://example.com/pic/health.jpg',
        'Simple tips for a healthier lifestyle.', 4, 1, 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (204, 101, 'Advanced Spring Boot Techniques', 'Adv Spring Boot', 'http://example.com/pic/springboot.jpg',
        'Deep dive into Spring Boot.', 3, 1, 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (205, 104, 'Exploring the Cosmos', 'Cosmos Exploration', 'http://example.com/pic/cosmos.jpg',
        'A journey through space and time.', 2, 1, 1, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00');
/*!40000 ALTER TABLE `article`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Article Detail
LOCK TABLES `article_detail` WRITE;
/*!40000 ALTER TABLE `article_detail`
    DISABLE KEYS */;
INSERT INTO `article_detail` (`id`, `article_id`, `version`, `content`, `deleted`, `create_time`, `update_time`)
VALUES (101, 201, 1, '## Chapter 1: What is Java?\nJava is a versatile and powerful programming language...', 0,
        '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (102, 202, 1, '## AI Today and Tomorrow\nArtificial Intelligence is rapidly evolving...', 0,
        '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (103, 203, 1, '## Eat Well, Live Well\nNutrition and exercise are key to a healthy life...', 0,
        '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (104, 204, 1, '## Mastering Spring Boot\nLearn about microservices, security, and more with Spring Boot...', 0,
        '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (105, 205, 1,
        '## Wonders of the Universe\nFrom black holes to distant galaxies, the cosmos is full of marvels...', 0,
        '2025-05-27 10:00:00', '2025-05-27 10:00:00');
/*!40000 ALTER TABLE `article_detail`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Article Tag
LOCK TABLES `article_tag` WRITE;
/*!40000 ALTER TABLE `article_tag`
    DISABLE KEYS */;
INSERT INTO `article_tag` (`id`, `article_id`, `tag_id`, `deleted`, `create_time`, `update_time`)
VALUES (101, 201, 401, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- Java Intro -> Java
       (102, 201, 410, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- Java Intro -> Tutorial
       (103, 202, 403, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- Future of AI -> AI
       (104, 202, 404, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- Future of AI -> Machine Learning
       (105, 204, 401, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- Adv Spring Boot -> Java
       (106, 204, 402, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- Adv Spring Boot -> Spring Boot
       (107, 204, 405, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'); -- Adv Spring Boot -> Web Development
/*!40000 ALTER TABLE `article_tag`
    ENABLE KEYS */;
UNLOCK TABLES;

-- Comments
LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment`
    DISABLE KEYS */;
INSERT INTO `comment` (`id`, `article_id`, `user_id`, `content`, `parent_comment_id`, `deleted`, `create_time`,
                       `update_time`)
VALUES (201, 201, 102, 'Great article on Java!', 0, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (202, 201, 103, 'Thanks for sharing.', 0, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (203, 201, 101, 'Glad you liked it!', 201, 0, '2025-05-27 10:00:00',
        '2025-05-27 10:00:00'), -- Reply to comment 201
       (204, 202, 104, 'AI is fascinating!', 0, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00'),
       (205, 202, 105, 'Indeed, the possibilities are endless.', 204, 0, '2025-05-27 10:00:00',
        '2025-05-27 10:00:00'), -- Reply to comment 204
       (206, 202, 102, 'I agree!', 205, 1, '2025-05-27 10:00:00',
        '2025-05-27 10:00:00'), -- Reply to comment 205 (child of 204)
       (207, 203, 101, 'Very useful tips!', 0, 0, '2025-05-27 10:00:00', '2025-05-27 10:00:00');
/*!40000 ALTER TABLE `comment`
    ENABLE KEYS */;
UNLOCK TABLES;

-- User Relation (Follows)
LOCK TABLES `user_relation` WRITE;
/*!40000 ALTER TABLE `user_relation`
    DISABLE KEYS */;
INSERT INTO `user_relation` (`id`, `user_id`, `follow_user_id`, `follow_state`, `create_time`, `update_time`)
VALUES (101, 101, 102, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 101 follows User 102
       (102, 101, 103, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 101 follows User 103
       (103, 102, 101, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 102 follows User 101
       (104, 103, 104, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 103 follows User 104
       (105, 104, 105, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'); -- User 104 follows User 105
/*!40000 ALTER TABLE `user_relation`
    ENABLE KEYS */;
UNLOCK TABLES;

-- User Footprint (Likes/Collections)
LOCK TABLES `user_foot` WRITE;
/*!40000 ALTER TABLE `user_foot`
    DISABLE KEYS */;
INSERT INTO `user_foot` (`id`, `user_id`, `document_id`, `document_type`, `collection_stat`, `praise_stat`, `read_stat`,
                         `create_time`, `update_time`)
VALUES (101, 101, 202, 1, 1, 0, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 101 collected Article 202
       (102, 101, 203, 1, 0, 1, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 101 liked Article 203
       (103, 102, 201, 1, 1, 1, 1, '2025-05-27 10:00:00',
        '2025-05-27 10:00:00'),                                                   -- User 102 collected and liked Article 201
       (104, 103, 204, 1, 0, 1, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 103 liked Article 204
       (105, 104, 201, 1, 1, 0, 1, '2025-05-27 10:00:00', '2025-05-27 10:00:00'), -- User 104 collected Article 201
       (106, 105, 205, 1, 1, 1, 1, '2025-05-27 10:00:00',
        '2025-05-27 10:00:00'); -- User 105 collected and liked Article 205
/*!40000 ALTER TABLE `user_foot`
    ENABLE KEYS */;
UNLOCK TABLES;
