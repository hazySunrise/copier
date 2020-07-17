/*
Navicat MySQL Data Transfer

Source Server         : jimi
Source Server Version : 50645
Source Host           : localhost:3306
Source Database       : mes_dbcopier

Target Server Type    : MYSQL
Target Server Version : 50645
File Encoding         : 65001

Date: 2019-08-24 17:41:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `backup_log`
-- ----------------------------
DROP TABLE IF EXISTS `backup_log`;
CREATE TABLE `backup_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `backup_time` datetime NOT NULL,
  `number` bigint(11) NOT NULL,
  `table` varchar(255) NOT NULL,
  `time` datetime NOT NULL,
  `consume_time` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=gbk;


-- ----------------------------
-- Table structure for `system_log`
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time` varchar(255) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `thread` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=gbk;

