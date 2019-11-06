/**
 * File: DataTransServiceImpl.java
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

import com.leatop.bee.management.dao.DataTransDAO;
import com.leatop.bee.management.po.DataTransEntity;
import com.leatop.bee.management.po.TopicEntity;

/**
 * 按kafka topic主题数据传输监控Service类
 * 
 * @author zlm
 *
 */
@Service("dataTransService")
public class DataTransServiceImpl implements DataTransService {

	private final DataTransDAO dataTransDAO;

	@Autowired
	public DataTransServiceImpl(final DataTransDAO dataTransDAO) {
		this.dataTransDAO = dataTransDAO;
	}

	/**
	 * 查询数据传输监控列表
	 * 
	 * @param 
	 * @return
	 */
	@Override
	public List<DataTransEntity> queryDataTrans(final Map<String, Object> params) {
		return dataTransDAO.queryDataTrans(params);
	}
	
	/**
	 * 查询数据传输监控总条数
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int countDataTrans(final Map<String, Object> params) {
		return dataTransDAO.countDataTrans(params);
	}
	
	/**
	 * 添加任务
	 * 
	 * @param dataTransConnectEntity
	 */
	@Override
	public void insertDataTrans(final DataTransEntity dataTransEntity) {
			
		this.dataTransDAO.insertDataTrans(dataTransEntity);
	}
	
	/**
	 * 返回ID最大的任务
	 * 
	 * @return
	 */
	@Override
	public DataTransEntity queryByMaxId() {
		
		return dataTransDAO.queryByMaxId();
	}

}
