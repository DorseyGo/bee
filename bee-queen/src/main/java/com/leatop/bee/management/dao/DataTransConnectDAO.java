/**
 * File: DataTransConnectDAO.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.dao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import com.leatop.bee.management.po.DataTransConnectEntity;


/**
 * kafka按任务数据传输监控管理Dao类
 * 
 * @author zlm
 *
 */
public interface DataTransConnectDAO {

	@Results(id = "dataTransConnectEntityRM", value = {
			@Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER),
			@Result(property = "transCount", column = "trans_Count", jdbcType = JdbcType.INTEGER),
			@Result(property = "successCount", column = "success_Count", jdbcType = JdbcType.INTEGER),
			@Result(property = "errorCount", column = "error_Count", jdbcType = JdbcType.INTEGER),
			@Result(property = "connectName", column = "connect_name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "startTime", column = "start_Time", jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "endTime", column = "end_Time", jdbcType = JdbcType.TIMESTAMP)})
	@SelectProvider(type = SqlBuilder.class, method = "queryDataTransConnect")
	List<DataTransConnectEntity> queryDataTransConnect(final Map<String, Object> params);

	@SelectProvider(type = SqlBuilder.class, method = "countDataTransConnect")
	int countDataTransConnect(final Map<String, Object> params);
	
	@InsertProvider(type = SqlBuilder.class, method = "insertDataTransConnect")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertDataTransConnect(final DataTransConnectEntity dataTransConnectEntity);
	
	@ResultMap(value = "dataTransConnectEntityRM")
	@SelectProvider(type = SqlBuilder.class, method = "queryByMaxId")
	DataTransConnectEntity queryByMaxId();
	
	public static class SqlBuilder {

		private static final String TABLE_NAME = "DATA_TRANS";
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		public String queryDataTransConnect(final Map<String, Object> params) {
			int page = ((int) params.get("page")-1)*(int) params.get("limit");
			String sql = "select * from data_trans_connect where connect_name = #{connectName} order by end_time desc limit "+page+",#{limit}";

			return sql;
		}
		
		public String countDataTransConnect(final Map<String, Object> params) {
			String sql = "select count(1) from data_trans_connect where connect_name = #{connectName}";

			return sql;
		}
		
		public String insertDataTransConnect(final DataTransConnectEntity dataTransConnectEntity) {
			String sql = "insert into data_trans_connect (connect_name,trans_count,start_time,end_time,success_count,error_count) values "
					+ "('"+dataTransConnectEntity.getConnectName()+"','"+dataTransConnectEntity.getTransCount()+"','"+simpleDateFormat.format(dataTransConnectEntity.getStartTime())+"',"
					+ "'"+simpleDateFormat.format(dataTransConnectEntity.getEndTime())+"','"+dataTransConnectEntity.getSuccessCount()+"','"+dataTransConnectEntity.getErrorCount()+"')";
			
			return sql.toString();
		}
		
		public String queryByMaxId() {
			String sql = "select * from data_trans_connect where id = (select max(id) from data_trans_connect)";
			
			return sql.toString();
		}
	}

}
