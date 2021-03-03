package com.dust.paxos;

import java.util.HashMap;
import java.util.Map;

public class Acceptor {

    private Map<String, MetaData> keyWithMetaData;

    private String uuid;

    public static Acceptor create(String uuid) {
        return new Acceptor(uuid);
    }

    private Acceptor(String uuid) {
        this.uuid = uuid;
        this.keyWithMetaData = new HashMap<>();
    }

    public String getUuid() {
        return uuid;
    }

    /**
     * 预先读取
     * @param key 读取关键字
     * @param rnd 读取rnd
     * @return 如果rnd小于元素的last_rnd则拒绝请求，否则，将rnd赋值到last_rnd，表示该数据只接受这个rnd的phase-2
     */
    public ReadRes read(String key, int rnd) {
        MetaData metaData = keyWithMetaData.getOrDefault(key, MetaData.empty(key));
        keyWithMetaData.put(key, metaData);
        if (metaData.read(rnd)) {
            return ReadRes.byMetaData(metaData, uuid);
        }
        return ReadRes.fail(metaData, uuid);
    }

    /**
     * 直接读取
     * @param key
     * @return
     */
    public GetRes get(String key) {
        if (keyWithMetaData.containsKey(key)) {
            return GetRes.byMetaData(keyWithMetaData.get(key), uuid);
        }
        return GetRes.empty(key, uuid);
    }

    /**
     * 设置
     * @param key
     * @param value
     * @param rnd
     * @return
     */
    public boolean set(String key, String value, int rnd) {
        if (!keyWithMetaData.containsKey(key)) {
            return false;
        }
        return keyWithMetaData.get(key).write(value, rnd);
    }

}
