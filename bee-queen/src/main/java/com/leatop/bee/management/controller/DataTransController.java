/**
 * File: DataTransController.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leatop.bee.management.util.cache.Cache;
import com.leatop.bee.management.util.cache.CacheManager;
import com.leatop.bee.common.log.LogFacade;
import com.leatop.bee.management.constant.Constant;
import com.leatop.bee.management.page.Pagination;
import com.leatop.bee.management.po.DataTransConnectEntity;
import com.leatop.bee.management.po.DataTransEntity;
import com.leatop.bee.management.resp.Resp;
import com.leatop.bee.management.service.DataTransConnectService;
import com.leatop.bee.management.service.DataTransService;

/**
 * 数据传输监控Controller类
 * 
 * @author zlm
 *
 */
@Controller
@RequestMapping("/datatrans")
public class DataTransController {

	private LogFacade LOGGER = LogFacade.getFacade(DataTransController.class);
	
	private final DataTransService dataTransService;
	
	private final DataTransConnectService dataTransConnectService;

	@Autowired
	public DataTransController(final DataTransService dataTransService,final DataTransConnectService dataTransConnectService) {
		this.dataTransService = dataTransService;
		this.dataTransConnectService = dataTransConnectService;
	}

	/**
	 * 按kafka的topic主题数据传输接口实时监控界面
	 * 
	 * @param topicName
	 * @return
	 * 
	 */
	@GetMapping("/list")
	public ModelAndView list(final String topicName) {
		ModelAndView mv = new ModelAndView("datatrans");
		mv.addObject("topicName",topicName);
        return mv;
	}
	
	/**
	 * 按kafka的topic主题数据传输接口历史查询界面
	 * 
	 * @param topicName
	 * @return
	 * 
	 */
	@GetMapping("/list_history")
	public ModelAndView list_history(final String topicName) {
		ModelAndView mv = new ModelAndView("datatrans-history");
		mv.addObject("topicName",topicName);
        return mv;
	}
	
	/**
	 * 按kafka的connector任务数据传输任务传输记录界面
	 * 
	 * @param connectName
	 * @return
	 * 
	 */
	@GetMapping("/list_connect")
	public ModelAndView list_connect(final String connectName) {
		ModelAndView mv = new ModelAndView("datatransconnect");
		mv.addObject("connectName",connectName);
        return mv;
	}
	
	/**
	 * 按kafka的topic主题数据传输记录查询加载
	 * 
	 * @param page
	 * @param limit
	 * @param topicName
	 * @param totalFlag
	 * @return
	 * 
	 */
	@ResponseBody
	@GetMapping("/load_list")
	public Pagination<DataTransEntity> load_list(final int page, final int limit,final String topicName,boolean totalFlag) {
		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		params.put("limit", limit);
		params.put("topicName", topicName);
		List<DataTransEntity> dataTransEntities = dataTransService.queryDataTrans(params);
		Pagination<DataTransEntity> res = new Pagination<>();
		res.setCode(0);
		res.setCount(dataTransService.countDataTrans(params));
		res.setData(dataTransEntities);
		
		return res;
	}
	/**
	 * 按kafka的connect任务数据传输记录查询
	 * 
	 * @param page
	 * @param limit
	 * @param connectName
	 * @return
	 */
	@ResponseBody
	@GetMapping("/load_list_connect")
	public Pagination<DataTransConnectEntity> load_list_connect(final int page, final int limit,final String connectName) {
		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		params.put("limit", limit);
		params.put("connectName", connectName);
		
		List<DataTransConnectEntity> dataTransConnectEntities = dataTransConnectService.queryDataTransConnect(params);
		
		Pagination<DataTransConnectEntity> res = new Pagination<>();
		res.setCode(0);
		res.setCount(dataTransConnectService.countDataTransConnect(params));
		res.setData(dataTransConnectEntities);
		
		return res;
	}
	
