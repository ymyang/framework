package com.ymyang.config.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheService {

//	private static final String PREFIX = "k-cloud-urban:";
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void set(String key, Object value) {
//		String k = PREFIX + key;
		redisTemplate.opsForValue().set(key, value);
	}

	public void set(String key, Object value, long timeout, TimeUnit unit) {
//		String k = PREFIX + key;
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}
	
	@SuppressWarnings("unchecked")
	public <V> V get(String key) {
//		String k = PREFIX + key;
		return (V) redisTemplate.opsForValue().get(key);
	}

}
