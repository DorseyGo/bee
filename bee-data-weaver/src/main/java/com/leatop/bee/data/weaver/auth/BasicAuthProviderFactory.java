/**
 * File: BasicAuthProviderFactory.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.auth;

import java.util.Map;

/**
 * @author Dorsey
 *
 */
public class BasicAuthProviderFactory {

	public static BasicAuthenticationProvider getAuthProvider(final String type,
			final Map<String, ?> configs) {
		AuthType authType = AuthType.of(type);
		BasicAuthenticationProvider provider = authType.provider;
		if (provider != null) {
			provider.configure(configs);
		}

		return provider;
	}

	public static enum AuthType {

		URL(new UrlBasicAuthenticationProvider()), USER_INFO(new UserInfoAuthenticationProvider());

		private final BasicAuthenticationProvider provider;
		private final String alias;

		private AuthType(final BasicAuthenticationProvider provider) {
			this.provider = provider;
			this.alias = this.provider.alias();
		}

		public static AuthType of(final String type) {
			AuthType[] authTypes = AuthType.values();
			for (AuthType authType : authTypes) {
				if (authType.alias.equalsIgnoreCase(type)) {
					return authType;
				}
			}

			throw new IllegalArgumentException("No authentication provider found by type: " + type);
		}
	}
}
