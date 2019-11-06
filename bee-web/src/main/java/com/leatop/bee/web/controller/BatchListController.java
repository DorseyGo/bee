/**
 * File: BatchListController.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月28日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.leatop.bee.web.conf.BeeWebConfiguration;
import com.leatop.bee.web.serializer.ListJsonDeserializer;
import com.leatop.bee.web.service.DataWeaveInManager;

/**
 *	 批量提交数据
 * 
 * @author Dorsey
 *
 */
@Controller
public class BatchListController extends BasicController {

	@Autowired
	public BatchListController(final ListJsonDeserializer deserializer,
			final DataWeaveInManager manager, final BeeWebConfiguration config) {
		super(deserializer, manager, config);
	}
	
	/**
	 * ETC 交易流水/交易凭证表接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/flag/toll/list")
	public void uploadFlagTollLists(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doService(request, response);
	}
	
	/**
	 * 	车辆图像记录表接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/flag/car/pass/list")
	public void uploadFlagCarPassLists(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doService(request, response);
	}
	
	/**
	 * CPC 卡通行记录表接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/flag/cpc/pass/list")
	public void uploadFlagCpcPassLists(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doService(request, response);
	}
	
	/**
	 * ETC 通行记录表接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/flag/etc/pass/list")
	public void uploadFlagEtcPassLists(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doService(request, response);
	}
	
	/**
	 * ETC 门架自由流运行状态表
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@PostMapping("/flag/run/status")
	public void uploadFlagRunStatuses(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		doService(request, response);
	}
}
