package com.abrahamhan.SecKill.redis;
/**
 * OrderKey的前缀类
 * @author abrahamhan
 *
 */
public class OrderKey extends BasePrefix {

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
 	}

}
