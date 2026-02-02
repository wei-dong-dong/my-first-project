package com.example.project2.demos.testerror;
/**
 * 无限递归导致CPU 100%（模拟递归终止条件缺失、算法错误场景）
 */
public class Cpu100RecursionTest {
//    public static void main(String[] args) {
//        // 启动无限递归
//        recursiveCompute(0);
//    }

    /**
     * 无终止条件的递归计算
     */
    private static long recursiveCompute(long num) {
        // 无意义计算，增加CPU消耗
        long result = num * num + recursiveCompute(num + 1);
        // 永远不会执行到这
        return result;
    }
}

