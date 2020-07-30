/*
 Navicat Premium Data Transfer

 Source Server         : 外网-5.7-02
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : ngrok2.xiaomiqiu.cn:3038
 Source Schema         : mall_1

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 30/07/2020 19:36:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for orders_3
-- ----------------------------
DROP TABLE IF EXISTS `orders_3`;
CREATE TABLE `orders_3`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `user_id` int(16) NULL DEFAULT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '订单表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
