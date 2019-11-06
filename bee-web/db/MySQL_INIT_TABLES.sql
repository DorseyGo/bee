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
	`subject` VARCHAR(24) NOT NULL,
	`schema` VARCHAR(4096),
	PRIMARY KEY (`id`),
	KEY  `key_version_sp` (`version`)
)ENGINE=InnoDB DEFAULT CHARSET=UTF8;