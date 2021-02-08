//package com.ymyang.framework.web;
//
//import java.lang.reflect.Method;
//
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.cache.RedisCacheWriter;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@EnableCaching
//@Configuration
//@AutoConfigureAfter(RedisAutoConfiguration.class)
//public class RedisConfig extends CachingConfigurerSupport {
//
//	@Bean
//	public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()//.entryTtl(Duration.ofMinutes(30))
//				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//				.disableCachingNullValues();
//		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
//		return new RedisCacheManager(redisCacheWriter, config);
//	}
//
//	@Bean
//	public RedisTemplate<?, ?> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
//		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
//		redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//		// key序列化方式;但是如果方法上有Long等非String类型的话，会报类型转换错误；
//		StringRedisSerializer redisSerializer = new StringRedisSerializer();// Long类型不可以会出现异常信息;
//		redisTemplate.setKeySerializer(redisSerializer);
//		redisTemplate.setHashKeySerializer(redisSerializer);
//
//		// JdkSerializationRedisSerializer序列化方式;
//		GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//		redisTemplate.setValueSerializer(jsonRedisSerializer);
//		redisTemplate.setHashValueSerializer(jsonRedisSerializer);
//		redisTemplate.afterPropertiesSet();
//
//		return redisTemplate;
//	}
//
//	@Bean
//	public KeyGenerator keyGenerator() {
//		return new KeyGenerator() {
//			@Override
//			public Object generate(Object target, Method method, Object... params) {
//				StringBuilder sb = new StringBuilder();
//				sb.append(target.getClass().getName()).append("_");
//				sb.append(method.getName()).append("_");
//				for (Object obj : params) {
//					sb.append(obj.toString()).append("_");
//				}
//				return sb.toString();
//			}
//		};
//	}
//}