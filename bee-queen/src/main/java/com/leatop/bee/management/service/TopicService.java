/**
 * File: TopicService.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月5日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.management.service;

import java.util.List;
import java.util.Map;

import com.leatop.bee.management.po.DataTransEntity;
import com.leatop.bee.management.po.TopicEntity;

/**
 * 主题管理Service类
 * 
 * @author zlm
 *
 */
public interface TopicService {

	/**
	 * 查询主题列表
	 * 
	 * @param params
	 * @return
	 */
	List<TopicEntity> queryTopics(final Map<String, Object> params);
	
	/**
	 * 查询主题总条数
	 * 
	 * @param params
	 * @return
	 */
	int countTopics(final Map<String, Object> params);
	
	/**
	 * 添加主题
	 * 
	 * @param topicEntity
	 */
	void insertTopic(final TopicEntity topicEntity);
	
	/**
	 * 修改主题
	 * 
	 * @param topicEntity
	 */
	void updateTopic(final TopicEntity topicEntity);
	
	/**
	 * 根据ID获取主题
	 * 
	 * @param id
	 * @return
	 */
	public TopicEntity queryById(final String id);
	
	/**
	 * 删除主题
	 * 
	 * @param ids
	 */
	void deleteTopic(String ids);
}
