CREATE database IF NOT EXISTS `learning`;
USE `learning`;

-- ------
-- ETC_PASS_LIST
-- ------
DROP TABLE IF EXISTS `ETC_PASS_LIST`;
CREATE TABLE `ETC_PASS_LIST` (
	`recordNo` INT(4) NOT NULL,
	`serialNo` VARCHAR(24) NOT NULL,
	`flagNetRoadID` INT(4),
	`flagRoadID` INT(4),
	`flagID` INT(4),
	`obuType` INT(4),
	`obuMacID` INT(4),
	`obuNum` VARCHAR(24) DEFAULT NULL,
	`payCardID` VARCHAR(24) DEFAULT NULL,
	`payCardType` INT(4) NOT NULL,
	`iccIssuer` VARCHAR(24) DEFAULT NULL,
	`cpuCardID` VARCHAR(24),
	`vehicleType` INT(4),
	`vehPlate` VARCHAR(24),
	`vehColor` VARCHAR(24),
	`opTime` TIMESTAMP NOT NULL,
	`direction` INT(4),
	`vehStatus` INT(4),
	`spare1` INT(4),
	`spare2` INT(4),
	`spare3` VARCHAR(24) DEFAULT NULL,
	`spare4` VARCHAR(24) DEFAULT NULL,
	PRIMARY KEY (`recordNo`)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE INDEX `indx_serial_no_epl` ON `ETC_PASS_LIST`(`serialNo`);

-- ---------
-- schema registry
-- ----------
DROP TABLE IF EXISTS `SCHEMA_REPO`;
CREATE TABLE `SCHEMA_REPO`(
	`id` INT(4) NOT NULL AUTO_INCREMENT,
	`version` INT(4) NOT NULL,
	`subject` VARCHAR(82) NOT NULL,
	`schema` VARCHAR(4096),
	PRIMARY KEY (`id`),
	KEY  `key_version_sp` (`version`)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;


-- 导出  表 beekb.data_trans 结构
CREATE TABLE IF NOT EXISTS `data_trans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trans_count` int(11) DEFAULT '0' COMMENT '传输条数（按小时累加）',
  `success_count` int(11) DEFAULT '0' COMMENT '成功入库条数（按小时累加）',
  `error_count` int(11) DEFAULT '0' COMMENT '入库失败条数（按小时累加）',
  `topic_name` varchar(64) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `host_name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11326 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 beekb.data_trans_connect 结构
CREATE TABLE IF NOT EXISTS `data_trans_connect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trans_count` int(11) DEFAULT '0' COMMENT '传输条数（按小时累加）',
  `success_count` int(11) DEFAULT '0' COMMENT '成功入库条数（按小时累加）',
  `error_count` int(11) DEFAULT '0' COMMENT '入库失败条数（按小时累加）',
  `connect_name` varchar(64) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8132 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 beekb.data_write 结构
CREATE TABLE IF NOT EXISTS `data_write` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_desc` varchar(200) DEFAULT NULL,
  `table_name` varchar(100) DEFAULT NULL,
  `registry_url` varchar(100) DEFAULT NULL,
  `tasks_max` tinyint(2) DEFAULT NULL,
  `max_retries` tinyint(2) DEFAULT NULL,
  `topics` varchar(100) DEFAULT NULL,
  `value_converter` varchar(100) DEFAULT NULL,
  `key_converter` varchar(100) DEFAULT NULL,
  `connection_url` varchar(100) DEFAULT NULL,
  `connection_user` varchar(100) DEFAULT NULL,
  `connection_passwd` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '0:启用，1：停止',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 beekb.topic 结构
CREATE TABLE IF NOT EXISTS `topic` (
  `name` varchar(64) NOT NULL COMMENT '主题名称',
  `zookeeper_host` varchar(64) DEFAULT '' COMMENT 'zookeeper地址',
  `replication_factor` int(11) DEFAULT NULL COMMENT '副本数量',
  `partitions` int(11) DEFAULT NULL COMMENT '主题partition数量',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;