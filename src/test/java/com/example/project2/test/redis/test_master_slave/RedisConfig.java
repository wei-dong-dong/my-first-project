package com.example.project2.test.redis.test_master_slave;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// 关键修改：指定Bean名称为masterSlaveRedisConfig
@Configuration("masterSlaveRedisConfig")
public class RedisConfig {

    // 主节点配置
    @Value("${spring.redis.host}")
    private String masterHost;
    @Value("${spring.redis.port}")
    private int masterPort;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private int database;

    // 从节点配置（单服务器多端口：6380）
    private String slaveHost = "192.168.92.129";
    private int slavePort = 6380;

    // ========== 主节点连接工厂（写操作） ==========
    @Bean("masterRedisConnectionFactory")
    public RedisConnectionFactory masterRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(masterHost);
        config.setPort(masterPort);
        config.setPassword(password);
        config.setDatabase(database);
        return new LettuceConnectionFactory(config);
    }

    // ========== 从节点连接工厂（读操作） ==========
    @Bean("slaveRedisConnectionFactory")
    public RedisConnectionFactory slaveRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(slaveHost);
        config.setPort(slavePort);
        config.setPassword(password);
        config.setDatabase(database);
        return new LettuceConnectionFactory(config);
    }

    // ========== 主节点 RedisTemplate（Object 类型） ==========
    // 关键修改：同时指定name为"redisTemplate"（默认Bean）和"masterRedisTemplate"（显式注入用）
    @Bean({"redisTemplate", "masterRedisTemplate"})
    @Primary  // 设置为主Bean，当不指定@Qualifier时默认注入这个
    public RedisTemplate<String, Object> masterRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(masterRedisConnectionFactory());
        setSerializer(template);
        return template;
    }

    // ========== 从节点 RedisTemplate（Object 类型） ==========
    @Bean("slaveRedisTemplate")
    public RedisTemplate<String, Object> slaveRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(slaveRedisConnectionFactory());
        setSerializer(template);
        return template;
    }

    // ========== 主节点 StringRedisTemplate（字符串类型，解决注入失败） ==========
    @Bean("masterStringRedisTemplate")
    public StringRedisTemplate masterStringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(masterRedisConnectionFactory());
        // StringRedisTemplate 默认就是 String 序列化，无需额外配置
        return template;
    }

    // ========== 从节点 StringRedisTemplate（字符串类型） ==========
    @Bean("slaveStringRedisTemplate")
    public StringRedisTemplate slaveStringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(slaveRedisConnectionFactory());
        return template;
    }

    // 序列化配置（给 Object 类型 RedisTemplate 用）
    private void setSerializer(RedisTemplate<String, Object> template) {
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
    }
}