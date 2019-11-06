/**
 * File: ISchemaService.java
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
 * 按kafka topic主题数据传输监控Service类
 * 
 * @author zlm
 *
 */
public interface DataTransService {

	/**
	 * 查询数据传输监控列表
	 * 
	 * @param 
	 * @return
	 */
	List<DataTransEntity> queryDataTrans(final Map<String, Object> params);
	
	/**
	 * 查询数据传输监控总条数
	 * 
	 * @param params
	 * @return
	 */
	int countDataTrans(final Map<String, Object> params);
	
	/**
	 * 添加任务
	 * 
	 * @param dataTransConnectEntity
	 */
	void insertDataTrans(final DataTransEntity dataTransEntity);
	
	/**
	 * 返回ID最大的任务
	 * 
	 * @return
	 */
	public DataTransEntity queryByMaxId();
}
