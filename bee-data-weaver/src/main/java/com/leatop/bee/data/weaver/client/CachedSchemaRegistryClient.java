/**
 * File: CachedSchemaRegistryClient.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;

import com.leatop.bee.data.weaver.auth.BasicAuthProviderFactory;
import com.leatop.bee.data.weaver.auth.BasicAuthenticationProvider;
import com.leatop.bee.data.weaver.client.exception.HttpClientException;
import com.leatop.bee.data.weaver.client.request.SchemaLookupResponse;
import com.leatop.bee.data.weaver.client.service.RestManager;

/**
 * An implementation of {@link SchemaRegistryClient}.
 * 
 * @author Dorsey
 *
 */
public class CachedSchemaRegistryClient implements SchemaRegistryClient {

	private final Map<String, Map<Integer, Schema>> idCache;
	private final Map<String, Map<Schema, Integer>> schemaCache;
	private final Map<String, Map<Schema, Integer>> versionCache;
	private final int idMapCapacity;
	private final RestManager restManager;

	public CachedSchemaRegistryClient(final String basicUri, final int idMapCapacity,
			final Map<String, ?> originals, final Map<String, String> reqHeaders) {
		this.idCache = new HashMap<>();
		schemaCache = new HashMap<>();
		versionCache = new HashMap<>();
		this.idMapCapacity = idMapCapacity;
		this.idCache.put(null, new HashMap<>());

		this.restManager = new RestManager(basicUri);
		configRestManager(originals, reqHeaders);
	}

	private void configRestManager(final Map<String, ?> configs,
			final Map<String, String> reqHeaders) {
		// assert that rest manager is not null
		if (reqHeaders != null) {
			this.restManager.setReqHeaders(reqHeaders);
		}

		if (configs != null) {
			String type = (String) configs
					.get(BasicAuthenticationProvider.Constants.BASIC_AUTH_PROVIDER_SOURCE);

			if (type != null && !type.isEmpty()) {
				BasicAuthenticationProvider basicAuthProvider = BasicAuthProviderFactory
						.getAuthProvider(type, configs);

				this.restManager.setBasicAuthProvider(basicAuthProvider);
			}
		}
	}

	@Override
	public synchronized int register(final String subject, final Schema schema)
			throws IOException, HttpClientException {
		return register(subject, schema, 0, -1);
	}

	@Override
	public synchronized int getId(final String subject, final Schema schema)
			throws IOException, HttpClientException {
		Map<Schema, Integer> schemaMaps = schemaCache.get(subject);
		if (schemaMaps == null) {
			schemaMaps = new HashMap<>();
			schemaCache.put(subject, schemaMaps);
		}

		Integer cacheId = schemaMaps.get(schema);
		if (cacheId != null) {
			return cacheId.intValue();
		}

		// validate
		if (schemaMaps.size() > idMapCapacity) {
			throw new IllegalStateException("too many schemas for subject: " + subject);
		}

		final int retrievedId = getIdFromRepository(subject, schema);
		schemaMaps.put(schema, retrievedId);
		idCache.get(null).put(retrievedId, schema);

		return retrievedId;
	}

	private int getIdFromRepository(final String subject, final Schema schema)
			throws IOException, HttpClientException {
		SchemaLookupResponse response = this.restManager.lookUpSubjectVersion(schema.toString(),
				subject);
		return response.getId();
	}

	@Override
	public synchronized int register(final String subject, final Schema schema, final int version,
			final int id) throws IOException, HttpClientException {

		Map<Schema, Integer> schemaIds = schemaCache.get(subject);
		if (schemaIds == null) {
			schemaIds = new HashMap<>();
			schemaCache.put(subject, schemaIds);
		}

		Integer cachedId = schemaIds.get(schema);
		if (cachedId != null) {
			if (id >= 0 && id != cachedId.intValue()) {
				throw new IllegalStateException(
						"schema already register with id: " + cachedId + ", instead of id: " + id);
			}

			return cachedId.intValue();
		}

		// validate
		if (schemaIds.size() > idMapCapacity) {
			throw new IllegalStateException("too many schemas for subject: " + subject);
		}

		final Integer retrieveId = (id >= 0)
				? registerAndGetId(subject, schema.toString(true), version, id)
				: registerAndGetId(subject, schema.toString(true));
		schemaIds.put(schema, retrieveId);
		idCache.get(null).put(retrieveId, schema);

		return retrieveId;
	}

	private int registerAndGetId(final String subject, final String schema)
			throws IOException, HttpClientException {
		return this.restManager.registerSchema(schema, subject);
	}

	private int registerAndGetId(final String subject, final String schema, final int version,
			final int id) throws IOException, HttpClientException {
		return this.restManager.registerSchema(schema, subject, version, id);
	}

	@Override
	public synchronized Schema getById(final int id) throws IOException, HttpClientException {
		return getBySubjectAndId(null, id);
	}

	/**
	 * @param object
	 * @param id
	 * @return
	 */
	@Override
	public synchronized Schema getBySubjectAndId(final String subject, final int id)
			throws IOException, HttpClientException {
		Map<Integer, Schema> idSchemaMap = idCache.get(subject);
		if (idSchemaMap == null) {
			idSchemaMap = new HashMap<>();
			idCache.put(subject, idSchemaMap);
		}

		Schema cacheSchema = idSchemaMap.get(id);
		if (cacheSchema != null) {
			return cacheSchema;
		}

		final Schema retrievedSchema = getSchemaByIdFromRepo(id);
		idSchemaMap.put(id, retrievedSchema);

		return retrievedSchema;
	}

	private Schema getSchemaByIdFromRepo(final int id) throws IOException, HttpClientException {
		String schema = this.restManager.getById(id);
		return new Schema.Parser().parse(schema);
	}

	@Override
	public synchronized Integer getVersion(final String subject, final Schema schema)
			throws IOException, HttpClientException {
		Map<Schema, Integer> schemaVersionCache = versionCache.get(subject);
		if (schemaVersionCache == null) {
			schemaVersionCache = new HashMap<>();
			versionCache.put(subject, schemaVersionCache);
		}

		Integer versionCached = schemaVersionCache.get(schema);
		if (versionCached != null) {
			return versionCached;
		}

		if (schemaVersionCache.size() >= idMapCapacity) {
			throw new IllegalStateException(
					"too many schema objects created for " + subject + ", with schema " + schema);
		}

		final int retrievedVersion = getVersionFromRepo(subject, schema);
		schemaVersionCache.put(schema, retrievedVersion);

		return retrievedVersion;
	}

	private int getVersionFromRepo(final String subject, final Schema schema)
			throws IOException, HttpClientException {
		SchemaLookupResponse response = restManager.lookUpSubjectVersion(schema.toString(),
				subject);
		return (response.getVersion() == null ? -1 : response.getVersion().intValue());
	}

}
