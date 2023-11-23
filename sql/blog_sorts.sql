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

 Date: 23/11/2023 21:32:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_sorts
-- ----------------------------
DROP TABLE IF EXISTS `blog_sorts`;
CREATE TABLE `blog_sorts`  (
  `sort_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `sort_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类名称',
  `sort_alias` varchar(15) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类别名',
  `sort_description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类描述',
  `parent_sort_id` bigint NOT NULL COMMENT '父分类ID',
  PRIMARY KEY (`sort_id`) USING BTREE,
  INDEX `sort_name`(`sort_name`) USING BTREE,
  INDEX `sort_alias`(`sort_alias`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
