package com.example.project2.test.redis.test_cluster;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClusterUtil {
    // 注入RedisTemplate（Spring Boot自动配置）
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 写入字符串数据（带过期时间）
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, value, timeout, unit);
    }

    /**
     * 读取字符串数据
     */
    public Object get(String key) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return valueOps.get(key);
    }

    /**
     * 删除数据
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除
     */
    public Long delete(Iterable<String> keys) {
        return redisTemplate.delete((Collection<String>) keys);
    }
}
