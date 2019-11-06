/**
 * File: SchemaDAO.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import com.leatop.bee.management.po.SchemaEntity;

/**
 * 对象模型管理Dao类
 * 
 * @author DORSEy
 *
 */
@Repository
public interface SchemaDAO {

	@InsertProvider(type = SQLBuilder.class, method = "saveSchemaAndGetId")
	@SelectKey(keyColumn = "id", keyProperty = "id", before = false, statement = "SELECT SCHEMA_REPO_ID.currval AS id FROM dual", resultType = Integer.class)
	int saveSchemaAndGetId(final SchemaEntity schemaEntity);

	@Results(id = "schemaEntityRM", value = {
			@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
			@Result(column = "version", property = "version", jdbcType = JdbcType.INTEGER),
			@Result(column = "subject", property = "subject", jdbcType = JdbcType.VARCHAR),
			@Result(column = "schema", property = "schema", jdbcType = JdbcType.VARCHAR) })
	@Select("SELECT * FROM SCHEMA_REPO WHERE subject = #{subject}")
	List<SchemaEntity> queryBySubject(final Map<String, String> params);

	@ResultMap(value = "schemaEntityRM")
	@Select("SELECT * FROM SCHEMA_REPO WHERE id = #{id}")
	SchemaEntity queryById(final Map<String, Integer> params);

	public static class SQLBuilder {
		private static final String TABLE_NAME = "SCHEMA_REPO";

		public String saveSchemaAndGetId(final SchemaEntity schemaEntity) {
			SQL sql = new SQL().INSERT_INTO(TABLE_NAME);
			sql.VALUES("id", "SCHEMA_REPO_ID.nextval");
			sql.VALUES("schema", "#{schema}");
			sql.VALUES("subject", "#{subject}");
			sql.VALUES("version", "#{version}");

			return sql.toString();
		}
	}
}
