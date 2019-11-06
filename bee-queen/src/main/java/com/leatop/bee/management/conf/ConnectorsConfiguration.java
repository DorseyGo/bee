package com.leatop.bee.management.conf;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leatop.bee.management.constant.Constant;
import com.leatop.bee.management.po.DataWrite;
import com.leatop.bee.management.resp.Resp;
import com.leatop.bee.management.util.BaseHttpServiceUtils;


/**
 * 	任务配置管理类
 * 
 * @author zlm,hongSheng
 *
 */
@Configuration
@ConfigurationProperties(prefix = "bee.data.write.kafka.connectors")
@PropertySource(encoding = "UTF-8", value = "classpath:data-write-kafka-connectors.properties")
public class ConnectorsConfiguration {
	
	private String connectorUrl;
	private String connectorClass;
	private String valueConverterSchemaRegistryUrl;
	private String tasksMax;
	private String maxRetries;
	private String valueConverter;
	private String keyConverter;
	private String metrixReporterUrl;
	private String tableNameGeneratorClass;
	private String sourceConnectorClass;
	
	
	public String getSourceConnectorClass() {
		return sourceConnectorClass;
	}
	public void setSourceConnectorClass(String sourceConnectorClass) {
		this.sourceConnectorClass = sourceConnectorClass;
	}
	public String getTableNameGeneratorClass() {
		return tableNameGeneratorClass;
	}
	public void setTableNameGeneratorClass(String tableNameGeneratorClass) {
		this.tableNameGeneratorClass = tableNameGeneratorClass;
	}
	public String getMetrixReporterUrl() {
		return metrixReporterUrl;
	}
	public void setMetrixReporterUrl(String metrixReporterUrl) {
		this.metrixReporterUrl = metrixReporterUrl;
	}
	public String getConnectorUrl() {
		return connectorUrl;
	}
	public void setConnectorUrl(String connectorUrl) {
		String[] connectorUrlAttr = connectorUrl.split(",");
		for(int i=0;i<connectorUrlAttr.length;i++) {
			if (isConnectorNormal(connectorUrlAttr[i])) {
				this.connectorUrl = connectorUrlAttr[i];
				break;
			}
		}
	}
	public String getConnectorClass() {
		return connectorClass;
	}
	public void setConnectorClass(String connectorClass) {
		this.connectorClass = connectorClass;
	}
	public String getValueConverterSchemaRegistryUrl() {
		return valueConverterSchemaRegistryUrl;
	}
	public void setValueConverterSchemaRegistryUrl(String valueConverterSchemaRegistryUrl) {
		this.valueConverterSchemaRegistryUrl = valueConverterSchemaRegistryUrl;
	}
	
	public String getTasksMax() {
		return tasksMax;
	}
	public void setTasksMax(String tasksMax) {
		this.tasksMax = tasksMax;
	}
	public String getMaxRetries() {
		return maxRetries;
	}
	public void setMaxRetries(String maxRetries) {
		this.maxRetries = maxRetries;
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
	
	@Override
	public String toString() {
		return "ConnectorsConfiguration [connectorUrl=" + connectorUrl + ", connectorClass="
				+ connectorClass + ", valueConverterSchemaRegistryUrl="
				+ valueConverterSchemaRegistryUrl + ", tasksmMax=" + tasksMax + ", maxRetries="
				+ maxRetries + ", valueConverter=" + valueConverter + ", keyConverter="
				+ keyConverter + "]";
	}

	/**
	 * 判断connector是否正常
	 * 
	 * @param name
	 * @return
	 */
	public Boolean isConnectorNormal(String connectorUrl) {
		boolean result = true;
		HttpURLConnection connection = null;
		try {
	        connection = setConnection(connection, connectorUrl,"GET");
	        if (connection.getResponseCode()!=200) {
				result = false;
			}
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        result = false;
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        }
	    }
		return  result;
	}
	
