/**
 * File: SchemaServiceImpl.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leatop.bee.management.dao.SchemaDAO;
import com.leatop.bee.management.po.SchemaEntity;

/**
 * 对象模型管理Service类
 * 
 * @author Dorsey
 *
 */
@Service("schemaService")
public class SchemaServiceImpl implements ISchemaService {

	private final SchemaDAO schemaDao;

	@Autowired
	public SchemaServiceImpl(final SchemaDAO schemaDao) {
		this.schemaDao = schemaDao;
	}

	/**
	 * 保存并返回对象模型
	 * 
	 * @param schemaEntity
	 * @return
	 */
	@Override
	public int saveIfAbsent(final SchemaEntity schemaEntity) {
		final SchemaEntity entityInRepo = lookup(schemaEntity.getSchema(), schemaEntity.getSubject());
		if (entityInRepo != null) {
			return entityInRepo.getId();
		}
		
		int affect = this.schemaDao.saveSchemaAndGetId(schemaEntity);
		if (affect > 0) {
			return schemaEntity.getId();
		}
		
		return -1;
	}

	/**
	 * 读取对象模型
	 * 
	 * @param subject
	 * @param entity
	 * @return
	 */
	@Override
	public SchemaEntity lookup(final String schema, final String subject) {
		Map<String, String> params = new HashMap<>(1);
		params.put("subject", subject);

		Schema inSchema = (schema == null) ? null : new Schema.Parser().parse(schema);
		List<SchemaEntity> entities = schemaDao.queryBySubject(params);
		if (entities != null && entities.isEmpty() == false) {
			for (SchemaEntity entity : entities) {
				final String schemaStr = entity.getSchema();
				Schema sch = new org.apache.avro.Schema.Parser().parse(schemaStr);
				if (sch.equals(inSchema)) {
					return entity;
				}
			}
		}
		
		return null;
	}

	/**
	 * 根据ID获取对象模型
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public SchemaEntity queryById(final int id) {
		Map<String, Integer> params = new HashMap<String, Integer>(1);
		params.put("id", id);
		
		return schemaDao.queryById(params);
	}

}
