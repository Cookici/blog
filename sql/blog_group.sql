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

 Date: 23/11/2023 21:32:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_group
-- ----------------------------
DROP TABLE IF EXISTS `blog_group`;
CREATE TABLE `blog_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` bigint NOT NULL COMMENT '群聊id',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '群聊名称',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `users_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户们id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`users_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
