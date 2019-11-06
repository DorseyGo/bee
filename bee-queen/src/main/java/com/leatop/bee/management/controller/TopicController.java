/**
 * File: TopicController.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leatop.bee.management.conf.KafkaConfiguration;
import com.leatop.bee.management.constant.Constant;
import com.leatop.bee.management.page.Pagination;
import com.leatop.bee.management.po.TopicEntity;
import com.leatop.bee.management.resp.Resp;
import com.leatop.bee.management.service.TopicService;
import com.leatop.bee.management.util.KafkaUtils;

/**
 * kafka主题管理Controller类
 * 
 * @author zlm
 *
 */
@Controller
@RequestMapping("/topic")
public class TopicController {
	
	@Autowired
	private KafkaConfiguration config;
	
	private final TopicService topicService;
	
	@Autowired
	public TopicController(final TopicService topicService) {
		this.topicService = topicService;
	}
	
	/**
	 * 主题列表查看界面
	 * 
	 * @return
	 */
	@GetMapping("/list")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView("topic");
        return mv;
	}
	
	/**
	 * 主题数据列表加载查询
	 * 
	 * @param page
	 * @param limit
	 * @param name
	 * @return
	 * 
	 */
	@ResponseBody
	@GetMapping("/load_list")
	public Pagination<TopicEntity> load_list(final int page, final int limit, final String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("page", page);
		params.put("limit", limit);
		params.put("name", name);
		//主题不保存在数据库了，直接调用API接口读取主题
//		List<TopicEntity> topicEntities = topicService.queryTopics(params);
		Pagination<TopicEntity> topicsList = KafkaUtils.getAllTopicsMetadata(config.getZookeeperHost(), 
				config.getSessionTimeout(), config.getConnectTimeout(),config.getBootstrapServers(),page,limit);
		
		return topicsList;
	}
	
	/**
	 * 主题添加界面
	 * 
	 * @return
	 */
	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView mv = new ModelAndView("topic-add");
        return mv;
	}
	
	/**
	 * 主题修改界面
	 * 
	 * @param operator
	 * @param name
	 * @return
	 */
	@GetMapping("/edit")
	public ModelAndView edit(final String operator, final String name) {
		ModelAndView mv = new ModelAndView("topic-edit");
		//TopicEntity topicEntity = topicService.queryById(name);
		List<PartitionInfo> partitionInfos = KafkaUtils.getTopicMetadata(config.getBootstrapServers(), name);
		TopicEntity topicEntity = new TopicEntity();
		topicEntity.setName(name);
		topicEntity.setPartitions(partitionInfos.size());
		for(int i=0;i<partitionInfos.size();i++) {
			if (partitionInfos.get(i).partition()==partitionInfos.size()-1) {
				topicEntity.setReplicationFactor(partitionInfos.get(i).replicas().length);
			}
		}
//		topicEntity.setReplicationFactor(partitionInfos.size()>0?partitionInfos.get(0).replicas().length:0);
		mv.addObject("topicEntity", topicEntity);
		mv.addObject("operator", operator);
        return mv;
	}
	
	/**
	 * 主题保存操作
	 * 
	 * @param entity
	 * @param operator
	 * @return
	 */
	@ResponseBody
	@PostMapping("/save")
	public Resp save(@RequestBody final TopicEntity entity, final String operator) {
		Resp resp = Resp.OK;
		try {
			if (entity != null) {
				if (operator.equals("add")) {
					//调用kafka javaAPI创建主题
					resp = KafkaUtils.createTopic(config,entity);
					if (resp.getStatusCode()==Constant.SUCCESS) {
						//主题保存进数据库
//						topicService.insertTopic(entity);
					}
				}else {
					//调用kafka javaAPI修改主题，主要是分区和副本重分配
					resp = KafkaUtils.reallocateReplica(config,entity);
					if (resp.getStatusCode()==Constant.SUCCESS) {
						//主题保存进数据库
//						topicService.updateTopic(entity);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		
		return resp;
	}
	
	/**
	 * 删除主题
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@PostMapping("/delete")
	public Resp delete(final String ids) {
		Resp resp = Resp.OK;
		try {
			if (ids != null) {
				String[] idAttr = ids.split(",");
				for(String id : idAttr) {
//					TopicEntity topicEntity = topicService.queryById(id);
					//调用kafka javaAPI删除主题
					KafkaUtils.deleteTopic(config,id);
				}
				//主题移除数据库
//				topicService.deleteTopic(ids);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp = Resp.FAILURE.withPhase(e.getMessage());
		}
		
		return resp;
	}
}
