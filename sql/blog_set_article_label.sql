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

 Date: 23/11/2023 21:32:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_set_article_label
-- ----------------------------
DROP TABLE IF EXISTS `blog_set_article_label`;
CREATE TABLE `blog_set_article_label`  (
  `article_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `label_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`article_id`, `label_id`) USING BTREE,
  INDEX `label_id`(`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
