package com.example.project2.test.redis.test_cluster;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class RedisClusterTest {
    @Resource
    private RedisClusterUtil redisClusterUtil;

    @GetMapping("/clusterRedis")
    public void testClusterOperation() {
        // 写入数据（过期时间1小时）
        redisClusterUtil.set("user:1", "{\"id\":1,\"name\":\"张三\"}", 1, TimeUnit.HOURS);
        // 读取数据
        Object value = redisClusterUtil.get("user:1");
        System.out.println("读取到的数据：" + value);

        /**
         * 删除数据
         *  Boolean delete = redisClusterUtil.delete("user:1");
         *  System.out.println("删除结果：" + delete);
         */
    }
}
