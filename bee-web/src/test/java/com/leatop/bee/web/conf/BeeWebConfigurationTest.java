/**
 * File: BeeWebConfigurationTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.conf;

import org.junit.Assert;
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
public class BeeWebConfigurationTest {

	@Autowired
	private BeeWebConfiguration config;

	@Test
	public void testGetEtcTradeListTopic() {
		final String topic = config.getEtcTradeListTopic();
		Assert.assertNotNull(topic);

		final String expected = "etc-trade-list";
		Assert.assertEquals(expected, topic);
	}

	@Test
	public void testGetEtcPassListTopic() {
		final String topic = config.getEtcPassListTopic();
		Assert.assertNotNull(topic);

		final String expected = "etc-pass-list";
		Assert.assertEquals(expected, topic);
	}

	@Test
	public void testGetImageListTopic() {
		final String topic = config.getImageListTopic();
		Assert.assertNotNull(topic);

		final String expected = "image-list";
		Assert.assertEquals(expected, topic);
	}

}
