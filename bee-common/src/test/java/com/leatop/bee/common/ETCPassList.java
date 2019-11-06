package com.leatop.bee.common;

import java.util.Date;

//import javax.validation.constraints.NotNull;


/**
* @ClassName: ETCPassList
* @Description: TODO(ETC 通行记录)
* @author hongsheng
* @date 2019年4月25日
*
*/
public class ETCPassList {

	public String getId() {
		// TODO Auto-generated method stub
		return this.SerialNo;
	}
	
	//1.记录号 SmallInt 长度 2
	//@NotNull(message = "记录号不能为空")
	private short RecordNo;
	//2.流水号 char 长度 16
	//@NotNull(message = "流水号不能为空")
	private String SerialNo;
	//3.ETC门架路网编号 SmallInt 长度 2
	//@NotNull(message = "ETC门架路网编号不能为空")
	private short FlagNetRoadID;
	//4.ETC门架路段编号 SmallInt 长度 2
	//@NotNull(message = "ETC门架路段编号不能为空")
	private short FlagRoadID;
	//5.ETC门架编号  SmallInt 长度 2
	//@NotNull(message = "ETC门架编号不能为空")
	private short FlagID;
	//6.OBU单双片标识 TinyInt 长度 1
	//@NotNull(message = "OBU单双片标识不能为空")
	private short OBUType;
	//7.OBU MAC地址 Integer 长度 4
	//@NotNull(message = "OBU MAC地址不能为空")
	private int OBUMacID;
	//8.OBU 的合同序列号 varchar 长度 16
	private String OBUNum;
	//9.非现金支付卡卡号 char 长度 16
	private String PayCardID;
	//10.非现金支付卡卡片类型 tinyInt 长度 1
	private short PayCardType;
	//11.CPU卡网络编号 carchar 长度 2
	private String ICClssuer;
	//12.CPU卡内部编号 varchar 长度 8
	private String CPUCardID;
	//13.车型 TintInt 长度 1
	private short VehicleType;
	//14.车牌号码 char 长度 12
	private String VehPlate;
	//15.车牌颜色 char 长度 2
	private String VehColor;
	//16.通过时间 DateTime 长度 8
	//@NotNull(message = "通过时间不能为空")
	private Date OpTime;
	//17.行驶方向 tinyInt 长度 1
	//@NotNull(message = "行驶方向不能为空")
	private short Direction;
	//18.车种 TinyInt 长度 1
	private short VehStatus;
	//19.备用1 Integer 长度 4
	private int Spare1;
	//20.备用2 Integer 长度 4
	private int Spare2;
	//21.备用3 varchar 长度 50
	private int Spare3;
	//22.备用4 varchar 长度 50
	private int Spare4;
	public short getRecordNo() {
		return RecordNo;
	}
	public void setRecordNo(short recordNo) {
		RecordNo = recordNo;
	}
	public String getSerialNo() {
		return SerialNo;
	}
	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}
	public short getFlagNetRoadID() {
		return FlagNetRoadID;
	}
	public void setFlagNetRoadID(short flagNetRoadID) {
		FlagNetRoadID = flagNetRoadID;
	}
	public short getFlagRoadID() {
		return FlagRoadID;
	}
	public void setFlagRoadID(short flagRoadID) {
		FlagRoadID = flagRoadID;
	}
	public short getFlagID() {
		return FlagID;
	}
	public void setFlagID(short flagID) {
		FlagID = flagID;
	}
	public short getOBUType() {
		return OBUType;
	}
	public void setOBUType(short oBUType) {
		OBUType = oBUType;
	}
	public int getOBUMacID() {
		return OBUMacID;
	}
	public void setOBUMacID(int oBUMacID) {
		OBUMacID = oBUMacID;
	}
	public String getOBUNum() {
		return OBUNum;
	}
	public void setOBUNum(String oBUNum) {
		OBUNum = oBUNum;
	}
	public String getPayCardID() {
		return PayCardID;
	}
	public void setPayCardID(String payCardID) {
		PayCardID = payCardID;
	}
	public short getPayCardType() {
		return PayCardType;
	}
	public void setPayCardType(short payCardType) {
		PayCardType = payCardType;
	}
	public String getICClssuer() {
		return ICClssuer;
	}
	public void setICClssuer(String iCClssuer) {
		ICClssuer = iCClssuer;
	}
	public String getCPUCardID() {
		return CPUCardID;
	}
	public void setCPUCardID(String cPUCardID) {
		CPUCardID = cPUCardID;
	}
	public short getVehicleType() {
		return VehicleType;
	}
	public void setVehicleType(short vehicleType) {
		VehicleType = vehicleType;
	}
	public String getVehPlate() {
		return VehPlate;
	}
	public void setVehPlate(String vehPlate) {
		VehPlate = vehPlate;
	}
	public String getVehColor() {
		return VehColor;
	}
	public void setVehColor(String vehColor) {
		VehColor = vehColor;
	}
	public Date getOpTime() {
		return OpTime;
	}
	public void setOpTime(Date opTime) {
		OpTime = opTime;
	}
	public short getDirection() {
		return Direction;
	}
	public void setDirection(short direction) {
		Direction = direction;
	}
	public short getVehStatus() {
		return VehStatus;
	}
	public void setVehStatus(short vehStatus) {
		VehStatus = vehStatus;
	}
	public int getSpare1() {
		return Spare1;
	}
	public void setSpare1(int spare1) {
		Spare1 = spare1;
	}
	public int getSpare2() {
		return Spare2;
	}
	public void setSpare2(int spare2) {
		Spare2 = spare2;
	}
	public int getSpare3() {
		return Spare3;
	}
	public void setSpare3(int spare3) {
		Spare3 = spare3;
	}
	public int getSpare4() {
		return Spare4;
	}
	public void setSpare4(int spare4) {
		Spare4 = spare4;
	}
	public ETCPassList() {
		super();
	}
	public ETCPassList(short recordNo, String serialNo, short flagNetRoadID, short flagRoadID, short flagID,
			short oBUType, int oBUMacID, String oBUNum, String payCardID, short payCardType, String iCClssuer,
			String cPUCardID, short vehicleType, String vehPlate, String vehColor, Date opTime, short direction,
			short vehStatus, int spare1, int spare2, int spare3, int spare4) {
		super();
		RecordNo = recordNo;
		SerialNo = serialNo;
		FlagNetRoadID = flagNetRoadID;
		FlagRoadID = flagRoadID;
		FlagID = flagID;
		OBUType = oBUType;
		OBUMacID = oBUMacID;
		OBUNum = oBUNum;
		PayCardID = payCardID;
		PayCardType = payCardType;
		ICClssuer = iCClssuer;
		CPUCardID = cPUCardID;
		VehicleType = vehicleType;
		VehPlate = vehPlate;
		VehColor = vehColor;
		OpTime = opTime;
		Direction = direction;
		VehStatus = vehStatus;
		Spare1 = spare1;
		Spare2 = spare2;
		Spare3 = spare3;
		Spare4 = spare4;
	}

}
