package com.example.project2.test.redis.test_master_slave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {
    @Autowired
    private Redis_master_slave_Service redisService;

    // 写操作（路由到主节点）
    @GetMapping("/redis/write")
    public String write() {
        redisService.set("springboot-test", "hello-master-slave");
        return "写入主节点成功";
    }

    // 读操作（路由到从节点）
    @GetMapping("/redis/read")
    public String read() {
        String value = redisService.get("springboot-test");
        return "从节点读取结果：" + value;
    }
}
