/**
 * File: TopicDAO.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import com.leatop.bee.management.po.TopicEntity;

/**
 * kafka主题管理Dao类
 * 
 * @author zlm
 *
 */
public interface TopicDAO {


	@Results(id = "topicEntityRM", value = {
//			@Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER),
			@Result(property = "name", column = "name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "zookeeperHost", column = "zookeeper_host", jdbcType = JdbcType.VARCHAR),
			@Result(property = "replicationFactor", column = "replication_factor", jdbcType = JdbcType.INTEGER),
			@Result(property = "partitions", column = "partitions", jdbcType = JdbcType.INTEGER)})
//			@Result(property = "createTime", column = "create_time", jdbcType = JdbcType.DATE),
//			@Result(property = "updateTime", column = "update_time", jdbcType = JdbcType.DATE)})
	@SelectProvider(type = SqlBuilder.class, method = "queryTopics")
	List<TopicEntity> queryTopics(final Map<String, Object> params);
	
	@SelectProvider(type = SqlBuilder.class, method = "countTopics")
	int countTopics(final Map<String, Object> params);
	
	@InsertProvider(type = SqlBuilder.class, method = "insertTopic")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertTopic(final TopicEntity topicEntity);
	
	@UpdateProvider(type = SqlBuilder.class, method = "updateTopic")
	void updateTopic(final TopicEntity topicEntity);
	
	@DeleteProvider(type = SqlBuilder.class, method = "deleteTopic")
	void deleteTopic(@Param("id") final int id);
	
	@ResultMap(value = "topicEntityRM")
	@SelectProvider(type = SqlBuilder.class, method = "queryById")
	TopicEntity queryById(final Map<String, String> params);
	
	public static class SqlBuilder {

		private static final String TABLE_NAME = "TOPIC";

		public String queryTopics(final Map<String, Object> params) {
//			int page = ((int) params.get("page")-1)*(int) params.get("limit");
			int rownum = (int) params.get("page")*(int) params.get("limit");
			int rn = ((int) params.get("page")-1)*(int) params.get("limit")+1;
//			String sql = "select * from topic where 1=1";
//					sql +=" limit "+page+",#{limit}";
			String sql = "select * from (select a.*, rownum rn from (select * from topic) a where rownum <= "+rownum+") where rn >= "+rn;
					 				

			return sql;
		}
		
		public String countTopics(final Map<String, Object> params) {
			String sql = "select count(1) from topic where 1=1";
			if (StringUtils.isNotBlank(params.get("name").toString())) {
				sql +=" and name = "+params.get("name").toString();
			}
			return sql;
		}
		
		public String insertTopic(final TopicEntity topicEntity) {
			String sql = "insert into topic (name,zookeeper_host,replication_factor,partitions,create_time,update_time) values "
					+ "('"+topicEntity.getName()+"','"+topicEntity.getZookeeperHost()+"','"+topicEntity.getReplicationFactor()+"','"+topicEntity.getPartitions()+"',"
//							+ "CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
							+ "sysdate,sysdate)";
			
			return sql.toString();
		}
		
		public String updateTopic(final TopicEntity topicEntity) {
			String sql = "update topic set name = '"+topicEntity.getName()+"',zookeeper_host = '"+topicEntity.getZookeeperHost()+"',replication_factor = "
					+ "'"+topicEntity.getReplicationFactor()+"',partitions = '"+topicEntity.getPartitions()+"'"
//							+ ",update_time = CURRENT_TIMESTAMP ";
							+ ",update_time = sysdate ";
//							+ "where id = '"+topicEntity.getId()+"'";
			
			return sql.toString();
		}
		
		public String deleteTopic(@Param("id") final int id) {
			String sql = "delete from topic where id='"+id+"'";
			
			return sql.toString();
		}
		
		public String queryById(final Map<String, Integer> params) {
			SQL sql = new SQL().SELECT("*").FROM(TABLE_NAME).WHERE("name = #{name}");

			return sql.toString();
		}
	}
	
}
