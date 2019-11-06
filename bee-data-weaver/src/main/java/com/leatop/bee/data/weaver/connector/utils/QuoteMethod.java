/**
 * File: QuoteMethod.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.utils;

/**
 * Enumeration for quote method, like <tt>always</tt>, <tt>never</tt>.
 * 
 * @author Dorsey
 *
 */
public enum QuoteMethod {
	ALWAYS("always"), NEVER("never");

	private final String name;

	private QuoteMethod(final String name) {
		this.name = name;
	}

	public static QuoteMethod get(final String name) {
		final QuoteMethod[] quotes = QuoteMethod.values();
		for (QuoteMethod quote : quotes) {
			if (quote.name.equalsIgnoreCase(name)) {
				return quote;
			}
		}

		throw new IllegalArgumentException("No quote method found for name: " + name);
	}
}
