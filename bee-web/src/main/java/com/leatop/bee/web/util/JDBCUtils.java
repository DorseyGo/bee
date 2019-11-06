package com.leatop.bee.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 	kafka-connectors写入工具类
 * 
 * @author zlm,hongSheng
 *
 */
public class JDBCUtils {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(JDBCUtils.class);
	
	private static  Queue<List<ConsumerRecord<?, byte[]>>> queueList_mysql = new LinkedList<List<ConsumerRecord<?, byte[]>>>();
	private static  Queue<List<ConsumerRecord<?, byte[]>>> queueList_sqlserver = new LinkedList<List<ConsumerRecord<?, byte[]>>>();
	private static  Queue<List<ConsumerRecord<?, byte[]>>> queueList_oracle = new LinkedList<List<ConsumerRecord<?, byte[]>>>();
	
	private static Connection conn_mysql = null;
	private static Connection conn_sqlserver =null;
	private static Connection conn_oracle = null;
	
	/**
	 * 	多条kafka记录消费
	 * 
	 * @param jdbcConf
	 * @param records
	 */
	public static void writeJDBCBatchQueue(List<Map<String,String>> jdbcConf,List<ConsumerRecord<?, byte[]>> records) {
		if(jdbcConf != null) {
			for(int i=0;i<jdbcConf.size();i++) {
				if(jdbcConf.get(i).get("jdbcURL").contains("jdbc:mysql://")) {
					queueList_mysql.offer(records);
					List<ConsumerRecord<?, byte[]>> listTMP= new ArrayList<ConsumerRecord<?, byte[]>>();
					while((listTMP=queueList_mysql.poll())!=null){
						setconnection_mysql_batch(jdbcConf.get(i),listTMP);
			        }
				}
				else if(jdbcConf.get(i).get("jdbcURL").contains("jdbc:sqlserver:")) {
					queueList_sqlserver.offer(records);
					List<ConsumerRecord<?, byte[]>> listTMP= new ArrayList<ConsumerRecord<?, byte[]>>();
					while((listTMP=queueList_sqlserver.poll())!=null){
						setconnection_sqlserver_batch(jdbcConf.get(i),listTMP);
			        }
					
				}
				else if(jdbcConf.get(i).get("jdbcURL").contains("jdbc:oracle:thin:@")) {
					queueList_oracle.offer(records);
					List<ConsumerRecord<?, byte[]>> listTMP= new ArrayList<ConsumerRecord<?, byte[]>>();
					while((listTMP=queueList_oracle.poll())!=null){
						setconnection_oracle_batch(jdbcConf.get(i),listTMP);
			        }
				}
			}
		}
	}
	
	
	
	
	/**
	 * 	单条消费kafka记录
	 * 
	 * @param jdbcConf
	 * @param recordMap
	 */
	public static void setconnection_sqlserver(Map<String,String> jdbcConf,Map<String, Object> recordMap) {
		Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //conn = DriverManager.getConnection("jdbc:sqlserver://128.8.51.175:1433;DatabaseName=RoadMidDb", "justdoit", "iloveyou");
            conn = DriverManager.getConnection(jdbcConf.get("jdbcURL"), jdbcConf.get("jdbcUser"), jdbcConf.get("jdbcPasswd"));
            conn.setAutoCommit(false);

            // 保存当前自动提交模式
            boolean autoCommit = conn.getAutoCommit();
            // 关闭自动提交
            conn.setAutoCommit(false);
            Statement stmt =conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY); 
            
