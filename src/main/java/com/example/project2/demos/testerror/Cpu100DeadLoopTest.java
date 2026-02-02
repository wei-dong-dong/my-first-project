package com.example.project2.demos.testerror;

/**
 * 死循环触发CPU 100%测试类
 * 每个线程占满1个CPU核心，核心数可通过THREAD_COUNT调整
 */
public class Cpu100DeadLoopTest {
        // 占满1个CPU核心
//        public static void main(String[] args) {
//            // 启动一个线程执行死循环
//            Thread cpuThread = new Thread(new CpuConsumeRunnable());
//            cpuThread.start();
//
//            // 主线程等待，防止程序退出
//            try {
//                cpuThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // 死循环任务类（无Lambda，兼容所有JDK 8+）
        static class CpuConsumeRunnable implements Runnable {
            @Override
            public void run() {
                long count = 0;
                // 死循环：纯CPU计算，避免JIT优化
                while (true) {
                    count++;
                    // 每100万次输出一次，确保循环不被优化
                    if (count % 1000000 == 0) {
                        System.out.println("CPU占用中，count=" + count);
                    }
                }
            }
        }
    }