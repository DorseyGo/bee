/**
 * File: SchemaRegistryClient.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.client;

import java.io.IOException;

import org.apache.avro.Schema;

import com.leatop.bee.data.weaver.client.exception.HttpClientException;

/**
 * @author Dorsey
 *
 */
public interface SchemaRegistryClient {

	/**
	 * @param subject
	 * @param schema
	 * @return
	 */
	public int register(final String subject, final Schema schema)
			throws IOException, HttpClientException;

	/**
	 * @param subject
	 * @param schema
	 * @return
	 */
	public int getId(final String subject, final Schema schema)
			throws IOException, HttpClientException;

	public int register(final String subject, final Schema schema, final int version, final int id)
			throws IOException, HttpClientException;

	/**
	 * @param id
	 */
	public Schema getById(int id) throws IOException, HttpClientException;

	public Integer getVersion(String subject, Schema schema)
			throws IOException, HttpClientException;

	public Schema getBySubjectAndId(String subject, int id) throws IOException, HttpClientException;
}
