/**
 * File: RangeIntrospectorTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年4月25日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.supervisor.introspector;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.data.supervisor.exception.DataInvalidException;
import com.leatop.bee.data.supervisor.introspector.annotation.Rangement;

/**
 * @author Dorsey
 *
 */
public class RangeIntrospectorTest {

	private RangeIntrospector introspector;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		introspector = new RangeIntrospector();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		introspector = null;
	}

	@Test
	public void testIntrospect() {
		Data data = new Data(13);
		try {
			introspector.introspect(data);
			Assert.assertTrue(false);
		} catch (DataInvalidException e) {
			Assert.assertTrue(true);
		}
	}

	public static class Data implements TrafficData<Long> {

		@Rangement.LongRangement(min = 1L, max = 12L)
		private long id;
		
		public Data(final long id) {
			setId(id);
		}
		
		public Long getId() {
			return id;
		}

		
		/**
		 * @param id the id to set
		 */
		public void setId(final long id) {
			this.id = id;
		}
	}
}
