package com.abrahamhan.SecKill.redis;
/**
 * OrderKey的前缀类
 * @author abrahamhan
 *
 */
public class OrderKey extends BasePrefix {

	public static OrderKey getSeckillOrderByUidGid = new OrderKey("moug");

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
 	}
	public OrderKey( String prefix) {
		super( prefix);
 	}
}
