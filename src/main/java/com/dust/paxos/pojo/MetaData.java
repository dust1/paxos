package com.dust.paxos.pojo;

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
    private int lastRnd;

    /**
     * 记录了在哪个round的时候value被写入了
     */
    private int vrnd;

    
    public static MetaData empty(String key) {
        MetaData metaData = new MetaData();
        metaData.setKey(key);
        metaData.setValue(null);
        metaData.setLastRnd(0);
        metaData.setVrnd(0);
        return metaData;
    }

    /**
     * 读取节点数据
     * @param rnd
     * @return 如果rnd数据小于lastRnd，则直接返回信息，具体逻辑交给客户端
     */
    public ResMessage read(int rnd) {
        ResMessage res = ResMessage.create(this);
        if (rnd >= lastRnd) {
            this.lastRnd = rnd;
        }
        return res;
    }

    /**
     * 写入数据
     * @param value
     * @param rnd
     * @return
     */
	public boolean write(String value, int rnd) {
        if (rnd != lastRnd) {
            return false;
        }
        this.value = value;
        this.vrnd = rnd;
		return true;
	}

}
