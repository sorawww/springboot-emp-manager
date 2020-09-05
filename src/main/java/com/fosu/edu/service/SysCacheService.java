package com.fosu.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fosu.edu.bean.CacheKeyConstants;
import com.google.common.base.Joiner;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Service
@Slf4j
public class SysCacheService {
	
	@Autowired
	private RedisPool redisPool;
	
	//保存chache
	public void  saveCache(String toSavedValue,int timeountSeconds,CacheKeyConstants prefix) {
		saveCache(toSavedValue, timeountSeconds, prefix, null);
	}
	
	public void  saveCache(String toSavedValue,int timeountSeconds,CacheKeyConstants prefix,String... keys) {
		if(toSavedValue == null) {
			return;
		}
		ShardedJedis shardedJedis = null;
		//尝试取连接，可能会有异常
		try {
			String cacheKey = generateCacheKey(prefix, keys);
			shardedJedis = redisPool.instance();
			shardedJedis.setex(cacheKey, timeountSeconds, toSavedValue);
		} catch (Exception e) {
			log.info("保存失败");
		}finally {
			redisPool.safeClose(shardedJedis);
		}
	}
	
	public String getFromCache(CacheKeyConstants prefix, String ...keys) {
		ShardedJedis shardedJedis = null;
		String cacheKey = generateCacheKey(prefix, keys);
		try {
			shardedJedis = redisPool.instance();
			String value = shardedJedis.get(cacheKey);
			return value;
		} catch (Exception e) {
			log.info("读取失败");
			return null;
		}finally {
			redisPool.safeClose(shardedJedis);
		}
		
	}
	
	
	private String generateCacheKey(CacheKeyConstants prefix,String...keys) {
		String key = prefix.name();
		if(keys !=null && keys.length>0) {
			key +="_" +Joiner.on("_").join(keys);
		}
		
		return key;
	}
	
}
