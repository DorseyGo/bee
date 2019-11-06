/**
 * File: CachedSchemaRegistryClientTest.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.leatop.bee.data.weaver.auth.BasicAuthenticationProvider;
import com.leatop.bee.data.weaver.client.exception.HttpClientException;


/**
 * @author Dorsey
 *
 */
public class CachedSchemaRegistryClientTest {
	
	private SchemaRegistryClient client;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		final String basicUri = "http://localhost:8081";
		final Map<String, String> originals = new HashMap<>();
		originals.put(BasicAuthenticationProvider.Constants.BASIC_AUTH_PROVIDER_SOURCE, "URL");
		
		client = new CachedSchemaRegistryClient(basicUri, 100, originals, null);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		client = null;
	}

	@Test
	public void testRegister() {
		final String schema = "{\"type\": \"int\"}";
		try {
			int id = client.register("etc-trade-list-value", new Schema.Parser().parse(schema));
			Assert.assertEquals(5, id);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		} catch (HttpClientException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

}
