package com.dust.paxos;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.dust.paxos.pojo.MetaData;
import com.dust.paxos.pojo.ResMessage;
import com.dust.paxos.utils.Node;

/**
 * 存储端
 */
public class Acceptor extends Node {

    private Map<String, MetaData> data;

    /**
     * 存储段初始化函数
     * @return
     */
    public static Acceptor create(String uuid) {
        //可能会有其他操作？
        return new Acceptor(uuid);
    }

    /**
     * 写入
     */
    public boolean set(String key, String value, int rnd) {
        MetaData metaData = data.getOrDefault(key, MetaData.empty(key));
        data.put(key, metaData);
        return metaData.write(value, rnd);
    }

    /**
     * 写前读取
     * @param rnd 每一轮的编号，单调递增，后写胜出，全局唯一，可以用来区分Proposer
     * @param key 要获取的key
     * @return 在返回之后，存储端需要记录客户端的rnd
     */
    public ResMessage get(int rnd, String key) {
        MetaData metaData = data.getOrDefault(key, MetaData.empty(key));
        data.put(key, metaData);
        return metaData.read(rnd);
    }
    
    /**
     * 一般读取
     * @param key
     * @return
     */
    public ResMessage read(String key) {
        MetaData metaData = data.getOrDefault(key, MetaData.empty(key));
        data.put(key, metaData);
        return metaData.read();
    }

    /**
     * 私有化构造函数方式通过new的方式创建对象
     */
    private Acceptor(String uuid) {
        super(uuid);
        this.data = new HashMap<>();
    }

    @Override
    public String toString() {
        return data.values().stream().map(MetaData::toString).collect(Collectors.joining(";")).toString();
    }

}