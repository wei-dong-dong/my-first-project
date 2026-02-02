package com.example.project2.test.redis.test_sentinel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RedisTestController {
    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/redis/set/{key}/{value}")
    public String setRedis(@PathVariable String key, @PathVariable String value) {
        redisUtil.set(key, value);
        return "Set success: " + key + "=" + value;
    }

    @GetMapping("/redis/get/{key}")
    public Object getRedis(@PathVariable String key) {
        return "Get result: " + redisUtil.get(key);
    }
}
