/**
 * File: ListController.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.controller;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leatop.bee.common.utils.ElasticSearchUtils;
import com.leatop.bee.web.resp.Resp;

/**
 * ElasticSearch中查询
 * 
 * @author hongSheng
 *
 */
@RestController
@RequestMapping("/query")
public class ElasticSearchController {

	// ~~~ fields
	// ==================================================
	
	private static final Logger LOGGER = Logger.getLogger(ElasticSearchController.class.getName());

	// ~~~ constructors
	// ==================================================

	// ~~~ methods
	// ==================================================
	@ResponseBody
	@PostMapping("/etc/trade")
	public Resp queryETCTradeList() {

		boolean succeed = false;
		Resp resp = (succeed) ? Resp.OK : Resp.FAILURE.withPhase("Failed to sync data");
		return resp;
	}
	
	/**
	 * passList中通过主键serialNo的值进行查询
	 * 
	 * @param value
	 * @return
	 */
	@ResponseBody
	@PostMapping("/etc/pass")
	public Resp queryETCPassListAvro(String value) {
		final String indexName = "po_01";
		
		final String esType = "ETCPassList";
		
		final String field = "serialNo.keyword";
		ElasticSearchUtils.getInstance().query(indexName, esType, field, value);
		boolean succeed = false;
		Resp resp = (succeed) ? Resp.OK : Resp.FAILURE.withPhase("Failed to sync data");
		return resp;
	}

	
}
