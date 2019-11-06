/**
 * 
 */
package com.leatop.bee.management.page;

import java.util.List;

/**
 * 数据分页查询封装
 * 
 * @author zlm
 * @param <T>
 *
 */
public class Pagination<T> {
	private int code;
	private int count;
	private List<T> data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
}
