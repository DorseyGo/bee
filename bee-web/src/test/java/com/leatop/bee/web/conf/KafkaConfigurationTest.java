/**
 * File: KafkaConfigurationTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.conf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leatop.bee.web.Application;

/**
 * @author Dorsey
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class KafkaConfigurationTest {

	@Autowired
	private KafkaConfiguration config;

	@Test
	public void testGetProducerAcks() {
	}
}
