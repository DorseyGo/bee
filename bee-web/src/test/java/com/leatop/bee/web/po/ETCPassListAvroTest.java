/**
 * File: ETCPassListAvroTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.po;

import org.apache.avro.Schema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dorsey
 *
 */
public class ETCPassListAvroTest {

	private ETCPassListAvro avro;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		avro = new ETCPassListAvro();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		avro = null;
	}

	/**
	 * Test method for
	 * {@link com.leatop.bee.web.po.ETCPassListAvro#getSchema()}.
	 */
	@Test
	public void testGetSchema() {
		Schema schema = avro.getSchema();
		System.out.println(schema.toString());
		
		final Schema sch = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ETCPassListAvro\",\"namespace\":\"com.leatop.bee.web.po\",\"fields\":[{\"name\":\"recordNo\",\"type\":\"int\"},{\"name\":\"serialNo\",\"type\":[\"string\",\"null\"]},{\"name\":\"flagNetRoadID\",\"type\":\"int\"},{\"name\":\"flagRoadID\",\"type\":\"int\"},{\"name\":\"flagID\",\"type\":\"int\"},{\"name\":\"obuType\",\"type\":\"int\"},{\"name\":\"obuMacID\",\"type\":\"int\"},{\"name\":\"obuNum\",\"type\":[\"string\",\"null\"]},{\"name\":\"payCardID\",\"type\":[\"string\",\"null\"]},{\"name\":\"payCardType\",\"type\":\"int\"},{\"name\":\"iccIssuer\",\"type\":[\"string\",\"null\"]},{\"name\":\"cpuCardID\",\"type\":[\"string\",\"null\"]},{\"name\":\"vehicleType\",\"type\":\"int\"},{\"name\":\"vehPlate\",\"type\":[\"string\",\"null\"]},{\"name\":\"vehColor\",\"type\":[\"string\",\"null\"]},{\"name\":\"opTime\",\"type\":[\"string\",\"null\"]},{\"name\":\"direction\",\"type\":\"int\"},{\"name\":\"vehStatus\",\"type\":\"int\"},{\"name\":\"spare1\",\"type\":\"int\"},{\"name\":\"spare2\",\"type\":\"int\"},{\"name\":\"spare3\",\"type\":[\"string\",\"null\"]},{\"name\":\"spare4\",\"type\":[\"string\",\"null\"]}]}");
		System.out.println(sch.toString());
		System.out.println(sch.getName());
	}

}
