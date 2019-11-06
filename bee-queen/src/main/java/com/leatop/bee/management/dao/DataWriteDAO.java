package com.leatop.bee.management.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.leatop.bee.management.dao.DataTransDAO.SqlBuilder;
import com.leatop.bee.management.po.DataWrite;

/**
* @ClassName: DataWriteInfoDAO.java
* @Description: TODO(任务配置管理Dao类)
* @author hongSheng
* @date 2019年5月23日
*
*/
@Mapper
public interface DataWriteDAO {
	
	@Insert("insert into data_write(id,name,name_desc,table_name,registry_url,tasks_max,max_retries,topics,value_converter,"
			+ "key_converter,connection_url,connection_user,connection_passwd,pivot_field,task_type,is_split,"
			+ "pivot_field_type,hdfs_url,source_connection_url,source_connection_user,source_connection_passwd,source_task_type,"
			+ "source_table_name,timestamp_column_name,incrementing_column_name,connector_type,fields_compare_filter,fields_compare_filter_text,table_use_fields,table_use_fields_type) "
			+ "values(DATA_WRITE_ID.nextval,#{name,jdbcType=VARCHAR},#{nameDesc,jdbcType=VARCHAR},#{tableName,jdbcType=VARCHAR},#{registryUrl,jdbcType=VARCHAR},#{tasksMax,jdbcType=INTEGER},"
			+ "#{maxRetries,jdbcType=INTEGER},#{topics,jdbcType=VARCHAR},#{valueConverter,jdbcType=VARCHAR},#{keyConverter,jdbcType=VARCHAR},#{connectionUrl,jdbcType=VARCHAR},"
			+ "#{connectionUser,jdbcType=VARCHAR},#{connectionPasswd,jdbcType=VARCHAR},#{pivotField,jdbcType=VARCHAR},#{taskType,jdbcType=INTEGER},#{isSplit,jdbcType=INTEGER},"
			+ "#{pivotFieldType,jdbcType=INTEGER},#{hdfsUrl,jdbcType=VARCHAR},#{sourceConnectionUrl,jdbcType=VARCHAR},#{sourceConnectionUser,jdbcType=VARCHAR},"
			+ "#{sourceConnectionPasswd,jdbcType=VARCHAR},#{sourceTaskType,jdbcType=INTEGER},#{sourceTableName,jdbcType=VARCHAR},#{timestampColumnName,jdbcType=VARCHAR},"
			+ "#{incrementingColumnName,jdbcType=VARCHAR},#{connectorType,jdbcType=INTEGER},#{fieldsCompareFilter,jdbcType=VARCHAR},#{fieldsCompareFilterText,jdbcType=VARCHAR},"
			+ "#{tableUseFields,jdbcType=CLOB},#{tableUseFieldsType,jdbcType=CLOB})")
	@SelectKey(keyColumn = "id", keyProperty = "id", before = false, statement = "SELECT DATA_WRITE_ID.currval AS id FROM dual", resultType = Integer.class)
	int addDataWrite(DataWrite dataWrite);
	
	
	@Results(id = "dataTransEntityRM", value = {
			@Result(property = "id", column = "id", jdbcType = JdbcType.INTEGER),
			@Result(property = "name", column = "name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "nameDesc", column = "name_desc", jdbcType = JdbcType.VARCHAR),
			@Result(property = "tableName", column = "table_name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "registryUrl", column = "registry_url", jdbcType = JdbcType.VARCHAR),
			@Result(property = "tasksMax", column = "tasks_max", jdbcType = JdbcType.INTEGER),
			@Result(property = "maxRetries", column = "max_retries", jdbcType = JdbcType.INTEGER) ,
			@Result(property = "topics", column = "topics", jdbcType = JdbcType.VARCHAR),
			@Result(property = "valueConverter", column = "value_converter", jdbcType = JdbcType.VARCHAR),
			@Result(property = "keyConverter", column = "key_converter", jdbcType = JdbcType.VARCHAR),
			@Result(property = "connectionUrl", column = "connection_url", jdbcType = JdbcType.VARCHAR),
			@Result(property = "connectionUser", column = "connection_user", jdbcType = JdbcType.VARCHAR),
			@Result(property = "status", column = "status", jdbcType = JdbcType.INTEGER),
			@Result(property = "pivotField", column = "pivot_field", jdbcType = JdbcType.VARCHAR),
			@Result(property = "taskType", column = "task_type", jdbcType = JdbcType.INTEGER),
			@Result(property = "isSplit", column = "is_split", jdbcType = JdbcType.INTEGER),
			@Result(property = "pivotFieldType", column = "pivot_field_type", jdbcType = JdbcType.INTEGER),
			@Result(property = "hdfsUrl", column = "hdfs_url", jdbcType = JdbcType.VARCHAR),
			@Result(property = "sourceConnectionUrl", column = "source_connection_url", jdbcType = JdbcType.VARCHAR),
			@Result(property = "sourceConnectionUser", column = "source_connection_user", jdbcType = JdbcType.VARCHAR),
			@Result(property = "sourceConnectionPasswd", column = "source_connection_passwd", jdbcType = JdbcType.VARCHAR),
			@Result(property = "sourceTaskType", column = "source_task_type", jdbcType = JdbcType.INTEGER),
			@Result(property = "sourceTableName", column = "source_table_name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "timestampColumnName", column = "timestamp_column_name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "incrementingColumnName", column = "incrementing_column_name", jdbcType = JdbcType.VARCHAR),
			@Result(property = "connectorType", column = "connector_type", jdbcType = JdbcType.INTEGER),
			@Result(property = "fieldsCompareFilter", column = "fields_compare_filter", jdbcType = JdbcType.VARCHAR),
			@Result(property = "fieldsCompareFilterText", column = "fields_compare_filter_text", jdbcType = JdbcType.VARCHAR),
			@Result(property = "tableUseFields", column = "table_use_fields", jdbcType = JdbcType.CLOB),
			@Result(property = "tableUseFieldsType", column = "table_use_fields_type", jdbcType = JdbcType.CLOB),
			@Result(property = "connectionPasswd", column = "connection_passwd", jdbcType = JdbcType.VARCHAR)})
	@SelectProvider(type = SqlBuilder.class, method = "queryDataWrite")
	List<DataWrite> queryDataWrite(final Map<String, Object> params);
	
	@SelectProvider(type = SqlBuilder.class, method = "countDataWrite")
	int countDataWrite(final Map<String, Object> params);
	
	@DeleteProvider(type = SqlBuilder.class, method = "deleteDataWrite")
	int deleteDataWrite(@Param("id") final int id);
	
	@UpdateProvider(type = SqlBuilder.class, method = "updateDataWrite")
	int updateDataWrite(DataWrite dataWrite);
	
	@UpdateProvider(type = SqlBuilder.class, method = "updateStatus")
	int updateStatus(final Map<String, Object> params);
	
	@ResultMap(value = "dataTransEntityRM")
	@SelectProvider(type = SqlBuilder.class, method = "getDataWrite")
	DataWrite getDataWrite(@Param("id") final int id);
	
	public static class SqlBuilder {

		private static final String TABLE_NAME = "DATA_WRITE";

		public String queryDataWrite(final Map<String, Object> params) {
			//int page = ((int) params.get("page")-1)*(int) params.get("limit");
			//String sql = "select * from data_write  order by id desc limit "+page+",#{limit}";
			String sql =" SELECT * FROM (SELECT ROWNUM AS rowno,r.*" + 
					    "       FROM(SELECT * FROM DATA_WRITE t where connector_type=#{connectorType} ORDER BY t.id asc" +
					    "               ) r " +
					    "       where ROWNUM <= #{page}*#{limit}" +
					    "      ) table_alias " +
					    "  WHERE table_alias.rowno > (#{page}-1)*#{limit}";
			return sql;
		}
		
		public String countDataWrite(final Map<String, Object> params) {
			String sql = "select count(1) from data_write where connector_type=#{connectorType}";

			return sql;
		}
		
		public String deleteDataWrite(@Param("id") final int id) {
			String sql = "delete from data_write where id='"+id+"'";
			
			return sql.toString();
		}
		
		public String updateDataWrite(@Param("dataWrite") DataWrite dataWrite) {
			String sql = "update data_write set name='"+dataWrite.getName()+"',name_desc='"+dataWrite.getNameDesc()+"',table_name='"+
						dataWrite.getTableName()+"',topics='"+dataWrite.getTopics()+
						"',connection_url='"+dataWrite.getConnectionUrl()+"',connection_user='"+dataWrite.getConnectionUser()+
						"',pivot_field='"+dataWrite.getPivotField()+"',task_type="+dataWrite.getTaskType()+
						",is_split="+dataWrite.getIsSplit()+",pivot_field_type="+dataWrite.getPivotFieldType()+
						",hdfs_url='"+dataWrite.getHdfsUrl()+"',source_connection_url='"+dataWrite.getSourceConnectionUrl()+
						"',source_connection_user='"+dataWrite.getSourceConnectionUser()+"',source_connection_passwd='"+dataWrite.getSourceConnectionPasswd()+
						"',source_task_type="+dataWrite.getSourceTaskType()+",source_table_name='"+dataWrite.getSourceTableName()+
						"',timestamp_column_name='"+dataWrite.getTimestampColumnName()+"',incrementing_column_name='"+dataWrite.getIncrementingColumnName()+
						"',connection_passwd='"+dataWrite.getConnectionPasswd()+"',fields_compare_filter='"+dataWrite.getFieldsCompareFilter()+
						"',fields_compare_filter_text='"+dataWrite.getFieldsCompareFilterText()+"',table_use_fields='"+dataWrite.getTableUseFields()+
						"',table_use_fields_type='"+dataWrite.getTableUseFieldsType()+"' where id="+dataWrite.getId();
			
			return sql.toString();
		}
		
		public String updateStatus(final Map<String, Object> params) {
			String sql = "update data_write set status = #{status} where id = #{id}";

			return sql;
		}
		
		public String getDataWrite(@Param("id") final int id) {
			String sql = "select * from data_write where id='"+id+"'";
			
			return sql.toString();
		}
	}
	
}
