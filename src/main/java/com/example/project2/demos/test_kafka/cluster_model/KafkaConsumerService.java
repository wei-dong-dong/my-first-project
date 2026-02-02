package com.example.project2.demos.test_kafka.cluster_model;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumerService {
    /**
     * 监听整个主题（自动分配分区，适合分布式消费）
     * @param record 消息记录（包含消息内容、分区、偏移量等）
     */
    @KafkaListener(topics = "cluster-test", groupId = "springboot-kafka-group")
    public void listenTopic(ConsumerRecord<String, String> record) {
        System.out.println("普通消费：");
        System.out.println("主题：" + record.topic());
        System.out.println("分区：" + record.partition());
        System.out.println("偏移量：" + record.offset());
        System.out.println("消息内容：" + record.value());
        System.out.println("------------------------");
    }

    /**
     * 监听指定分区（适合精准消费某几个分区）
     * @param record 消息记录
     */
    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "cluster-test", partitions = {"0", "1"})
    }, groupId = "springboot-kafka-group-2")
    public void listenSpecificPartition(ConsumerRecord<String, String> record) {
        System.out.println("指定分区消费：");
        System.out.println("主题：" + record.topic());
        System.out.println("分区：" + record.partition());
        System.out.println("消息内容：" + record.value());
        System.out.println("------------------------");
    }

    /**
     * 批量消费（需在配置中设置listener.type=BATCH）
     * @param records 消息列表
     * @param ack 手动确认偏移量（需关闭自动提交）
     */
    @KafkaListener(topics = "cluster-test", groupId = "springboot-kafka-group-3")
    public void batchListen(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        System.out.println("批量消费，消息数量：" + records.size());
        for (ConsumerRecord<String, String> record : records) {
            System.out.println("分区：" + record.partition() + "，内容：" + record.value());
        }
        // 手动确认偏移量（确保消息被消费后再提交）
        ack.acknowledge();
        System.out.println("------------------------");
    }
}
