package com.example.project2.demos.testerror;

import java.util.concurrent.locks.ReentrantLock;

public class Cpu100LockContentionTest {
    // 全局锁（所有线程争抢）
    private static final ReentrantLock LOCK = new ReentrantLock();
    // 竞争线程数
    private static final int COMPETE_THREAD_COUNT = 2;

    public static void main(String[] args) {
//        for (int i = 0; i < COMPETE_THREAD_COUNT; i++) {
//            int threadId = i;
//            new Thread(() -> {
//                while (true) {
//                    // 争抢锁
//                    LOCK.lock();
//                    try {
//                        // 持有锁执行死循环计算（占用CPU+阻塞其他线程）
//                        long count = 0;
//                        while (count < 1000000) {
//                            count++;
//                            double result = Math.sin(count) * Math.cos(count);
//                        }
//                        System.out.printf("线程%d：持有锁完成计算%n", threadId);
//                    } finally {
//                        LOCK.unlock();
//                    }
//                }
//            }, "Lock-Thread-" + threadId).start();
//        }
    }
}
