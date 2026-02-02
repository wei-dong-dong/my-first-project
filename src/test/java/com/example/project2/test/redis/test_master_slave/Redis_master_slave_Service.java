package com.example.project2.test.redis.test_master_slave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class Redis_master_slave_Service {

    // 若用主节点写：注入 masterStringRedisTemplate
    @Autowired
    @Qualifier("masterStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    // 若用从节点读：新增从节点 StringRedisTemplate 注入
    @Autowired
    @Qualifier("slaveStringRedisTemplate")
    private StringRedisTemplate slaveStringRedisTemplate;

    // 写操作（主节点）
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 读操作（从节点）
    public String get(String key) {
        return slaveStringRedisTemplate.opsForValue().get(key);
    }
}
