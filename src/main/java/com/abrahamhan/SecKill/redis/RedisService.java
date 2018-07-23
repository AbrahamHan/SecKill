package com.abrahamhan.SecKill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {
	@Autowired
	JedisPool jedisPool;
	/**
	 * 获取单个对象
	 * @param prefix
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(KeyPrefix prefix,String key, Class<T> clazz) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			String str = jedis.get(realKey );
			T t = stringToBean(str,clazz);
			return t;
		}
		finally {
			returnToPool(jedis); 
		}
	}
	/**
	 * 判断是否存在
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> Boolean exists(KeyPrefix prefix,String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			Boolean res = jedis.exists(realKey );
			return res;
		}
		finally {
			returnToPool(jedis); 
		}
	}
	/**
	 * 增加value值
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> Long incr(KeyPrefix prefix,String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			return jedis.incr(realKey);
		}
		finally {
			returnToPool(jedis); 
		}
	}
	/**
	 * 减少value值
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> Long decr(KeyPrefix prefix,String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			return jedis.decr(realKey);
		}
		finally {
			returnToPool(jedis); 
		}
	}
	
	/**
	 * 设置<key,value>
	 * @param prefix
	 * @param key
	 * @param value
	 * @return
	 */
	
	public<T> boolean set(KeyPrefix prefix,String key, T value) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis = jedisPool.getResource();
			String str = beanToString(value);
			if(str == null || str.length() <= 0)
			{
				return false;
			}
			//生成真正的key
			String realKey = prefix.getPrefix() + key;
			
			int secs = prefix.expireSeconds();
			//设置过期时间
			if(secs <= 0)
			{
				jedis.set(realKey, str);
			}
			else
			{
				jedis.setex(realKey,secs,str);
			}
			
			return true;
		}
		finally {
			returnToPool(jedis); 
		}
	}
	
	
	private<T> String beanToString(T value) {
		if(value == null)
		{
			return null;
		}
		
		Class<?> clazz = value.getClass();
		
		
		if(clazz == int.class || clazz == Integer.class)
		{
			return ""+value;
		}else if(clazz == String.class)
		{
			return (String)value;
		}else if(clazz == long.class || clazz == Long.class)
		{
			return ""+value;
		}else
		{
			return JSON.toJSONString(value);
		}
	}

	@SuppressWarnings("unchecked")
	private<T> T stringToBean(String str,Class<T> clazz) {
		if(str == null || str.length() <= 0 || clazz == null)
		{
			return null;
		}
		if(clazz == int.class || clazz == Integer.class)
		{
			return (T)Integer.valueOf(str);
		}else if(clazz == String.class)
		{
			return (T) str;
		}else if(clazz == long.class || clazz == Long.class)
		{
			return (T) Long.valueOf(str);
		}else
		{
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
	private void returnToPool(Jedis jedis) {
		if(jedis != null)
		{
			jedis.close();
		}
		
	}
	

}
