package com.leatop.bee.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leatop.bee.management.conf.ConnectorsConfiguration;
import com.leatop.bee.management.constant.Constant;
import com.leatop.bee.management.dao.DataWriteDAO;
import com.leatop.bee.management.po.DataWrite;
import com.leatop.bee.management.resp.Resp;

/**
 * 任务配置管理Service类
 * 
 * @author zlm
 *
 */
@Service
public class DataWriteServiceImpl implements DataWriteService {
	
	private final DataWriteDAO dataWriteDAO;
	private final ConnectorsConfiguration connectorsConfiguration;
	
	@Autowired
	public DataWriteServiceImpl(final DataWriteDAO dataWriteDAO,final ConnectorsConfiguration connectorsConfiguration) {
		this.dataWriteDAO = dataWriteDAO;
		this.connectorsConfiguration = connectorsConfiguration;
	}

	/**
	 * 添加任务
	 * 
	 * @param dataWrite
	 * @return
	 */
	@Override
	public int addDataWrite(DataWrite dataWrite) {
		return dataWriteDAO.addDataWrite(dataWrite);
	}

	/**
	 * 查询任务列表
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<DataWrite> queryDataWrite(Map<String, Object> params) {
		List<DataWrite> dataWrites = dataWriteDAO.queryDataWrite(params);
		for(DataWrite dataWrite : dataWrites) {
			Resp resp = connectorsConfiguration.getConnectorsStatus(dataWrite.getName());
			if (resp.getStatusCode()==Resp.OK.getStatusCode()) {
				dataWrite.setIsSuccess(Constant.SUCCESS);
			}else {
				dataWrite.setIsSuccess(Constant.ERROR);
			}
		}
		return dataWrites;
	}

	/**
	 * 查询任务总条数
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int countDataWrite(Map<String, Object> params) {
		return dataWriteDAO.countDataWrite(params);
	}

	/**
	 * 删除任务
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public int deleteDataWrite(int id) {
		return dataWriteDAO.deleteDataWrite(id);
	}
	
	/**
	 * 更改任务状态
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public int updateStatus(final Map<String, Object> params) {
		return dataWriteDAO.updateStatus(params);
	}
	
	/**
	 * 修改任务
	 * 
	 * @param dataWrite
	 * @return
	 */
	public int updateDataWrite(DataWrite dataWrite) {
		return dataWriteDAO.updateDataWrite(dataWrite);
	}

	/**
	 * 根据ID获取任务
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public DataWrite getDataWrite(int id) {
		return dataWriteDAO.getDataWrite(id);
	}

}
