/**
 * File: DataWeaveInManagerTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月8日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.service;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leatop.bee.common.domain.ETCPassList;
import com.leatop.bee.common.domain.ETCPassList.ETCPassListBuilder;
import com.leatop.bee.web.Application;

/**
 * @author Dorsey
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class DataWeaveInManagerTest {

	@Autowired
	private DataWeaveInManager manager;

	private ETCPassList etcPassList;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ETCPassListBuilder builder = new ETCPassList.ETCPassListBuilder();
		builder.withCpuCardID("123").withDirection(0).withFlagID(0).withFlagNetRoadID(1)
				.withFlagRoadID(-1).withIccIssuer("leatop").withObuMacID(19).withObuNum("xl0001")
				.withObuType(1).withOpTime(new Date()).withPayCardID("pcixxx").withPayCardType(1)
				.withRecordNo(12).withSerialNo("xyz123").withVehColor("blue").withVehicleType(1)
				.withVehPlate("粤A9999").withVehStatus(0);
		etcPassList = builder.build();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		etcPassList = null;
		manager = null;
	}

	/**
	 * Test method for
	 * {@link com.leatop.bee.web.service.DataWeaveInManager#weaveIn(java.lang.String, java.lang.String, com.leatop.bee.common.domain.TrafficData)}.
	 */
	@Test
	public void testWeaveIn() {
		manager.weaveIn("hdfs-test", etcPassList.getSerialNo(), etcPassList);
	}

}
