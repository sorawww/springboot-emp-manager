package com.fosu.edu.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
@Slf4j
public class RedisPool {
	@Value("${spring.redis.host}")
    private static String host;

    @Value("${spring.redis.port}")
    private static int port;

    @Value("${spring.redis.timeout}")
    private static int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private static int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private static long maxWaitMillis;

    @Value("${spring.redis.password}")
    private static String password;
    
    private static JedisPoolConfig jedisPoolConfig;
    private static List<JedisShardInfo> list;
    
    static {
    	jedisPoolConfig = new JedisPoolConfig();
         jedisPoolConfig.setMaxIdle(maxIdle);
         jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        list = Lists.newArrayList(new JedisShardInfo(host,port,timeout,password));
    }
	
	private ShardedJedisPool shardedJedisPool = new ShardedJedisPool(jedisPoolConfig,list);
	
	//获取实例
	public ShardedJedis instance() {
		return shardedJedisPool.getResource();
	}
	
	//安全关闭,每次连接redis后一定要记得关闭
	public void safeClose(ShardedJedis shardedJedis) {
		try {
			if(shardedJedis != null) {
				shardedJedis.close(); 
			}
			
		} catch (Exception e) {
			log.error("redis 关闭异常");
		}
	}
	
	
}
