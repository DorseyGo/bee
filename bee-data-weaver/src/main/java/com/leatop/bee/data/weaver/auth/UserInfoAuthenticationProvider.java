/**
 * File: UserInfoAuthenticationProvider.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.auth;

import java.net.URL;
import java.util.Map;

import org.apache.kafka.common.config.ConfigException;

/**
 * @author Dorsey
 *
 */
public class UserInfoAuthenticationProvider implements BasicAuthenticationProvider {

	private String userInfo;

	@Override
	public void configure(final Map<String, ?> configs) {
		userInfo = (String) configs.get(Constants.USER_INFO_CONFIG);
		if (userInfo == null) {
			throw new ConfigException(
					Constants.USER_INFO_CONFIG + ", should be configured in configuration");
		}
	}

	@Override
	public String alias() {
		return "USER_INFO";
	}

	@Override
	public String getUserInfo(final URL url) {
		return userInfo;
	}

}
