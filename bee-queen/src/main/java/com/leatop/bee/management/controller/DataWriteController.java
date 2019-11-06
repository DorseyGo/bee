package com.leatop.bee.management.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
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
@RequestMapping("/datawrite")
public class DataWriteController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataWriteController.class);
	
	private final DataWriteService dataWriteService;
	private final ConnectorsConfiguration connectorsConfiguration;
	private final KafkaConfiguration kafkaConfiguration;

	@Autowired
	public DataWriteController(final DataWriteService dataWriteService,
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
	public String dataWriteList() {
		return "datawrite-list";
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
		params.put("connectorType", 0);
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
		ModelAndView model = new ModelAndView("datawrite-add");
		List<String> topicsList = KafkaUtils.getAllTopics(kafkaConfiguration.getZookeeperHost(), 
				kafkaConfiguration.getSessionTimeout(), kafkaConfiguration.getConnectTimeout());
		model.addObject("topicsList", topicsList);
		Map<String, String> fieldsCompareFilter = new HashMap<>();
		fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter", "ETC门架交易流水过滤测试");
		fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter2", "出口流水过滤测试");
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
			//设置筛选字段
			dataWrite = setColumnsAndTypes(dataWrite);
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
		ModelAndView model = new ModelAndView("datawrite-edit");
		DataWrite dataWrite =dataWriteService.getDataWrite(id);
		
		LOGGER.info("get connectors id: "+id+", name:"+name);
		if(dataWrite!=null) {
			List<String> tables = getTables(dataWrite);
			model.addObject("tables",tables);
			List<String> columns = getColumns(dataWrite,null);
			model.addObject("columns",columns);
			List<String> pivotFieldColumns = getColumns(dataWrite,"1");
			model.addObject("pivotFieldColumns",pivotFieldColumns);
			Map<String, String> fieldsCompareFilter = new HashMap<>();
			fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter", "ETC门架交易流水过滤测试");
			fieldsCompareFilter.put("com.leatop.bee.data.weaver.sink.jdbc.filter.FieldsCompareFilter222", "出口流水过滤测试");
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
		ModelAndView model = new ModelAndView("datawrite-view");
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
		  //任务类型为source
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
	
	public static void main(String[] args) {
		System.out.println(zzbd("dsf2019"));
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
				  //任务类型为source
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
		        	 if (rs.getString(2).equalsIgnoreCase(dataWrite.getConnectionUser())) {
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
		  //任务类型为source
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
         String sql = "select * from " + dataWrite.getTableName();
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
				String schemaName = meta.getSchemaName(i);
				String ColumnClassName = meta.getColumnClassName(i);
				String ColumnLabel = meta.getColumnLabel(i);
				int ColumnType = meta.getColumnType(i);
				String ColumnTypeName = meta.getColumnTypeName(i);
				String ColumnName = meta.getColumnName(i);
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
	
	
	public DataWrite setColumnsAndTypes(DataWrite dataWrite){
		//验证数据库信息配置
		String driverClassName = "";
		String url = "";
	    String password = "";
	    String user= "";
	    if(dataWrite!=null) {
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
			  //任务类型为source
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
		    Connection conn = null;
	       try {
	         Class.forName(driverClassName);
	       	 conn = DriverManager.getConnection(url, user, password);
	         String sql = "select * from " + dataWrite.getTableName();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery();
	         ResultSetMetaData meta = rs.getMetaData();
	         int columeCount = meta.getColumnCount();
	         if(dataWrite.getTableUseFields().trim().isEmpty()) {
	        	 String useFields = "";
	        	 String useFieldsType = "";
	        	 for (int i = 1; i < columeCount + 1; i++) {
	 				String schemaName = meta.getSchemaName(i);
	 				System.out.println(schemaName);
	 				String ColumnClassName = meta.getColumnClassName(i);
	 				String ColumnLabel = meta.getColumnLabel(i);
	 				int ColumnType = meta.getColumnType(i);
	 				int scale = meta.getScale(i);
	 				int precision = meta.getPrecision(i);
	 				boolean isSigned = meta.isSigned(i);
	 				String ColumnTypeName = meta.getColumnTypeName(i);
	 				String ColumnName = meta.getColumnName(i);
	 				String schemaType = getSchemaTypeFromField(precision,scale,isSigned,ColumnType,false);
	        		 	//String[] columnClassNames = meta.getColumnClassName(i).split("\\.");
	        		 	if(i==columeCount) {
	        		 		useFields = useFields + meta.getColumnName(i);
	        		 		//useFieldsType = useFieldsType + columnClassNames[columnClassNames.length-1];
	        		 		useFieldsType = useFieldsType + schemaType;
	        		 	}else {
	        		 		useFields = useFields + meta.getColumnName(i)+",";
	        		 		//useFieldsType = useFieldsType + columnClassNames[columnClassNames.length-1]+",";
	        		 		useFieldsType = useFieldsType + schemaType+",";
	        		 	}
		         }
	        	 dataWrite.setTableUseFields(useFields);
	        	 dataWrite.setTableUseFieldsType(useFieldsType);
	         }else {
	        	 String[] useFields = dataWrite.getTableUseFields().trim().split(",");
	        	 String useFieldsType = "";
	        	 for(int j=0;j<useFields.length;j++) {
	        		 for (int i = 1; i < columeCount + 1; i++) {
	        			    String[] columnClassNames = meta.getColumnClassName(i).split("\\.");
	        			 	if(useFields[j].equals(meta.getColumnName(i))&&j==useFields.length-1) {
	        			 		useFieldsType = useFieldsType + columnClassNames[columnClassNames.length-1];
	        			 		break;
	        			 	}
	        			 	if(useFields[j].equals(meta.getColumnName(i))&&j!=useFields.length-1) {
	        			 		useFieldsType = useFieldsType + columnClassNames[columnClassNames.length-1];
	        			 		break;
	        			 	}
			         }
	        	 }
	        	 dataWrite.setTableUseFieldsType(useFieldsType);
	        	 
	         }
	         
	       } catch (Exception ex1) {
	    	   ex1.printStackTrace();
	       } finally {
	       	if (conn != null) {
	       		try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
	       		}
	       }
	    }
	    return dataWrite;
	}
	
	 /**
	   * Use the supplied {@link SchemaBuilder} to add a field that corresponds to the column with the
	   * specified definition. This is intended to be easily overridden by subclasses.
	   *
	   * @param columnDefn the definition of the column; may not be null
	   * @param builder    the schema builder; may not be null
	   * @param sqlType    the JDBC {@link java.sql.Types type} as obtained from the column definition
	   * @param optional   true if the field is to be optional as obtained from the column definition
	   * @return the name of the field, or null if no field was added
	   */
	
	//protected static final int NUMERIC_TYPE_SCALE_LOW = -84;
	//protected static final int NUMERIC_TYPE_SCALE_HIGH = 127;
	//protected static final int NUMERIC_TYPE_SCALE_UNSET = -127;
	protected String getSchemaTypeFromField(int precision,int scale, boolean isSigned,
			final int sqlType, final boolean optional) {
		String result= null;
		switch (sqlType) {
		case Types.NULL: {
			return null;
		}

		case Types.BOOLEAN: {
			result = optional ? "OPTIONAL_BOOLEAN_SCHEMA" : "BOOLEAN_SCHEMA";
			break;
		}

		// ints <= 8 bits
		case Types.BIT: {
			result = optional ? "OPTIONAL_INT8_SCHEMA" : "INT8_SCHEMA";
			break;
		}

		case Types.TINYINT: {
			if (isSigned) {
				result = optional ? "OPTIONAL_INT8_SCHEMA" : "INT8_SCHEMA";
			} else {
				result = optional ? "OPTIONAL_INT16_SCHEMA" : "INT16_SCHEMA";
			}
			break;
		}

		// 16 bit ints
		case Types.SMALLINT: {
			if (isSigned) {
				result = optional ? "OPTIONAL_INT16_SCHEMA" : "INT16_SCHEMA";
			} else {
				result = optional ? "OPTIONAL_INT32_SCHEMA" : "INT32_SCHEMA";
			}
			break;
		}

		// 32 bit ints
		case Types.INTEGER: {
			if (isSigned) {
				result = optional ? "OPTIONAL_INT32_SCHEMA" : "INT32_SCHEMA";
			} else {
				result = optional ? "OPTIONAL_INT64_SCHEMA" : "INT64_SCHEMA";
			}
			break;
		}

		// 64 bit ints
		case Types.BIGINT: {
			result = optional ? "OPTIONAL_INT64_SCHEMA" : "INT64_SCHEMA";
			break;
		}

		// REAL is a single precision floating point value, i.e. a Java float
		case Types.REAL: {
			result = optional ? "OPTIONAL_FLOAT32_SCHEMA" : "FLOAT32_SCHEMA";
			break;
		}

		// FLOAT is, confusingly, double precision and effectively the same as DOUBLE.
		// See REAL
		// for single precision
		case Types.FLOAT:
		case Types.DOUBLE: {
			result = optional ? "OPTIONAL_FLOAT64_SCHEMA" : "FLOAT64_SCHEMA";
			break;
		}

		case Types.NUMERIC:
			if (scale == 0 && precision < 19) { // integer
				if (precision > 9) {
					result = optional ? "OPTIONAL_INT64_SCHEMA" : "INT64_SCHEMA";
				} else if (precision > 4) {
					result = optional ? "OPTIONAL_INT32_SCHEMA" : "INT32_SCHEMA";
				} else if (precision > 2) {
					result = optional ? "OPTIONAL_INT16_SCHEMA" : "INT16_SCHEMA";
				} else {
					result = optional ? "OPTIONAL_INT8_SCHEMA" : "INT8_SCHEMA";
				}
				break;
			}
			if (scale > 0 && precision < 19) { 
				result = optional ? "OPTIONAL_FLOAT64_SCHEMA" : "FLOAT64_SCHEMA";
				break;
			}
			
			/*
			 * if (mapNumerics == NumericMapping.BEST_FIT) {
			 * //log.debug("NUMERIC with precision: '{}' and scale: '{}'",
			 * precision, scale); if (precision < 19) { // fits in primitive
			 * data types. if (scale < 1 && scale >= NUMERIC_TYPE_SCALE_LOW) {
			 * // integer Schema schema; if (precision > 9) { schema =
			 * (optional) ? Schema.OPTIONAL_INT64_SCHEMA : Schema.INT64_SCHEMA;
			 * } else if (precision > 4) { schema = (optional) ?
			 * Schema.OPTIONAL_INT32_SCHEMA : Schema.INT32_SCHEMA; } else if
			 * (precision > 2) { schema = (optional) ?
			 * Schema.OPTIONAL_INT16_SCHEMA : Schema.INT16_SCHEMA; } else {
			 * schema = (optional) ? Schema.OPTIONAL_INT8_SCHEMA :
			 * Schema.INT8_SCHEMA; } builder.field(fieldName, schema); break; }
			 * else if (scale > 0) { // floating point - use double in all cases
			 * Schema schema = (optional) ? Schema.OPTIONAL_FLOAT64_SCHEMA :
			 * Schema.FLOAT64_SCHEMA; builder.field(fieldName, schema); break; }
			 * } }
			 */
			// fallthrough

		case Types.DECIMAL: {
			if(isSigned) {
				result = optional ? "OPTIONAL_FLOAT64_SCHEMA" : "FLOAT64_SCHEMA";
			}
			else {
				result = optional ? "OPTIONAL_INT64_SCHEMA" : "INT64_SCHEMA";
			}
			break;
		}

		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
		case Types.NCHAR:
		case Types.NVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
		case Types.NCLOB:
		case Types.DATALINK:
		case Types.SQLXML: {
			// Some of these types will have fixed size, but we drop this from the schema
			// conversion
			// since only fixed byte arrays can have a fixed size
			result = optional ? "OPTIONAL_STRING_SCHEMA" : "STRING_SCHEMA";
			break;
		}

		// Binary == fixed bytes
		// BLOB, VARBINARY, LONGVARBINARY == bytes
		case Types.BINARY:
		case Types.BLOB:
		case Types.VARBINARY:
		case Types.LONGVARBINARY: {
			result = optional ? "OPTIONAL_BYTES_SCHEMA" : "BYTES_SCHEMA";
			break;
		}

		// Date is day + moth + year
		case Types.DATE: {
			result = "DATE";
			break;
		}

		// Time is a time of day -- hour, minute, seconds, nanoseconds
		case Types.TIME: {
			result = "TIME";
			break;
		}

		// Timestamp is a date + time
		case Types.TIMESTAMP: {
			result = "TIMESTAMP";
			break;
		}

		case Types.ARRAY:
		case Types.JAVA_OBJECT:
		case Types.OTHER:
		case Types.DISTINCT:
		case Types.STRUCT:
		case Types.REF:
		case Types.ROWID:
		default: {
			return null;
		}
		}
		return result;
	}
}
