/**
 * File: DataSupervisorConfigurationTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.conf;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import com.leatop.bee.web.Application;


/**
 * @author Dorsey
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class DataSupervisorConfigurationTest {
	
	@Autowired
	private DataSupervisorConfiguration config;

	@Test
	public void test() {
		String preProcessors = config.getPreProcessors();
		Assert.assertNotNull(preProcessors);
		
		Set<String> preProcessorList = StringUtils.commaDelimitedListToSet(preProcessors);
		Assert.assertTrue(preProcessorList.size() == 2);
	}

}
