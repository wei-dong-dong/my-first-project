package com.example.project2.demos.test_standard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestStandardRedisController {
    @Autowired
    private RedisStandardService redisService;

    @GetMapping("/standardRedis")
    public void testStandardRedis() {
        // 直接使用注入的 redisService，无需手动创建
        redisService.setString("test_key", "hello_redis");
        String value = redisService.getString("test_key");
        System.out.println("读取字符串：" + value);

        redisService.setHash("user_1", "name", "张三");
        String name = redisService.getHash("user_1", "name");
        System.out.println("读取Hash：" + name);

        redisService.deleteKey("test_key");
        System.out.println("删除后读取：" + redisService.getString("test_key"));
    }

}
