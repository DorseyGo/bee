package com.leatop.bee.management.util.cache;

import java.util.Collection;
import java.util.Set;

public interface Cache {

	/**
	 * 设置缓存名称
	 * 
	 * 
	 * @param name
	 * @return
	 */
	public Cache name(String name);

	/**
	 * 根据key获取缓存数据
	 * 
	 * 
	 * @param key
	 * @return
	 */
	public <T> T get(String key);

	/**
	 * 添加缓存获取
	 * 
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Cache add(String key, Object value);

	/**
	 * 移除缓存数据
	 * 
	 * 
	 * @param key
	 * @return
	 */
	public Object remove(String key);
	
	/**
	 * 清除所有数据
	 * 
	 * 
	 * @return
	 */
	public void clear();

	/**
	 * 获取缓存数量
	 * 
	 * 
	 * @return
	 */
	public int size();
	
	/**
	 * 返回数据key列表
	 * 
	 * @return
	 */
	public Set<String> keys();
	
	/**
	 * 返回数据缓存列表
	 * 
	 * 
	 * @return
	 */
	public <T> Collection<T> values();
}
