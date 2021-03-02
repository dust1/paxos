package com.dust.paxos;

import lombok.Getter;
import lombok.Setter;

/**
 * 写前读取返回结果
 */
@Getter
@Setter
public class ReadRes {

    private int lastRnd;

    private String value;

    private int vrnd;

    private boolean success;

    private String uuid;

    public static ReadRes fail( MetaData metaData, String uuid) {
        return new ReadRes(metaData.getLastRnd(), metaData.getValue(),
                metaData.getVrnd(), false, uuid);
    }

    public static ReadRes byMetaData(MetaData metaData, String uuid) {
        return new ReadRes(metaData.getLastRnd(),
                metaData.getValue(), metaData.getVrnd(),
                true, uuid);
    }

    private ReadRes(int lastRnd, String value,
                    int vrnd, boolean success,
                    String uuid) {
        this.lastRnd = lastRnd;
        this.value = value;
        this.vrnd = vrnd;
        this.success = success;
        this.uuid = uuid;
    }

}
