/**
 * File: SchemaServiceImplTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年7月10日
 * Copyright: All rights reserved.
 */
package com.leatop.bee.management.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leatop.bee.management.Queen;
import com.leatop.bee.management.po.SchemaEntity;

/**
 * @author Dorsey
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Queen.class })
public class SchemaServiceImplTest {

	@Autowired
	private SchemaServiceImpl schemaService;

	@Test
	public void testSaveIfAbsent() {
		SchemaEntity schemaEntity = new SchemaEntity();
		schemaEntity.setSchema(
				"{\"type\":\"record\",\"name\":\"ETCPassListAvro\",\"namespace\":\"com.leatop.bee.web.po\",\"fields\":[{\"name\":\"recordNo\",\"type\":\"int\"},{\"name\":\"serialNo\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"flagNetRoadID\",\"type\":\"int\"},{\"name\":\"flagRoadID\",\"type\":\"int\"},{\"name\":\"flagID\",\"type\":\"int\"},{\"name\":\"obuType\",\"type\":\"int\"},{\"name\":\"obuMacID\",\"type\":\"int\"},{\"name\":\"obuNum\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"payCardID\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"payCardType\",\"type\":\"int\"},{\"name\":\"iccIssuer\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"cpuCardID\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"vehicleType\",\"type\":\"int\"},{\"name\":\"vehPlate\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"vehColor\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"opTime\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"direction\",\"type\":\"int\"},{\"name\":\"vehStatus\",\"type\":\"int\"},{\"name\":\"spare1\",\"type\":\"int\"},{\"name\":\"spare2\",\"type\":\"int\"},{\"name\":\"spare3\",\"type\":[\"string\",\"null\"],\"default\":\"null\"},{\"name\":\"spare4\",\"type\":[\"string\",\"null\"],\"default\":\"null\"}]}");
		schemaEntity.setVersion(2);
		schemaEntity.setSubject("test");

		int id = schemaService.saveIfAbsent(schemaEntity);
		Assert.assertTrue(id != -1);
	}

}
