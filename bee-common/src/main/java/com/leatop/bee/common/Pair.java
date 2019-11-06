/**
 * File: Pair.java
 * Author: DORSEy Q F TANG
 * Created: 2019年5月6日
 * CopyRight: All Rights Reserved
 */
package com.leatop.bee.common;

import java.util.Objects;

/**
 * @author Dorsey
 *
 */
public class Pair<K, V> {

	private final K key;
	private final V value;

	/**
	 * Constructor of {@link Pair}, with key and value specified.
	 * 
	 * @param key
	 *            the key.
	 * @param value
	 *            the value.
	 */
	public Pair(final K key, final V value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @return the value
	 */
	public V getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Pair))
			return false;

		Pair other = (Pair) obj;
		return Objects.equals(key, other.key) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "Pair [key=" + key + ", value=" + value + "]";
	}

}
