/**
 * File: HttpUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年6月12日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

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

import com.leatop.bee.common.auth.BasicAuthenticationProvider;
import com.leatop.bee.common.domain.ErrorMessage;
import com.leatop.bee.common.exception.HttpClientException;

/**
 * @author Dorsey
 *
 */
public class HttpUtils {

	private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);
	private static final Map<String, String> DEFAULT_REQUEST_PROPS;

	private static final int HTTP_CONNECT_TIMEOUT_MS = 60 * 1000;
	private static final int HTTP_READ_TIMEOUT_MS = 60 * 1000;
	private static final int JSON_PARSE_ERROR_CODE = 50005;
	private static final String APPLICATION_V2_JSON = "application/vnd.schemaregistry.v2+json";
	private static final String CONTENT_TYPE = "Content-Type";

	private final ObjectMapper jsonDeserializer = new ObjectMapper();
	private final String basicUri;
	private final int httpConnectTimeoutMs;
	private final int httpReadTimeoutMs;

	private SSLSocketFactory sslSocketFactory;
	private Map<String, String> reqHeaders;
	private BasicAuthenticationProvider basicAuthProvider;

	static {
		DEFAULT_REQUEST_PROPS = new HashMap<>();
		DEFAULT_REQUEST_PROPS.put(CONTENT_TYPE, APPLICATION_V2_JSON);
	}

	public HttpUtils(final String uri) {
		this(uri, HTTP_CONNECT_TIMEOUT_MS, HTTP_READ_TIMEOUT_MS);
	}

	public HttpUtils(final String basicUri, final int httpConnectTimeoutMs,
			final int httpReadTimeoutMs) {
		this.basicUri = basicUri;
		this.httpConnectTimeoutMs = httpConnectTimeoutMs;
		this.httpReadTimeoutMs = httpReadTimeoutMs;
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
	public <T> T sendHttpRequest(final String requestUrl, final String method,
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

			connection.setConnectTimeout(getHttpConnectTimeoutMs());
			connection.setReadTimeout(getHttpReadTimeoutMs());

			setupSSL(connection);
			connection.setRequestMethod(method);
			setBasicAuthRequestHeader(connection);
			setCustomHeaders(connection);

			// connection.getResponseCode() implicitly calls getInputStream, so
			// always set to true.
			// On the other hand, leaving this out breaks nothing.

			connection.setDoInput(true);
			if (requestProps != null) {
				for (Map.Entry<String, String> entry : requestProps.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
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

	public <T> T httpRequest(final String path, final String method, final byte[] requestBodyData,
			final Map<String, String> requestProperties, final TypeReference<T> respFormat)
			throws IOException, HttpClientException {
		String url = buildRequestUrl(basicUri, path);
		return sendHttpRequest(url, method, requestBodyData, requestProperties, respFormat);
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

	private String buildRequestUrl(final String basicUri, final String path) {
		return basicUri.replaceFirst("/$", "") + "/" + path.replaceFirst("^/", "");
	}

	/**
	 * @param basicAuthProvider
	 *            the basicAuthProvider to set
	 */
	public void setBasicAuthProvider(final BasicAuthenticationProvider basicAuthProvider) {
		this.basicAuthProvider = basicAuthProvider;
	}

	/**
	 * @return the httpConnectTimeoutMs
	 */
	public int getHttpConnectTimeoutMs() {
		return httpConnectTimeoutMs;
	}

	/**
	 * @return the httpReadTimeoutMs
	 */
	public int getHttpReadTimeoutMs() {
		return httpReadTimeoutMs;
	}

	/**
	 * @return the sslSocketFactory
	 */
	public SSLSocketFactory getSslSocketFactory() {
		return sslSocketFactory;
	}

	/**
	 * @param sslSocketFactory
	 *            the sslSocketFactory to set
	 */
	public void setSslSocketFactory(final SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}
}
