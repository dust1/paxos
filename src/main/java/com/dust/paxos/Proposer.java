package com.dust.paxos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dust.paxos.utils.Node;

/**
 * 客户端
 */
public class Proposer extends Node {

    private Map<String, Acceptor> aMap;

    public static Proposer create(String uuid) {
        return new Proposer(uuid);
    }

    private Proposer(String uuid) {
        super(uuid);
        this.aMap = new HashMap<>();
    }

    /**
     * 给客户端添加一个存储端对象
     * 这一步如果正常来说添加的应该是存储端的网络信息，但既然是本地实现那就直接添加对象好了
     * @param acceptor 存储端对象
     * @return 是否已经有重复
     */
    public boolean addAcceptor(Acceptor acceptor) {
        //TODO 检查该存储端是否已经存在，如果存在则返回false
        return false;
    }

    public String get(String key) {
        //TODO 从存储系统中获取某个key对应的value
        return null;
    }

    public void set(String key, String newVal) {
        //TODO 设置下一个版本的key的value为newVal
    }

    

}