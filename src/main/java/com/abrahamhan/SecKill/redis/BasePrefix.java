package com.abrahamhan.SecKill.redis;

/**
 * 前缀类的抽象类
 * @author abrahamhan
 *
 */
public abstract class BasePrefix  implements KeyPrefix{
	
	private int expireSeconds;
	private String prefix;
	
	public BasePrefix( String prefix) {
		this(0, prefix);
	}
	
	public BasePrefix(int expireSeconds,String prefix)
	{
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}
	
	//默认0代表永不过期
	public int expireSeconds()
	{
		return this.expireSeconds;
	}
	
	public String getPrefix()
	{
		String className = getClass().getSimpleName();
		return className + ":" + prefix;
	}
}
