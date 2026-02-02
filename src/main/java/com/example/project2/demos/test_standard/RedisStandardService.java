package com.example.project2.demos.test_standard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisStandardService {
    // 注入StringRedisTemplate（Spring Boot自动配置）
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 1. 写入字符串
    public void setString(String key, String value) {
        // 可选：设置过期时间（比如60秒）
        stringRedisTemplate.opsForValue().set(key, value, 60);
    }

    // 2. 读取字符串
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 3. 写入Hash类型
    public void setHash(String hashKey, String field, String value) {
        stringRedisTemplate.opsForHash().put(hashKey, field, value);
    }

    // 4. 读取Hash类型
    public String getHash(String hashKey, String field) {
        return (String) stringRedisTemplate.opsForHash().get(hashKey, field);
    }

    // 5. 删除key
    public void deleteKey(String key) {
        stringRedisTemplate.delete(key);
    }
}
