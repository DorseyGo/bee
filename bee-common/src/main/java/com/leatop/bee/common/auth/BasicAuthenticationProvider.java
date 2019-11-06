/**
 * File: BasicAuthenticationProvider.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.auth;

import java.net.URL;

/**
 * @author Dorsey
 *
 */
public interface BasicAuthenticationProvider {

	String alias();

	String getUserInfo(URL url);

	/**
	 * Constants.
	 * 
	 * @author Dorsey
	 *
	 */
	public static interface Constants {

		public static final String USER_INFO_CONFIG = "basic.auth.user.info";
		public static final String BASIC_AUTH_PROVIDER_SOURCE = "basic.auth.provider";
	}
}