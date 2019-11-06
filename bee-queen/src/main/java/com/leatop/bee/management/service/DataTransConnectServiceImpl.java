/**
 * File: DataTransConnectServiceImpl.java
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

import com.leatop.bee.management.dao.DataTransConnectDAO;
import com.leatop.bee.management.po.DataTransConnectEntity;
import com.leatop.bee.management.po.TopicEntity;

/**
 * 按kafka connector任务数据传输监控Service类
 * 
 * @author zlm
 *
 */
@Service("dataTransConnectService")
public class DataTransConnectServiceImpl implements DataTransConnectService {

	private final DataTransConnectDAO dataTransConnectDAO;

	@Autowired
	public DataTransConnectServiceImpl(final DataTransConnectDAO dataTransConnectDAO) {
		this.dataTransConnectDAO = dataTransConnectDAO;
	}

	/**
	 * 查询数据传输监控列表
	 * 
	 * @param 
	 * @return
	 */
	@Override
	public List<DataTransConnectEntity> queryDataTransConnect(final Map<String, Object> params) {
		return dataTransConnectDAO.queryDataTransConnect(params);
	}
	
	/**
	 * 查询数据传输监控总条数
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int countDataTransConnect(final Map<String, Object> params) {
		return dataTransConnectDAO.countDataTransConnect(params);
	}
	
	/**
	 * 添加任务
	 * 
	 * @param dataTransConnectEntity
	 */
	@Override
	public void insertDataTransConnect(final DataTransConnectEntity dataTransConnectEntity) {
			
		this.dataTransConnectDAO.insertDataTransConnect(dataTransConnectEntity);
	}

	/**
	 * 返回ID最大的任务
	 * 
	 * @return
	 */
	@Override
	public DataTransConnectEntity queryByMaxId() {
		
		return dataTransConnectDAO.queryByMaxId();
	}

}
