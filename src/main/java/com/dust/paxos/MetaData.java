package com.dust.paxos;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储端元数据
 */
@Getter
@Setter
public class MetaData {

    private String key;
    private String value;
    private int lastRnd;
    private int vrnd;

    public static MetaData empty(String key) {
        return new MetaData(key, null);
    }

    private MetaData(String key, String value) {
        this.key = key;
        this.value = value;
        this.lastRnd = 0;
        this.vrnd = 0;
    }

    public synchronized boolean read(int rnd) {
        if (rnd < lastRnd) {
            return false;
        }
        this.lastRnd = rnd;
        return true;
    }

    public synchronized boolean write(String value, int rnd) {
        if (rnd != lastRnd) {
            return false;
        }
        this.value = value;
        this.vrnd = rnd;
        return true;
    }


}
