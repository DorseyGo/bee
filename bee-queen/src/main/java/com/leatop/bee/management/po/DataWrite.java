package com.leatop.bee.management.po;

/**
 * @ClassName: DataWriteInfo.java
 * @Description: TODO(任务配置管理实体类)
 * @author hongSheng
 * @date 2019年5月23日
 *
 */
public class DataWrite {

	private Integer id;
	private String name;// 任务名称
	private String nameDesc;// 任务描述
	private String tableName;// 目标库表名
	private String registryUrl;// 注册地址
	private Integer tasksMax;// 任务最大个数
	private Integer maxRetries;// 最大尝试次数
	private String topics;// 主题
	private String valueConverter;// value
	private String keyConverter;// key
	private String connectionUrl;// 目标库url地址
	private String connectionUser;// 目标库用户名
	private String connectionPasswd;// 目标库密码
	private Integer status;// 任务状态，0:启用，1：停止

	private Integer taskType;// 任务类型，0:JDBC，1：HDFS
	private Integer isSplit;// 是否分表，0：是，1：否
	private String pivotField;// 分表字段
	private Integer pivotFieldType;// 分表类型，0:按年，1：按月，2：按日
	private String hdfsUrl;// hdfs地址

	private String sourceConnectionUrl;// 源库url地址
	private String sourceConnectionUser;// 源库用户名
	private String sourceConnectionPasswd;// 源库密码
	private Integer sourceTaskType;// 源类型，0:自增列，1：时间戳，2：时间戳+自增列
	private String sourceTableName;// 源库表名
	private String timestampColumnName;// 自增列字段
	private String incrementingColumnName;// 时间戳字段
	private String fieldsCompareFilter;// 过滤条件类名
	private String fieldsCompareFilterText;// 过滤条件功能描述
	
	private Integer connectorType;// connector类型，0：sink，1：source
	private String tableUseFields;// 传输白名单
	private String tableUseFieldsType;// 传输白名单中的对应字段类型

	
	public String getFieldsCompareFilterText() {
		return fieldsCompareFilterText;
	}

	public void setFieldsCompareFilterText(String fieldsCompareFilterText) {
		this.fieldsCompareFilterText = fieldsCompareFilterText;
	}

	public String getTableUseFields() {
		return tableUseFields;
	}

	public void setTableUseFields(String tableUseFields) {
		this.tableUseFields = tableUseFields;
	}

	public Integer getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(Integer connectorType) {
		this.connectorType = connectorType;
	}

	public String getFieldsCompareFilter() {
		return fieldsCompareFilter;
	}

	public void setFieldsCompareFilter(String fieldsCompareFilter) {
		this.fieldsCompareFilter = fieldsCompareFilter;
	}

	public String getSourceConnectionUrl() {
		return sourceConnectionUrl;
	}

	public void setSourceConnectionUrl(String sourceConnectionUrl) {
		this.sourceConnectionUrl = sourceConnectionUrl;
	}

	public String getSourceConnectionUser() {
		return sourceConnectionUser;
	}

	public void setSourceConnectionUser(String sourceConnectionUser) {
		this.sourceConnectionUser = sourceConnectionUser;
	}

	public String getSourceConnectionPasswd() {
		return sourceConnectionPasswd;
	}

	public void setSourceConnectionPasswd(String sourceConnectionPasswd) {
		this.sourceConnectionPasswd = sourceConnectionPasswd;
	}

	public Integer getSourceTaskType() {
		return sourceTaskType;
	}

	public void setSourceTaskType(Integer sourceTaskType) {
		this.sourceTaskType = sourceTaskType;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public String getTimestampColumnName() {
		return timestampColumnName;
	}

	public void setTimestampColumnName(String timestampColumnName) {
		this.timestampColumnName = timestampColumnName;
	}

	public String getIncrementingColumnName() {
		return incrementingColumnName;
	}

	public void setIncrementingColumnName(String incrementingColumnName) {
		this.incrementingColumnName = incrementingColumnName;
	}

	private Integer isSuccess;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameDesc() {
		return nameDesc;
	}

	public void setNameDesc(String nameDesc) {
		this.nameDesc = nameDesc;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRegistryUrl() {
		return registryUrl;
	}

	public void setRegistryUrl(String registryUrl) {
		this.registryUrl = registryUrl;
	}

	public Integer getTasksMax() {
		return tasksMax;
	}

	public void setTasksMax(Integer tasksMax) {
		this.tasksMax = tasksMax;
	}

	public Integer getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(Integer maxRetries) {
		this.maxRetries = maxRetries;
	}

	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	public String getValueConverter() {
		return valueConverter;
	}

	public void setValueConverter(String valueConverter) {
		this.valueConverter = valueConverter;
	}

	public String getKeyConverter() {
		return keyConverter;
	}

	public void setKeyConverter(String keyConverter) {
		this.keyConverter = keyConverter;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getConnectionUser() {
		return connectionUser;
	}

	public void setConnectionUser(String connectionUser) {
		this.connectionUser = connectionUser;
	}

	public String getConnectionPasswd() {
		return connectionPasswd;
	}

	public void setConnectionPasswd(String connectionPasswd) {
		this.connectionPasswd = connectionPasswd;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getIsSplit() {
		return isSplit;
	}

	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}

	public String getPivotField() {
		return pivotField;
	}

	public void setPivotField(String pivotField) {
		this.pivotField = pivotField;
	}

	public Integer getPivotFieldType() {
		return pivotFieldType;
	}

	public void setPivotFieldType(Integer pivotFieldType) {
		this.pivotFieldType = pivotFieldType;
	}

	public String getHdfsUrl() {
		return hdfsUrl;
	}

	public void setHdfsUrl(String hdfsUrl) {
		this.hdfsUrl = hdfsUrl;
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getTableUseFieldsType() {
		return tableUseFieldsType;
	}

	public void setTableUseFieldsType(String tableUseFieldsType) {
		this.tableUseFieldsType = tableUseFieldsType;
	}

	@Override
	public String toString() {
		return "DataWrite [id=" + id + ", name=" + name + ", nameDesc=" + nameDesc + ", tableName="
				+ tableName + ", registryUrl=" + registryUrl + ", tasksMax=" + tasksMax
				+ ", maxRetries=" + maxRetries + ", topics=" + topics + ", valueConverter="
				+ valueConverter + ", keyConverter=" + keyConverter + ", connectionUrl="
				+ connectionUrl + ", connectionUser=" + connectionUser + ", connectionPasswd="
				+ connectionPasswd + ", status=" + status + ", taskType=" + taskType + ", isSplit="
				+ isSplit + ", pivotField=" + pivotField + ", pivotFieldType=" + pivotFieldType
				+ ", hdfsUrl=" + hdfsUrl + ", sourceConnectionUrl=" + sourceConnectionUrl
				+ ", sourceConnectionUser=" + sourceConnectionUser + ", sourceConnectionPasswd="
				+ sourceConnectionPasswd + ", sourceTaskType=" + sourceTaskType
				+ ", sourceTableName=" + sourceTableName + ", timestampColumnName="
				+ timestampColumnName + ", incrementingColumnName=" + incrementingColumnName
				+ ", fieldsCompareFilter=" + fieldsCompareFilter + ", fieldsCompareFilterText="
				+ fieldsCompareFilterText + ", connectorType=" + connectorType + ", tableUseFields="
				+ tableUseFields + ", tableUseFieldsType=" + tableUseFieldsType + ", isSuccess="
				+ isSuccess + "]";
	}

}
