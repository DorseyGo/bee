/**
 * File: SchemaController.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.leatop.bee.management.po.SchemaEntity;
import com.leatop.bee.management.resp.LookupSchemaResponse;
import com.leatop.bee.management.resp.SchemaRegistryResponse;
import com.leatop.bee.management.resp.SchemaRetrievalResponse;
import com.leatop.bee.management.service.ISchemaService;

/**
 * 对象模型管理Controller类
 * @author Dorsey
 *
 */
@Controller
public class SchemaController {

	private final ISchemaService schemaService;

	@Autowired
	public SchemaController(final ISchemaService schemaService) {
		this.schemaService = schemaService;
	}

	/**
	 * 读取对象模型
	 * 
	 * @param subject
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@PostMapping("/subjects/{subject}")
	public LookupSchemaResponse lookup(@PathVariable("subject") final String subject,
			@RequestBody final SchemaEntity entity) {
		LookupSchemaResponse response = new LookupSchemaResponse();

		if (entity != null) {
			String schema = entity.getSchema();
			SchemaEntity schemaEntity = schemaService.lookup(schema, subject);

			if (schemaEntity != null) {
				response.setId(schemaEntity.getId());
				response.setSchema(schemaEntity.getSchema());
				response.setVersion(schemaEntity.getVersion());
				response.setSubject(schemaEntity.getSubject());
			}
		}

		return response;
	}

	/**
	 * 保存并返回对象模型
	 * 
	 * @param subject
	 * @param entity
	 * @return
	 */
	@ResponseBody
	@PostMapping("/subjects/{subject}/versions")
	public SchemaRegistryResponse registerAndGetId(@PathVariable("subject") final String subject,
			@RequestBody final SchemaEntity entity) {
		SchemaRegistryResponse response = new SchemaRegistryResponse();

		if (entity != null) {
			entity.setSubject(subject);
			int id = schemaService.saveIfAbsent(entity);

			response.setId(id);
		}

		return response;
	}
	
	/**
	 * 根据ID获取对象模型
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@GetMapping("/schemas/ids/{id}")
	public SchemaRetrievalResponse retrieveSchema(@PathVariable("id") final int id) {
		SchemaRetrievalResponse response = new SchemaRetrievalResponse();
		SchemaEntity entity = this.schemaService.queryById(id);
		response.setSchema(entity.getSchema());
		
		return response;
	}
}
