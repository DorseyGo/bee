package com.leatop.bee.management.util.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.leatop.bee.management.util.cache.impl.MemoryCache;


/**
 * 缓存管理接口
 * 
 */
public class CacheManager {

	private static ConcurrentHashMap<String, Cache> cacheManager = new ConcurrentHashMap<String, Cache>();
	static ICacheManager _CreateCache;

	protected CacheManager() {
	}

	static {
		_CreateCache = new ICacheManager() {
			public Cache getCache() {
				return new MemoryCache();
			}
		};
	}

	public static void setCache(ICacheManager thisCache) {
		_CreateCache = thisCache;
	}

	public static Cache get(String name) {
		Cache cache = cacheManager.get(name);
		if (cache == null) {
			synchronized (cacheManager) {
				cache = cacheManager.get(name);
				if (cache == null) {
					cache = _CreateCache.getCache();
					cache.name(name);
					cacheManager.put(name, cache);
				}
			}
		}
		return cache;
	}

	public static int size() {
		return cacheManager.size();
	}

	public static Collection<Cache> values() {
		return cacheManager.values();
	}

	public static Set<String> keys() {
		return cacheManager.keySet();
	}

}