/*
Navicat MySQL Data Transfer

Source Server         : 192.168.178.128
Source Server Version : 50724
Source Host           : 192.168.178.128:3306
Source Database       : simple_pay_service_notify

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-31 13:49:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `simple_notify_record`
-- ----------------------------
DROP TABLE IF EXISTS `simple_notify_record`;
CREATE TABLE `simple_notify_record` (
  `id` varchar(50) NOT NULL DEFAULT '' COMMENT '主键ID',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本事情',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `edit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `notify_rule` varchar(255) DEFAULT NULL COMMENT '通知规则（单位:分钟）',
  `notify_times` int(11) NOT NULL DEFAULT '0' COMMENT '已通知次数',
  `limit_notify_times` int(11) NOT NULL DEFAULT '0' COMMENT '最大通知次数限制',
  `url` varchar(2000) NOT NULL DEFAULT '' COMMENT '通知请求链接（包含通知内容）',
  `merchant_order_no` varchar(50) NOT NULL DEFAULT '' COMMENT '商户订单号',
  `merchant_no` varchar(50) NOT NULL DEFAULT '' COMMENT '商户编号',
  `status` varchar(50) NOT NULL DEFAULT '' COMMENT '通知状态（对应枚举值）',
  `notify_type` varchar(30) DEFAULT NULL COMMENT '通知类型',
  PRIMARY KEY (`id`),
  KEY `AK_KEY_2` (`merchant_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of simple_notify_record
-- ----------------------------

-- ----------------------------
-- Table structure for `simple_notify_record_log`
-- ----------------------------
DROP TABLE IF EXISTS `simple_notify_record_log`;
CREATE TABLE `simple_notify_record_log` (
  `id` varchar(50) NOT NULL DEFAULT '' COMMENT 'ID',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `edit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `notify_id` varchar(50) NOT NULL DEFAULT '' COMMENT '通知记录ID',
  `request` varchar(2000) NOT NULL DEFAULT '' COMMENT '请求内容',
  `response` varchar(2000) NOT NULL DEFAULT '' COMMENT '响应内容',
  `merchant_no` varchar(50) NOT NULL DEFAULT '' COMMENT '商户编号',
  `merchant_order_no` varchar(50) NOT NULL COMMENT '商户订单号',
  `http_status` varchar(50) NOT NULL COMMENT 'HTTP状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of simple_notify_record_log
-- ----------------------------
