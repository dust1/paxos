package com.dust.paxos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dust.paxos.pojo.ResMessage;
import com.dust.paxos.utils.Node;
import com.dust.paxos.utils.RndTools;

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
        String uuid = acceptor.getUuid();
        if (aMap.containsKey(uuid)) {
            return false;
        }
        aMap.put(uuid, acceptor);
        return true;
    }

    public Proposer appendAcceptor(Acceptor acceptor) {
        addAcceptor(acceptor);
        return this;
    }

    /**
     * 根据key获取对应的value
     * 根据多数派读写规则，如果出现多个不同的value，则以最新的值为准
     * @param key
     * @return
     */
    public String get(String key) {
        List<ResMessage> resMessages = aMap.values().stream()
            .map(a -> {
                return a.read(key);
            })
            .collect(Collectors.toList());
        if (resMessages.isEmpty()) {
            return null;
        }
        ResMessage latestNode = resMessages.stream()
            .max((r1, r2) -> {
                return Integer.compare(r1.getVrnd(), r2.getVrnd());
            }).get();
        return latestNode.getValue();
    }

    /**
     * 写入
     * @param key
     * @param newVal
     * @return
     */
    public boolean set(String key, String newVal) {
        int rnd = RndTools.getRnd();
        //写前读取
        List<ResMessage> resMessages = aMap.values().stream()
        .map(a -> {
            return a.get(rnd, key);
        })
        .collect(Collectors.toList());
        ResMessage latestNode = resMessages.stream()
            .max((r1, r2) -> {
                return Integer.compare(r1.getLastRnd(), r2.getLastRnd());
            }).get();
        if (latestNode.getLastRnd() > rnd) {
            //恢复数据
            aMap.values().stream().map(a -> {
                return a.set(key, latestNode.getValue(), latestNode.getLastRnd());
            });
            return false;
        }

        return aMap.values().stream().map(a -> {
            return a.set(key, newVal, rnd);
        })
        .allMatch(b -> b);
    }

}