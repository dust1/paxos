package com.dust.paxos.pojo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储在存储端的数据对象
 * 一个key对应一个对象
 */
@Getter
@Setter
public class MetaData {
    
    private String key;

    private String value;

    /**
     * 记录最后一次进行写前读取的客户端
     */
    private AtomicInteger lastRnd;

    /**
     * 记录了在哪个round的时候value被写入了
     */
    private AtomicLong vrnd;

}
