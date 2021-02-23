package com.dust.paxos.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于全局获取唯一rnd的工具类
 */
public class RndTools {

    private static AtomicInteger rnd = new AtomicInteger(0);

    public static int getRnd() {
        return rnd.incrementAndGet();
    }

}
