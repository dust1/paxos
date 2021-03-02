package com.dust.paxos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Proposer {

    private static AtomicInteger rndSeed;

    {
        init();
    }

    private synchronized void init() {
        rndSeed = new AtomicInteger();
    }


    private String uuid;
    private Map<String, Acceptor> acceptorMap;

    public static Proposer create(String uuid) {
        return new Proposer(uuid);
    }

    private Proposer(String uuid) {
        this.uuid = uuid;
        this.acceptorMap = new HashMap<>();
    }

    /**
     * 客户端对外开放获取数据端口
     * @param key
     * @return
     */
    public String get(String key) {
        List<List<GetRes>> lists = acceptorMap.values()
                .stream().map(a -> a.get(key))
                .filter(GetRes::isSuccess)
                .collect(Collectors.groupingBy(GetRes::getValue))
                .values()
                .stream()
                .sorted((l1, l2) -> l2.size() - l1.size())
                .collect(Collectors.toList());
        if (lists.size() < (acceptorMap.size() / 2)) {
            return null;
        }
        GetRes res = lists.get(0).get(0);
        if (lists.size() > 1) {
            set(key, res.getValue());
        }
        return res.getValue();
    }

    /**
     * 客户端对外开放设置数据端口
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, String value) {
        int rnd = rndSeed.getAndIncrement();
        if (acceptorMap.isEmpty()) {
            return false;
        }
        List<ReadRes> list = acceptorMap.values().stream()
                .map(a -> a.read(key, rnd))
                .collect(Collectors.toList());
        List<ReadRes> successList = list.stream()
                .collect(
                        Collectors.groupingBy(ReadRes::isSuccess)
                ).getOrDefault(
                        Boolean.TRUE, Collections.emptyList()
                );
        if (successList.size() < (acceptorMap.size() / 2)) {
            return false;
        }

        ReadRes maxReadRes = list.stream().max(Comparator.comparing(ReadRes::getLastRnd)).get();
        boolean result = false;
        if (maxReadRes.getLastRnd() > rnd) {
            //如果存在未完成的任务，则将其恢复并完成
            write(key, maxReadRes.getValue(), maxReadRes.getLastRnd());
        } else {
            //否则写入自身
            result = write(key, value, rnd);
        }
        return result;
    }

    /**
     * 给客户端注册Acceptor
     * @param acceptor
     */
    public synchronized void appendAcceptor(Acceptor acceptor) {
        acceptorMap.put(acceptor.getUuid(), acceptor);
    }

    private boolean write(String key, String value, int rnd) {
        Map<Boolean, List<Boolean>> resultMap = acceptorMap.values()
                .stream()
                .map(a -> a.set(key, value, rnd))
                .collect(Collectors.groupingBy(Boolean::booleanValue));
        return resultMap.getOrDefault(Boolean.TRUE, Collections.emptyList()).size() >= (acceptorMap.size() / 2);
    }

}
