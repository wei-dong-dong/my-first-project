package com.example.project2.demos.test_kafka.cluster_model;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

@Service
public class KafkaProducerService {
    // 注入KafkaTemplate，泛型为<Key类型, Value类型>
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    // 主题名称（需提前在Kafka集群创建，或开启自动创建）
    private static final String TOPIC_NAME = "cluster-test";

    /**
     * 发送普通消息（不指定分区和键）
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NAME, message);
        // 异步回调处理发送结果
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("消息发送成功：" + result.getRecordMetadata());
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("消息发送失败：" + ex.getMessage());
            }
        });
    }

    /**
     * 发送带键的消息（Kafka会根据键的哈希值分配分区，相同键的消息进入同一分区）
     * @param key 消息键
     * @param message 消息内容
     */
    public void sendMessageWithKey(String key, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NAME, key, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("带键消息发送成功，分区：" + result.getRecordMetadata().partition());
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("带键消息发送失败：" + ex.getMessage());
            }
        });
    }

    /**
     * 指定分区发送消息
     * @param partition 分区号
     * @param message 消息内容
     */
    public void sendMessageToPartition(int partition, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NAME, partition, null, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("指定分区消息发送成功，分区：" + result.getRecordMetadata().partition());
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("指定分区消息发送失败：" + ex.getMessage());
            }
        });
    }
}
