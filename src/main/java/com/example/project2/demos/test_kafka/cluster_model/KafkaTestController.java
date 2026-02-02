package com.example.project2.demos.test_kafka.cluster_model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class KafkaTestController {
    @Resource
    private KafkaProducerService producerService;

    /**
     * 发送普通消息
     * @param message 消息内容
     * @return 提示信息
     */
    @GetMapping("/send/{message}")
    public String sendMessage(@PathVariable String message) {
        producerService.sendMessage(message);
        return "消息发送中，请查看日志！";
    }

    /**
     * 发送带键的消息
     * @param key 消息键
     * @param message 消息内容
     * @return 提示信息
     */
    @GetMapping("/send/key/{key}/{message}")
    public String sendMessageWithKey(@PathVariable String key, @PathVariable String message) {
        producerService.sendMessageWithKey(key, message);
        return "带键消息发送中，请查看日志！";
    }

    /**
     * 指定分区发送消息
     * @param partition 分区号
     * @param message 消息内容
     * @return 提示信息
     */
    @GetMapping("/send/partition/{partition}/{message}")
    public String sendMessageToPartition(@PathVariable int partition, @PathVariable String message) {
        producerService.sendMessageToPartition(partition, message);
        return "指定分区消息发送中，请查看日志！";
    }
}