	/**
	 * 按主题，数据传输写入kafka总数据量的接口。建议每3秒调用一次，接收参数：topicName(主题名称),transCount(已经传输数据量)
	 * 调用url：/datatrans/saveToCache?topicName=xxx&transCount=111
	 * 
	 * @param topicName
	 * @param transCount
	 * @return
	 */
	@ResponseBody
	@GetMapping("/saveToCache")
	public Resp saveToCache(final String topicName, final String transCount) {
		Resp resp = Resp.OK.withPhase("success");
		try {
			Cache cache = CacheManager.get(Constant.TOPICCACHE);
			cache.add(topicName, transCount);
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		
		return resp;
	}
	
	/**
	 * 读取主题总传输量
	 * 
	 * @param topicName
	 * @return
	 * 
	 */
	@ResponseBody
	@GetMapping("/readForCache")
	public Resp readForCache(final String topicName) {
		Resp resp = Resp.OK.withPhase("success");
		try {
			Cache cache = CacheManager.get(Constant.TOPICCACHE);
			String result = cache.get(topicName);
			if (result != null) {
				resp.withPhase(result);
			}else {
				resp.withPhase("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		
		return resp;
	}
	
	/**
	 * 按主题，数据传输写入kafka的接口。建议每小时调用一次，接收参数：topicName(主题名称),successCount(成功条数),errorCount(失败条数)
	 * 调用url：/datatrans/saveToKafka?topicName=xxx&successCount=111&errorCount=111
	 * 
	 * @param hostName
	 * @param topicName
	 * @param successCount
	 * @param errorCount
	 * 
	 */
	@ResponseBody
	@GetMapping("/saveToKafka")
	public Resp saveToKafka(final String hostName,final String topicName, final int successCount, final int errorCount) {
		LOGGER.info("/saveToKafka");
		Resp resp = Resp.OK.withPhase("success");
		try {
			Cache cache = CacheManager.get(Constant.TOPICCACHE);
			DataTransEntity entity = new DataTransEntity();
			entity.setHostName(hostName);
			entity.setTopicName(topicName);
			//entity.setStartTime(dataTransService.queryByMaxId()==null?new Date():dataTransService.queryByMaxId().getEndTime());//该批数据传输开始时间取上一批数据的传输结束时间
			Date result = cache.get(hostName);///该批数据传输开始时间取缓存中hostName对应的日期
			Date date = new Date();
			if (result != null) {
				entity.setStartTime(result);
			}else {
				entity.setStartTime(date);
			}
			cache.add(hostName, date);//更新cache
			entity.setEndTime(date);//该批数据传输结束时间取当前时间
			entity.setTransCount(successCount+errorCount);
			entity.setSuccessCount(successCount);
			entity.setErrorCount(errorCount);
			dataTransService.insertDataTrans(entity);
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		
		return resp;
	}
	
	/**
	 * 按主题，数据传输入存储介质，目前只支持JDBC、HDFS，ES的接口。建议每小时调用一次，接收参数：connectorName(connect任务名称),successCount(成功条数),errorCount(失败条数)
	 * 调用url：/datatrans/saveToObject?connectName=xxx&successCount=111&errorCount=111
	 * 备注：失败数没有意义，去掉了，只记录入库成功的
	 * 
	 * @param connectorName
	 * @param successCount
	 * 
	 */
	@ResponseBody
	@GetMapping("/saveToObject")
	public Resp saveToObject(final String connectorName, final int successCount) {
		Resp resp = Resp.OK.withPhase("success");
		try {
			DataTransConnectEntity entity = new DataTransConnectEntity();
			entity.setConnectName(connectorName);
			entity.setStartTime(dataTransConnectService.queryByMaxId()==null?new Date():dataTransConnectService.queryByMaxId().getEndTime());//该批数据传输开始时间取上一批数据的传输结束时间
			entity.setEndTime(new Date());//该批数据传输结束时间取当前时间
			entity.setSuccessCount(successCount);
			//entity.setErrorCount(errorCount);
			//entity.setTransCount(successCount+errorCount);//传输条数
			dataTransConnectService.insertDataTransConnect(entity);
			LOGGER.info("String saveToObject success: {}",entity);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("String saveToObject error:"+e.getMessage());
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		
		return resp;
	}
}
