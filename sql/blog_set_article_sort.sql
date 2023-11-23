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

 Date: 23/11/2023 21:32:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_set_article_sort
-- ----------------------------
DROP TABLE IF EXISTS `blog_set_article_sort`;
CREATE TABLE `blog_set_article_sort`  (
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `sort_id` bigint NOT NULL COMMENT '分类ID',
  PRIMARY KEY (`article_id`, `sort_id`) USING BTREE,
  INDEX `sort_id`(`sort_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