            //for (int i = 1; i <= 3000; i++) {
                stmt.addBatch("update IMAGE_LIST set IMAGE_LIST.image = '"+recordMap.get("path")+"' where IMAGE_LIST.serialNo = '"+recordMap.get("key")+"'");
                //stmt.addBatch("insert into IMAGE_LIST(listNo,serialNo,image) values("+i+",'insert_"+i+"','insert_path_"+i+"')"); 
            //}
            stmt.executeBatch();   
            conn.commit(); 
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 	mysql消息批量写入数据库
	 * 
	 * @param jdbcConf
	 * @param records
	 */
	public static void setconnection_mysql_batch(Map<String,String> jdbcConf,List<ConsumerRecord<?, byte[]>> records) {
        try {
        	if(conn_mysql==null) {
        		Class.forName("com.mysql.jdbc.Driver");
                //conn = DriverManager.getConnection("jdbc:mysql://128.8.39.166:3306/test", "test", "test");
        		conn_mysql = DriverManager.getConnection(jdbcConf.get("jdbcURL"), jdbcConf.get("jdbcUser"), jdbcConf.get("jdbcPasswd"));
        		conn_mysql.setAutoCommit(false);

                // 保存当前自动提交模式
                boolean autoCommit = conn_mysql.getAutoCommit();
                // 关闭自动提交
                conn_mysql.setAutoCommit(false);
        	}
             Statement stmt =conn_mysql.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY); 

             for (int i = 0; i < records.size(); i++) {
                 String[] results = FastDFSUtil.uploadFile(records.get(i).value(),String.valueOf(records.get(i).key()),"jpg");
         	      if(results!=null) {
         	    	  Map<String, Object> recordMap = new HashMap<>();
         		      recordMap.put("key", records.get(i).key());
         		      recordMap.put("path", results[0]+"/"+results[1]);
         		      stmt.addBatch("update IMAGE_LIST set IMAGE_LIST.image = '"+recordMap.get("path")+"' where IMAGE_LIST.serialNo = '"+recordMap.get("key")+"'");
                     //stmt.addBatch("insert into IMAGE_LIST(listNo,serialNo,image) values("+i+",'insert_"+i+"','insert_path_"+i+"')"); 
         	      }else {
         	    	  LOGGER.info("fastDFS上传文件失败：{ } ",records.get(i).key()); 
         	      }
                   
               }
            stmt.executeBatch();   
            conn_mysql.commit(); 
            conn_mysql.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
            	conn_mysql.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 	sqlserver消息批量写入数据库
	 * 
	 * @param jdbcConf
	 * @param records
	 */
	public static void setconnection_sqlserver_batch(Map<String,String> jdbcConf,List<ConsumerRecord<?, byte[]>> records) {
        try {
        	if(conn_sqlserver==null) {
        		Class.forName("com.mysql.jdbc.Driver");
    			conn_sqlserver = DriverManager.getConnection(jdbcConf.get("jdbcURL"), jdbcConf.get("jdbcUser"), jdbcConf.get("jdbcPasswd"));
    			conn_sqlserver.setAutoCommit(false);
    			// 保存当前自动提交模式
                boolean autoCommit = conn_sqlserver.getAutoCommit();
                // 关闭自动提交
                conn_sqlserver.setAutoCommit(false);
        	}
        	Statement stmt =conn_sqlserver.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY); 
            
            for (int i = 0; i < records.size(); i++) {
              String[] results = FastDFSUtil.uploadFile(records.get(i).value(),String.valueOf(records.get(i).key()),"jpg");
      	      if(results!=null) {
      	    	  Map<String, Object> recordMap = new HashMap<>();
      		      recordMap.put("key", records.get(i).key());
      		      recordMap.put("path", results[0]+"/"+results[1]);
      		      stmt.addBatch("update IMAGE_LIST set IMAGE_LIST.image = '"+recordMap.get("path")+"' where IMAGE_LIST.serialNo = '"+recordMap.get("key")+"'");
                  //stmt.addBatch("insert into IMAGE_LIST(listNo,serialNo,image) values("+i+",'insert_"+i+"','insert_path_"+i+"')"); 
      	      }else {
      	    	  LOGGER.info("fastDFS上传文件失败：{ } ",records.get(i).key()); 
      	      }
                
            }
            stmt.executeBatch();   
            conn_sqlserver.commit(); 
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
            	conn_sqlserver.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * oracle消息批量写入数据库
	 * 
	 * @param jdbcConf
	 * @param records
	 */
	public static void setconnection_oracle_batch(Map<String,String> jdbcConf,List<ConsumerRecord<?, byte[]>> records) {
        try {
        	if(conn_oracle==null) {
        		Class.forName("oracle.jdbc.driver.OracleDriver");
        		conn_oracle = DriverManager.getConnection(jdbcConf.get("jdbcURL"), jdbcConf.get("jdbcUser"), jdbcConf.get("jdbcPasswd"));
        		conn_oracle.setAutoCommit(false);

                // 保存当前自动提交模式
                boolean autoCommit = conn_oracle.getAutoCommit();
                // 关闭自动提交
                conn_oracle.setAutoCommit(false);
        	}
            
            Statement stmt =conn_oracle.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY); 

            for (int i = 0; i < records.size(); i++) {
                String[] results = FastDFSUtil.uploadFile(records.get(i).value(),String.valueOf(records.get(i).key()),"jpg");
        	      if(results!=null) {
        	    	  Map<String, Object> recordMap = new HashMap<>();
        		      recordMap.put("key", records.get(i).key());
        		      recordMap.put("path", results[0]+"/"+results[1]);
        		      stmt.addBatch("update IMAGE_LIST set IMAGE_LIST.image = '"+recordMap.get("path")+"' where IMAGE_LIST.serialNo = '"+recordMap.get("key")+"'");
                    //stmt.addBatch("insert into IMAGE_LIST(listNo,serialNo,image) values("+i+",'insert_"+i+"','insert_path_"+i+"')"); 
        	      }else {
        	    	  LOGGER.info("fastDFS上传文件失败：{ } ",records.get(i).key()); 
        	      }
                  
              }
            stmt.executeBatch();   
            conn_oracle.commit(); 
            conn_oracle.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
            	conn_oracle.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
}
