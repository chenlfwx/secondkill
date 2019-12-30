/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50636
Source Host           : localhost:3306
Source Database       : wolfcode_member

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2019-05-30 17:56:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL COMMENT '自增id',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `salt` varchar(10) DEFAULT NULL COMMENT '盐',
  `head` varchar(128) DEFAULT NULL COMMENT '头像',
  `register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登录日期',
  `login_count` int(11) DEFAULT NULL COMMENT '登录次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('13088889999', 'lanxw', 'e5d22cfc746c7da8da84e0a996e0fffa', '1a2d3c4d', null, null, null, null);
