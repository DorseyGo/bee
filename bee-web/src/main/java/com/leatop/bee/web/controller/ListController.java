package com.leatop.bee.web.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.leatop.bee.common.domain.ETCPassList;
import com.leatop.bee.common.domain.ETCTradeList;
import com.leatop.bee.common.domain.ImageList;
import com.leatop.bee.common.utils.DateUtils;
import com.leatop.bee.web.conf.BeeWebConfiguration;
import com.leatop.bee.web.resp.Resp;
import com.leatop.bee.web.service.DataWeaveInManager;

/**
 * Main entry point for uploading traffic list to <tt>road center</tt> or
 * <tt>province center</tt>.
 * 
 * @author Dorsey
 *
 */
@RestController
@RequestMapping("/list")
public class ListController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ListController.class);

	// ~~~ fields
	// ==================================================
	private final DataWeaveInManager dataWeaveInManager;
	private final BeeWebConfiguration config;

	// ~~~ constructors
	// ==================================================
	@Autowired
	public ListController(final DataWeaveInManager dataWeaveInManager,
			final BeeWebConfiguration config) {
		this.dataWeaveInManager = dataWeaveInManager;
		this.config = config;
	}

	// ~~~ methods
	// ==================================================
	/**
	 *	ETCTradeList上传接口
	 * 
	 * @param etcTradeList
	 * @return
	 */
	@ResponseBody
	@PostMapping("/etc/trade")
	public Resp uploadETCTradeList(@RequestBody final ETCTradeList etcTradeList) {
		if (etcTradeList == null) {
			return Resp.FAILURE.withPhase("Argument etc trade list MUST not be null");
		}

		final String topic = config.getEtcTradeListTopic();
		Date opTime = etcTradeList.getOpTime();
		final String key = DateUtils.format(opTime);

		boolean succeed = this.dataWeaveInManager.weaveIn(topic, etcTradeList.getId(), etcTradeList);
		Resp resp = (succeed) ? Resp.OK : Resp.FAILURE.withPhase("Failed to sync data");
		return resp;
	}
	
	/**
	 * 	ETCPassList上传接口
	 * 
	 * @param etcPassList
	 * @return
	 */
	@ResponseBody
	@PostMapping("/etc/pass")
	public Resp uploadETCPassList(@RequestBody final ETCPassList etcPassList) {
		if (etcPassList == null) {
			return Resp.FAILURE.withPhase("Argument etc trade list MUST not be null");
		}

		final String topic = config.getEtcPassListTopic();
		/**
		if(topic==null) {
			LOGGER.info("in method /list/etc/pass topic is null");
		}else {
			LOGGER.info("in method /list/etc/pass topic is not  null");
		}
		**/
		boolean succeed = this.dataWeaveInManager.weaveIn(topic, etcPassList.getId(), etcPassList);
		Resp resp = (succeed) ? Resp.OK : Resp.FAILURE.withPhase("Failed to sync data");
		return resp;
	}

	/**
	 *	ImageList上传接口
	 * 
	 * @param imageList
	 * @return
	 */
	@ResponseBody
	@PostMapping("/image")
	public Resp uploadETCPassList(@RequestBody final ImageList imageList) {
		if (imageList == null) {
			return Resp.FAILURE.withPhase("Argument image list MUST not be null");
		}

		final String topic = config.getImageListTopic();

		boolean succeed = this.dataWeaveInManager.weaveIn(topic, imageList.getId(), imageList);
		Resp resp = (succeed) ? Resp.OK : Resp.FAILURE.withPhase("Failed to sync data");
		return resp;
	}
}
