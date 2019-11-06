package com.leatop.bee.management.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leatop.bee.management.conf.ConnectorsConfiguration;
import com.leatop.bee.management.conf.KafkaConfiguration;
import com.leatop.bee.management.page.Pagination;
import com.leatop.bee.management.po.DataWrite;
import com.leatop.bee.management.resp.Resp;
import com.leatop.bee.management.service.DataWriteService;
import com.leatop.bee.management.util.JDBCUtils;
import com.leatop.bee.management.util.KafkaUtils;


/**
* @ClassName: ConnectorController.java
* @Description: TODO(kafka任务管理配置Controller类)
* @author hongSheng
* @date 2019年5月22日
*
*/
@Controller
@RequestMapping("/datawritesource")
public class DataWriteSourceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataWriteSourceController.class);
	
	private final DataWriteService dataWriteService;
	private final ConnectorsConfiguration connectorsConfiguration;
	private final KafkaConfiguration kafkaConfiguration;

	@Autowired
	public DataWriteSourceController(final DataWriteService dataWriteService,
			final ConnectorsConfiguration connectorsConfiguration,final KafkaConfiguration kafkaConfiguration) {
		this.dataWriteService = dataWriteService;
		this.connectorsConfiguration = connectorsConfiguration;
		this.kafkaConfiguration = kafkaConfiguration;
	}
	
	/**
	 * 获取kafka主题
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getTopics")
	public List<String> getTopics(){
		List<String> topics = KafkaUtils.getAllTopics(kafkaConfiguration.getZookeeperHost(), 
				kafkaConfiguration.getSessionTimeout(), kafkaConfiguration.getConnectTimeout());
		return topics;
	}
	
	/**
	 * 任务配置列表展示界面
	 * 
	 * @return
	 */
	@RequestMapping("/dataList")
	public String dataSourceList() {
		return "datawrite-source-list";
	}
	
	/**
	 * 任务配置数据加载查询
	 * 
	 * @param page
	 * @param limit
	 * @return
	 */
	@ResponseBody
	@GetMapping("/pageList")
	public Pagination<DataWrite> dataWritePageList(final int page, final int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		params.put("limit", limit);
		params.put("connectorType", 1);
		List<DataWrite> dataWrites = dataWriteService.queryDataWrite(params);
		
		Pagination<DataWrite> res = new Pagination<>();
		res.setCode(0);
		res.setCount(dataWriteService.countDataWrite(params));
		res.setData(dataWrites);
		LOGGER.info(connectorsConfiguration.toString());
		
		return res;
	}
	
	/**
	 * 任务配置数据添加界面
	 * 
	 * @return
	 */
	@RequestMapping("/addPage")
	public ModelAndView dataWriteAddPage() {
		ModelAndView model = new ModelAndView("datawrite-source-add");
		List<String> topicsList = KafkaUtils.getAllTopics(kafkaConfiguration.getZookeeperHost(), 
				kafkaConfiguration.getSessionTimeout(), kafkaConfiguration.getConnectTimeout());
		model.addObject("topicsList", topicsList);
		Map<String, String> fieldsCompareFilter = new HashMap<>();
		fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter", "过滤111");
		fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter222", "过滤222");
		model.addObject("fieldsCompareFilter",fieldsCompareFilter);
		return model;
	}
	
	/**
	 * 任务配置数据添加
	 * 
	 * @param dataWrite
	 * @return
	 */
	@ResponseBody
	@PostMapping("/add")
	public Resp dataWriteAdd(DataWrite dataWrite) {
		//验证数据库信息配置
		Resp resp = JDBCUtils.testDataSource(dataWrite);
		if (resp.getStatusCode() == Resp.FAILURE.getStatusCode()) {
			return resp;
		}
		if(connectorsConfiguration.isExitConnectors(dataWrite.getName())!=404) {
			if (connectorsConfiguration.isExitConnectors(dataWrite.getName())==900) {
				return Resp.FAILURE.withPhase("connector连接异常!");
			}else {
				return Resp.FAILURE.withPhase("该任务已经存在!");
			}
		}
		try {
			dataWriteService.addDataWrite(dataWrite);
			JDBCUtils.dataBaseIsChange = true;//当为true时，kafka-connectors配置信息重新读取，重新写入数据库
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		return resp;

	}
	
	/**
	 * 任务配置数据删除
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@ResponseBody
	@PostMapping("/delete")
	public Resp dataWriteDelete(String ids) {
		
		Resp resp = Resp.OK;
		try {
			if (ids != null) {
				String[] idAttr = ids.split(",");
				for(String id : idAttr) {
//					TopicEntity topicEntity = topicService.queryById(id);
					//调用kafka javaAPI删除主题
					resp = connectorsConfiguration.deleteConnectors(dataWriteService.getDataWrite(Integer.valueOf(id)).getName());
					if (resp.getStatusCode() == Resp.OK.getStatusCode()||"提示：错误码为404".equals(resp.getPhase())) {
						dataWriteService.deleteDataWrite(Integer.valueOf(id));
						resp = Resp.OK;
						//JDBCUtils.dataBaseIsChange = true;//当为true时，kafka-connectors配置信息重新读取，重新写入数据库
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		return resp;
	}
	
	/**
	 * 更新任务状态
	 * 
	 * @param id
	 * @param status：0为恢复，1为停止
	 * @param name
	 * @return
	 */
	@ResponseBody
	@PostMapping("/updateStatus")
	public Resp dataWriteupdateStatus(int id,int status,String name) {
		Resp resp = Resp.OK;
		if(connectorsConfiguration.isExitConnectors(name)==404) {
			resp = connectorsConfiguration.addConnectors(dataWriteService.getDataWrite(id));
		}
		if (status==0&&resp.getStatusCode()==Resp.OK.getStatusCode()) {
			resp = connectorsConfiguration.resumeConnectors(name);
		}else if (status==1&&resp.getStatusCode()==Resp.OK.getStatusCode()) {
			resp = connectorsConfiguration.pauseConnectors(name);
		}
		LOGGER.info("delete connectors id: "+id+", name:"+name);
		if (resp.getStatusCode() == Resp.OK.getStatusCode()) {
			Map<String, Object> params = new HashMap<>();
			params.put("status", status);
			params.put("id", id);
			dataWriteService.updateStatus(params);
			JDBCUtils.dataBaseIsChange = true;//当为true时，kafka-connectors配置信息重新读取，重新写入数据库
			LOGGER.info("update kafka-connectors success");
		}else {
			LOGGER.info("update kafka-connectors failed ");
		}
		return resp;
	}
	
	/**
	 * 重启任务
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@ResponseBody
	@PostMapping("/restartConnectors")
	public Resp restartConnectors(int id,String name) {
		Resp resp = Resp.OK;
		try {
			resp = connectorsConfiguration.restartConnectors(name);
			if (resp.getStatusCode() == Resp.OK.getStatusCode()) {
				resp = connectorsConfiguration.resumeConnectors(name);
			}
			Map<String, Object> params = new HashMap<>();
			params.put("status", 0);
			params.put("id", id);
			dataWriteService.updateStatus(params);
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		return resp;
	}
	
	/**
	 * 重建任务（任务删除，再以原来任务的配置信息创建任务）
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@ResponseBody
	@PostMapping("/recreateConnectors")
	public Resp recreateConnectors(int id,String name) {
		Resp resp = Resp.OK;
		try {
			if(connectorsConfiguration.isExitConnectors(name)==404) {
				connectorsConfiguration.addConnectors(dataWriteService.getDataWrite(id));
			}else {
				connectorsConfiguration.deleteConnectors(name);
				connectorsConfiguration.addConnectors(dataWriteService.getDataWrite(id));
			}
			Map<String, Object> params = new HashMap<>();
			params.put("status", 0);
			params.put("id", id);
			dataWriteService.updateStatus(params);
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		return resp;
	}
	
	/**
	 * 获取任务状态
	 * 
	 * @param name
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getConnectorsStatus")
	public Resp getConnectorsStatus(String name) {
		Resp resp = connectorsConfiguration.getConnectorsStatus(name);
		return resp;
	}
	
	/**
	 * 任务配置修改界面
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/editPage")
	public ModelAndView dataWriteEditPage(int id,String name) {
		ModelAndView model = new ModelAndView("datawrite-source-edit");
		DataWrite dataWrite =dataWriteService.getDataWrite(id);
		List<String> topicsList = KafkaUtils.getAllTopics(kafkaConfiguration.getZookeeperHost(), 
				kafkaConfiguration.getSessionTimeout(), kafkaConfiguration.getConnectTimeout());
		model.addObject("topicsList", topicsList);
		LOGGER.info("get connectors id: "+id+", name:"+name);
		if(dataWrite!=null) {
			List<String> tables = getTables(dataWrite);
			model.addObject("tables",tables);
			List<String> timestampColumns = getColumns(dataWrite,"1");
			model.addObject("timestampColumns",timestampColumns);
			List<String> incrementingColumns = getColumns(dataWrite,"2");
			model.addObject("incrementingColumns",incrementingColumns);
			Map<String, String> fieldsCompareFilter = new HashMap<>();
			fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter", "过滤111");
			fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter222", "过滤222");
			model.addObject("fieldsCompareFilter",fieldsCompareFilter);
			LOGGER.info("create kafka-connectors success");
		}else {
			LOGGER.info("create kafka-connectors failed ");
		}
		model.addObject("dataWrite",dataWrite);
		return model;
	}
	
	/**
	 * 任务配置修改
	 * 
	 * @param dataWrite
	 * @return
	 */
	@ResponseBody
	@PostMapping("/edit")
	public Resp dataWriteEditPage(DataWrite dataWrite) {
		Resp resp = Resp.OK;
		try {
			//验证数据库信息配置
			resp = JDBCUtils.testDataSource(dataWrite);
			if (resp.getStatusCode() == Resp.FAILURE.getStatusCode()) {
				return resp;
			}
			resp = connectorsConfiguration.updateConnectors(dataWrite);
			LOGGER.info(dataWrite.toString());
			if(resp.getStatusCode() == Resp.OK.getStatusCode()) {
				dataWriteService.updateDataWrite(dataWrite);
				//JDBCUtils.dataBaseIsChange = true;//当为true时，kafka-connectors配置信息重新读取，重新写入数据库
				LOGGER.info("create kafka-connectors success");
			}else {
				LOGGER.info("create kafka-connectors failed ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		
		return resp;
	}
	
	/**
	 * 任务配置查看界面
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/view")
	public ModelAndView dataWriteView(int id,String name) {
		ModelAndView model = new ModelAndView("datawrite-source-view");
		DataWrite dataWrite =dataWriteService.getDataWrite(id);
		LOGGER.info("get connectors id: "+id+", name:"+name);
		if(dataWrite!=null) {
			LOGGER.info("create kafka-connectors success");
		}else {
			LOGGER.info("create kafka-connectors failed ");
		}
		model.addObject("dataWrite",dataWrite);
		Resp resp = connectorsConfiguration.getConnectorsStatus(dataWrite.getName());
		model.addObject("statusCode",resp.getStatusCode());
		model.addObject("phase",resp.getPhase());
		return model;
	}
	
	/**
	 * 获取数据库表
	 * 
	 * @param dataWrite
	 * @return
	 */
	@ResponseBody
	@PostMapping("/tables")
	public List<String> dataWriteTables(DataWrite dataWrite) {
		List<String> tables = new ArrayList<>();
		tables=getTables(dataWrite);
		
		return tables;
	}
	
	
	/**
	 * 获取数据库表
	 * 
	 * @param dataWrite
	 * @return
	 */
	@ResponseBody
	@PostMapping("/columns")
	public List<String> dataWriteColumns(DataWrite dataWrite,String type) {
		List<String> columns = new ArrayList<>();
		columns=getColumns(dataWrite,type);
		
		return columns;
	}
	
	public void formatConnectionInfo(DataWrite dataWrite,String driverClassName,String url,String password,String user) {
		if (dataWrite.getConnectorType()==0) {
			if (dataWrite.getConnectionUrl().indexOf("jdbc:mysql")!=-1) {
				driverClassName = "com.mysql.jdbc.Driver";
			}else if (dataWrite.getConnectionUrl().indexOf("jdbc:sqlserver")!=-1) {
				driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			}else if (dataWrite.getConnectionUrl().indexOf("jdbc:oracle")!=-1) {
				driverClassName = "oracle.jdbc.driver.OracleDriver";
			}else {
			}
			url = dataWrite.getConnectionUrl();
		    password = dataWrite.getConnectionPasswd();
		    user= dataWrite.getConnectionUser();
		}else if (dataWrite.getConnectorType()==1) {
			if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:mysql")!=-1) {
				driverClassName = "com.mysql.jdbc.Driver";
			}else if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:sqlserver")!=-1) {
				driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			}else if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:oracle")!=-1) {
				driverClassName = "oracle.jdbc.driver.OracleDriver";
			}else {
			}
			url = dataWrite.getSourceConnectionUrl();
		    password = dataWrite.getSourceConnectionPasswd();
		    user= dataWrite.getSourceConnectionUser();
		}
	}
	
	public List<String> getTables(DataWrite dataWrite){
		List<String> tables = new ArrayList<>();
		//验证数据库信息配置
				String driverClassName = "";
				String url = "";
			    String password = "";
			    String user= "";
			  //任务类型为sink
			    if (dataWrite.getConnectorType()==0) {
					if (dataWrite.getConnectionUrl().indexOf("jdbc:mysql")!=-1) {
						driverClassName = "com.mysql.jdbc.Driver";
					}else if (dataWrite.getConnectionUrl().indexOf("jdbc:sqlserver")!=-1) {
						driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
					}else if (dataWrite.getConnectionUrl().indexOf("jdbc:oracle")!=-1) {
						driverClassName = "oracle.jdbc.driver.OracleDriver";
					}else {
					}
					url = dataWrite.getConnectionUrl();
				    password = dataWrite.getConnectionPasswd();
				    user= dataWrite.getConnectionUser();
				}else if (dataWrite.getConnectorType()==1) {
					if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:mysql")!=-1) {
						driverClassName = "com.mysql.jdbc.Driver";
					}else if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:sqlserver")!=-1) {
						driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
					}else if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:oracle")!=-1) {
						driverClassName = "oracle.jdbc.driver.OracleDriver";
					}else {
					}
					url = dataWrite.getSourceConnectionUrl();
				    password = dataWrite.getSourceConnectionPasswd();
				    user= dataWrite.getSourceConnectionUser();
				}
		       try {
		           Class.forName(driverClassName);
		       } catch (ClassNotFoundException ex) {
		       		return tables;
		       }
		       Connection conn = null;
		       try {
		       	 conn = DriverManager.getConnection(url, user, password);
		         DatabaseMetaData dbMetaData = conn.getMetaData();
		         ResultSet rs = dbMetaData.getTables(null, null, null,new String[] { "TABLE" });
		         while (rs.next()) {// ///TABLE_TYPE/REMARKS
		        	 if (rs.getString(2).equalsIgnoreCase(dataWrite.getSourceConnectionUser())) {
		        		 if (!zzbd(rs.getString(3))) {
		        			 tables.add(rs.getString(3));
						}
					}
		         }
		       } catch (Exception ex1) {
		       } finally {
		       	if (conn != null) {
		       		try {
							conn.close();
						} catch (SQLException e) {
						}
		       		}
		       }
		       return tables;
	}
	
	public List<String> getColumns(DataWrite dataWrite,String type){
		List<String> columns = new ArrayList<>();
		//验证数据库信息配置
		String driverClassName = "";
		String url = "";
	    String password = "";
	    String user= "";
	    if (dataWrite.getConnectorType()==0) {
			if (dataWrite.getConnectionUrl().indexOf("jdbc:mysql")!=-1) {
				driverClassName = "com.mysql.jdbc.Driver";
			}else if (dataWrite.getConnectionUrl().indexOf("jdbc:sqlserver")!=-1) {
				driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			}else if (dataWrite.getConnectionUrl().indexOf("jdbc:oracle")!=-1) {
				driverClassName = "oracle.jdbc.driver.OracleDriver";
			}else {
			}
			url = dataWrite.getConnectionUrl();
		    password = dataWrite.getConnectionPasswd();
		    user= dataWrite.getConnectionUser();
		}else if (dataWrite.getConnectorType()==1) {
			if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:mysql")!=-1) {
				driverClassName = "com.mysql.jdbc.Driver";
			}else if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:sqlserver")!=-1) {
				driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			}else if (dataWrite.getSourceConnectionUrl().indexOf("jdbc:oracle")!=-1) {
				driverClassName = "oracle.jdbc.driver.OracleDriver";
			}else {
			}
			url = dataWrite.getSourceConnectionUrl();
		    password = dataWrite.getSourceConnectionPasswd();
		    user= dataWrite.getSourceConnectionUser();
		}
       try {
           Class.forName(driverClassName);
       } catch (ClassNotFoundException ex) {
       		return columns;
       }
       Connection conn = null;
       try {
       	 conn = DriverManager.getConnection(url, user, password);
         String sql = "select * from " + dataWrite.getSourceTableName();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();
         ResultSetMetaData meta = rs.getMetaData();
         int columeCount = meta.getColumnCount();
         for (int i = 1; i < columeCount + 1; i++) {
        	if (type!=null && type.equals("1")) {
				if (meta.getColumnTypeName(i).toUpperCase().indexOf("TIME")!=-1
						||meta.getColumnTypeName(i).toUpperCase().indexOf("DATE")!=-1) {
					columns.add(meta.getColumnName(i));
				}
			}else if (type!=null && type.equals("2")) {
				if (meta.getColumnTypeName(i).toUpperCase().indexOf("INT")!=-1
						||meta.getColumnTypeName(i).toUpperCase().indexOf("NUM")!=-1) {
					columns.add(meta.getColumnName(i));
				}
			}else {
				columns.add(meta.getColumnName(i));
			}
         }
       } catch (Exception ex1) {
       } finally {
       	if (conn != null) {
       		try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
       		}
       }
		
		return columns;
	}

	public static boolean zzbd(String s){
		Pattern pattern = Pattern.compile(".*\\d{4}$|.*\\d{4}\\d{2}$|.*\\d{4}\\d{2}\\d{2}$");
		Matcher matcher = pattern.matcher(s);
		if (matcher.find())
		{
			return true;
		}else {
			return false;
		}
	}
}
