package com.leatop.bee.management.service;

import java.util.List;
import java.util.Map;


import com.leatop.bee.management.po.DataWrite;

/**
 * 任务配置管理Service类
 * 
 * @author zlm
 *
 */
public interface DataWriteService {
	
	/**
	 * 添加任务
	 * 
	 * @param dataWrite
	 * @return
	 */
	int addDataWrite(DataWrite dataWrite);
	
	/**
	 * 查询任务列表
	 * 
	 * @param params
	 * @return
	 */
	List<DataWrite> queryDataWrite(final Map<String, Object> params);
	
	/**
	 * 查询任务总条数
	 * 
	 * @param params
	 * @return
	 */
	int countDataWrite(final Map<String, Object> params);
	
	/**
	 * 删除任务
	 * 
	 * @param id
	 * @return
	 */
	int deleteDataWrite(final int id);
	
	/**
	 * 修改任务
	 * 
	 * @param dataWrite
	 * @return
	 */
	int updateDataWrite(DataWrite dataWrite);
	
	/**
	 * 根据ID获取任务
	 * 
	 * @param id
	 * @return
	 */
	DataWrite getDataWrite(final int id);
	
	/**
	 * 更改任务状态
	 * 
	 * @param params
	 * @return
	 */
	int updateStatus(final Map<String, Object> params);
}
