/**
 * File: ISchemaService.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.service;

import com.leatop.bee.management.po.SchemaEntity;

/**
 * 对象模型管理Service类
 * 
 * @author Dorsey
 *
 */
public interface ISchemaService {
	
	/**
	 * 保存并返回对象模型
	 * 
	 * @param schemaEntity
	 * @return
	 */
	int saveIfAbsent(final SchemaEntity schemaEntity);

	/**
	 * 读取对象模型
	 * 
	 * @param subject
	 * @param entity
	 * @return
	 */
	SchemaEntity lookup(String schema, String subject);

	/**
	 * 根据ID获取对象模型
	 * 
	 * @param id
	 * @return
	 */
	SchemaEntity queryById(int id);
}
