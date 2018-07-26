package com.abrahamhan.SecKill.redis;
/**
 * 前缀接口
 * @author abrhamhan
 *
 */
public interface KeyPrefix {
	public int expireSeconds();
	
	public String getPrefix();
}
