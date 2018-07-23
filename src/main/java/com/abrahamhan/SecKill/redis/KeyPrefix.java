package com.abrahamhan.SecKill.redis;

public interface KeyPrefix {
	public int expireSeconds();
	
	public String getPrefix();
}
