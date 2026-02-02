package com.example.project2.demos.test_memory_exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 内存泄露异常案例
 */
public class MemoryLeakDemo {
    // 静态集合（GC Root）：持有User对象的强引用，且永不清理
    private static final Map<Long, User> CACHE = new HashMap<>();
    private static final Random RANDOM = new Random();

    // 模拟业务对象：占用一定内存（方便MAT识别）
    static class User {
        // 字节数组占用内存，让泄漏更明显
        private byte[] data = new byte[1024 * 1024]; // 1MB/个
        private Long id;
        private String name;

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    // 模拟业务逻辑：持续往缓存添加User，不删除
    public static void addUserToCache() {
        long id = RANDOM.nextLong();
        User user = new User(id, "User-" + id);
        CACHE.put(id, user);
        System.out.println("添加User到缓存，当前缓存大小：" + CACHE.size());
    }

    public static void main(String[] args) throws InterruptedException {
        // JVM参数建议（启动时添加）：
        // -Xms512m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./heap-dump.hprof
        // 限制堆内存512M，OOM时自动导出快照到当前目录

        // 循环添加User，直到OOM
        while (true) {
            addUserToCache();
            Thread.sleep(10); // 放慢速度，便于观察
        }
    }
}
