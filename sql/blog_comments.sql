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

 Date: 23/11/2023 21:31:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_comments
-- ----------------------------
DROP TABLE IF EXISTS `blog_comments`;
CREATE TABLE `blog_comments`  (
  `comment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint NOT NULL COMMENT '发表用户ID',
  `article_id` bigint NOT NULL COMMENT '评论博文ID',
  `comment_like_count` bigint NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_date` datetime(0) NULL DEFAULT NULL COMMENT '评论日期',
  `comment_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容',
  `comment_content_img` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '评论图片',
  `parent_comment_id` bigint NOT NULL COMMENT '父评论ID',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `article_id`(`article_id`) USING BTREE,
  INDEX `comment_date`(`comment_date`) USING BTREE,
  INDEX `parent_comment_id`(`parent_comment_id`) USING BTREE,
  INDEX `uid`(`user_id`) USING BTREE,
  CONSTRAINT `blog_comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_users` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 122 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
