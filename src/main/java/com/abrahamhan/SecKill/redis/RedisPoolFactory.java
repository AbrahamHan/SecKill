package com.abrahamhan.SecKill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * Redis缓存的工厂类
 * @author abrahamhan
 *
 */
@Service
public class RedisPoolFactory {
	//获取redis的配置
	@Autowired
	RedisConfig redisConfig;
	//生成redis的JedisPool
	@Bean
	public JedisPool JedisPoolFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
		poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
		poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);
		JedisPool jp = new JedisPool(poolConfig,redisConfig.getHost(),redisConfig.getPort(),
				redisConfig.getTimeout()*1000,redisConfig.getPassword(),0);
		return jp;
		
	}
}
