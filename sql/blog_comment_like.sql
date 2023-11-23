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

 Date: 23/11/2023 21:31:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment_like`;
CREATE TABLE `blog_comment_like`  (
  `comment_like_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `comment_id` bigint NOT NULL,
  `comment_create_date` datetime(0) NOT NULL,
  `comment_update_date` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`comment_like_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
