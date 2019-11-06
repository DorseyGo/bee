/*
 Navicat Premium Data Transfer

 Source Server         : testSqlserver
 Source Server Type    : SQL Server
 Source Server Version : 10501600
 Source Host           : 128.8.51.175:1433
 Source Catalog        : RoadMidDb
 Source Schema         : dbo

 Target Server Type    : SQL Server
 Target Server Version : 10501600
 File Encoding         : 65001

 Date: 11/06/2019 20:09:23
*/


-- ----------------------------
-- Table structure for IMAGE_LIST
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[IMAGE_LIST]') AND type IN ('U'))
	DROP TABLE [dbo].[IMAGE_LIST]
GO

CREATE TABLE [dbo].[IMAGE_LIST] (
  [listNo] int  NOT NULL,
  [serialNo] varchar(40) COLLATE Chinese_PRC_CI_AS  NULL,
  [flagNetRoadID] int  NULL,
  [flagRoadID] int  NULL,
  [flagID] int  NULL,
  [deviceID] int DEFAULT '' NULL,
  [laneNo] int  NULL,
  [directionNo] varchar(1) COLLATE Chinese_PRC_CI_AS  NULL,
  [vehPlate] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [vehSpeed] int  NULL,
  [vehBodyColorNo] int  NULL,
  [vehBodyDeepNo] int  NULL,
  [vehTypeNo] int  NULL,
  [plateTypeNo] int  NULL,
  [image] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL,
  [vehColor] varchar(255) COLLATE Chinese_PRC_CI_AS  NULL
)
GO

ALTER TABLE [dbo].[IMAGE_LIST] SET (LOCK_ESCALATION = TABLE)
GO


IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID(N'[dbo].[ETC_PASS_LIST]') AND type IN ('U'))
	DROP TABLE [dbo].[ETC_PASS_LIST]
GO

CREATE TABLE [dbo].[ETC_PASS_LIST] (
  [recordNo] int  NOT NULL,
  [serialNo] varchar(40) COLLATE Chinese_PRC_CI_AS  NOT NULL,
  [flagNetRoadID] int  NULL,
  [flagRoadID] int  NULL,
  [flagID] int  NULL,
  [obuType] int  NULL,
  [obuMacID] int  NULL,
  [obuNum] varchar(24) COLLATE Chinese_PRC_CI_AS  NULL,
  [payCardID] varchar(24) COLLATE Chinese_PRC_CI_AS  NULL,
  [payCardType] int  NOT NULL,
  [iccIssuer] varchar(24) COLLATE Chinese_PRC_CI_AS  NULL,
  [cpuCardID] varchar(24) COLLATE Chinese_PRC_CI_AS  NULL,
  [vehicleType] int  NULL,
  [vehPlate] varchar(24) COLLATE Chinese_PRC_CI_AS  NULL,
  [vehColor] varchar(24) COLLATE Chinese_PRC_CI_AS  NULL,
  [opTime] datetime  NOT NULL,
  [direction] int  NULL,
  [vehStatus] int  NULL,
  [spare1] int  NULL,
  [spare2] int  NULL,
  [spare3] varchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [spare4] varchar(50) COLLATE Chinese_PRC_CI_AS  NULL,
  [loadtime] smalldatetime DEFAULT (getdate()) NULL
)
GO

ALTER TABLE [dbo].[ETC_PASS_LIST] SET (LOCK_ESCALATION = TABLE)
GO