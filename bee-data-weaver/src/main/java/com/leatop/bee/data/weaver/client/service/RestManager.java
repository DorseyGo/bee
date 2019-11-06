/**
 * File: RestManager.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.client.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leatop.bee.data.weaver.auth.BasicAuthenticationProvider;
import com.leatop.bee.data.weaver.client.domain.ErrorMessage;
import com.leatop.bee.data.weaver.client.exception.HttpClientException;
import com.leatop.bee.data.weaver.client.request.SchemaLookupResponse;
import com.leatop.bee.data.weaver.client.request.SchemaRegistryRequest;
import com.leatop.bee.data.weaver.client.request.SchemaRegistryResponse;
import com.leatop.bee.data.weaver.client.request.SchemaRetreivalResponse;

/**
 * @author Dorsey
 *
 */
public class RestManager {

	private static final Logger LOG = LoggerFactory.getLogger(RestManager.class);
	private static final Map<String, String> DEFAULT_REQUEST_PROPS;

	private static final int HTTP_CONNECT_TIMEOUT_MS = 60 * 1000;
	private static final int HTTP_READ_TIMEOUT_MS = 60 * 1000;
	private static final int JSON_PARSE_ERROR_CODE = 50005;

	private final ObjectMapper jsonDeserializer = new ObjectMapper();
	private final String basicUri;
	private SSLSocketFactory sslSocketFactory;
	private Map<String, String> reqHeaders;
	private BasicAuthenticationProvider basicAuthProvider;

	// ~~~ constants
	// ==================================================================================
	private static final TypeReference<SchemaRetreivalResponse> RETRIVAL_SCHEMA_RESPONSE_TYPE = new TypeReference<SchemaRetreivalResponse>() {
	};

	private static final TypeReference<SchemaLookupResponse> LOOKUP_SCHEMA_RESPONSE_TYPE = new TypeReference<SchemaLookupResponse>() {
	};

	private static final TypeReference<SchemaRegistryResponse> REGISTRY_SCHEMA_RESPONSE_TYPE = new TypeReference<SchemaRegistryResponse>() {
	};

	static {
		DEFAULT_REQUEST_PROPS = new HashMap<>();
		DEFAULT_REQUEST_PROPS.put(Constants.CONTENT_TYPE, Constants.APPLICATION_V2_JSON);
	}

	/**
	 * Constructor of {@link RestManager}, with uri specified.
	 * 
	 * @param basicUri
	 *            the uri.
	 */
	public RestManager(final String basicUri) {
		this.basicUri = basicUri;
	}

	/**
	 * 
	 * @param <T>
	 * @param requestUrl
	 *            the url to issue the request.
	 * @param method
	 *            http method, which is "GET", "POST", "PUT" etc.
	 * @param reqBodyData
	 *            request body.
	 * @param requestProps
	 *            header properties.
	 * @param respFormat
	 *            response format.
	 * @return
	 * @throws HttpClientException
	 * @throws IOException
	 *             if any I/O error detected.
	 */
	private <T> T sendHttpRequest(final String requestUrl, final String method,
			final byte[] reqBodyData, final Map<String, String> requestProps,
			final TypeReference<T> respFormat) throws HttpClientException, IOException {
		String reqData = reqBodyData == null ? "null"
				: new String(reqBodyData, StandardCharsets.UTF_8);
		LOG.debug(String.format("Sending %s with input %s to %s", method, reqData, requestUrl));

		HttpURLConnection connection = null;
		InputStream is = null;
		try {
			URL url = new URL(requestUrl);
			connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(HTTP_CONNECT_TIMEOUT_MS);
			connection.setReadTimeout(HTTP_READ_TIMEOUT_MS);

			setupSSL(connection);
			connection.setRequestMethod(method);
			setBasicAuthRequestHeader(connection);
			setCustomHeaders(connection);

			// connection.getResponseCode() implicitly calls getInputStream, so
			// always set to true.
			// On the other hand, leaving this out breaks nothing.

			connection.setDoInput(true);
			for (Map.Entry<String, String> entry : requestProps.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}

			connection.setUseCaches(false);
			if (reqBodyData != null) {
				connection.setDoOutput(true);
				OutputStream os = null;
				try {
					os = connection.getOutputStream();
					os.write(reqBodyData);
					os.flush();
				} catch (IOException e) {
					LOG.error("Failed to send HTTP request to endpoint: " + url, e);
					throw e;
				} finally {
					if (os != null) {
						os.close();
					}
				}
			}

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				is = connection.getInputStream();
				T result = jsonDeserializer.readValue(is, respFormat);

				return result;
			}

			if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
				return null;
			}

			is = connection.getErrorStream();
			ErrorMessage errorMessage;
			try {
				errorMessage = jsonDeserializer.readValue(is, ErrorMessage.class);
			} catch (JsonProcessingException e) {
				errorMessage = new ErrorMessage(JSON_PARSE_ERROR_CODE, e.getMessage());
			}

