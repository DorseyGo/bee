package com.leatop.bee.common;

import org.junit.Before;
import org.junit.Test;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.common.log.LogFacade;

/**
 * Unit test for simple App.
 */
public class LogFacadeTest {
	private LogFacade facade;
	
	@Before
	public void setup() {
		facade = LogFacade.getFacade(TrafficData.class);
	}
	
	@Test
	public void testInfo() {
		facade.info("A test for log");
	}
}
