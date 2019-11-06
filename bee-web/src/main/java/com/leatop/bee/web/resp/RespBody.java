/**
 * File: RespBody.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月29日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.web.resp;

import java.util.Date;

import com.leatop.bee.common.utils.DateUtils;

/**
 * @author Dorsey
 *
 */
public class RespBody {

	private final String info;
	private final String receiveTime;
	private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	
	public RespBody(final String info) {
		this(info, DateUtils.format(new Date(), PATTERN));
	}

	public RespBody(final String info, final String receiveTime) {
		this.info = info;
		this.receiveTime = receiveTime;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @return the receiveTime
	 */
	public String getReceiveTime() {
		return receiveTime;
	}

}