			throw new HttpClientException(errorMessage.getMessage(), responseCode,
					errorMessage.getErrorCode());

		} finally {
			if (is != null) {
				is.close();
			}

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * @param connection
	 */
	private void setCustomHeaders(final HttpURLConnection connection) {
		if (reqHeaders != null) {
			Entry<String, String> entry = null;
			for (Iterator<Entry<String, String>> iter = reqHeaders.entrySet().iterator(); iter
					.hasNext();) {
				entry = iter.next();
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * @param connection
	 */
	private void setBasicAuthRequestHeader(final HttpURLConnection connection) {
		String userInfo = null;
		if (basicAuthProvider != null
				&& ((userInfo = basicAuthProvider.getUserInfo(connection.getURL())) != null)) {
			String auth = Base64.encodeBase64String(userInfo.getBytes(StandardCharsets.UTF_8));
			connection.setRequestProperty("Authorization", "Basic " + auth);
		}
	}

	/**
	 * @param connection
	 */
	private void setupSSL(final HttpURLConnection connection) {
		if (connection instanceof HttpsURLConnection && sslSocketFactory != null) {
			((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
		}
	}

	// ~~~~ setters
	// ===================================================================================

	/**
	 * @param sslSocketFactory
	 *            the sslSocketFactory to set
	 */
	public void setSslSocketFactory(final SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}

	/**
	 * @param reqHeaders
	 *            the reqHeaders to set
	 */
	public void setReqHeaders(final Map<String, String> reqHeaders) {
		this.reqHeaders = reqHeaders;
	}

	/**
	 * @param basicAuthProvider
	 *            the basicAuthProvider to set
	 */
	public void setBasicAuthProvider(final BasicAuthenticationProvider basicAuthProvider) {
		this.basicAuthProvider = basicAuthProvider;
	}

	// ~~~ inner class
	// ===========================================================================================

	private static interface Constants {

		String CONTENT_TYPE = "Content-Type";
		String APPLICATION_V2_JSON = "application/vnd.schemaregistry.v2+json";
	}

	public int registerSchema(final SchemaRegistryRequest req, final String subject)
			throws IOException, HttpClientException {
		return registerSchema(DEFAULT_REQUEST_PROPS, req, subject);
	}

	public int registerSchema(final Map<String, String> reqProps,
			final SchemaRegistryRequest registerSchemaRequest, final String subject)
			throws IOException, HttpClientException {
		String path = String.format("/subjects/%s/versions", subject);

		SchemaRegistryResponse response = httpRequest(path, "POST",
				registerSchemaRequest.toJson().getBytes(StandardCharsets.UTF_8), reqProps,
				REGISTRY_SCHEMA_RESPONSE_TYPE);

		return response.getId();
	}

	private <T> T httpRequest(final String path, final String method, final byte[] requestBodyData,
			final Map<String, String> requestProperties, final TypeReference<T> respFormat)
			throws IOException, HttpClientException {
		String url = buildRequestUrl(basicUri, path);
		return sendHttpRequest(url, method, requestBodyData, requestProperties, respFormat);
	}

	private String buildRequestUrl(final String basicUri, final String path) {
		return basicUri.replaceFirst("/$", "") + "/" + path.replaceFirst("^/", "");
	}

	public int registerSchema(final String schema, final String subject)
			throws IOException, HttpClientException {
		SchemaRegistryRequest request = new SchemaRegistryRequest();
		request.setSchema(schema);

		return registerSchema(request, subject);
	}

	public int registerSchema(final String schema, final String subject, final int version,
			final int id) throws IOException, HttpClientException {
		SchemaRegistryRequest request = new SchemaRegistryRequest();
		request.setSchema(schema);
		request.setId(id);
		request.setVersion(version);

		return registerSchema(request, subject);
	}

	public SchemaLookupResponse lookUpSubjectVersion(final String schema, final String subject)
			throws IOException, HttpClientException {
		SchemaRegistryRequest request = new SchemaRegistryRequest();
		request.setSchema(schema);
		return lookUpSubjectVersion(request, subject);
	}

	public SchemaLookupResponse lookUpSubjectVersion(
			final SchemaRegistryRequest registerSchemaRequest, final String subject)
			throws IOException, HttpClientException {
		return lookUpSubjectVersion(DEFAULT_REQUEST_PROPS, registerSchemaRequest, subject);
	}

	public SchemaLookupResponse lookUpSubjectVersion(Map<String, String> requestProperties,
			final SchemaRegistryRequest schemaRegistryReq, final String subject)
			throws IOException, HttpClientException {
		String path = String.format("/subjects/%s", subject);
		if (requestProperties.isEmpty()) {
			requestProperties = DEFAULT_REQUEST_PROPS;
		}

		SchemaLookupResponse schema = httpRequest(path, "POST",
				schemaRegistryReq.toJson().getBytes(StandardCharsets.UTF_8), requestProperties,
				LOOKUP_SCHEMA_RESPONSE_TYPE);

		return schema;
	}

	public String getById(final int id) throws IOException, HttpClientException {
		return getById(DEFAULT_REQUEST_PROPS, id);
	}

	public String getById(final Map<String, String> reqProps, final int id)
			throws IOException, HttpClientException {
		return getSchemaById(reqProps, id).getSchema();
	}

	private SchemaRetreivalResponse getSchemaById(final Map<String, String> reqProps, final int id)
			throws IOException, HttpClientException {
		String path = String.format("/schemas/ids/%d", id);
		return httpRequest(path, "GET", null, reqProps, RETRIVAL_SCHEMA_RESPONSE_TYPE);
	}
}
