package com.abrahamhan.SecKill.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

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
	 * 删除key值
	 * @param prefix
	 * @param key
	 * @return
	 */
	
	public<T> boolean delete(KeyPrefix prefix,String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			//生成真正的key
			String realKey = prefix.getPrefix() + key;
			long ret = jedis.del(realKey);
			return ret > 0;
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
	/**
	 * 将javabean转换成String类型
	 * @param value
	 * @return
	 */
	
	public static <T> String beanToString(T value) {
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
	/**
	 * 将Json对象还原成对象
	 * @param str
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T stringToBean(String str,Class<T> clazz) {
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
	
	public boolean delete(KeyPrefix prefix) {
		if(prefix == null) {
			return false;
		}
		List<String> keys = scanKeys(prefix.getPrefix());
		if(keys==null || keys.size() <= 0) {
			return true;
		}
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(keys.toArray(new String[0]));
			return true;
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(jedis != null) {
				jedis.close();
			}
		}
		
	}

	public List<String> scanKeys(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> keys = new ArrayList<String>();
			String cursor = "0";
			ScanParams sp = new ScanParams();
			sp.match("*"+key+"*");
			sp.count(100);
			do{
				ScanResult<String> ret = jedis.scan(cursor, sp);
				List<String> result = ret.getResult();
				if(result!=null && result.size() > 0){
					keys.addAll(result);
				}
				//再处理cursor
				cursor = ret.getStringCursor();
			}while(!cursor.equals("0"));
			return keys;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	

}
