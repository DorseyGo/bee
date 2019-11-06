package com.leatop.bee.management.util.cache.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.leatop.bee.management.util.cache.Cache;

/**
 * 内存管理配置
 * 
 */
public class MemoryCache implements Cache {

	protected String name;
	protected Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public MemoryCache() {
	}

	public String name() {
		return name;
	}

	public MemoryCache name(String name) {
		this.name = name;
		return this;
	}

	public MemoryCache add(String key, Object value) {
		map.put(key, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) map.get(key);
	}

	public Object remove(String key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}
	
	public int size() {
		return map.size();
	}

	public Set<String> keys() {
		if (map.size() == 0) {
			return null;
		}
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<T> values() {
		if (map.size() == 0) {
			return null;
		}

		Collection<T> list = new ArrayList<T>();
		for (Object obj : map.values()) {
			list.add((T) obj);
		}

		return list;
	}

}
