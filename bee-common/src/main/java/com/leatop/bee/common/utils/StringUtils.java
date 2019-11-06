/**
 * File: StringUtils.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月7日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class for operation on squence of characters.
 * 
 * @author Dorsey
 *
 */
public class StringUtils {

	public static <T> String join(final Iterable<T> elements, final String delim) {
		final StringBuilder builder = new StringBuilder();
		Iterator<T> iter = elements.iterator();
		while (iter.hasNext()) {
			builder.append(iter.next());

			if (iter.hasNext()) {
				builder.append(delim);
			}
		}

		return builder.toString();
	}
	
	
	/**
	 * Check whether the given string <tt>obj</tt> is empty or not. An
	 * IllegalArgumentException will be raised if the given string is empty.
	 * 
	 * @param obj the given string.
	 * @param message message in exception.
	 */
	public static void requiresNonEmpty(final String obj, final String message) {
		if (obj.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}
	
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}
	
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}
	
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] {str};
		}

		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		}
		else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		if (result == null) {
			return null;
		}
		return result.toArray(new String[result.size()]);
	}
	
	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}

		StringBuilder sb = new StringBuilder(inString.length());
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static boolean hasLength(String str) {
		return (str != null && !str.isEmpty());
	}

}
