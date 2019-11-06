/**
 * File: UrlBasicAuthenticationProvider.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.auth;

import java.net.URL;
import java.util.Map;

/**
 * Authentication based on the url.
 * 
 * @author Dorsey
 *
 */
public class UrlBasicAuthenticationProvider implements BasicAuthenticationProvider {

	@Override
	public void configure(final Map<String, ?> configs) {
		// emtpy.
	}

	@Override
	public String alias() {
		return "URL";
	}

	@Override
	public String getUserInfo(final URL url) {
		return url.getUserInfo();
	}

}