	/**
	 * 判断任务是否存在
	 * 
	 * @param name
	 * @return
	 */
	public int isExitConnectors(String name) {
		int status = 0;
		HttpURLConnection connection = null;
		try {
	        connection = setConnection(connection, connectorUrl+name+"/status","GET");
	        status = connection.getResponseCode();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        status = 900;
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	 status = 900;
	        }
	    }
		return  status;
	}
	
	/**
	 * 获取任务状态
	 * 
	 * @param name
	 * @return
	 */
	public Resp getConnectorsStatus(String name) {
		Resp resp = Resp.OK;
		HttpURLConnection connection = null;
		try {
			connection = setConnection(connection, connectorUrl+name+"/status","GET");
	        if (connection.getResponseCode() == 200) {
	        	byte[] resultBytes = BaseHttpServiceUtils.readStream(connection.getInputStream());
	        	String json = new String(resultBytes,"utf-8");
	        	String connector = JSONObject.parseObject(JSONObject.parseObject(json).getString("connector")).getString("state");
	        	if (connector.equals("PAUSED")) {
	        		resp = Resp.FAILURE.withPhase("任务已经停止");
				}else {
					JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(json).getString("tasks"));
					if(jsonArray.size()>0){
					  for(int i=0;i<jsonArray.size();i++){
						  JSONObject jsonObject = jsonArray.getJSONObject(i);
						  if (jsonObject.getString("state").equals("FAILED")) {
							  resp = Resp.FAILURE.withPhase(jsonObject.getString("trace"));
							  break;
						  }
					   }
				    }
				}
	        }else if (connection.getResponseCode() == 404) {
	        	resp = Resp.FAILURE.withPhase("任务不存在，已经被删除");
			}else {
				resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
			}
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        resp = Resp.FAILURE.withPhase(ex.getMessage());
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	 ex.printStackTrace();
	        }
	    }
		return  resp;
	}
	
	/**
	 * 添加任务
	 * 
	 * @param dataWrite
	 * @return
	 */
	public Resp addConnectors(DataWrite dataWrite) {
		Resp resp = Resp.OK;
		String params = "";
		HttpURLConnection connection = null;
		if (dataWrite.getConnectorType()==0) {
			if (dataWrite.getTaskType()==0) {
				if (dataWrite.getFieldsCompareFilter()==null) {
					dataWrite.setFieldsCompareFilter("");
				}
				if (dataWrite.getTableUseFields()==null) {
					dataWrite.setTableUseFields("");
				}
				String tableName = dataWrite.getTableName();
				//JDBC类型
				//如果分表
				if (dataWrite.getIsSplit()==0) {
					//按年
					if (dataWrite.getPivotFieldType()==0) {
						tableName = tableName+Constant.pivotFormatYear;//表名+{yyyy}
					}
					//按月
					if (dataWrite.getPivotFieldType()==1) {
						tableName = tableName+Constant.pivotFormatMonth;//表名+{yyyyMM}
					}
					//按日
					if (dataWrite.getPivotFieldType()==2) {
						tableName = tableName+Constant.pivotFormatDay;//表名+{yyyyMMdd}
					}
					params ="{\r\n" + 
							"	\"name\": \""+dataWrite.getName()+"\",\r\n" + 
							"    \"config\": {\r\n" + 
							"        \"connector.class\": \""+this.connectorClass+"\",\r\n" + 
							"        \"table.name.format\": \""+tableName+"\",\r\n" + 
							"        \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
							"        \"connection.password\": \""+dataWrite.getConnectionPasswd()+"\",\r\n" + 
							"        \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
							"        \"topics\": \""+dataWrite.getTopics()+"\",\r\n" + 
							"        \"connection.user\": \""+dataWrite.getConnectionUser()+"\",\r\n" + 
							"        \"max.retries\": \""+this.maxRetries+"\",\r\n" + 
							"        \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
							"        \"connection.url\": \""+dataWrite.getConnectionUrl()+"\",\r\n" + 
							"        \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
							"        \"metrix.reporter.url\": \""+this.metrixReporterUrl+"\",\r\n" + 
							"        \"pivot.field\": \""+dataWrite.getPivotField().toUpperCase()+"\",\r\n" + 
							"        \"filter.name.class\": \""+dataWrite.getFieldsCompareFilter()+"\",\r\n" + 
							"        \"table.use.fields\": \""+dataWrite.getTableUseFields()+"\",\r\n" + 
							"        \"table.name.generator.class\": \""+this.tableNameGeneratorClass+"\"\r\n" + 
							"    }\r\n" + 
							"}";
				}else {
					//如果不分表，传空字符串
					dataWrite.setPivotField("");
					params ="{\r\n" + 
							"	\"name\": \""+dataWrite.getName()+"\",\r\n" + 
							"    \"config\": {\r\n" + 
							"        \"connector.class\": \""+this.connectorClass+"\",\r\n" + 
							"        \"table.name.format\": \""+tableName+"\",\r\n" + 
							"        \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
							"        \"connection.password\": \""+dataWrite.getConnectionPasswd()+"\",\r\n" + 
							"        \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
							"        \"topics\": \""+dataWrite.getTopics()+"\",\r\n" + 
							"        \"connection.user\": \""+dataWrite.getConnectionUser()+"\",\r\n" + 
							"        \"max.retries\": \""+this.maxRetries+"\",\r\n" + 
							"        \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
							"        \"connection.url\": \""+dataWrite.getConnectionUrl()+"\",\r\n" + 
							"        \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
							"        \"metrix.reporter.url\": \""+this.metrixReporterUrl+"\",\r\n" + 
							"        \"pivot.field\": \""+dataWrite.getPivotField().toUpperCase()+"\",\r\n" + 
							"        \"filter.name.class\": \""+dataWrite.getFieldsCompareFilter()+"\",\r\n" + 
							"        \"table.use.fields\": \""+dataWrite.getTableUseFields()+"\"\r\n" + 
							"    }\r\n" + 
							"}";
				}
				
			}else if(dataWrite.getTaskType()==1) {
				//HDFS
			}
		}else if (dataWrite.getConnectorType()==1) {
			if (dataWrite.getSourceTaskType()==0) {
				//自增列
				params = "{\r\n" + 
						"	\"name\": \""+dataWrite.getName()+"\",\r\n" + 
						"    \"config\": {\r\n" + 
						"        \"connector.class\": \""+this.sourceConnectorClass+"\",\r\n" + 
						"        \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
						"        \"table.whitelist\": \""+dataWrite.getSourceTableName()+"\",\r\n" + 
						"        \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
						"        \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
						"        \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
						"        \"incrementing.column.name\": \""+dataWrite.getIncrementingColumnName().toUpperCase()+"\",\r\n" + 
						"        \"mode\": \"incrementing"+"\",\r\n" + 
						"        \"topic.prefix\": \""+"\",\r\n" + 
						"        \"connection.url\": \""+dataWrite.getSourceConnectionUrl()+"\",\r\n" + 
						"        \"connection.user\": \""+dataWrite.getSourceConnectionUser()+"\",\r\n" + 
						"        \"connection.password\": \""+dataWrite.getSourceConnectionPasswd()+"\"\r\n" + 
						"    }\r\n" + 
						"}";
			}else if (dataWrite.getSourceTaskType()==1) {
				//时间戳
				params = "{\r\n" + 
						"	\"name\": \""+dataWrite.getName()+"\",\r\n" + 
						"    \"config\": {\r\n" + 
						"        \"connector.class\": \""+this.sourceConnectorClass+"\",\r\n" + 
						"        \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
						"        \"table.whitelist\": \""+dataWrite.getSourceTableName()+"\",\r\n" + 
						"        \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
						"        \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
						"        \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
						"        \"timestamp.column.name\": \""+dataWrite.getTimestampColumnName().toUpperCase()+"\",\r\n" + 
						"        \"mode\": \"timestamp"+"\",\r\n" + 
						"        \"topic.prefix\": \""+"\",\r\n" + 
						"        \"connection.url\": \""+dataWrite.getSourceConnectionUrl()+"\",\r\n" + 
						"        \"connection.user\": \""+dataWrite.getSourceConnectionUser()+"\",\r\n" + 
						"        \"connection.password\": \""+dataWrite.getSourceConnectionPasswd()+"\"\r\n" + 
						"    }\r\n" + 
						"}";
			}else if (dataWrite.getSourceTaskType()==2) {
				//时间戳+自增列
				params = "{\r\n" + 
						"	\"name\": \""+dataWrite.getName()+"\",\r\n" + 
						"    \"config\": {\r\n" + 
						"        \"connector.class\": \""+this.sourceConnectorClass+"\",\r\n" + 
						"        \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
						"        \"table.whitelist\": \""+dataWrite.getSourceTableName()+"\",\r\n" + 
						"        \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
						"        \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
						"        \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
						"        \"timestamp.column.name\": \""+dataWrite.getTimestampColumnName().toUpperCase()+"\",\r\n" + 
						"        \"incrementing.column.name\": \""+dataWrite.getIncrementingColumnName().toUpperCase()+"\",\r\n" + 
						"        \"mode\": \"timestamp+incrementing"+"\",\r\n" + 
						"        \"topic.prefix\": \""+"\",\r\n" + 
						"        \"connection.url\": \""+dataWrite.getSourceConnectionUrl()+"\",\r\n" + 
						"        \"connection.user\": \""+dataWrite.getSourceConnectionUser()+"\",\r\n" + 
						"        \"connection.password\": \""+dataWrite.getSourceConnectionPasswd()+"\"\r\n" + 
						"    }\r\n" + 
						"}";
			}
		}
		try {
			connection = setConnection(connection, connectorUrl,"POST");
	        connection.getOutputStream().write(params.getBytes("utf-8"));
	        connection.getOutputStream().flush();
	        if (!String.valueOf(connection.getResponseCode()).startsWith("20")) {
	        	resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
	        	return resp;
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        resp = Resp.FAILURE.withPhase(ex.getMessage());
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	resp = Resp.FAILURE.withPhase(ex.getMessage());
	        }
	    }
		return  resp;
	}
	
	/**
	 * 修改任务
	 * 
	 * @param dataWrite
	 * @return
	 */
	public Resp updateConnectors(DataWrite dataWrite) {
		Resp resp = Resp.OK;
		HttpURLConnection connection = null;
		String params ="";
		if (dataWrite.getConnectorType()==0) {
			if (dataWrite.getTaskType()==0) {
				if (dataWrite.getFieldsCompareFilter()==null) {
					dataWrite.setFieldsCompareFilter("");
				}
				if (dataWrite.getTableUseFields()==null) {
					dataWrite.setTableUseFields("");
				}
				String tableName = dataWrite.getTableName();
				//JDBC类型
				//如果分表
				if (dataWrite.getIsSplit()==0) {
					//按年
					if (dataWrite.getPivotFieldType()==0) {
						tableName = tableName+Constant.pivotFormatYear;//表名+{yyyy}
					}
					//按月
					if (dataWrite.getPivotFieldType()==1) {
						tableName = tableName+Constant.pivotFormatMonth;//表名+{yyyyMM}
					}
					//按日
					if (dataWrite.getPivotFieldType()==2) {
						tableName = tableName+Constant.pivotFormatDay;//表名+{yyyyMMdd}
					}
					params ="{\r\n" + 
							"   \"connector.class\": \""+this.connectorClass+"\",\r\n" + 
							"   \"table.name.format\": \""+tableName+"\",\r\n" + 
							"   \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
							"   \"connection.password\": \""+dataWrite.getConnectionPasswd()+"\",\r\n" + 
							"   \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
							"   \"topics\": \""+dataWrite.getTopics()+"\",\r\n" + 
							"   \"connection.user\": \""+dataWrite.getConnectionUser()+"\",\r\n" + 
							"   \"max.retries\": \""+this.maxRetries+"\",\r\n" + 
							"   \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
							"   \"connection.url\": \""+dataWrite.getConnectionUrl()+"\",\r\n" + 
							"   \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
							"   \"metrix.reporter.url\": \""+this.metrixReporterUrl+"\",\r\n" + 
							"   \"filter.name.class\": \""+dataWrite.getFieldsCompareFilter()+"\",\r\n" + 
							"   \"table.use.fields\": \""+dataWrite.getTableUseFields()+"\",\r\n" + 
							"   \"pivot.field\": \""+dataWrite.getPivotField().toUpperCase()+"\",\r\n" + 
							"   \"table.name.generator.class\": \""+this.tableNameGeneratorClass+"\"\r\n" + 
							"}";
				}else {
					//如果不分表，传空字符串
					dataWrite.setPivotField("");
					params ="{\r\n" + 
							"   \"connector.class\": \""+this.connectorClass+"\",\r\n" + 
							"   \"table.name.format\": \""+tableName+"\",\r\n" + 
							"   \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
							"   \"connection.password\": \""+dataWrite.getConnectionPasswd()+"\",\r\n" + 
							"   \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
							"   \"topics\": \""+dataWrite.getTopics()+"\",\r\n" + 
							"   \"connection.user\": \""+dataWrite.getConnectionUser()+"\",\r\n" + 
							"   \"max.retries\": \""+this.maxRetries+"\",\r\n" + 
							"   \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
							"   \"connection.url\": \""+dataWrite.getConnectionUrl()+"\",\r\n" + 
							"   \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
							"   \"metrix.reporter.url\": \""+this.metrixReporterUrl+"\",\r\n" + 
							"   \"filter.name.class\": \""+dataWrite.getFieldsCompareFilter()+"\",\r\n" + 
							"   \"table.use.fields\": \""+dataWrite.getTableUseFields()+"\",\r\n" + 
							"   \"pivot.field\": \""+dataWrite.getPivotField().toUpperCase()+"\"\r\n" + 
							"}";
				}
					params ="{\r\n" + 
							"   \"connector.class\": \""+this.connectorClass+"\",\r\n" + 
							"   \"table.name.format\": \""+tableName+"\",\r\n" + 
							"   \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
							"   \"connection.password\": \""+dataWrite.getConnectionPasswd()+"\",\r\n" + 
							"   \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
							"   \"topics\": \""+dataWrite.getTopics()+"\",\r\n" + 
							"   \"connection.user\": \""+dataWrite.getConnectionUser()+"\",\r\n" + 
							"   \"max.retries\": \""+this.maxRetries+"\",\r\n" + 
							"   \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
							"   \"connection.url\": \""+dataWrite.getConnectionUrl()+"\",\r\n" + 
							"   \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
							"   \"metrix.reporter.url\": \""+this.metrixReporterUrl+"\",\r\n" + 
							"   \"filter.name.class\": \""+dataWrite.getFieldsCompareFilter()+"\",\r\n" + 
							"   \"table.use.fields\": \""+dataWrite.getTableUseFields()+"\",\r\n" + 
							"   \"pivot.field\": \""+dataWrite.getPivotField().toUpperCase()+"\",\r\n" + 
							"   \"table.name.generator.class\": \""+this.tableNameGeneratorClass+"\"\r\n" + 
							"}";
			}else if(dataWrite.getTaskType()==1) {
				//HDFS
			}
		}else if (dataWrite.getConnectorType()==1) {
			if (dataWrite.getSourceTaskType()==0) {
				//自增列
				params = "{\r\n" + 
						"   \"connector.class\": \""+this.sourceConnectorClass+"\",\r\n" + 
						"   \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
						"   \"table.whitelist\": \""+dataWrite.getSourceTableName()+"\",\r\n" + 
						"   \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
						"   \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
						"   \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
						"   \"incrementing.column.name\": \""+dataWrite.getIncrementingColumnName().toUpperCase()+"\",\r\n" + 
						"   \"mode\": \"incrementing"+"\",\r\n" + 
						"   \"topic.prefix\": \""+"\",\r\n" + 
						"   \"connection.url\": \""+dataWrite.getSourceConnectionUrl()+"\",\r\n" + 
						"   \"connection.user\": \""+dataWrite.getSourceConnectionUser()+"\",\r\n" + 
						"   \"connection.password\": \""+dataWrite.getSourceConnectionPasswd()+"\"\r\n" + 
						"}";
			}else if (dataWrite.getSourceTaskType()==1) {
				//时间戳
				params = "{\r\n" + 
						"   \"connector.class\": \""+this.sourceConnectorClass+"\",\r\n" + 
						"   \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
						"   \"table.whitelist\": \""+dataWrite.getSourceTableName()+"\",\r\n" + 
						"   \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
						"   \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
						"   \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
						"   \"timestamp.column.name\": \""+dataWrite.getTimestampColumnName().toUpperCase()+"\",\r\n" + 
						"   \"mode\": \"timestamp"+"\",\r\n" + 
						"   \"topic.prefix\": \""+"\",\r\n" + 
						"   \"connection.url\": \""+dataWrite.getSourceConnectionUrl()+"\",\r\n" + 
						"   \"connection.user\": \""+dataWrite.getSourceConnectionUser()+"\",\r\n" + 
						"   \"connection.password\": \""+dataWrite.getSourceConnectionPasswd()+"\"\r\n" + 
						"}";
			}else if (dataWrite.getSourceTaskType()==2) {
				//时间戳+自增列
				params = "{\r\n" + 
						"   \"connector.class\": \""+this.sourceConnectorClass+"\",\r\n" + 
						"   \"tasks.max\": \""+this.tasksMax+"\",\r\n" + 
						"   \"table.whitelist\": \""+dataWrite.getSourceTableName()+"\",\r\n" + 
						"   \"key.converter\": \""+this.keyConverter+"\",\r\n" + 
						"   \"value.converter\": \""+this.valueConverter+"\",\r\n" + 
						"   \"value.converter.schema.registry.url\": \""+this.valueConverterSchemaRegistryUrl+"\",\r\n" + 
						"   \"timestamp.column.name\": \""+dataWrite.getTimestampColumnName().toUpperCase()+"\",\r\n" + 
						"   \"incrementing.column.name\": \""+dataWrite.getIncrementingColumnName().toUpperCase()+"\",\r\n" + 
						"   \"mode\": \"timestamp+incrementing"+"\",\r\n" + 
						"   \"topic.prefix\": \""+"\",\r\n" + 
						"   \"connection.url\": \""+dataWrite.getSourceConnectionUrl()+"\",\r\n" + 
						"   \"connection.user\": \""+dataWrite.getSourceConnectionUser()+"\",\r\n" + 
						"   \"connection.password\": \""+dataWrite.getSourceConnectionPasswd()+"\"\r\n" + 
						"}";
			}
		}
		try {
			connection = setConnection(connection, connectorUrl+dataWrite.getName()+"/config","PUT");
	        connection.getOutputStream().write(params.getBytes("utf-8"));
	        connection.getOutputStream().flush();
	        if (!String.valueOf(connection.getResponseCode()).startsWith("20")) {
	        	resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        resp = Resp.FAILURE.withPhase(ex.getMessage());
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	resp = Resp.FAILURE.withPhase(ex.getMessage());
	        }
	    }
		return  resp;
	}
	
	/**
	 * 删除任务
	 * 
	 * @param name
	 * @return
	 */
	public Resp deleteConnectors(String name) {
		Resp resp = Resp.OK;
		HttpURLConnection connection = null;
		try {
			connection = setConnection(connection, connectorUrl+name,"DELETE");
	        if (!String.valueOf(connection.getResponseCode()).startsWith("20")) {
	        	resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        resp = Resp.FAILURE.withPhase(ex.getMessage());
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	resp = Resp.FAILURE.withPhase(ex.getMessage());
	        }
	    }
		return  resp;
	}
	
	/**
	 * 停止任务
	 * 
	 * @param name
	 * @return
	 */
	public Resp pauseConnectors(String name) {
		Resp resp = Resp.OK;
		HttpURLConnection connection = null;
		try {
			connection = setConnection(connection, connectorUrl+name+"/pause","PUT");
	        if (!String.valueOf(connection.getResponseCode()).startsWith("20")) {
	        	resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        resp = Resp.FAILURE.withPhase(ex.getMessage());
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	resp = Resp.FAILURE.withPhase(ex.getMessage());
	        }
	    }
		return  resp;
	}
	
	/**
	 * 恢复任务
	 * 
	 * @param name
	 * @return
	 */
	public Resp resumeConnectors(String name) {
		Resp resp = Resp.OK;
		HttpURLConnection connection = null;
		try {
			connection = setConnection(connection, connectorUrl+name+"/resume","PUT");
	        if (!String.valueOf(connection.getResponseCode()).startsWith("20")) {
	        	resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        resp = Resp.FAILURE.withPhase(ex.getMessage());
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	resp = Resp.FAILURE.withPhase(ex.getMessage());
	        }
	    }
		return  resp;
	}
	
	/**
	 * 重启任务
	 * 
	 * @param name
	 * @return
	 */
	public Resp restartConnectors(String name) {
		Resp resp = Resp.OK;
		HttpURLConnection connection = null;
		try {
			connection = setConnection(connection, connectorUrl+name+"/status","GET");
	        if (connection.getResponseCode() == 200) {
	        	resp = doRestartConnect(connection,name);
	    	    
	        }else if (connection.getResponseCode() == 404) {
	        	resp = Resp.FAILURE.withPhase("任务不存在，已经被删除");
			}else {
				resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
			}
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        resp = Resp.FAILURE.withPhase(ex.getMessage());
	    } finally {
	        try {
	            connection.disconnect();
	        } catch (Exception ex) {
	        	resp = Resp.FAILURE.withPhase(ex.getMessage());
	        }
	    }
		return  resp;
	}
	
	private HttpURLConnection setConnection(HttpURLConnection connection,String url,String method) throws Exception {
		URL httpUrl = new URL(url);
        connection = (HttpURLConnection) httpUrl.openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        return connection;
	}
	
	/**
	 * 重启任务
	 * 
	 * @param connection
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unused")
	private Resp doRestartConnect(HttpURLConnection connection,String name) throws Exception {
		Resp resp = Resp.OK;
		byte[] resultBytes = BaseHttpServiceUtils.readStream(connection.getInputStream());
	    String json = new String(resultBytes,"utf-8");
		connection = setConnection(connection, connectorUrl+name+"/restart","POST");
        if (connection.getResponseCode() == 200||connection.getResponseCode() == 204) {
        	connection = setConnection(connection, connectorUrl+name+"/status","GET");
	        if (connection.getResponseCode() == 200) {
	    	    resultBytes = BaseHttpServiceUtils.readStream(connection.getInputStream());
	    	    json = new String(resultBytes,"utf-8");
	    	    String connector = JSONObject.parseObject(JSONObject.parseObject(json).getString("connector")).getString("state");
	    	    if (connector.equals("RUNNING")) {
	    	    	JSONArray jsonArray = JSONArray.parseArray(JSONObject.parseObject(json).getString("tasks"));
					if(jsonArray.size()>0){
					  for(int i=0;i<jsonArray.size();i++){
						  JSONObject jsonObject = jsonArray.getJSONObject(i);
						  String state=jsonObject.getString("state");
						  String idString=jsonObject.getString("id");
						  if (state.equals("FAILED")) {
							  connection = setConnection(connection, connectorUrl+name+"/tasks/"+idString+"/restart","POST");
						        if (!String.valueOf(connection.getResponseCode()).startsWith("20")) {
						        	resp = Resp.FAILURE.withPhase("提示：错误码为"+connection.getResponseCode());
						        	return resp;
						        }
						    }
					     }
				      }
				  }
	         }
        }
        return resp;
	}
}
