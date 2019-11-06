/**
 * File: Identifier.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月9日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.data.weaver.connector.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dorsey
 *
 */
public class Identifier {

	// ~~~ fields
	// =======================================================================================
	private static final String DEFAULT_DELIM = ".";
	private static final String DEFAULT_QUOTE = "\"";
	private static final String UN_SUPPORTED_QUOTES = " ";
	private final String leadingQuote;
	private final String trailQuote;
	private final String delim;
	public static final Identifier DEFAULT_IDENTIFIER = new Identifier(DEFAULT_QUOTE);

	// ~~~ constructors
	// =======================================================================================
	public Identifier(final String quote) {
		this(DEFAULT_DELIM, quote);
	}

	public Identifier(final String delim, final String quote) {
		this(delim, quote, quote);
	}

	public Identifier(final String delim, final String leadingQuote, final String trailQuote) {
		this.leadingQuote = leadingQuote != null ? leadingQuote : DEFAULT_QUOTE;
		this.trailQuote = trailQuote != null ? trailQuote : DEFAULT_DELIM;
		this.delim = delim != null ? delim : DEFAULT_DELIM;
	}

	// ~~~ methods
	// =======================================================================================
	public List<String> parse(String fqn) {
		String orig = fqn;
		String delim = getDelim();
		String lead = getLeadingQuote();
		String trail = getTrailQuote();
		List<String> parts = new ArrayList<>();
		int index;
		String segment;
		do {
			if (!lead.equals(UN_SUPPORTED_QUOTES) && fqn.startsWith(lead)) {
				int end = fqn.indexOf(trail, lead.length());
				if (end < 0) {
					throw new IllegalArgumentException(
							"Failure parsing fully qualified identifier; missing trailing quote in " + orig);
				}
				segment = fqn.substring(lead.length(), end);
				fqn = fqn.substring(end + trail.length());
				if (fqn.startsWith(delim)) {
					fqn = fqn.substring(delim.length());
					if (fqn.isEmpty()) {
						throw new IllegalArgumentException(
								"Failure parsing fully qualified identifier; ends in delimiter " + orig);
					}
				}
			} else {
				index = fqn.indexOf(delim, 0);
				if (index == -1) {
					segment = fqn;
					fqn = "";
				} else {
					segment = fqn.substring(0, index);
					fqn = fqn.substring(index + delim.length());
					if (fqn.isEmpty()) {
						throw new IllegalArgumentException(
								"Failure parsing fully qualified identifier; ends in delimiter " + orig);
					}
				}
			}
			parts.add(segment);
		} while (fqn.length() > 0);
		return parts;
	}
	

	public String getLeadingQuote() {
		return leadingQuote;
	}

	public String getTrailQuote() {
		return trailQuote;
	}

	public String getDelim() {
		return delim;
	}

	public SQLBuilder sqlBuilder() {
		return new SQLBuilder(this);
	}
}
