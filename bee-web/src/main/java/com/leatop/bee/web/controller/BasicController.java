/**
 * File: BasicController.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.leatop.bee.common.domain.FlagCarPassList;
import com.leatop.bee.common.domain.FlagCpcPassList;
import com.leatop.bee.common.domain.FlagEtcPassList;
import com.leatop.bee.common.domain.FlagRunStatus;
import com.leatop.bee.common.domain.FlagTollList;
import com.leatop.bee.common.domain.TrafficData;
import com.leatop.bee.web.conf.BeeWebConfiguration;
import com.leatop.bee.web.domain.ReqHeaderInfo;
import com.leatop.bee.web.domain.RespHeaderInfo;
import com.leatop.bee.web.resp.RespBody;
import com.leatop.bee.web.resp.ResponseWrapper;
import com.leatop.bee.web.serializer.ListJsonDeserializer;
import com.leatop.bee.web.serializer.ListJsonDeserializer.DeserializationException;
import com.leatop.bee.web.service.DataWeaveInManager;

/**
 * Provides common operations shared by its subclasses.
 * 
 * @author Dorsey
 *
 */
public class BasicController {

	protected final ListJsonDeserializer deserializer;
	protected final DataWeaveInManager manager;
	protected final BeeWebConfiguration config;
	protected final Map<String, String> LIST_TOPIC = new HashMap<String, String>();

	public BasicController(final ListJsonDeserializer deserializer,
			final DataWeaveInManager manager, final BeeWebConfiguration config) {
		this.deserializer = deserializer;
		this.manager = manager;
		this.config = config;
		initialize();
	}

	private void initialize() {
		LIST_TOPIC.put(FlagTollList.class.getSimpleName(), config.getFlagTollListTopic());
		LIST_TOPIC.put(FlagEtcPassList.class.getSimpleName(), config.getFlagEtcPassListTopic());
		LIST_TOPIC.put(FlagCarPassList.class.getSimpleName(), config.getFlagCarPassListTopic());
		LIST_TOPIC.put(FlagCpcPassList.class.getSimpleName(), config.getFlagCpcPassListTopic());
		LIST_TOPIC.put(FlagRunStatus.class.getSimpleName(), config.getFlagRunStatusTopic());
	}

	protected ReqHeaderInfo retrieveRequestHeader(final HttpServletRequest request) {
		final String md5 = request.getHeader(Constants.BINFILE_MD5);
		final String authCode = request.getHeader(Constants.BINFILE_AUTH);
		final String gzipStr = request.getHeader(Constants.BINFILE_GZIP);

		boolean gzip = false;
		try {
			gzip = Boolean.valueOf(gzipStr);
		} catch (NumberFormatException e) {
			gzip = false;
		}

		return new ReqHeaderInfo(md5, gzip, authCode);
	}

	protected String retrieveListName(final String filename) {
		final int underLineIndex = filename.indexOf(Constants.UNDER_LINE);
		if (underLineIndex >= 0) {
			final int secondUnderLineIndex = filename.indexOf(Constants.UNDER_LINE, underLineIndex);
			if (secondUnderLineIndex >= 0) {
				return filename.substring(underLineIndex + 1, secondUnderLineIndex);
			}
		}

		throw new IllegalArgumentException("file: " + filename + " invalid");
	}

	protected <ID> void doService(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		final ReqHeaderInfo reqHeaderInfo = retrieveRequestHeader(request);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		final MultipartFile multipartFile = multipartRequest.getFile(Constants.BINFILE);
		String filename = multipartRequest.getParameter(Constants.FILE_NAME);
		filename = filename.replace("_REQ_", "_RES_");

		RespHeaderInfo header = null;
		try {
			final String listName = retrieveListName(filename);
			// validation
			boolean integrity = validateIntegrity(reqHeaderInfo.getMd5(), multipartFile);
			if (integrity) {
				List<TrafficData<ID>> datas = deserializer.deserialize(listName,
						multipartFile.getInputStream());
				this.manager.weaveIn(LIST_TOPIC.get(listName), datas);
				// succeed
				header = new RespHeaderInfo(200).setContentDispositionWithFilename(filename);
				// FIXME if any info attribute
				RespBody body = new RespBody("");
				ResponseWrapper.respondWithSuccess(response, header, body);

				return;
			}

			// md5 not match
			header = new RespHeaderInfo(951);
			ResponseWrapper.respondWithError(response, header);
		} catch (DeserializationException e) {
			header = new RespHeaderInfo(702);
			ResponseWrapper.respondWithError(response, header);
			return;
		} catch (IOException e) {
			throw e;
		}
	}

	protected boolean validateIntegrity(final String expect, final MultipartFile actual)
			throws IOException {
		byte[] content = actual.getBytes();
		String actualMd5 = DigestUtils.md5Hex(content);

		return actualMd5.equals(expect);
	}

	/**
	 * The constants.
	 * 
	 * @author Dorsey
	 *
	 */
	protected static interface Constants {

		String UNDER_LINE = "_";
		String BINFILE_MD5 = "binfile_md5";
		String BINFILE_GZIP = "binfile_gzip";
		String BINFILE_AUTH = "binfile_auth";

		String BINFILE = "binFile";
		String FILE_NAME = "filename";
	}
}
