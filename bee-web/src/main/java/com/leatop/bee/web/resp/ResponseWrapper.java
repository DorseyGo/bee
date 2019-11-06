/**
 * File: ResponeWrapper.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月29日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.resp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leatop.bee.web.domain.RespHeaderInfo;

/**
 * @author Dorsey
 *
 */
public class ResponseWrapper {

	private static final Logger LOG = LoggerFactory.getLogger(ResponseWrapper.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	public static void respondWithError(final HttpServletResponse response,
			final RespHeaderInfo headerInfo) {
		int statusCode = headerInfo.getStatusCode();
		if (statusCode <= 700) {
			// discard this
			return;
		}

		Errors errors = Errors.getErrors(statusCode);
		final String phase = errors.getErrorPhase();
		headerInfo.setContentLength(phase.length());
		setHeader(response, headerInfo);
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(phase);
		} catch (IOException ignore) {
			LOG.error("Failed to respond with phase: " + phase, ignore);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public static void respondWithSuccess(final HttpServletResponse response, final RespHeaderInfo header,
			final RespBody body) {
		PrintWriter writer = null;
		String json = null;
		try {
			json = mapper.writeValueAsString(body);
			
			header.setContentLength(json.length());
			String md5 = DigestUtils.md5Hex(json.getBytes());
			header.setMd5(md5);
			setHeader(response, header);

			writer = response.getWriter();
			writer.write(json);
		} catch (IOException ignore) {
			LOG.error("Failed to respond with : " + json, ignore);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public static void setHeader(final HttpServletResponse response, final RespHeaderInfo header) {
		response.setHeader(Constants.BINFILE_MD5, header.getMd5());
		response.setHeader(Constants.CONTENT_DISPOSITION, header.getContentDisposition());
		response.setHeader(Constants.CONTENT_ENCODING, header.getContentEncoding());
		response.setHeader(Constants.CONTENT_LENGTH, header.getContentLength() + "");
		response.setContentLength(header.getContentLength());
		response.setStatus(header.getStatusCode());
	}

	public enum Errors {
		FILE_PARSING_ERROR(702, Constants.FILE_PARSING_ERROR), BUSINESS_VALIDATION_ERROR(704,
				Constants.BUSINESS_VALIDATION_ERROR), MD5_VALIDATION_FAIL(951, Constants.MD5_VALIDATION_FAIL);

		private final int errorCode;
		private final String errorPhase;

		private Errors(final int errorCode, final String errorPhase) {
			this.errorCode = errorCode;
			this.errorPhase = errorPhase;
		}

		public static Errors getErrors(final int errorCode) {
			Errors[] errors = Errors.values();
			for (Errors error : errors) {
				if (error.getErrorCode() == errorCode) {
					return error;
				}
			}

			throw new IllegalArgumentException("No errors defined via code: " + errorCode);
		}

		/**
		 * @return the errorCode
		 */
		public int getErrorCode() {
			return errorCode;
		}

		/**
		 * @return the errorPhase
		 */
		public String getErrorPhase() {
			return errorPhase;
		}

	}

	private static interface Constants {

		String MD5_VALIDATION_FAIL = "文件md5校验失败";
		String CONTENT_LENGTH = "Content-Length";
		String CONTENT_ENCODING = "gzip";
		String CONTENT_DISPOSITION = "Content-Disposition";
		String BINFILE_MD5 = "binfile-md5";
		String FILE_PARSING_ERROR = "文件解释失败";
		String BUSINESS_VALIDATION_ERROR = "业务校验失败";
	}

}
