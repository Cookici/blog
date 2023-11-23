/*
 Navicat MySQL Data Transfer

 Source Server         : nacos
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : 192.168.29.200:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 23/11/2023 21:31:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_articles
-- ----------------------------
DROP TABLE IF EXISTS `blog_articles`;
CREATE TABLE `blog_articles`  (
  `article_id` bigint NOT NULL AUTO_INCREMENT COMMENT '博文ID',
  `user_id` bigint NOT NULL COMMENT '发表用户ID',
  `article_title` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '博文标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '博文内容',
  `article_views` bigint NOT NULL COMMENT '浏览量',
  `article_comment_count` bigint NOT NULL COMMENT '评论总数',
  `article_date` datetime(0) NULL DEFAULT NULL COMMENT '发表时间',
  `article_like_count` bigint NOT NULL COMMENT '点赞数',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`article_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `blog_articles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
