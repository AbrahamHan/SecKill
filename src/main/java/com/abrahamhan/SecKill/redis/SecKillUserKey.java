package com.abrahamhan.SecKill.redis;
/**
 * 秒杀用户key
 * @author abrahamhan
 *
 */
public class SecKillUserKey extends BasePrefix{
	//过期时间
	public static final int TOKEN_EXPIRE = 3600*24 * 2;
	
	private SecKillUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	//token的过期时间设置为2天
	public static SecKillUserKey token = new SecKillUserKey(TOKEN_EXPIRE, "tk");
	//用户的缓存设置为永不失效
	public static SecKillUserKey getById = new SecKillUserKey(0, "tk");
}
