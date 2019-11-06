/**
 * File: TopicServiceImpl.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leatop.bee.management.dao.TopicDAO;
import com.leatop.bee.management.po.TopicEntity;
import com.leatop.bee.management.po.TopicEntity;
import com.leatop.bee.management.po.TopicEntity;

/**
 * 主题管理Service类
 * 
 * @author zlm
 *
 */
@Service("topicService")
public class TopicServiceImpl implements TopicService {

	private final TopicDAO topicDao;

	@Autowired
	public TopicServiceImpl(final TopicDAO topicDao) {
		this.topicDao = topicDao;
	}

	/**
	 * 查询主题列表
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<TopicEntity> queryTopics(final Map<String, Object> params) {
		return topicDao.queryTopics(params);
	}
	
	/**
	 * 查询主题总条数
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int countTopics(final Map<String, Object> params) {
		return topicDao.countTopics(params);
	}
	
	/**
	 * 添加主题
	 * 
	 * @param topicEntity
	 */
	@Override
	public void insertTopic(final TopicEntity topicEntity) {
			
		this.topicDao.insertTopic(topicEntity);
	}
	
	/**
	 * 修改主题
	 * 
	 * @param topicEntity
	 */
	@Override
	public void updateTopic(final TopicEntity topicEntity) {
			
		this.topicDao.updateTopic(topicEntity);
	}
	
	/**
	 * 根据ID获取主题
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public TopicEntity queryById(final String id) {
		Map<String, String> params = new HashMap<String, String>(1);
		params.put("name", id);
		
		return topicDao.queryById(params);
	}
	
	/**
	 * 删除主题
	 * 
	 * @param ids
	 */
	@Override
	public void deleteTopic(String ids) {
		String[] idAttr = ids.split(",");
		for(String id : idAttr) {
			this.topicDao.deleteTopic(Integer.valueOf(id));
		}
	}
}
