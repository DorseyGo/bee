/**
 * File: DataTransDAO.java
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
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import com.leatop.bee.management.dao.TopicDAO.SqlBuilder;
import com.leatop.bee.management.po.DataTransEntity;
import com.leatop.bee.management.po.TopicEntity;


/**
 * kafka按主题数据传输监控管理Dao类
 * 
 * @author zlm
 *
 */
public interface DataTransDAO {

	@Results(id = "dataTransEntityRM", value = {
			@Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER),
			@Result(property = "transCount", column = "trans_count", jdbcType = JdbcType.INTEGER),
			@Result(property = "successCount", column = "success_count", jdbcType = JdbcType.INTEGER),
			@Result(property = "errorCount", column = "error_count", jdbcType = JdbcType.INTEGER),
			@Result(property = "topicName", column = "topic_name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "hostName", column = "host_name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "startTime", column = "start_time", jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "endTime", column = "end_time", jdbcType = JdbcType.TIMESTAMP)})
	@SelectProvider(type = SqlBuilder.class, method = "queryDataTrans")
	List<DataTransEntity> queryDataTrans(final Map<String, Object> params);

	@SelectProvider(type = SqlBuilder.class, method = "countDataTrans")
	int countDataTrans(final Map<String, Object> params);
	
	@InsertProvider(type = SqlBuilder.class, method = "insertDataTrans")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertDataTrans(final DataTransEntity dataTransEntity);
	
	@ResultMap(value = "dataTransEntityRM")
	@SelectProvider(type = SqlBuilder.class, method = "queryByMaxId")
	DataTransEntity queryByMaxId();
	
	public static class SqlBuilder {

		private static final String TABLE_NAME = "DATA_TRANS";
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		public String queryDataTrans(final Map<String, Object> params) {
			int page = ((int) params.get("page")-1)*(int) params.get("limit");
			String sql = "select * from data_trans where topic_name = #{topicName} order by end_time desc limit "+page+",#{limit}";

			return sql;
		}
		
		public String countDataTrans(final Map<String, Object> params) {
			String sql = "select count(1) from data_trans where topic_name = #{topicName}";

			return sql;
		}
		
		public String insertDataTrans(final DataTransEntity dataTransEntity) {
			String sql = "insert into data_trans (topic_name,trans_count,start_time,end_time,success_count,error_count,host_name) values "
					+ "('"+dataTransEntity.getTopicName()+"','"+dataTransEntity.getTransCount()+"','"+simpleDateFormat.format(dataTransEntity.getStartTime())+"',"
					+ "'"+simpleDateFormat.format(dataTransEntity.getEndTime())+"','"+dataTransEntity.getSuccessCount()+"','"+dataTransEntity.getErrorCount()+"','"+dataTransEntity.getHostName()+"')";
			
			return sql.toString();
		}
		
		public String queryByMaxId() {
			String sql = "select * from data_trans where id = (select max(id) from data_trans)";
			
			return sql.toString();
		}
	}

}
